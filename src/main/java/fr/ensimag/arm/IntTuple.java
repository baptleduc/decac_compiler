package fr.ensimag.arm;

public class IntTuple {

    private int first;
    private int second;

    public IntTuple(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntTuple) {
            IntTuple tuple = (IntTuple) obj;
            return first == tuple.first && second == tuple.second;
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

}
