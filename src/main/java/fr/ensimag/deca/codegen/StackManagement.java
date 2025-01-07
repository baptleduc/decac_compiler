package fr.ensimag.deca.codegen;

public class StackManagement {
    private int currentMaxStack = 0;

    public int getCurrentMaxStack() {
        return currentMaxStack;
    }

    public void incrementMaxStack(int increment) {
        currentMaxStack += increment;
    }

    public void incrementMaxStack() {
        incrementMaxStack(1);
    }
}
