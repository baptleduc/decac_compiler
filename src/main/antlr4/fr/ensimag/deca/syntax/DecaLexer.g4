lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

//mots réservés, positionnement en haut du fichier pour utiliser le principe de la première règle prioritaire
ASM : 'asm';
CLASS : 'class';
EXTENDS : 'extends';
ELSE : 'else';
FALSE : 'false';
IF : 'if';
INSTANCEOF : 'instanceof';
NEW : 'new';
NULL : 'null';
READINT : 'readInt';
READFLOAT : 'readFloat';
PRINT : 'print';
PRINTX: 'printx';
PRINTLN : 'println';
PRINTLNX : 'printlnx';
PROTECTED : 'protected';
RETURN : 'return';
THIS : 'this';
TRUE : 'true';
WHILE : 'while';




//Symboles spéciaux
LT : '<';
GT : '>';
EQUALS : '=';
PLUS : '+';
MINUS : '-';
TIMES : '*';
SLASH : '/';
PERCENT : '%';
DOT : '.';
COMMA : ',';
OPARENT : '(';
CPARENT : ')';
OBRACE : '{';
CBRACE : '}';
EXCLAM : '!';
SEMI : ';';
EQEQ : '==';
NEQ : '!=';
GEQ : '>=';
LEQ  : '<=';
AND : '&&';
OR : '||';
        
//Identificateurs
fragment LETTER: ('a' .. 'z'|'A' .. 'Z');
fragment DIGIT : '0' .. '9';
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;

//Integer
fragment POSITIVE_DIGIT : '0' .. '9';
INT : '0' | (POSITIVE_DIGIT DIGIT*) ;

// Flottants
fragment EOL:'\n';
fragment NUM: DIGIT+;
fragment SIGN : '+' | '-'|;
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
// on passe par un autre fragment car on veut ignorer le f
fragment FLOATDEC : (DEC | EXP EXP) ('F' | 'f'|);
fragment DIGITHEX :('0' .. '9') | ('A' .. 'F') | ('a' .. 'f');
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X')NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f'|)?;
FLOAT : FLOATDEC | FLOATHEX;


//Chaînes de caractères
fragment STRING_CAR :  ~["\\\n];
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | EOL | '\\"' | '\\\\')* '"';


// arateur
WS  :   ( ' '
        | '\t'
        | '\r'
        | EOL
        ) {
              skip();
          }
    ;

//Commentaires
COMMENT : '/*' .*? '*/'
                { skip(); } ;
MONO_LIGNE_COMMENT : '//' .*? (EOL|EOF)
                { skip(); } ;
                

// Inclusion de fichier
fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"'
                {
                   doInclude(getText());
                };

