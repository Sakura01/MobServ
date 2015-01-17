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
import com.kawtar.listshopping.Dijkstra;
import com.kawtar.listshopping.Graph;
import com.kawtar.listshopping.Link;
import com.kawtar.listshopping.ProductToSend;
import com.shopping.list.bean.Product;

import java.util.LinkedList;

public class ShoppingListFragment extends Fragment {
    private List<ProductToSend> nodes;
    private List<Link> links;
    private  OnShoppingItemSelectedListener mListener;
    private final String TAG_SHOPPING_LIST_FRAGMENT="ShoppingListFragment";
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

    private void displayListView() {
        String supermarketName=OutdoorMapActivity.superMarketMap;
        List<ResponseFromServer> offer=null;// RequestActivity.getOfferFromServer();
            List<ProductToSend> list=new ArrayList<ProductToSend>();
            if(offer.size()!=0)
            {
                for(int i=0;i<offer.size();i++)
                {
                    if(offer.get(i).getSuperMarket().getIndoorMapUrl().equals(supermarketName))
                    {
                        list=offer.get(i).getList();
                        break;
                    }
                }
                //List to sort out
                nodes = new ArrayList<ProductToSend>();
                links = new ArrayList<Link>();
                for (int i = 0; i < list.size(); i++) {

                    ProductToSend location = new ProductToSend(list.get(i).getName(), list.get(i).getName(),list.get(i).getQuantity(),list.get(i).getUnit(),list.get(i).getPositionx(),list.get(i).getPositiony(),list.get(i).getPrice(),false);
                    nodes.add(location);
                }
                for (int j=0; j<list.size(); j++){
                    for(int k=0; k<list.size();k++){
                        if(k!=j){
                            addLane("Edge"+(j+k),j,k);
                        }

                    }
                }

                // Lets check from location Loc_1 to Loc_10
                Graph graph = new Graph(nodes, links);
                Dijkstra dijkstra = new Dijkstra(graph);
                dijkstra.execute(nodes.get(0));
                LinkedList<ProductToSend> path = dijkstra.getPath(nodes.get(10));


                List<ProductToSend>listSorted=new ArrayList<ProductToSend>();
                for (ProductToSend vertex : path) {
                    System.out.println(vertex);
                    listSorted.add(vertex);

                }
                //create an ArrayAdaptar from the String Array
                ArrayAdapter<ProductToSend> dataAdapter = new ArrayAdapter<ProductToSend>(getActivity(),
                        R.layout.shopping_list_fragment, listSorted);//list
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
    private void addLane(String laneId, int sourceLocNo, int destLocNo) {
        Link lane = new Link(laneId,nodes.get(sourceLocNo), nodes.get(destLocNo));
        links.add(lane);
    }
}