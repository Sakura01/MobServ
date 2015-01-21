package com.kawtar.jsoncontrol;


import com.kawtar.listshopping.ProductToSend;
import com.shopping.list.bean.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuperMarket {  
    private String mName;     
    private double mPositionX;
    private double mPositionY;
    private String mIndoorMapUrl;
    private List<ProductToSend> list;
    public SuperMarket(List<ProductToSend>list,String name,double positionX, double positionY,String mapUrl)
    {         
    	mName = name;
        this.list=new ArrayList<ProductToSend>();
        this.list=list;
    	mIndoorMapUrl=mapUrl;
    	mPositionX=positionX;
        mPositionY=positionY;
    }
    public void setList(List<ProductToSend>liste)
    {
        list=liste;
    }
    public List<ProductToSend> getList()
    {
        return list;
    }
    public String getIndoorMapUrl()
    {
    	return mIndoorMapUrl;
    }
    public String getName()
    {
        return mName;
    }
    public double getPositionX() {
        return mPositionX;
    }
    public double getPositionY() {
        return mPositionY;
    }
    public void setIndoorMapUrl(String mapUrl)
    {
        mIndoorMapUrl=mapUrl;
    }
    public void setName (String name)
    {
    	mName=name;
    }
    public void setPositionX(double positionX)
    {
    	mPositionX=positionX;
    }

    public void setPositionY(double positionY)
    {
    	mPositionY=positionY;
    }
    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", getName());
            jsonObject.put("supermarket_positionX",String.valueOf(getPositionX()));
            jsonObject.put("supermarket_positionY",String.valueOf( getPositionX()));
            return jsonObject;
        } catch (JSONException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
    public SuperMarket parseJSON(String result){
        List<ProductToSend> liste=new ArrayList<ProductToSend>();
        ProductToSend p=new ProductToSend("","",0,"",0,0,0,false);
        liste.add(p);
    	SuperMarket superMarket = new SuperMarket(liste,"",0.0,0.0,"");
        try {
        	JSONObject jsonObject= new JSONObject(result);
        	JSONObject superMarketRowName=jsonObject.getJSONObject("name");
			JSONObject superMarketRowPositionX=jsonObject.getJSONObject("supermarket_positionX");
			JSONObject superMarketRowPositionY=jsonObject.getJSONObject("supermarket_positionY");
			superMarket.setName(superMarketRowName.toString());
			superMarket.setPositionX(Double.parseDouble(superMarketRowPositionX.toString()));
			superMarket.setPositionY(Double.parseDouble(superMarketRowPositionY.toString()));
			return superMarket;
        } catch (JSONException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}
