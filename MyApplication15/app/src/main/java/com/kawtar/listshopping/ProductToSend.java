package com.kawtar.listshopping;

/**
 * Created by Kawtar on 11/27/2014.
 */
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;
public class ProductToSend  {
    //private static final long serialVersionUID = -5435670920302756945L;

    private String name = "";
    private int id = 0;
    private String unit = "";
    private int quantity ;
    private float positionx;
    private float positiony;
    private float price;
    private boolean marker;
    public ProductToSend(int id,String name, int quantity, String unit,float positionx, float positiony, float price, boolean marker) {

        this.setName(name);
        this.setId(id);
        this.setQuantity(quantity);
        this.setPositionx(positionx);
        this.setPositiony(positiony);
        this.setPrice(price);
        this.setMarker(marker);
        this.setUnit(unit);

    }
    public void setMarker(boolean marker)
    {
        this.marker = marker;
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
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public void setPositiony(float positiony)
    {
        this.positiony=positiony;
    }
    public float getPositiony (){
        return positiony;
    }
    public void setPositionx(float positionx)
    {
        this.positionx=positionx;
    }
    public float getPositionx (){
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
            jsonObject.put("unit", getUnit());
            jsonObject.put("quantity", String.valueOf(getQuantity()));
            return jsonObject;
        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public String toString() {
        return name;
    }
}