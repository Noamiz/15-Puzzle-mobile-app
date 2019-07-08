package com.puzzlesolver.android;

import java.util.HashSet;
import java.util.Stack;

public class DFS extends SearchAlgo {

    DFS(State startState) {
        super(startState);
    }

    public void run() {
        int openLstPops = 0;
        double startTime = getCurTimeSecs();

        Stack<State> openList = new Stack<>();
        HashSet<State> closedList = new HashSet<>();
        // check if the goal state is the starting state
        if (startState.isGoal()) {
            makeOutput(0, getCurTimeSecs() - startTime, startState);
            return;
        }
        openList.push(startState);
        closedList.add(startState);
        while (!openList.isEmpty()) {
            openLstPops++;
            State curState = openList.pop();
            for (Step step : Step.values()) {
                State g = curState.makeState(step);
                if (g == null || closedList.contains(g))
                    continue; // if can't apply the operator or if was developed ignore it
                if (g.isGoal()) {
                    makeOutput(openLstPops,getCurTimeSecs() - startTime, g);
                    return;
                } else {
                    openList.push(g);
                    closedList.add(g);
                }
            }
        }
        makeOutput(openLstPops, getCurTimeSecs() - startTime, null);
    }
}
