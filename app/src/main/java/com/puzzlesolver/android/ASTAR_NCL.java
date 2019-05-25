package com.puzzlesolver.android;

import java.util.*;

public class ASTAR_NCL extends SearchAlgo {

    ASTAR_NCL(State startState) {
        super(startState);
    }

    public void run() {
        //for output only
        int openLstPops = 0;
        double startTime = getCurTimeSecs();

        PriorityQueue<State> openList = new PriorityQueue<>(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return State.compareCostThenTimeThenStepFromParent(o1, o2);
            }
        });


        openList.add(startState);

        while (!openList.isEmpty()) {
            openLstPops++;
            State curState = openList.remove();
            if (curState.isGoal()) {
                makeOutput(openLstPops, getCurTimeSecs() - startTime, curState);
                return;
            } else {
                for (Step step : Step.values()) {
                    State g = curState.makeState(step);
                    if (g == null) continue;
                    openList.add(g);
                }
            }
        }
        // open list is empty, and no goal found
        makeOutput(openLstPops, getCurTimeSecs() - startTime, null);
    }
}

