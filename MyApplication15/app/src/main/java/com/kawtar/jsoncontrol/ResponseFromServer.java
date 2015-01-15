package com.kawtar.jsoncontrol;

import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.kawtar.listshopping.Product;

public class ResponseFromServer 
{
	private SuperMarket mSuperMarket;
	private float mTotal;
	private static List<Product> listSh;
	private float mDistance;
    private static String toParse;
    private static List<ResponseFromServer> offers;
    private static ResponseFromServer responseOffers;
    private static SuperMarket superMarket;
    private static List<Product>lip;
	public ResponseFromServer(List<Product> list,SuperMarket supermarket,float total,float distance )
	{         
			listSh=new ArrayList<Product>();
			listSh=list;
	    	mTotal=total;
	    	mDistance=distance;
	    	mSuperMarket=new SuperMarket("",0.0,0.0,"");
	    	mSuperMarket=supermarket;
    }

	public SuperMarket getSuperMarket()
    {
		return mSuperMarket;
    }


	public float getDistance()
	{
		return mDistance;
	}


	public float getTotal()
	{
		return mTotal;
	}
	public  List<Product> getList()
	{
    	return listSh;
	}
	public void setList(List<Product> list)
	{
		listSh=list;
	}
	public static List<ResponseFromServer> parseJSONResult(String result)
	{

		try
	    {
          offers=new ArrayList<ResponseFromServer>();
		  JSONArray jarray=new JSONArray(result);
	      for(int i=0;i<jarray.length();i++)
	     {
	    	     JSONArray jb= jarray.getJSONArray(i);

	             for(int k=0;k<jb.length();k++)
	             {

	            	 JSONObject jbo=jb.getJSONObject(k);
		             String totalPrice =  jbo.getString("total_price");

                     String supermarketName =  jbo.getString("name");
                     String supermarketMapUrl= jbo.getString("supermarket_localMap");
                     String supermarketDistance= jbo.getString("distance");
                     String supermarketPositionX =  jbo.getString("supermarket_positionX");
                     String supermarketPositionY =  jbo.getString("supermarket_positionY");
                     superMarket=new SuperMarket(supermarketName,Double.parseDouble(supermarketPositionX),Double.parseDouble(supermarketPositionY),supermarketMapUrl);
                     JSONArray listShopping = jbo.getJSONArray("list");
                     lip=getParsing(listShopping);
                     responseOffers=new ResponseFromServer(lip,superMarket,round(Float.parseFloat(totalPrice), 2),round(Float.parseFloat(supermarketDistance), 2));
                     offers.add(responseOffers);
                 }
         }

	   }
       catch(Exception e)
	   {
		   e.printStackTrace();
		   return null;
	   }
    return offers;
    }
    public static float round(float value, int places) {
        //here 2 means 2 places after decimal
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (float) tmp / factor;
    }
    public static List<Product> getParsing(JSONArray listShopping)
    {
        List <Product>lit=new ArrayList<Product>();
        try {
            for(int j=0;j<listShopping.length();j++)
            {
                Product p;
                JSONObject jb2 = listShopping.getJSONObject(j);
                String name = jb2.getString("name");
                String brand = jb2.getString("brand");
                String color = jb2.getString("color");
                String quantity = jb2.getString("quantity");
                String initQuantity=jb2.getString("init_quantity");
                String price = jb2.getString("price");
                String acceptProduct=jb2.getString("accept_product");
                String product_positionx = jb2.getString("product_positionx");
                String product_positiony = jb2.getString("product_positiony");
                int q=Integer.parseInt(quantity);
                if(q!=-1)
                {
                    if(Integer.parseInt(initQuantity)>q)
                    {

                        String status=q+"max available";
                        p=new Product(name,brand,color,Integer.parseInt(initQuantity),Float.parseFloat(product_positionx),Float.parseFloat(product_positiony),round( Float.parseFloat(price), 2),status,Boolean.valueOf(acceptProduct),false);


                    }
                    else
                    {
                        String status="Ok";
                        p=new Product(name,brand,color,Integer.parseInt(initQuantity), Float.parseFloat(product_positionx),Float.parseFloat(product_positiony),round( Float.parseFloat(price), 2),status,Boolean.valueOf(acceptProduct),false);

                    }

                }
                else
                {
                    String status="Missing";
                    p=new Product(name,brand,color,0, Float.parseFloat(product_positionx),Float.parseFloat(product_positiony),round( Float.parseFloat(price), 2),status,Boolean.valueOf(acceptProduct),false);

                }
                lit.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lit;
    }
    public static List<Product> parseJSONResultFalse(String result)
	{
		try
	    {
		  List<Product> list=new ArrayList<Product>();
		  JSONArray jarray=new JSONArray(result);
	      for(int i=0;i<jarray.length();i++)
	      {

	            	 JSONObject jbo=jarray.getJSONObject(i);
			         String name = jbo.getString("name");
			         String brand = jbo.getString("brand");
			         String color = jbo.getString("color");
			         String quantity = jbo.getString("quantity");
			         String initQuantity=jbo.getString("init_quantity");
			         String acceptProduct=jbo.getString("accept_product");
                     Product p;
                     int q=Integer.parseInt(quantity);
                     if(q!=-1){
                        if(Integer.parseInt(initQuantity)>q )
                        {
                            String status=q+"max available";
                            p=new Product(name,brand,color,Integer.parseInt(initQuantity), 0,0, 0,status,Boolean.valueOf(acceptProduct),false);
                        }
                        else
                        {
                            String status="Ok";
                            p=new Product(name,brand,color,Integer.parseInt(initQuantity), 0,0, 0,status,Boolean.valueOf(acceptProduct),false);
                        }
                    }
                    else
                    {
                        String status="Missing";
                        p=new Product(name,brand,color,0, 0,0, 0,status,Boolean.valueOf(acceptProduct),false);

                    }
                    list.add(p);
	             }

	      return list;
	   }catch(Exception e)
	   {
		   e.printStackTrace();
		   return null;
	   }
	
	}

}

