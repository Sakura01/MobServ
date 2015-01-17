package com.kawtar.indoornavigation;

/**
 * Created by Kawtar on 11/27/2014.
 */


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.kawtar.myapplication.PrepareIndoorActivity;
import com.example.kawtar.myapplication.R;
import com.kawtar.listshopping.ProductToSend;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MapFragment extends Fragment
{

    /*
     *  Create a custom RelativeLayout and implement the inherited 'onDraw'
     *  method
     */
    private Canvas canvas;
    private ImageView img;
    private Bitmap map;
    private Bitmap marker;
    private View rootView ;
    private MapLayout layout;
    private Bitmap tempBitmap;
    private Paint paint;
    private         ImageLoader imageLoader = null;
    private class MapLayout extends RelativeLayout
    {
        private MapLayout(Context context)
        {
            super(context);
            marker = BitmapFactory.decodeResource(getResources(), R.drawable.mark_item);
            map= PrepareIndoorActivity.getIndoorMap();
                    //BitmapFactory.decodeResource(getResources(), R.drawable.casino);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        /*
         *  Create the layout
         */
        layout = new MapLayout(getActivity());
        layout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        /*
         *  Inflate your xml view
         */
        // Load image, decode it to Bitmap and return Bitmap synchronously

        paint=new Paint();
        rootView =  inflater.inflate(R.layout.indoor_map_fragment_view, container, false);
        img = (ImageView) rootView.findViewById(R.id.imageView);
        //img.setScaleType(ImageView.ScaleType.MATRIX);
        //Create a new image bitmap and attach a brand new canvas to it
        tempBitmap = Bitmap.createBitmap(map.getWidth(), map.getHeight(), Bitmap.Config.RGB_565);
        canvas = new Canvas(tempBitmap);
        rootView.setOnTouchListener(new PanZoomListener(layout, img));
        canvas.drawColor(Color.TRANSPARENT);
        //Draw the image bitmap into the canvas
        canvas.drawBitmap(map,0,0, paint);



        //Attach the canvas to the ImageView
        img.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));

        /*
         *  Add the view to the MapLayout
         */
        layout.addView(rootView);

        /*
         *  Return the 'layout' instead of just the 'rootView'
         */
        return layout;
    }

    public void drawMarker(ProductToSend item)
    {
        Bitmap combinedBitmap = getCombinedBitmap(map, marker,item);

        //Attach the canvas to the ImageView
        img.setImageDrawable(new BitmapDrawable(getResources(), combinedBitmap));
    }
    private Bitmap getCombinedBitmap(Bitmap b, Bitmap b2,ProductToSend item) {
        Bitmap drawnBitmap = null;

        try {
            drawnBitmap = Bitmap.createBitmap(map.getWidth(), map.getHeight(), Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(drawnBitmap);
            float boardPosX = (float) item.getPositionx();
            float boardPosY = (float) item.getPositiony();

            // JUST CHANGE TO DIFFERENT Bitmaps and coordinates .
            canvas.drawBitmap(b, 0, 0, paint);
            canvas.drawBitmap(b2,boardPosX,boardPosY,paint);
            //for more images :
            // canvas.drawBitmap(b3, 0, 0, null);
            // canvas.drawBitmap(b4, 0, 0, null);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return drawnBitmap;
    }

}
