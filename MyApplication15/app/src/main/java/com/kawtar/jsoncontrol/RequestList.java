package com.kawtar.jsoncontrol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kawtar.listshopping.Product;


public class RequestList {
	private List<Product> mList;   
    private int mDistanceRange;     
    private int mBudget; 
    private static boolean mAccept;
    private double mLatitude;
    private double mLongitude;
    public RequestList(List<Product> list,int distanceRange, int budget,double latitude,double longitude,boolean accept)  
    {         
    	mList=new ArrayList<Product>();
    	mList=list;
    	mDistanceRange = distanceRange; 
    	mBudget = budget;
    	mAccept=accept;
    	mLatitude=latitude;
    	mLongitude=longitude;
    }
    public double getLatitude()
    {
    	return mLatitude;
    }
    public double getLongitude()
    {
    	return mLongitude;
    }
    public void setList(Product product)
    {
    	mList.add(product);
    }
    public List<Product> getList()
    {
    	return mList;
    }
    public int getBudget()
    {
    	return mBudget;
    }
    public int getDistanceRange()
    {
    	return mDistanceRange;
    }
    public boolean getAcceptFlag()
    {
    	
    	return mAccept;
    }
    public String toJSON(){

        
        JSONObject jsonResponse= new JSONObject();
        JSONArray jsonArray=new JSONArray(); 
        try {
		    	jsonResponse.put("distancerange", String.valueOf(getDistanceRange()));
		    	jsonResponse.put("budget", String.valueOf(getBudget()));
		    	jsonResponse.put("latitudeU", String.valueOf(getLatitude()));
		    	jsonResponse.put("longitudeU", String.valueOf(getLongitude()));
        	  for(int i=0;i<getList().size();i++)
              {
        		  JSONObject jsonObject= new JSONObject();
        		  jsonObject=getList().get(i).toJSON();
        		  jsonArray.put(jsonObject);
              }
        	  jsonResponse.put("list",jsonArray);
            return jsonResponse.toString();
        } catch (JSONException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }


}
