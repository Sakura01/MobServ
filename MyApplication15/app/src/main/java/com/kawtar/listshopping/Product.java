package com.kawtar.listshopping;

/**
 * Created by Kawtar on 11/27/2014.
 */
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
public class Product implements Serializable {
    private static final long serialVersionUID = -5435670920302756945L;

    private String brand = "";
    private String name = "";
    private String color = "";
    private int quantity ;
    private float positionx;
    private float positiony;
    private float price;
    private String status;
    private boolean accept;
    private boolean marker;
    public Product(String name, String brand, String color, int quantity,float positionx,float positiony,float price,String status,boolean accept,boolean marker) {
        this.setBrand(brand);
        this.setName(name);
        this.setColor(color);
        this.setQuantity(quantity);
        this.setPositionx(positionx);
        this.setPositiony(positiony);
        this.setPrice(price);
        this.setStatus(status);
        this.setAccept(accept);
        this.setMarker(marker);

    }
    public String toString()
    {
        return String.format("%s ", name);
    }
    public void setMarker(boolean marker)
    {
        this.marker = marker;
    }
    public boolean getMarker() {
        return marker;
    }
    public void setAccept(boolean accept)
    {
        this.accept = accept;
    }
    public boolean getAccept() {
        return accept;
    }
    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setColor(String color)
    {
        this.color = color;
    }
    public String getColor() {
        return color;
    }
    public void setBrand(String brand)
    {
        this.brand = brand;
    }
    public String getBrand() {
        return brand;
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
    public void setStatus(String status)
    {
        this.status=status;
    }
    public String getStatus (){
        return status;
    }
    public JSONObject toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("name", getName());
            jsonObject.put("brand", getBrand());
            jsonObject.put("color", getColor());
            jsonObject.put("quantity", String.valueOf(getQuantity()));
            jsonObject.put("accept_product", Boolean.toString(getAccept()));
            Log.i("CheckState",""+getAccept());
            return jsonObject;
        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }
}