package com.kawtar.jsoncontrol;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.kawtar.listshopping.ProductToSend;

public class ResponseFromServer 
{
	private SuperMarket mSuperMarket;
	private float mTotal;
	private static List<ProductToSend> listSh;
	private float mDistance;
    private static String toParse;
    private static List<ResponseFromServer> offers;
    private static ResponseFromServer responseOffers;
    private static SuperMarket superMarket;
    private static List<ProductToSend>lip;
	public ResponseFromServer(List<ProductToSend> list,SuperMarket supermarket,float total,float distance )
	{         
			listSh=new ArrayList<ProductToSend>();
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
	public  List<ProductToSend> getList()
	{
    	return listSh;
	}
	public void setList(List<ProductToSend> list)
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
    public static List<ProductToSend> getParsing(JSONArray listShopping)
    {
        List <ProductToSend>lit=new ArrayList<ProductToSend>();
        try {
            for(int j=0;j<listShopping.length();j++)
            {
                ProductToSend p;
                JSONObject jb2 = listShopping.getJSONObject(j);
                String name = jb2.getString("name");
                String unit = jb2.getString("unit");
                String initQuantity=jb2.getString("quantity");
                String price = jb2.getString("price");
                String product_positionx = jb2.getString("product_positionx");
                String product_positiony = jb2.getString("product_positiony");
                p=new ProductToSend(name,name,Integer.parseInt(initQuantity),unit,Float.parseFloat(product_positionx),Float.parseFloat(product_positiony),round(Float.parseFloat(price), 2),false);
                lit.add(p);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lit;
    }

}

