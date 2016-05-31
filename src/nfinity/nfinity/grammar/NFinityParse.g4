parser grammar NFinityParse;

@header {package nfinity.nfinity.grammar;}

options { tokenVocab= NFinityLexer; }

//These are various possibilites for code sections
line
    : type_block
    | preprocess NEWLINE
    | NEWLINE
    | <EOF>
    ;

//A type block defines some behaviour for a type
//This can be a new proc, an existing proc, a new verb, an existing verb, a new var, an existing var, or a nested type
type_block
    : (type_declare? separator)? method_declare
    | (type_declare? separator)? method_implement
    | (type_declare? separator)? verb_declare
    | (type_declare? separator)? verb_implement
    | (type_declare? separator)? var_or_assignment NEWLINE
    | type_declare separator? NEWLINE INDENT type_block+ DEDENT
    ;

// Preprocessor defines and includes
preprocess
    : HASH INCLUDE bare_value
    | HASH DEFINE member_name statement?
    | HASH UNDEF member_name
    | HASH IFDEF member_name
    | HASH ELSEIF member_name
    | HASH ELSE member_name
    ;

// A code block section - this can be a simple statement or a more complex block
expression
    //method call, field access, etc
    : statement
    | IF '(' statement ')' expression_body (ELSE expression_body)?
    | FOR '(' assignment ';' statement ';' statement ')' expression_body
    | FOR '(' var_declare IN statement ')' expression_body
    | WHILE '(' statement ')' expression_body
    | SWITCH '(' statement ')' switch_body
    | SPAWN '(' statement ')' expression_body
    | RETURN statement?
    | preprocess
    ;

// A number of code block sections together
expression_body
    : NEWLINE INDENT (expression NEWLINE)* DEDENT
    ;

//The body of a switch statement - multiple if blocks, and then an else block
switch_body
    : NEWLINE INDENT (IF '(' statement ')' expression_body)* (ELSE expression_body)? DEDENT
    ;

// The top-level kind of statement
statement
    //These are new vars or existing ones being assigned
    : assignment
    //This is the top of the statement tree
    | trinary_statement
    ;

//Trinary statements are simplified if elses
trinary_statement
    : or_statement ('?' statement ':' statement)?
    ;

or_statement
    : and_statement (BINARY_OR and_statement)*
    ;

and_statement
	: compare_statement (BINARY_AND compare_statement)*
	;

compare_statement
	: low_statement (BINARY_COMPARE low_statement)*
	;

//See the lexer file for how these are prioritised
low_statement
	: med_statement (BINARY_LOW med_statement)*
	;

med_statement
	: high_statement (BINARY_MED high_statement)*
	;

high_statement
	: unary_statement (BINARY_HIGH unary_statement)*
	;

unary_statement
    : UNARY_PRE single_statement
    | single_statement UNARY_POST
    | single_statement
    ;

//The most basic kind of statement
single_statement
    //When you get a field
    : field_access
    //When you call a proc
    | method_access
    //When you type in a string, a number, or a typepath
    | bare_value
    //Then you create something
    | NEW single_statement '(' arguments? ')'
    //Nesting for statements
    | '(' statement ')'
    ;

//This is when new procs are defined
method_declare
    : PROC separator (access_modifier separator)? (typepath separator)? member_name '(' argument_declares? ')' expression_body
    | PROC NEWLINE INDENT (access_modifier separator)? (typepath separator)? member_name'(' argument_declares? ')' expression_body DEDENT
    ;

//This is when existing procs are implemented
method_implement
    : member_name '(' argument_declares? ')' expression_body
    ;

//This is when new verbs are defined
verb_declare
	: VERB separator member_name '(' argument_declares? ')' verb_body
	| VERB NEWLINE INDENT member_name '(' argument_declares? ')' verb_body DEDENT
	;

//This is when existing verbs are implemented
verb_implement
    : member_name '(' argument_declares? ')' verb_body
    ;

//This is what a verb can contain
verb_body
    : NEWLINE INDENT (set_statement NEWLINE)* (expression NEWLINE)* DEDENT
    ;

//These are verb set lines e.g. set name = "this verb name"
set_statement
    : SET VERB_SET (AS | ASSIGNMENT) statement
    ;

//These are the way you declare arguments for procs
argument_declares
    : (argument_declare ',')* argument_declare (',' ELLIPSIS)?
    ;

//Each argument can have a value or not
argument_declare
    : argument_var_declare AS (IDENT BITWISE_OR)* IDENT
    | argument_var_declare ASSIGNMENT statement
    | argument_var_declare
    ;

//Since byond allows both var/this and just this, we allow both
argument_var_declare
    : var_declare
    | optional_var_declare
    ;

//Just when a is set to b (possibly with an operator assignment e.g. a *= b)
assignment
    : var_declare_assignment
    | field_assignment
    ;

//Since var declares can never be the result of op assigns, this is just var declares or pures
var_or_assignment
    : var_declare_assignment
    | field_pure
    ;

var_declare_assignment
    : var_declare ASSIGNMENT statement
    | var_declare
    ;

var_declare
    : VAR separator optional_var_declare
    ;

//No var means you just give a typepath
optional_var_declare
    : (access_modifier separator)? (scope_modifier separator)? (typepath separator)? member_name
    ;

//Any kind of existing var assignment
field_assignment
    : field_op
    | field_pure
    ;

//When a var is set using an operator + the assignment e.g. +=
field_op
    : field_access ASSIGNER_OP statement
    ;

//When the existing var is just set
field_pure
    : field_access ASSIGNMENT statement
    ;

//When a proc is called
method_access
    : access_path? method_call
    ;

//Where the proc call actually happens
method_call
	: DOT '(' arguments? ')'
	| DOT DOT '(' arguments ? ')'
    | member_name '(' arguments? ')'
    ;

//Proc arguments
arguments
    : (argument ',')* argument
    ;

// An argument in a proc call - some of this is for the list() special case
argument
    : statement
    | member_name ASSIGNMENT statement
    | bare_value ASSIGNMENT statement
    ;

//Generic restriction block
restrictions
    : (restriction ',')* restriction
    ;

// Generic restrictors - types, procs, verbs, or vars
restriction
    : typepath
    | method_declare
    | verb_declare
    | var_declare
    ;

access_modifier
    : PUBLIC
    | PRIVATE
    ;

type_declare
    : separator? ((typepath separator)? ABSTRACT separator)? typepath
    | separator? (typepath separator)? GENERIC separator member_name (WHERE restrictions)?
    ;

scope_modifier
	: GLOBAL
	| STATIC
	| CONST
	;

// When you access a var by doing dot.path.to.var
field_access
    : access_path? member_name
    ;

// A dot path for access
access_path
    : access_start ACCESS (access_part ACCESS)*
    ;

// The possible names for types, procs, vars, etc
member_name
	: IDENT
	;

// How a dotpath can start
access_start
    : access_part
    | '(' statement ')'
    | bare_value
    | SRC
    ;

// How a dotpath can continue
access_part
    : method_call
    | member_name
    ;

//Possible bare values are strings, nums (including binary), the null identifier and typepaths with a starting /
bare_value
    : NUM
    | STRING
    | BINARY
    | NULL
    | separator typepath
    ;

//A typepath - no leading / is enforced
typepath
    : (member_name separator)* member_name
    ;

//What separates typepaths out
separator
    : DIV
    ;
