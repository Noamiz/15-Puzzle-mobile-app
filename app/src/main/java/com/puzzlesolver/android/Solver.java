package com.puzzlesolver.android;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum ProblemIdentifier {initBoard, costHalf, cost2, someCost2cost05Same}


public class Solver {
    class SolverThread extends Thread {
        public void run() {
            ips.initOutput();
            SearchAlgo alg = null;

            Log.d("myTag", algo.toString());

            if (algo == Algo.BFS) {
                alg = new BFS(startState);
                alg.run();
            } else if (algo == Algo.DFID) {
                alg = new DFID(startState, 350);
                alg.run();
            } else if (algo == Algo.ASTAR) {
                alg = new ASTAR(startState);
                alg.run();
            } else if (algo == Algo.IDASTAR) {
                alg = new IDASTAR(startState, 320);
                alg.run();
            }
            else if (algo == Algo.DFS) {
                alg = new DFS(startState);
                alg.run();
            }
            else if (algo == Algo.Dijkstra) {
                alg = new Dijkstra(startState);
                alg.run();
            }
            else if (algo == Algo.Greedy) {
                alg = new Greedy(startState);
                alg.run();
            }
            else if (algo == Algo.DFS_RECURSIVE) {
                alg = new DFS_RECURSIVE(startState);
                alg.run();
            }
            else if (algo == Algo.GDSMASTAR) {
                alg = new GDSMASTAR(startState);
                alg.run();
            }
            else if (algo == Algo.TWASTAR) {
                alg = new TWASTAR(startState);
                alg.run();
            }
            else if (algo == Algo.BFS_NCL) {
                alg = new BFS_NCL(startState);
                alg.run();
            }
            else if (algo == Algo.DFS_NCL) {
                alg = new DFS_NCL(startState);
                alg.run();
            }
            else if (algo == Algo.Dijkstra_NCL) {
                alg = new Dijkstra_NCL(startState);
                alg.run();
            }
            else if (algo == Algo.ASTAR_NCL) {
                alg = new ASTAR_NCL(startState);
                alg.run();
            }
            else {//if (algo == Algo.DFBNB) {
                alg = new DFBnB(startState, 9999999.0);
                alg.run();
            }
            ips.showResult(alg.output);
        }
    }

    private State startState;
    private Algo algo;
    private UI_IO ips;

    Solver(UI_IO ips) throws InputException {
        initialInputCheck(ips.initialBoard, ips.costHalf, ips.cost2);
        checkParseData(ips);
        this.ips = ips;
    }

    void solve() {
        SolverThread st = new SolverThread();
        st.start();
    }

    private void checkParseData(UI_IO ips) throws InputException {
        algo = Algo.values()[ips.idxOfSelectedAlg];

        List<Integer> cost2 = new ArrayList<>();
        List<Integer> costHalf = new ArrayList<>();

        // Parse the 0.5 cost blocks
        String[] halfCostsArr = ips.costHalf.replaceAll("\\s+", "").split(",");
        if (!ips.costHalf.equals("")) {
            for (String s : halfCostsArr) {
                costHalf.add(Integer.parseInt(s));
            }
        }
        // Parse the 2 cost blocks
        String[] twoCostsArr = ips.cost2.replaceAll("\\s+", "").split(",");
        if (!ips.cost2.equals("")) {
            for (String s : twoCostsArr) {
                cost2.add(Integer.parseInt(s));
            }
        }

        // Verify the array of blocks costing 0.5 and
        // array of blocks costing 2 don't have the same blocks
        for (int i : cost2) {
            if (costHalf.contains(i)) {
                throw new InputException(ProblemIdentifier.someCost2cost05Same);
            }
        }
        if(hasDuplicate(cost2)){
            throw new InputException(ProblemIdentifier.cost2);
        }
        if(hasDuplicate(costHalf)){
            throw new InputException(ProblemIdentifier.costHalf);
        }
        checkParseMatrix(costHalf, cost2, ips);
    }

    private void checkParseMatrix(List<Integer> costHalf, List<Integer> cost2, UI_IO ips)
            throws InputException {
        int emptyXpos = 0, emptyYpos = 0;
        ArrayList<ArrayList<Integer>> tempBoard = new ArrayList<>();
        BufferedReader bufReader = new BufferedReader(new StringReader(ips.initialBoard));
        String line;
        try {
            while ((line = bufReader.readLine()) != null) {
                line = line.replace("\n", "");
                ArrayList<Integer> temp = new ArrayList<>();
                for (String s : line.split(",")) {
                    try {
                        temp.add(Integer.parseInt(s));
                    } catch (Exception e) {
                        if (s.equals("_")) {
                            temp.add(0);
                        } else {
                            throw new InputException(ProblemIdentifier.initBoard);
                        }
                    }
                }
                tempBoard.add(temp);
            }
        } catch (IOException e) {

        }

        // Convert to block array
        Block[][] startStateMat = new Block[tempBoard.size()][tempBoard.get(0).size()];
        int i = 0;
        for (ArrayList<Integer> row : tempBoard) {
            int j = 0;
            for (int num : row) {
                Block b = null;
                if (num == 0) {
                    b = new Block(-1, Color.COST1);
                    emptyXpos = j;
                    emptyYpos = i;
                } else if (cost2.contains(num)) {
                    b = new Block(row.get(j), Color.COST2);
                } else if (costHalf.contains(num)) {
                    b = new Block(row.get(j), Color.COSTHALF);
                } else {
                    b = new Block(row.get(j), Color.COST1);
                }
                startStateMat[i][j] = b;
                j++;
            }
            i++;
        }

        // Verify that blocks' array dimensions are fine
        int n = startStateMat.length;
        int m = startStateMat[0].length;
        for (Block[] bArr : startStateMat) {
            if (bArr.length != m) {
                throw new InputException(ProblemIdentifier.initBoard);
            }
        }
        // Verify that the lists of blocks of cost 2 and blocks of cost 0.5
        // don't exceed the max block number in the board
        int res = State.areCost2Cost05Valid(startStateMat, costHalf, cost2);
        if (res == 1) {
            throw new InputException(ProblemIdentifier.costHalf);
        }
        if (res == 2) {
            throw new InputException(ProblemIdentifier.cost2);
        }
        if (!State.areNumbersValid(startStateMat)) {
            throw new InputException(ProblemIdentifier.initBoard);
        }
        startState = new State(startStateMat,
                null, null, emptyXpos, emptyYpos, 0, 0);
    }

    private void initialInputCheck(String initialBoard, String costHalf, String cost2)
            throws InputException {
        // This pattern needs to match costs input and every line of the initial board
        Pattern pattern = Pattern.compile("^(\\d+,)*\\d+$");

        // Check that the input for the initial board has the correct pattern in each line
        // Replace to 0 to make it easier to match a pattern with numbers only
        initialBoard = initialBoard.replace("_", "0");
        BufferedReader bufReader = new BufferedReader(new StringReader(initialBoard));
        String line = null;
        int counterOfZeros = 0;
        try {
            while ((line = bufReader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (line.contains("0")) counterOfZeros++;
                if (!matcher.find() || counterOfZeros > 1) {
                    throw new InputException(ProblemIdentifier.initBoard);
                }
            }
        } catch (IOException e) {
        }
        if (counterOfZeros != 1) {
            throw new InputException(ProblemIdentifier.initBoard);
        }
        // Check that the two inputs of other cost blocks have correct patterns
        Matcher matcher = pattern.matcher(costHalf);
        if (!costHalf.isEmpty() && !matcher.find()) {
            throw new InputException(ProblemIdentifier.costHalf);
        }
        matcher = pattern.matcher(cost2);
        if (!cost2.isEmpty() && !matcher.find()) {
            throw new InputException(ProblemIdentifier.cost2);
        }
    }

    public boolean hasDuplicate(List<Integer> list) {
        Set<Integer> set = new HashSet<>(list);
        return set.size() != list.size();
    }
}
