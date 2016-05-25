parser grammar NFinityParse;

options { tokenVocab= NFinityLexer; }

statement
    : single_statement
    | binary_statement
    | UNARY_PRE statement
    | statement UNARY_POST
    ;

single_statement
    : field_access
    | method_access
    | BARE_VALUE
    ;

binary_statement
    : statement BINARY_OP statement
    ;

var_declare
    : VAR SEPARATOR TYPEPATH SEPARATOR IDENT
    | VAR SEPARATOR IDENT
    ;

method_declare
    : PROC SEPARATOR IDENT '(' argument_declares? ')'
    | PROC SEPARATOR TYPEPATH SEPARATOR IDENT '(' argument_declares? ')'
    ;

argument_declares
    : (argument_declare ',')* argument_declare
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
    : TYPEPATH SEPARATOR IDENT
    | IDENT
    ;

assignment
    : var_declare ASSIGNER statement
    | field_access ASSIGNER statement
    ;

method_access
    : access_path? method_call
    ;

method_call
    : IDENT '(' arguments? ')'
    ;

arguments
    : (argument ',')* argument
    ;

argument
    : ELLIPSIS
    | statement
    | IDENT ASSIGNMENT statement
    ;

field_access
    : access_path? IDENT
    ;

access_path
    : access_start '.' (access_part '.')*
    ;

access_start
    : access_part
    | BARE_VALUE
    | SRC
    ;

access_part
    : method_call
    | IDENT
    ;