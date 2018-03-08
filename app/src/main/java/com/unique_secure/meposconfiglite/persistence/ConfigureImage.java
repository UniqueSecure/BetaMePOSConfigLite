package com.unique_secure.meposconfiglite.persistence;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;


public class ConfigureImage {
    private Context context;

    public ConfigureImage(Context context) {
        this.context = context;
    }

    public void SetImage(int imageReceived, ImageView imageViewReceived){
        try {
            SVG setImage = SVGParser.getSVGFromResource(context.getResources(), imageReceived);
            imageViewReceived.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            imageViewReceived.setImageDrawable(setImage.createPictureDrawable());
        }catch(Exception e){
            Log.e("ConfigureImage.java", e.getMessage());
        }

    }

}
