package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.DecacCompiler;

/**
 * Operand that contains a value.
 * 
 * @author Ensimag
 * @date 01/01/2025
 */
public abstract class DVal extends Operand {

    public abstract GPRegister codeGenToGPRegister(DecacCompiler  compiler);

}
