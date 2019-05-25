package com.puzzlesolver.android;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;


enum Algo {
    BFS, DFID, ASTAR, IDASTAR, DFBNB, DFS, Dijkstra, Greedy, DFS_RECURSIVE, GDSMASTAR, TWASTAR, ASTAR_NCL, BFS_NCL, DFS_NCL, Dijkstra_NCL, ERROR
}

public class UI_IO {
    MainActivity act;
    // The input data to pass to the solver
    ProblemIdentifier prob;
    String initialBoard;
    String costHalf;
    String cost2;
    int idxOfSelectedAlg;

    UI_IO(MainActivity act) {
        this.act = act;
        // Initialize all the string with the inputs from the UI
        initialBoard = ((EditText) act.findViewById(R.id.edittxtBoard)).getText().toString();
        costHalf = ((EditText) act.findViewById(R.id.edittxtHalf)).getText().toString();
        cost2 = ((EditText) act.findViewById(R.id.edittxtCost2)).getText().toString();

        // Get the index of the selected radio button
        RadioGroup rg = ((RadioGroup) act.findViewById(R.id.radioGroupAlgs));
        View selectedRadioButton = rg.findViewById(rg.getCheckedRadioButtonId());
        idxOfSelectedAlg = rg.indexOfChild(selectedRadioButton);

        RadioGroup rg2 = ((RadioGroup) act.findViewById(R.id.hChooser));
        View selectedRadioButton2 = rg2.findViewById(rg2.getCheckedRadioButtonId());
        State.iHeu = rg2.indexOfChild(selectedRadioButton2);
        ///Log.d("go2", String.valueOf(State.iHeu));
    }

    public void initOutput(){
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText editTextOutput = act.findViewById(R.id.edittxtOutput);
                editTextOutput.setText(R.string.solving);
            }
        });
    }

    public void showResult(final String output){
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText editTextOutput = act.findViewById(R.id.edittxtOutput);
                editTextOutput.setText(output);
            }
        });
    }
}
