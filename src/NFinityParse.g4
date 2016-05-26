parser grammar NFinityParse;

options { tokenVocab= NFinityLexer; }

statement
    : single_statement
    | or_statement
    ;

single_statement
    : field_access
    | method_access
    | BARE_VALUE
    | '(' statement ')'
    | UNARY_PRE single_statement
    | single_statement UNARY_POST
    ;

or_statement
    : and_statement BINARY_OR and_statement
    ;

and_statement
	: compare_statement BINARY_AND compare_statement
	;

compare_statement
	: low_statement BINARY_COMPARE low_statement
	;

low_statement
	: mid_statement BINARY_LOW mid_statement
	;

mid_statement
	: high_statement BINARY_MID high_statement
	;

high_statement
	: single_statement BINARY_HIGH single_statement
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
    : var_declare ASSIGNER statement
    | field_access ASSIGNER statement
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
    : access_start '.' (access_part '.')*
    ;

member_name
	: IDENT
	;

access_start
    : access_part
    | single_statement
    | BARE_VALUE
    | SRC
    ;

access_part
    : method_call
    | member_name
    ;