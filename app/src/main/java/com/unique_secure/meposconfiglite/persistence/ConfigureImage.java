package com.unique_secure.meposconfiglite.persistence;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;


public class ConfigureImage {
    private Activity activity;

    public ConfigureImage(Activity activity) {
        this.activity = activity;
    }

    public void SetImage(int imageReceived, ImageView imageViewReceived){
        try {
            SVG setImage = SVGParser.getSVGFromResource(activity.getResources(), imageReceived);
            imageViewReceived.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            imageViewReceived.setImageDrawable(setImage.createPictureDrawable());
        }catch(Exception e){
            Log.e("ConfigureImage.java", e.getMessage());
        }

    }

}
