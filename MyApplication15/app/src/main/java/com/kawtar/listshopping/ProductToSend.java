package com.kawtar.listshopping;

/**
 * Created by Kawtar on 11/27/2014.
 */
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
public class ProductToSend implements Serializable {
    private static final long serialVersionUID = -5435670920302756945L;

    private String name = "";
    private int quantity ;
    private float positionx;
    private float positiony;
    private float price;
    private boolean marker;
    public ProductToSend(String name, int quantity, float positionx, float positiony, float price, boolean marker) {

        this.setName(name);
        this.setQuantity(quantity);
        this.setPositionx(positionx);
        this.setPositiony(positiony);
        this.setPrice(price);
        this.setMarker(marker);

    }
    public void setMarker(boolean marker)
    {
        this.marker = marker;
    }
    public boolean getMarker() {
        return marker;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPositiony(float positiony)
    {
        this.positiony=positiony;
    }
    public double getPositiony (){
        return positiony;
    }
    public void setPositionx(float positionx)
    {
        this.positionx=positionx;
    }
    public double getPositionx (){
        return positionx;
    }
    public void setPrice(float price)
    {
        this.price=price;
    }
    public float getPrice (){
        return price;
    }
    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", getName());
            jsonObject.put("quantity", String.valueOf(getQuantity()));
            return jsonObject;
        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}