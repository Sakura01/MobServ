package com.kawtar.jsoncontrol;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.kawtar.listshopping.ProductToSend;
import com.shopping.list.bean.Product;

public class ResponseFromServer 
{
	private SuperMarket mSuperMarket;
	private float mTotal;
	private float mDistance;
    private static List<ResponseFromServer> offers;
    private static ResponseFromServer responseOffers;
    private static SuperMarket superMarket;
    private static List<ProductToSend>lip;
	public ResponseFromServer(SuperMarket supermarket,float total,float distance )
	{
	    	mTotal=total;
	    	mDistance=distance;
            List<ProductToSend>list=new ArrayList<ProductToSend>();
	    	mSuperMarket=new SuperMarket(list,"",0.0,0.0,"");
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
	public static List<ResponseFromServer> parseJSONResult(String result)
	{

		try
	    {
          offers=new ArrayList<ResponseFromServer>();
		  JSONArray jarray=new JSONArray(result);
           Log.i("Resultat parsage",result);
            for(int i=0;i<jarray.length();i++)
            {
                JSONObject json=jarray.getJSONObject(i);
                String totalPrice =  json.getString("total_price");

                String supermarketName =   json.getString("name");
                String supermarketMapUrl=  json.getString("supermarket_localMap");
                String supermarketDistance=  json.getString("distance");
                String supermarketPositionX =   json.getString("supermarket_positionX");
                String supermarketPositionY =   json.getString("supermarket_positionY");

                JSONArray listShopping = json.getJSONArray("list");
                Log.i("list"," " + i +" "+ listShopping.toString());
                lip=new ArrayList<ProductToSend>();
                lip=getParsing(listShopping);
                for(int l=0;l<lip.size();l++)
                {
                    Log.i("Log of lip",""+l+lip.get(l).getName()+lip.get(l).getPrice());
                }
                superMarket=new SuperMarket(lip,supermarketName,Double.parseDouble(supermarketPositionX),Double.parseDouble(supermarketPositionY),supermarketMapUrl);
                responseOffers=new ResponseFromServer(superMarket,round(Float.parseFloat(totalPrice), 2),round(Float.parseFloat(supermarketDistance), 2));
                /*for(int k=0;k<responseOffers.getList().size();k++)
                {
                    Log.i("list in the response offer","index"+k+responseOffers.getList().get(k).getName()+responseOffers.getList().get(k).getPrice());
                }*/
                offers.add(responseOffers);
                /*for(int k=0;k<offers.size();k++)
                {
                    List<ProductToSend>listt=new ArrayList<ProductToSend>();
                    listt=offers.get(k).getList();
                    for(int m=0;m<listt.size();m++)
                    Log.i("list in the response offer after offers","index"+k+listt.get(m).getName()+listt.get(m).getPrice());
                }*/

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

