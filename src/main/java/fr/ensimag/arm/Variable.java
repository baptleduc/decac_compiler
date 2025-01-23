package fr.ensimag.arm;

public class Variable {

    private int firstOccurence;
    private int secondOccurence;
    private int size;

    public Variable(int first, int second, int size) {
        this.firstOccurence = first;
        this.secondOccurence = second;
        this.size = size;
    }

    public int getFirstOccurence() {
        return firstOccurence;
    }

    public int getSecondOccurence() {
        return secondOccurence;
    }

    public int getSize() {
        return size;
    }

    public void setFirstOccurence(int first) {
        this.firstOccurence = first;
    }

    public void setSecondOccurence(int second) {
        this.secondOccurence = second;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            Variable tuple = (Variable) obj;
            return firstOccurence == tuple.firstOccurence && secondOccurence == tuple.secondOccurence;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + firstOccurence + ", " + secondOccurence + ")";
    }

}
