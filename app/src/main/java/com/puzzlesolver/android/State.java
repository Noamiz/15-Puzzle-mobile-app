package com.puzzlesolver.android;

import android.util.Log;

import java.util.Arrays;
import java.util.List;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class State {
    public static int iHeu = 1;
    private final Block[][] blocksMat;
    private int emptyXpos;
    private int emptyYpos;
    State parentState;
    Step stepFromParent;
    double costFromRoot;
    double heursticToGoal;
    private int hashCode;
    private int creatoinTime;

    double getHeursticToGoal() {
        return this.heursticToGoal;
    }

    double costFromRoot() {
        return this.costFromRoot;
    }

    State(Block[][] blockMat, State parentState, Step stepFromParent,
          int emptyXpos, int emptyYpos, double costFromRoot, int creatoinTime) {
        this.blocksMat = blockMat;
        this.parentState = parentState;
        this.stepFromParent = stepFromParent;
        this.emptyXpos = emptyXpos;
        this.emptyYpos = emptyYpos;
        this.costFromRoot = costFromRoot;
        this.heursticToGoal = calculateHeuristicToGoal(blocksMat);
        this.hashCode = makeHashOnce();
        this.creatoinTime = creatoinTime;
    }

    static boolean areNumbersValid(Block[][] blocksMat) {
        // The total numbers of different blocks' numbers that the board is consisted
        // minus 1 becuase there is one empty block
        int nums = blocksMat.length * blocksMat[0].length - 1;
        int sum = nums * (1 + nums) / 2;
        int matSum = 0;
        // Check that the sum of the board is as the correct sum that should be
        for (int i = 0; i < blocksMat.length; i++) {
            for (int j = 0; j < blocksMat[0].length; j++) {
                // Verify the block isn't the empty one
                if (blocksMat[i][j].num != -1) {
                    matSum += blocksMat[i][j].num;
                }
            }
        }
        return sum == matSum;
    }

    // Verify the arrays of each block cost contain only blocks numbers from the board
    static int areCost2Cost05Valid(Block[][] blocksMat,
                                   List<Integer> costhalf, List<Integer> cost2) {
        int maxBlockNum = blocksMat.length * blocksMat[0].length - 1;
        for (int i : costhalf) {
            if (i > maxBlockNum) {
                return 1;
            }
        }
        for (int i : cost2) {
            if (i > maxBlockNum) {
                return 2;
            }
        }
        return 0;
    }

    private int makeHashOnce() {
        int[][] toInt = new int[blocksMat.length][blocksMat[0].length];
        for (int i = 0; i < blocksMat.length; i++) {
            for (int j = 0; j < blocksMat[0].length; j++) {
                toInt[i][j] = blocksMat[i][j].num;
            }
        }
        return Arrays.deepHashCode(toInt);
    }

    @Override
    public String toString() {
        String toPrint = "";
        for (int i = 0; i < blocksMat.length; i++) {
            for (int j = 0; j < blocksMat[0].length; j++) {
                toPrint += blocksMat[i][j] + " ";
            }
            toPrint += "\n";
        }
        return toPrint;
    }

    public boolean isGoal() {
        for (int i = 0; i < blocksMat.length; i++) {
            for (int j = 0; j < blocksMat[0].length; j++) {
                if (i != blocksMat.length - 1 || j != blocksMat[0].length - 1) {
                    if (blocksMat[i][j].num != i * blocksMat[0].length + j + 1)
                        return false;
                }
            }
        }
        return true;
    }

    private Block[][] blockArrShallowCopy() {
        Block[][] blockMatCopy = new Block[blocksMat.length][blocksMat[0].length];
        for (int i = 0; i < blocksMat.length; i++) {
            for (int j = 0; j < blocksMat[0].length; j++) {
                //blockMatCopy[i][j] = new Block(blocksMat[i][j]);
                blockMatCopy[i][j] = blocksMat[i][j];
            }
        }
        return blockMatCopy;
    }

    public State makeState(Step step) {
        if (step == Step.L) {
            if (emptyXpos == 0) {
                return null;
            } else {
                Block[][] blockMatNewState = blockArrShallowCopy();
                double cost = blockMatNewState[emptyYpos][emptyXpos - 1].getCost();
                blockMatNewState[emptyYpos][emptyXpos] =
                        blockMatNewState[emptyYpos][emptyXpos - 1];
                blockMatNewState[emptyYpos][emptyXpos - 1] = new Block(Block.EMPTY, Color.COST1);
                return new State(blockMatNewState, this, step,
                        emptyXpos - 1, emptyYpos, this.costFromRoot + cost, this.creatoinTime + 1);
            }
        } else if (step == Step.U) {
            if (emptyYpos == 0) {
                return null;
            } else {
                Block[][] blockMatNewState = blockArrShallowCopy();
                double cost = blockMatNewState[emptyYpos - 1][emptyXpos].getCost();
                blockMatNewState[emptyYpos][emptyXpos] =
                        blockMatNewState[emptyYpos - 1][emptyXpos];
                blockMatNewState[emptyYpos - 1][emptyXpos] = new Block(Block.EMPTY, Color.COST1);
                return new State(blockMatNewState, this, step,
                        emptyXpos, emptyYpos - 1, this.costFromRoot + cost, this.creatoinTime + 1);
            }
        } else if (step == Step.R) {
            if (emptyXpos == blocksMat[0].length - 1) {
                return null;
            } else {
                Block[][] blockMatNewState = blockArrShallowCopy();
                double cost = blockMatNewState[emptyYpos][emptyXpos + 1].getCost();
                blockMatNewState[emptyYpos][emptyXpos] =
                        blockMatNewState[emptyYpos][emptyXpos + 1];
                blockMatNewState[emptyYpos][emptyXpos + 1] = new Block(Block.EMPTY, Color.COST1);
                return new State(blockMatNewState, this, step,
                        emptyXpos + 1, emptyYpos, this.costFromRoot + cost, this.creatoinTime + 1);
            }
        } else {
            if (emptyYpos == blocksMat.length - 1) {
                return null;
            } else {
                Block[][] blockMatNewState = blockArrShallowCopy();
                double cost = blockMatNewState[emptyYpos + 1][emptyXpos].getCost();
                blockMatNewState[emptyYpos][emptyXpos] =
                        blockMatNewState[emptyYpos + 1][emptyXpos];
                blockMatNewState[emptyYpos + 1][emptyXpos] = new Block(Block.EMPTY, Color.COST1);
                return new State(blockMatNewState, this, step,
                        emptyXpos, emptyYpos + 1, this.costFromRoot + cost, this.creatoinTime + 1);
            }
        }
    }

    public boolean isInPath(State newState) {
        if (this.equals(newState)) return true;
        State cur = parentState;
        while (cur != null) {
            if (cur.equals(newState)) {
                return true;
            }
            cur = cur.parentState;
        }
        return false;
    }


    public boolean isInPathAndAlreadyCheaper(State newState) {
        if (this.equals(newState)) return true;
        State cur = parentState;
        while (cur != null) {
            if (cur.equals(newState)) {
                return cur.costFromRoot_plus_heuristicToGoal() <=
                        newState.costFromRoot_plus_heuristicToGoal();
            }
            cur = cur.parentState;
        }
        return false;
    }

    public static int compareCostThenTimeThenStepFromParent(State o1, State o2) {
        double costDiff = o1.costFromRoot_plus_heuristicToGoal() - o2.costFromRoot_plus_heuristicToGoal();
        if (costDiff != 0) {
            if (costDiff > 0) {
                return 1;
            } else {
                return -1;
            }
        }
        double timeDiff = o1.creatoinTime - o2.creatoinTime;
        if (timeDiff != 0) {
            if (timeDiff > 0) {
                return 1;
            } else {
                return -1;
            }
        }
        int stepO1 = 0;
        if (o1.stepFromParent == Step.R) {
            stepO1 = 4;
        } else if (o1.stepFromParent == Step.D) {
            stepO1 = 3;
        } else if (o1.stepFromParent == Step.L) {
            stepO1 = 2;
        } else if (o1.stepFromParent == Step.U) {
            stepO1 = 1;
        }
        int stepO2 = 0;
        if (o2.stepFromParent == Step.R) {
            stepO2 = 4;
        } else if (o2.stepFromParent == Step.D) {
            stepO2 = 3;
        } else if (o2.stepFromParent == Step.L) {
            stepO2 = 2;
        } else if (o2.stepFromParent == Step.U) {
            stepO2 = 1;
        }
        return -(stepO1 - stepO2);
    }

    public double costFromRoot_plus_heuristicToGoal() {
        return costFromRoot + heursticToGoal;
    }

    private double calcManhattanDist(int i, int j) {
        int realI = blocksMat[i][j].num / blocksMat[0].length;
        int realJ = blocksMat[i][j].num % blocksMat[0].length;
        if (blocksMat[i][j].num % blocksMat[0].length == 0) {
            realI--;
        }
        if (realJ == 0) {
            realJ = blocksMat[0].length - 1;
        } else {
            realJ--;
        }

        ///Log.d("go", String.valueOf(iHeu));

        if (iHeu == 1) {
//          manhatten
            Log.d("go", "manhatten");

            return (Math.abs(i - realI) + Math.abs(j - realJ)) * blocksMat[i][j].getCost();
        } else if (iHeu == 2) {
//        //Chebyshev
            return blocksMat[i][j].getCost() * (Math.abs(i - realI) + Math.abs(j - realJ)) + (1 - 2 * blocksMat[i][j].getCost()) * min(Math.abs(i - realI), Math.abs(j - realJ));
        } else if (iHeu == 3) {
           Log.d("go", "octile");
//        //octile
            return blocksMat[i][j].getCost() * ((Math.abs(i - realI) + Math.abs(j - realJ)) + (sqrt(2) - 2 * blocksMat[i][j].getCost()) * min(Math.abs(i - realI), Math.abs(j - realJ)));
        } else {
//
//        //Euclidean
            return blocksMat[i][j].getCost() * sqrt(((Math.abs(i - realI) ^ 2) + (Math.abs(j - realJ) ^ 2)));
        }
    }

    private double calculateHeuristicToGoal(Block[][] blocksMat) {
        //need to add the chosen heuristic
        double cost = 0;
        for (int i = 0; i < blocksMat.length; i++) {
            for (int j = 0; j < blocksMat[0].length; j++) {
                if (blocksMat[i][j].num != Block.EMPTY) { // the empty block doesn't affect the heuristic
                    // if the block num isn't supposed to be there
                    if (i * blocksMat[0].length + 1 + j != blocksMat[i][j].num) {
                        cost += calcManhattanDist(i, j);
                    }
                }
            }
        }
        return cost;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof State))
            return false;
        if (obj == this)
            return true;
        State rhs = (State) obj;
        for (int i = 0; i < blocksMat.length; i++) {
            for (int j = 0; j < blocksMat[0].length; j++) {
                if (!rhs.blocksMat[i][j].equals(this.blocksMat[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
