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
PRINTLN : 'println';

// fragment rulxes are used by other rules, but do not produce tokens:
fragment DIGIT : '0' .. '9';
fragment POSITIVE_DIGIT : '0' .. '9';
fragment LETTER: ('a' .. 'z'|'A' .. 'Z');


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
