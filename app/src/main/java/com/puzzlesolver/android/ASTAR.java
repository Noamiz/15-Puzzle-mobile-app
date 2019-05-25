package com.puzzlesolver.android;

import java.util.*;

public class ASTAR extends SearchAlgo {

    ASTAR(State startState) {
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

        HashMap<State, Double> closedList = new HashMap<>();

        openList.add(startState);
        closedList.put(startState, startState.costFromRoot_plus_heuristicToGoal());

        while (!openList.isEmpty()) {
            openLstPops++;
            State curState = openList.remove();
            if (curState.isGoal()) {
                makeOutput(openLstPops, getCurTimeSecs() - startTime, curState);
                return;
            } else {
                for (Step step : Step.values()) {
                    State g = curState.makeState(step);
                    if (g == null || isClosedListContains(closedList, g)) continue;
                    openList.add(g);
                    closedList.put(g, g.costFromRoot_plus_heuristicToGoal());
                }
            }
        }
        // open list is empty, and no goal found
        makeOutput(openLstPops, getCurTimeSecs() - startTime, null);
    }
}

