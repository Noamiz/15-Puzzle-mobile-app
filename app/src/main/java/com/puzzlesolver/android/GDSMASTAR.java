package com.puzzlesolver.android;

// Greedy SMA star

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class GDSMASTAR extends SearchAlgo {

    GDSMASTAR(State startState) {
        super(startState);
    }

    public void run() {
        //for output only
        int openLstPops = 0;
        double startTime = getCurTimeSecs();

        PriorityQueue<State> openList = new PriorityQueue<>(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return State.compareCostThenTimeThenStepFromParent(o1,o2);
            }
        });

        PriorityQueue<State> minPopLst = new PriorityQueue<>(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return State.compareCostThenTimeThenStepFromParent(o1,o2);
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
                    minPopLst.add(g);
                    closedList.put(g, g.costFromRoot_plus_heuristicToGoal());
                }
                if(!minPopLst.isEmpty()) {
                    State FirstMinState = minPopLst.remove();
                    openList.add(FirstMinState);
                }
            }
        }
        // open list is empty, and no goal found
        makeOutput(openLstPops, getCurTimeSecs() - startTime, null);
    }
}