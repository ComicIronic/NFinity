parser grammar NFinityParse;

@header {package nfinity.nfinity.grammar;}

options { tokenVocab= NFinityLexer; }

line
    : NEWLINE type_declare type_body
    | NEWLINE (type_body_declare separator)? method_declare expression_body
    | NEWLINE (type_body_declare separator)? verb_declare verb_body
    | NEWLINE (type_body_declare separator)? var_with_value_declare
    | NEWLINE (type_body_declare separator)? field_pure
    | NEWLINE preprocess
    | NEWLINE
    ;

// Preprocessor define
preprocess
    : HASH INCLUDE BARE_VALUE
    | HASH DEFINE member_name statement
    | HASH UNDEF member_name
    ;

// A code block section - this can be a simple statement or a more complex block
expression
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
    : INDENT (expression NEWLINE)* DEDENT
    ;

//What can follow a type declaration - only assignments
type_body
    : NEWLINE INDENT (pure_assignment NEWLINE)* DEDENT
    ;

switch_body
    : NEWLINE INDENT (IF '(' statement ')' expression_body)* (ELSE expression_body)? DEDENT
    ;

// The top-level kind of statement
statement
    : assignment
    | trinary_statement
    ;

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
    : field_access
    | method_access
    | BARE_VALUE
    | NEW single_statement '(' arguments? ')'
    | '(' statement ')'
    ;

type_declare
    : type_simple_declare
    | type_generic_declare (WHERE restrictions)?
    ;

type_body_declare
    : type_simple_declare
    | type_generic_declare
    ;

type_simple_declare
    : separator? typepath separator member_name
    ;

type_generic_declare
    : separator? typepath separator member_name separator GENERIC separator member_name
    ;

var_with_value_declare
    : var_declare
    | var_declare_assignment
    ;

var_declare
    : VAR separator typepath separator member_name
    | VAR separator member_name
    ;

method_declare
    : PROC separator member_name '(' argument_declares? ')'
    | PROC separator typepath separator member_name '(' argument_declares? ')'
    ;

verb_declare
	: VERB separator member_name '(' argument_declares? ')'
	;

verb_body
    : INDENT set_statement* DEDENT expression_body
    ;

set_statement
    : SET VERB_SET (AS | ASSIGNMENT) statement
    ;

argument_declares
    : (argument_declare ',')* argument_declare (',' ELLIPSIS)?
    ;

argument_declare
    : argument_var_declare
    | argument_var_declare ASSIGNMENT statement
    | argument_var_declare AS TYPE_GROUP
    ;

argument_var_declare
    : var_declare
    | optional_var_declare
    ;

optional_var_declare
    : typepath separator member_name
    | member_name
    ;

assignment
    : var_declare_assignment
    | field_assignment
    ;

pure_assignment
    : var_declare_pure
    | var_declare
    | field_pure
    ;

var_declare_assignment
    : var_declare_pure
    | var_declare_op
    ;

var_declare_op
    : var_declare ASSIGNER_OP statement
    ;

var_declare_pure
    : var_declare ASSIGNMENT statement
    ;

field_assignment
    : field_op
    | field_pure
    ;

field_op
    : field_access ASSIGNER_OP statement
    ;

field_pure
    : field_access ASSIGNMENT statement
    ;

method_access
    : access_path? method_call
    ;

method_call
    : member_name '(' arguments? ')'
    ;

arguments
    : (argument ',')* argument
    ;

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

// An argument in a proc call
argument
    : statement
    | member_name ASSIGNMENT statement
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
    | BARE_VALUE
    | SRC
    ;

// How a dotpath can continue
access_part
    : method_call
    | member_name
    ;

typepath
    : (member_name separator)* member_name
    ;

separator
    : DIV
    | INDENT
    ;
