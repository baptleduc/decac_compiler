package fr.ensimag.arm;

public enum ARMDataType {
    ASCIZ, // we use printf so we use .asciz instead of .ascii to have string terminated by
           // \0
    BYTE, WORD, DOUBLE_WORD
}
