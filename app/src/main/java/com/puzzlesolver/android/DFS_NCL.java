package com.puzzlesolver.android;

import java.util.Stack;

public class DFS_NCL extends SearchAlgo {

    DFS_NCL(State startState) {
        super(startState);
    }

    public void run() {
        int openLstPops = 0;
        double startTime = getCurTimeSecs();

        Stack<State> openList = new Stack<>();
        // check if the goal state is the starting state
        if (startState.isGoal()) {
            makeOutput(0, getCurTimeSecs() - startTime, startState);
            return;
        }
        openList.push(startState);
        while (!openList.isEmpty()) {
            openLstPops++;
            State curState = openList.pop();
            for (Step step : Step.values()) {
                State g = curState.makeState(step);
                if (g == null)
                    continue; // if can't apply the operator or if was developed ignore it
                if (g.isGoal()) {
                    makeOutput(openLstPops,getCurTimeSecs() - startTime, g);
                    return;
                } else {
                    openList.push(g);
                }
            }
        }
        makeOutput(openLstPops, getCurTimeSecs() - startTime, null);
    }
}
