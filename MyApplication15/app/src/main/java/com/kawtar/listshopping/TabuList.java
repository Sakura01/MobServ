package com.kawtar.listshopping;

/**
 * Created by Kawtar on 2/10/2015.
 */
public class TabuList {

    int [][] tabuList ;
    public TabuList(int numCities){
        tabuList = new int[numCities][numCities]; //city 0 is not used here, but left for simplicity
    }

    public void tabuMove(int city1, int city2,int dim){ //tabus the swap operation
        tabuList[city1][city2]+=dim;
        tabuList[city2][city1]+= dim;

    }

    public void decrementTabu(){
        for(int i = 0; i<tabuList.length; i++){
            for(int j = 0; j<tabuList.length; j++){
                tabuList[i][j]-=tabuList[i][j]<=0?0:1;
            }
        }
    }

}