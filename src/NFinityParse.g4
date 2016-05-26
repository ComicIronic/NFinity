parser grammar NFinityParse;

options { tokenVocab= NFinityLexer; }

line
    : type_declare type_body
    | (type_declare SEPARATOR)? method_declare expression_body
    | (type_declare SEPARATOR)? verb_declare verb_body
    | (type_declare SEPARATOR)? var_with_value_declare
    | (type_declare SEPARATOR)? field_pure
    | preprocess
    ;

preprocess
    : HASH (DEFINE | UNDEF | INCLUDE) statement?
    ;

expression
    : statement
    | IF '(' statement ')' expression_body (ELSE expression_body)?
    | FOR '(' assignment ';' statement ';' statement ')' expression_body
    | FOR '(' var_declare IN statement ')'
    | WHILE '(' statement ')'
    | SWITCH '(' statement ')' (IF '(' statement ')' expression_body)* (ELSE expression_body)?
    | preprocess
    ;

expression_body
    : expression*
    ;

type_body
    : pure_assignment*
    ;

statement
    : single_statement
    | trinary_statement
    | assignment
    | RETURN statement?
    ;

single_statement
    : field_access
    | method_access
    | BARE_VALUE
    | '(' statement ')'
    | UNARY_PRE single_statement
    | single_statement UNARY_POST
    ;

trinary_statement
    : or_statement '?' statement ':' statement
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
	: single_statement (BINARY_HIGH single_statement)*
	;

type_declare
    : SEPARATOR? TYPEPATH SEPARATOR member_name
    | SEPARATOR? TYPEPATH SEPARATOR member_name SEPARATOR GENERIC SEPARATOR member_name
    ;

var_with_value_declare
    : var_declare
    | var_declare_assignment
    ;

var_declare
    : VAR SEPARATOR TYPEPATH SEPARATOR member_name
    | VAR SEPARATOR member_name
    ;

method_declare
    : PROC SEPARATOR member_name '(' argument_declares? ')'
    | PROC SEPARATOR TYPEPATH SEPARATOR member_name '(' argument_declares? ')'
    ;

verb_declare
	: VERB SEPARATOR member_name '(' argument_declares? ')'
	;

verb_body
    : set_statement* expression_body
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
    : TYPEPATH SEPARATOR member_name
    | member_name
    ;

assignment
    : var_declare_assignment
    | field_assignment
    ;

pure_assignment
    : var_declare_pure
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

argument
    : statement
    | member_name ASSIGNMENT statement
    ;

field_access
    : access_path? member_name
    ;

access_path
    : access_start ACCESS (access_part ACCESS)*
    ;

member_name
	: IDENT
	;

access_start
    : access_part
    | '(' statement ')'
    | BARE_VALUE
    | SRC
    ;

access_part
    : method_call
    | member_name
    ;