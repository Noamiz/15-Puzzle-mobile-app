package com.puzzlesolver.android;

import java.util.LinkedList;
import java.util.Queue;
import java.util.HashSet;

public class BFS extends SearchAlgo {

    BFS(State startState) {
        super(startState);
    }

    public void run() {
        int openLstPops = 0;
        double startTime = getCurTimeSecs();

        Queue<State> openList = new LinkedList<>();
        HashSet<State> closedList = new HashSet<>();
        // check if the goal state is the starting state
        if (startState.isGoal()) {
            makeOutput(0, getCurTimeSecs() - startTime, startState);
            return;
        }
        openList.add(startState);
        closedList.add(startState);
        while (!openList.isEmpty()) {
            openLstPops++;
            State curState = openList.remove();
            for (Step step : Step.values()) {
                State g = curState.makeState(step);
                if (g == null || closedList.contains(g))
                    continue; // if can't apply the operator or if was developed ignore it
                if (g.isGoal()) {
                    makeOutput(openLstPops,getCurTimeSecs() - startTime, g);
                    return;
                } else {
                    openList.add(g);
                    closedList.add(g);
                }
            }
        }
        makeOutput(openLstPops, getCurTimeSecs() - startTime, null);
    }
}
