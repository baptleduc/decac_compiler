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
NEW : 'NEW';
NULL : 'null';
READINT : 'readInt';
READFLOAT : 'readFloat';
PRINT : 'print';
PRINTLN : 'println';
PRINTLNX : 'printlnx';
PROTECTED : 'protected';
RETURN : 'return';
THIS : 'this';
TRUE : 'true';
WHILE : 'while';

// fragment rulxes are used by other rules, but do not produce tokens:
fragment DIGIT : '0' .. '9';
fragment POSITIVE_DIGIT : '0' .. '9';
fragment LETTER: ('a' .. 'z'|'A' .. 'Z');

//Symboles spéciaux
INF : '<';
SUPP : '>';
AFFECT : '=';
ADD : '+';
MINUS : '-';
TIMES : '*';
DIVID : '/';
MOD : '%';
DOT : '.';
COMMA : ',';
OPAR : '(';
CPAR : ')';
OACO : '{';
CACO : '}';
NOT : '!';
SEPA : ';';
EQUAL : '==';
NEQUAL : '!=';
SEQUAL : '>=';
IEQUAL  : '<=';
AND : '&&';
OR : '||';

// Flottants
fragment NUM: DIGIT+;
fragment SIGN : '+' | '-' | ' ';
EXP : ('E' | 'e') SIGN NUM;
DEC : NUM '.' NUM;
FLOATDEC : (DEC | DEC EXP) ('F' | 'f' | ' ');
DIGITHEX :'0' .. '9' | 'A' .. 'F' + 'a' .. 'f';
NUMHEX : DIGITHEX+;
FLOATHEX : ('Ox' | 'OX')NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | ' ');
FLOAT : FLOATDEC | FLOATHEX;

//Chaînes de caractères
fragment STRING_CAR :  ~["\\\r\n] ;
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | '\n' | '\\"' | '\\\\')* '"';

// Séparateur
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) {
              skip(); // avoid producing a token
          }
    ;

//Commentaires
COMMENT : '/*' .*? '*/'
                { skip(); } ;
MONO_LIGNE_COMMENT : '//' (~('\n'))*
                { skip(); } ;
                

// Inclusion de fichier
fragment FILENAME : (LETTER | DIGIT | '.' | '-' + '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"' {};
