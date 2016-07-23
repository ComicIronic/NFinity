lexer grammar NFinityLexer;

tokens { INDENT, DEDENT }

@header {package nfinity.nfinity.grammar;}

@members {
  //Used by interpreter for line warnings and errors
  public int LineCount = 1;

  // A queue where extra tokens are pushed on (see the NEWLINE lexer rule).
  private java.util.LinkedList<Token> tokens = new java.util.LinkedList<Token>();
  // The stack that keeps track of the indentation level.
  private java.util.Stack<Integer> indents = new java.util.Stack<Integer>();
  // The amount of opened braces, brackets and parenthesis.
  private int opened = 0;
  // The most recently produced token.
  private Token lastToken = null;
  @Override
  public void emit(Token t) {
    super.setToken(t);
    tokens.offer(t);
  }

  @Override
  public Token nextToken() {
    // Check if the end-of-file is ahead and there are still some DEDENTS expected.
    if (_input.LA(1) == EOF && !this.indents.isEmpty()) {
      // Remove any trailing EOF tokens from our buffer.
      for (int i = tokens.size() - 1; i >= 0; i--) {
        if (tokens.get(i).getType() == EOF) {
          tokens.remove(i);
        }
      }

      // First emit an extra line break that serves as the end of the statement.
      this.emit(commonToken(NFinityParse.NEWLINE, "\n"));

      // Now emit as much DEDENT tokens as needed.
      while (!indents.isEmpty()) {
        this.emit(createDedent());
        indents.pop();
      }

      // Put the EOF back on the token stream.
      this.emit(commonToken(NFinityParse.EOF, "<EOF>"));
    }

    Token next = super.nextToken();

    if (next.getChannel() == Token.DEFAULT_CHANNEL) {
      // Keep track of the last token on the default channel.
      this.lastToken = next;
    }

    return tokens.isEmpty() ? next : tokens.poll();
  }

  private Token createDedent() {
    CommonToken dedent = commonToken(NFinityParse.DEDENT, "");
    dedent.setLine(this.lastToken.getLine());
    return dedent;
  }

  private CommonToken commonToken(int type, String text) {
    int stop = this.getCharIndex() - 1;
    int start = text.isEmpty() ? stop : stop - text.length() + 1;
    return new CommonToken(this._tokenFactorySourcePair, type, DEFAULT_TOKEN_CHANNEL, start, stop);
  }

  // Calculates the indentation of the provided spaces
  static int getIndentationCount(String spaces) {
    int count = 0;
    for (char ch : spaces.toCharArray()) {
      switch (ch) {
        case '\t':
          count += 4 - (count % 4);
          break;
        default:
          // A normal space char.
          count++;
      }
    }

    return count;
  }

  boolean atStartOfInput() {
    return super.getCharPositionInLine() == 0 && super.getLine() == 1;
  }
}

NEWLINE
 : ( {atStartOfInput()}?   SPACES
   | LINEBREAK SPACES*
   )
   {
     LineCount++;
     
     String newLine = getText().replaceAll("[^\r\n]+", "");
     String spaces = getText().replaceAll("[\r\n]+", "");
     int next = _input.LA(1);
     if (opened > 0 || next == '\r' || next == '\n' || next == '/') {
       // If we're inside a list or on a blank line, ignore all indents,
       // dedents and line breaks.
       skip();
     }
     else {
       emit(commonToken(NEWLINE, newLine));
       int indent = getIndentationCount(spaces);
       int previous = indents.isEmpty() ? 0 : indents.peek();
       if (indent == previous) {
         // skip indents of the same size as the present indent-size
         skip();
       }
       else if (indent > previous) {
         indents.push(indent);
         emit(commonToken(NFinityParse.INDENT, spaces));
       }
       else {
         // Possibly emit more than 1 DEDENT token.
         while(!indents.isEmpty() && indents.peek() > indent) {
           this.emit(createDedent());
           indents.pop();
         }
       }
     }
   }
 ;

SINGLE_LINE_COMMENT:     '//'  INPUTCHAR*            -> skip;
DELIMITED_COMMENT:       '/*'  .*? '*/'              -> skip;
LINEJOINS:               LINE_JOIN                   -> skip;
WHITESPACE:              SPACES+                     -> skip;

fragment LINE_JOIN: '\\' SPACES* LINEBREAK;
fragment SPACES : ' ' | '\t' ;

fragment LINEBREAK
    : '\r'? '\n'
    | '\r'
    ;

fragment INPUTCHAR : ~('\r'| '\n');

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
IFDEF:     'ifdef';
ELSEIF:    'elseif';
ELSE:      'else';
SWITCH:    'switch';
TO:        'to';
STEP:      'step';
AS:        'as';
SET:       'set';
SRC:       'src';
NULL:      'null';
WHERE:     'where';
SPAWN:     'spawn';

PUBLIC:    'public';
PRIVATE:   'private';

CONST:     'const';
GLOBAL:    'global';
STATIC:    'static';

GENERIC:   'generic';
ABSTRACT:  'abstract';

SECS:      'SECONDS';
MINS:      'MINUTES';
HOURS:     'HOURS';

DEFINE:    'define' ;
UNDEF:     'undef'  ;
INCLUDE:   'include';

//Any valid type, var, or method name follows the ident pattern
IDENT : IDENTSTART IDENTPART*;

fragment IDENTSTART
    : [A-Z]
    | [a-z]
    | '_' ;

fragment IDENTPART
    : IDENTSTART
    | DIGIT ;

STRING
    : SINGLESTRING
    | MULTISTRING
    ;

fragment SINGLESTRING
    : '"' (~["\\])* '"'
    | '\'' (~[\'\\])* '\''
    ;

fragment MULTISTRING: '{"' (~[\\])* '"}' ;

NUM
    : DECIMAL
    | INTEGER
    ;

fragment DECIMAL: DIGIT* '.' DIGIT+ ;
fragment INTEGER: DIGIT+ ;

fragment DIGIT : [0-9];

TIME_MOD
    : SECS
    | MINS
    | HOURS
    ;

BINARY : BINARYDIGIT+ ;

fragment BINARYDIGIT : [01];

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
ELLIPSIS:                 '...';
HASH:                     '#';

ACCESS
    : DOT
    | COLON
    ;

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

fragment UNARY_BOTH
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
    | HIDSET
    | POPUP
    | INSTANT
    | INVIS
    | SRC
    | BACKG
    ;

fragment HIDSET:    'hidden';
fragment NAME:      'name';
fragment DESC:      'desc';
fragment CATEGORY:  'category';
fragment POPUP:     'popup_menu';
fragment INSTANT:   'instant';
fragment INVIS:     'invisbility';
fragment BACKG:     'background';