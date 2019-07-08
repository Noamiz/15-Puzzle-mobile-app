package com.puzzlesolver.android;

import java.util.*;

public class IDASTAR extends SearchAlgo{

    private double maxThreshold;

    IDASTAR(State startState, double maxThreshold) {
        super(startState);
        this.maxThreshold = maxThreshold;
    }

    private double getMinCandidate(ArrayList<Double> candidates) {
        double min = candidates.get(0);
        for (Double d : candidates) {
            min = Math.min(d, min);
        }
        return min;
    }

    public void run() {
        //for output only
        int openLstPops = 0;
        double startTime = getCurTimeSecs();
        // check if the goal state is the starting state
        if (startState.isGoal()) {
            makeOutput(0, getCurTimeSecs() - startTime, startState);
            return;
        }
        Stack<State> openList = new Stack<>();
        HashMap<State, Double> openListHash = new HashMap<>();
        ArrayList<Double> nextThresholdCandidates = new ArrayList<>();

        double costThreshold = startState.heursticToGoal;
        while(costThreshold < maxThreshold) {
            openList.add(startState);
            openListHash.put(startState, startState.costFromRoot_plus_heuristicToGoal());
            while (!openList.isEmpty()) {
                openLstPops++;
                State curState = openList.pop();
                openListHash.remove(curState);
                for (String stepStr : STEPS_REVERSED) {
                    Step step = Step.valueOf(stepStr);
                    State g = curState.makeState(step);
                    if (g == null || // if can't apply the operator ignore it
                            curState.isInPathAndAlreadyCheaper(g) ||
                            isClosedListContains(openListHash, g)) continue;
                    //The cost threshold increases in each iteration to
                    //the total cost of the lowest-cost node that was
                    //pruned during the previous iteration
                    if (g.costFromRoot_plus_heuristicToGoal() > costThreshold) {
                        nextThresholdCandidates.add(g.costFromRoot_plus_heuristicToGoal());
                        continue;
                    }
                    if (g.isGoal()) {
                        makeOutput(openLstPops,getCurTimeSecs() - startTime, g);
                        return;
                    } else {
                        openList.add(g);
                        openListHash.put(g, g.costFromRoot_plus_heuristicToGoal());

                    }
                }
            }
            // no new candidates, hence no new nodes are available so stop the search
            if(nextThresholdCandidates.isEmpty()){
                break;
            }
            costThreshold = getMinCandidate(nextThresholdCandidates);
            nextThresholdCandidates.clear();
        }
        // reached the max threshold and no goal found
        makeOutput(openLstPops,getCurTimeSecs() - startTime, null);
    }
}
