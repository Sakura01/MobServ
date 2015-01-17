package com.kawtar.jsoncontrol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.kawtar.listshopping.ProductToSend;


public class RequestList {
	private List<ProductToSend> mList;
    private int mDistanceRange;     
    private int mBudget; 
    private static boolean mAccept;
    private double mLatitude;
    private double mLongitude;
    public RequestList(List<ProductToSend> list,int distanceRange, int budget,double latitude,double longitude)
    {         
    	mList=new ArrayList<ProductToSend>();
    	mList=list;
    	mDistanceRange = distanceRange; 
    	mBudget = budget;
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
    public void setList(ProductToSend product)
    {
    	mList.add(product);
    }
    public List<ProductToSend> getList()
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
