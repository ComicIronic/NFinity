lexer grammar Nfinity;

//@lexer::header  { /datum/lexer }

channels { COMMENTS_CHANNEL, DIRECTIVE }

SINGLE_LINE_COMMENT:     '//'  InputCharacter*    -> channel(COMMENTS_CHANNEL);
DELIMITED_COMMENT:       '/*'  .*? '*/'           -> channel(COMMENTS_CHANNEL);

InputCharacter : ~[\r\n\u0085\u2028\u2029];

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
SPAWN:     'spawn';
GLOBAL:    'global';
TO:        'to';
AS:        'as';

SINGLESTRING: '"' (~["\\\r\n\u0085\u2028\u2029])* '"';
MULTISTRING: '"{' (~[\}])* '}"' ;

STRING : SINGLESTRING | MULTISTRING;

DECIMAL: [0-9]* '.' [0-9]+ ;
INTEGER: [0-9]+ ;

NUM: DECIMAL | INTEGER ;

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

IDENTSTART : [a-z] | [A-Z] | '_' ;
IDENTPART : IDENTSTART | [0-9] ;

IDENT : IDENTSTART IDENTPART*;

DEFINE :  'define'  -> channel(DIRECTIVE);
UNDEF :   'undef'   -> channel(DIRECTIVE);
INCLUDE : 'include' -> channel(DIRECTIVE);
DIRECTIVE_OPEN_PARENS:         '('                              -> channel(DIRECTIVE), type(OPEN_PARENS);
DIRECTIVE_CLOSE_PARENS:        ')'                              -> channel(DIRECTIVE), type(CLOSE_PARENS);
DIRECTIVE_BANG:                '!'                              -> channel(DIRECTIVE), type(BANG);
DIRECTIVE_OP_EQ:               '=='                             -> channel(DIRECTIVE), type(OP_EQ);
DIRECTIVE_OP_NE:               '!='                             -> channel(DIRECTIVE), type(OP_NE);
DIRECTIVE_OP_AND:              '&&'                             -> channel(DIRECTIVE), type(OP_AND);
DIRECTIVE_OP_OR:               '||'                             -> channel(DIRECTIVE), type(OP_OR);

DIRECTIVE_SINGLE_LINE_COMMENT: '//' InputCharacter*  -> channel(COMMENTS_CHANNEL), type(SINGLE_LINE_COMMENT);




