package com.kawtar.indoornavigation;
/**
 * Created by Kawtar on 11/27/2014.
 */
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import com.example.kawtar.myapplication.OutdoorMapActivity;
import com.example.kawtar.myapplication.R;
import com.kawtar.jsoncontrol.ResponseFromServer;
import com.kawtar.listshopping.ProductToSend;
import com.kawtar.listshopping.TSPEnvironment;
import com.kawtar.listshopping.TabuList;
import com.kawtar.listshopping.TabuSearch;
import com.shopping.list.ShoppinglistActivity;

public class ShoppingListFragment extends Fragment {
    private  OnShoppingItemSelectedListener mListener;
    private final String TAG_SHOPPING_LIST_FRAGMENT="ShoppingListFragment";
    private static List<ProductToSend> list=new ArrayList<ProductToSend>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG_SHOPPING_LIST_FRAGMENT, "onCreate()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG_SHOPPING_LIST_FRAGMENT, "onActivityCreated().");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");

        //Generate list View from ArrayList
        displayListView();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG_SHOPPING_LIST_FRAGMENT, "onCreateView()");
        Log.v("ListContainer", container == null ? "true" : "false");
        Log.v("ListsavedInstanceState", savedInstanceState == null ? "true" : "false");
        if (container == null) {
            return null;
        }
        View view = inflater.inflate(R.layout.list_shopping_indoor_view, container, false);
        return view;
    }


    // Container Activity must implement this interface
    public interface OnShoppingItemSelectedListener {
        public void onShoppingItemSelected(ProductToSend mSelectedFromList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnShoppingItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnShoppingItemSelectedListener");
        }
    }

    public void tsp() {

        TSPEnvironment tspEnvironment = new TSPEnvironment();
        tspEnvironment.distances = //Distance matrix, 5x5, used to represent distances
                new int[][]{{0, 1, 3, 4, 5},
                        {1, 0, 1, 4, 8},
                        {3, 1, 0, 5, 1},
                        {4, 4, 5, 0, 2},
                        {5, 8, 1, 2, 0}};
        //Between cities. 0,1 represents distance between cities 0 and 1, and so on.
        //initial solution
        //city numbers start from 0
        // the first and last cities' positions do not change
        int dim=getList().size();
        List<ProductToSend>lit=getList();
        int[] currSolution = new int[dim];
        for(int j=0;j<lit.size();j++)
        {
            ProductToSend p=lit.get(j);
            p.setId(j);
            Log.i("ele id",""+p.getId());
            currSolution[j] = p.getId();
            Log.i("Init mat",""+currSolution);
        }
        //Compute weights
        int[][] mat = new int[dim][dim];
        Log.i("Dim",""+dim);
        for(int k=0;k<dim;k++)
        {
            for(int l=0;l<dim;l++)
            {
              double weight=Math.sqrt(Math.pow(getList().get(l).getPositionx()-getList().get(k).getPositionx(),2)+Math.pow(getList().get(l).getPositiony()-getList().get(k).getPositiony(),2));
              Log.i("weight",""+weight);
              int conv=(int) weight;
              mat[k][l]=conv;
              Log.i("Matrix values",""+mat[k][l]);
            }
        }
        tspEnvironment.distances=mat;

        int numberOfIterations = 100;
        int tabuLength = 10;
        TabuList tabuList = new TabuList(tabuLength);

        int[] bestSol = new int[currSolution.length]; //this is the best Solution So Far
        System.arraycopy(currSolution, 0, bestSol, 0, bestSol.length);
        int bestCost = tspEnvironment.getObjectiveFunctionValue(bestSol);

        for (int i = 0; i < numberOfIterations; i++) { // perform iterations here

            currSolution = TabuSearch.getBestNeighbour(tabuList, tspEnvironment, currSolution);
            //printSolution(currSolution);
            int currCost = tspEnvironment.getObjectiveFunctionValue(currSolution);

            //System.out.println("Current best cost = " + tspEnvironment.getObjectiveFunctionValue(currSolution));

            if (currCost < bestCost) {
                System.arraycopy(currSolution, 0, bestSol, 0, bestSol.length);
                bestCost = currCost;
            }
        }

        System.out.println("Search done! \nBest Solution cost found = " + bestCost + "\nBest Solution :");

        printSolution(bestSol);



    }
    public static List<ProductToSend> getList()
    {
        String supermarketName=OutdoorMapActivity.superMarketMap;
        String result= ShoppinglistActivity.getResultServer();
        List<ResponseFromServer> offer= ResponseFromServer.parseJSONResult(result);
        List<ProductToSend>lit=new ArrayList<ProductToSend>();
        if(offer.size()!=0) {
            for (int i = 0; i < offer.size(); i++) {
                if (offer.get(i).getSuperMarket().getIndoorMapUrl().equals(supermarketName)) {
                    lit = offer.get(i).getSuperMarket().getList();

                    break;
                }
            }
        }
        return lit;
    }
    public static void printSolution(int[] solution) {
        String path="";
        //List<ProductToSend> tmp=new ArrayList<ProductToSend>();
        //for (int i = 0; i < solution.length; i++)
        //{
            //ProductToSend produ=getList().get(i);
            //if(produ.getId()==solution[i]) {
             //   tmp.add(produ);
           // }
         //   Log.i("TMP list",tmp.get(i).getName());
       // }
    }
    private void displayListView() {
        String supermarketName=OutdoorMapActivity.superMarketMap;
        Log.i("Carte fragment list",supermarketName);
        String result= ShoppinglistActivity.getResultServer();
        List<ResponseFromServer> offer= ResponseFromServer.parseJSONResult(result);
        if(offer.size()!=0)
            {
               for(int i=0;i<offer.size();i++)
              {
                    if(offer.get(i).getSuperMarket().getIndoorMapUrl().equals(supermarketName))
                    {
                       list=offer.get(i).getSuperMarket().getList();

                        break;
                    }
                }
                //create an ArrayAdaptar from the String Array
                tsp();
                ArrayAdapter<ProductToSend> dataAdapter = new ArrayAdapter<ProductToSend>(getActivity(),
                        R.layout.shopping_list_fragment, list);//list
                final ListView listView = (ListView) getView().findViewById(R.id.listofShoppingItems);
                // Assign adapter to ListView
                listView.setAdapter(dataAdapter);
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        // Send the URL to the host activity
                        ProductToSend mSelectedFromList = (ProductToSend) (listView.getItemAtPosition(position));
                        mSelectedFromList.setMarker(true);
                        mListener.onShoppingItemSelected(mSelectedFromList);
                    }
                });
            }
            else
            {
                createDialog("Error offer", "A problem has occured, sorry");
            }



    }

    public  void createDialog(final String title, String text) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());
        // Button to create an alert dialog for setting the connection to the server
        ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

               // Intent activity=new Intent(getActivity(), RequestActivity.class);
                //startActivity(activity);

            }
        });
        // Set the alert dialog title, create it and show it
        ad.setTitle(title + ":" + text);
        AlertDialog alert1 = ad.create();
        alert1.show();
    }

}