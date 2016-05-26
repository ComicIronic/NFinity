lexer grammar NFinityLexer;

SINGLE_LINE_COMMENT:     '//'  INPUTCHAR*         -> skip;
DELIMITED_COMMENT:       '/*'  .*? '*/'           -> skip;
LINEJOINS:               LINE_JOIN                -> skip;

fragment LINE_JOIN: '\\' SPACES? ( '\r'? '\n' | '\r' );
fragment SPACES : [ \t]+;

fragment INPUTCHAR : ~[\r\n\u0085\u2028\u2029];

VAR:       'var';
PROC:      'proc';
VERB:      'verb';
NEW:       'new';
FOR:       'for';
WHILE:     'while';
DO:        'do';
CONTINUE:  'continue';
BREAK:     'break';
RETURN:    'return';
IN:        'in';
IF:        'if';
ELSE:      'else';
SWITCH:    'switch';
GLOBAL:    'global';
TO:        'to';
AS:        'as';
SET:       'set';
SRC:       'src';
GENERIC:   'generic';
HIDDEN:    'hidden';
NAME:      'name';
DESC:      'desc';
CATEGORY:  'category';
POPUP:     'popup_menu';
INSTANT:   'instant';
INVIS:     'invisbility';
BACKG:     'background';
SPAWN:     'spawn';

SECS:      'SECONDS';
MINS:      'MINUTES';
HOURS:     'HOURS';

DEFINE:    'define' ;
UNDEF:     'undef'  ;
INCLUDE:   'include';

SINGLESTRING
    : '"' (~["\\]|INPUTCHAR)* '"'
    | '\'' (~[\'\\]|INPUTCHAR)* '\''
    ;

MULTISTRING: '{"' (~[\\])* '"}' ;

STRING
    : SINGLESTRING
    | MULTISTRING
    ;

BINARYDIGIT : [01];

BINARY : BINARYDIGIT+ ;

DECIMAL: [0-9]* '.' [0-9]+ ;
INTEGER: [0-9]+ ;

NUM
    : DECIMAL
    | INTEGER
    ;

TIME_MOD
    : SECS
    | MINS
    | HOURS
    ;

BARE_VALUE
    : NUM TIME_MOD?
    | STRING
    | BINARY
    ;

OPEN_BRACKET:             '[';
CLOSE_BRACKET:            ']';
OPEN_PARENS:              '(';
CLOSE_PARENS:             ')';
DOT:                      '.';
COMMA:                    ',';
COLON:                    ':';
SEMICOLON:                ';';
PLUS:                     '+';
MINUS:                    '-';
STAR:                     '*';
DIV:                      '/';
SEPARATOR:                '/';
PERCENT:                  '%';
AMP:                      '&';
BITWISE_OR:               '|';
CARET:                    '^';
BANG:                     '!';
TILDE:                    '~';
ASSIGNMENT:               '=';
LT:                       '<';
GT:                       '>';
INTERR:                   '?';
DOUBLE_COLON:             '::';
OP_COALESCING:            '??';
OP_INC:                   '++';
OP_DEC:                   '--';
OP_AND:                   '&&';
OP_OR:                    '||';
OP_EQ:                    '==';
OP_NE:                    '!=';
OP_LE:                    '<=';
OP_GE:                    '>=';
OP_ADD_ASSIGNMENT:        '+=';
OP_SUB_ASSIGNMENT:        '-=';
OP_MULT_ASSIGNMENT:       '*=';
OP_DIV_ASSIGNMENT:        '/=';
OP_MOD_ASSIGNMENT:        '%=';
OP_AND_ASSIGNMENT:        '&=';
OP_OR_ASSIGNMENT:         '|=';
OP_XOR_ASSIGNMENT:        '^=';
OP_LEFT_SHIFT:            '<<';
OP_LEFT_SHIFT_ASSIGNMENT: '<<=';
ELLIPSIS:                 '...';
HASH:                     '#';

IDENTSTART : [a-z] | [A-Z] | '_' ;
IDENTPART : IDENTSTART | [0-9] ;

//Any valid type, var, or method name follows the ident pattern
IDENT : IDENTSTART IDENTPART*;

ACCESS
    : DOT
    | COLON
    ;

TYPEPATH : DEEPPATH | IDENT ;

//The generic demands at least two depth i.e. path/to
fragment DEEPPATH : (IDENT SEPARATOR)+ IDENT;

//This is the groupings of mob|obj|turf etc.
TYPE_GROUP: (IDENT BITWISE_OR)* IDENT;

ASSIGNER_OP
    : OP_ADD_ASSIGNMENT
    | OP_SUB_ASSIGNMENT
    | OP_MULT_ASSIGNMENT
    | OP_DIV_ASSIGNMENT
    | OP_MOD_ASSIGNMENT
    | OP_AND_ASSIGNMENT
    | OP_OR_ASSIGNMENT
    | OP_XOR_ASSIGNMENT
    ;

BINARY_OP
    : OP_OR
    | OP_AND
    | LT
    | GT
    | PLUS
    | MINUS
    | STAR
    | DIV
    | AMP
    | BITWISE_OR
    | PERCENT
    ;

BINARY_OR : OP_OR ;

BINARY_AND : OP_AND ;

BINARY_COMPARE
	: LT
	| GT
	| OP_EQ
	| OP_NE
	| OP_LE
	| OP_GE
	;

BINARY_LOW
	: PLUS
	| MINUS
	| BITWISE_OR
	| AMP
	| CARET
	;

BINARY_MED
	: STAR
	| DIV
	;

BINARY_HIGH
	: PERCENT
	;

UNARY_BOTH
    : OP_INC
    | OP_DEC
    ;

UNARY_POST
    : UNARY_BOTH
    ;

UNARY_PRE
    : BANG
    | TILDE
    | UNARY_BOTH
    ;

VERB_SET
    : NAME
    | DESC
    | CATEGORY
    | HIDDEN
    | POPUP
    | INSTANT
    | INVIS
    | SRC
    | BACKG
    ;
