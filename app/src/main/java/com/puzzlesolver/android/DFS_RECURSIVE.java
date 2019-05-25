package com.puzzlesolver.android;

import java.util.HashSet;

public class DFS_RECURSIVE extends SearchAlgo {

    DFS_RECURSIVE(State startState) {
        super(startState);
    }

    public void run() {

        int openLstPops = 0;
        double startTime = getCurTimeSecs();
        HashSet<State> closedList = new HashSet<>();

        RecursiveDfs(openLstPops, startTime, closedList, startState);

        makeOutput(openLstPops, getCurTimeSecs() - startTime, null);
    }

    void RecursiveDfs(int openLstPops, double startTime, HashSet<State> closedList, State startState) {
        if (startState.isGoal()) {
            makeOutput(openLstPops, getCurTimeSecs() - startTime, startState);
            return;
        }
        closedList.add(startState);
        State curState = startState;
        for (Step step : Step.values()) {
            State g = curState.makeState(step);
            if (g == null || closedList.contains(g))
                continue; // if can't apply the operator or if was developed ignore it
            else {
                closedList.add(g);
                RecursiveDfs(openLstPops + 1, startTime, closedList, g);
            }
        }
    }
}
