package com.puzzlesolver.android;

public class DFID extends SearchAlgo {
    private State goalState;
    //for output only
    private int openLstPops;
    private double startTime;
    private int maxLevel;

    DFID(State startState, int maxLevel) {
        super(startState);
        goalState = null;
        openLstPops = 0;
        this.maxLevel = maxLevel;
    }

    public void run() {
        startTime = getCurTimeSecs();
        // check if the goal state is the starting state
        if (startState.isGoal()) {
            makeOutput(0, getCurTimeSecs() - startTime, startState);
            return;
        }
        try{
            for (int i = 1; i < maxLevel && goalState == null; i++) {
                recursiveAlgo(i, startState);
            }
            makeOutput(openLstPops, getCurTimeSecs() - startTime, goalState);
        } catch (Exception e){}

    }

    private void recursiveAlgo(int level, State curState) throws Exception{
        if (level > 0) {
            openLstPops++;
            for (Step step : Step.values()) {
                State g = curState.makeState(step);
                if (g == null || // if can't apply the operator ignore it
                        curState.isInPath(g)) continue;
                if (g.isGoal()) {
                    makeOutput(openLstPops, getCurTimeSecs() - startTime, g);
                    goalState = g;
                    // breaking out of the recursion
                    throw new Exception();
                } else {
                    recursiveAlgo(level - 1, g);
                }
            }
        }
    }
}
