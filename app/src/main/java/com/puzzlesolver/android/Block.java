package com.puzzlesolver.android;

enum Color
{
    COST2, COSTHALF, COST1;
}

public class Block {
    static int EMPTY = -1;
    int num;
    private Color color;

    Block(int num, Color color){
        this.num = num;
        this.color = color;
    }

    Block(Block b){
        this.num = b.num;
        this.color = b.color;
    }

    public double getCost(){
        if(num == EMPTY){
            return -1;
        }
        if(color == Color.COSTHALF){
            return 0.5;
        }
        else if(color == Color.COST2){
            return 2;
        }
        return 1;
    }

    @Override
    public String toString() {
        String toPrint = "(" + num;
        if (color == Color.COST2){
            toPrint += ",Costs_2)";
        }
        else if (color == Color.COSTHALF){
            toPrint += ",Costs_0.5)";
        }
        else {
            toPrint += ",Costs_1)";
        }
        return toPrint;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Block))
            return false;
        if (obj == this)
            return true;

        Block rhs = (Block) obj;
        return (rhs.num == this.num && rhs.color == this.color);
    }
}
