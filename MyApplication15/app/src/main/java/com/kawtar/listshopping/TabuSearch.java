package com.kawtar.listshopping;

/**
 * Created by Kawtar on 2/10/2015.
 */

public class TabuSearch {

    public static int[] getBestNeighbour(TabuList tabuList,
                                         TSPEnvironment tspEnviromnet,
                                         int[] initSolution,int dim) {


        int[] bestSol = new int[initSolution.length]; //this is the best Solution So Far
        System.arraycopy(initSolution, 0, bestSol, 0, bestSol.length);
        int bestCost = tspEnviromnet.getObjectiveFunctionValue(initSolution);
        int city1 = 0;
        int city2 = 0;
        boolean firstNeighbor = true;

        for (int i = 1; i < bestSol.length - 1; i++) {
            for (int j = 2; j < bestSol.length - 1; j++) {
                if (i == j) {
                    continue;
                }

                int[] newBestSol = new int[bestSol.length]; //this is the best Solution So Far
                System.arraycopy(bestSol, 0, newBestSol, 0, newBestSol.length);

                newBestSol = swapOperator(i, j, initSolution); //Try swapping cities i and j
                // , maybe we get a bettersolution
                int newBestCost = tspEnviromnet.getObjectiveFunctionValue(newBestSol);



                if ((newBestCost > bestCost || firstNeighbor) && tabuList.tabuList[i][j] == 0) { //if better move found, store it
                    firstNeighbor = false;
                    city1 = i;
                    city2 = j;
                    System.arraycopy(newBestSol, 0, bestSol, 0, newBestSol.length);
                    bestCost = newBestCost;
                }


            }
        }

        if (city1 != 0) {
            tabuList.decrementTabu();
            tabuList.tabuMove(city1, city2,dim);
        }
        return bestSol;


    }

    //swaps two cities
    public static int[] swapOperator(int city1, int city2, int[] solution) {
        int temp = solution[city1];
        solution[city1] = solution[city2];
        solution[city2] = temp;
        return solution;
    }

}
