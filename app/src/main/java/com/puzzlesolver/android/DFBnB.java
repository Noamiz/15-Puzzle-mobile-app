package com.puzzlesolver.android;

import java.util.*;

public class DFBnB extends SearchAlgo {
    private double startThreshold;

    DFBnB(State startState, double startThreshold) {
        super(startState);
        this.startThreshold = startThreshold;
    }

    public void run() {
        //for output only
        int openLstPops = 0;
        double startTime = getCurTimeSecs();

        Stack<State> openList = new Stack<>();
        // make the comparing function reversed to add the states to the stack from the most expensive to the cheapest
        PriorityQueue<State> curBranchOrdering = new PriorityQueue<>(new Comparator<State>() {
            @Override
            public int compare(State o1, State o2) {
                return -1*State.compareCostThenTimeThenStepFromParent(o1,o2);
            }
        });
        HashMap<State, Double> openListHash = new HashMap<>();

        // check if the goal state is the starting state
        if (startState.isGoal() && startState.costFromRoot <= startThreshold) {
            makeOutput(0, getCurTimeSecs() - startTime, startState);
            return;
        }

        openList.add(startState);
        openListHash.put(startState, startState.costFromRoot_plus_heuristicToGoal());
        double alpha = startThreshold;
        State bestGoalState = null;

        while (!openList.isEmpty()) {
            openLstPops++;
            State curState = openList.pop();
            openListHash.remove(curState);
            for (String stepStr : STEPS_REVERSED) {
                Step step = Step.valueOf(stepStr);
                State g = curState.makeState(step);
                if (g == null || curState.isInPathAndAlreadyCheaper(g) ||
                        g.costFromRoot_plus_heuristicToGoal() >= alpha ||
                        isClosedListContains(openListHash, g)) continue;
                // if I reached a goal state then it's cost is surely cheaper than alpha
                if (g.isGoal()) {
                    alpha = g.costFromRoot;
                    bestGoalState = g;
                } else {
                    curBranchOrdering.add(g);
                }
            }
            addToOpenListBigToSmall(openList, openListHash, curBranchOrdering);
            curBranchOrdering.clear();
        }
        makeOutput(openLstPops, getCurTimeSecs() - startTime, bestGoalState);
    }

    private void addToOpenListBigToSmall (Stack<State> openList, HashMap<State, Double> openListHash,
                                          PriorityQueue<State> curBranchOrdering){
        int size = curBranchOrdering.size();
        for(int i = 0;i<size;i++){
            State cur = curBranchOrdering.poll();
            openListHash.put(cur, cur.costFromRoot_plus_heuristicToGoal());
            openList.add(cur);
        }
    }
}
