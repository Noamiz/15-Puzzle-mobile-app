package com.puzzlesolver.android;

public class InputException extends Exception {
    ProblemIdentifier prob;

    InputException(ProblemIdentifier prob){
        this.prob = prob;
    }
}
