package com.puzzlesolver.android;

import java.util.HashMap;
import java.util.Locale;

enum Step {
    R, D, L, U
}

abstract class SearchAlgo {
    State startState;
    String output;
    /* make a reversed order of the steps to use them on the algorithms with a stack as an open list
    to preserve the same priority of operators */
    final String[] STEPS_REVERSED = {"U", "L", "D", "R"};

    SearchAlgo(State startState) {
        this.startState = startState;
    }

    double getCurTimeSecs() {
        return ((double) System.currentTimeMillis() / 1000);
    }

    /*
This method not only checks if the state is already in the closed list but also
if it's there it checks if the same state found now with a cheaper cost
if it is it returns false os we can update to a cheaper cost
 */
    protected boolean isClosedListContains(HashMap<State, Double> closedList, State s) {
        if (!closedList.containsKey(s)) return false;
        double prevCost = closedList.remove(s);
        // put back the state that just was checked
        closedList.put(s, prevCost);
        return prevCost <= s.costFromRoot_plus_heuristicToGoal();
    }

    abstract void run();

    void makeOutput(int openLstPops, double totalTime, State goal) {
        output = "";
        if (goal == null) {
            try {
                output = " no path to goal";
            } catch (Exception e) {
                System.out.println(e);
            }
        } else {
            StringBuilder stepsToGoal = new StringBuilder();
            double totalCost = 0;
            State cur = goal;
            totalCost += goal.costFromRoot;
            while (cur.stepFromParent != null) {
                if (cur.stepFromParent == Step.R) {
                    stepsToGoal.append("R-");
                }
                if (cur.stepFromParent == Step.D) {
                    stepsToGoal.append("D-");
                }
                if (cur.stepFromParent == Step.L) {
                    stepsToGoal.append("L-");
                }
                if (cur.stepFromParent == Step.U) {
                    stepsToGoal.append("U-");
                }
                cur = cur.parentState;
            }
            String path = "\"Move\" the empty block: ";
            if (stepsToGoal.length() > 0) {
                path += stepsToGoal.reverse().toString().substring(1)
                        .replace("R","Right")
                        .replace("D","Down")
                        .replace("L","Left")
                        .replace("U","Up");
            }
            String tries = "Tries: " + openLstPops;
            String cost = "Cost: " + totalCost;
            String time = String.format(Locale.ENGLISH, "%.3f", totalTime) + " seconds";
            output = path +"\n" + tries + "\n" + cost + "\n" + time;
        }
    }
}