package com.works.skynet.common.view;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by wangfengchen on 14-7-22.
 */
public class ParallaxTransformer implements ViewPager.PageTransformer {

    float parallaxCoefficient;
    float distanceCoefficient;

    int[] layer;
    public ParallaxTransformer(int[] layer,float parallaxCoefficient, float distanceCoefficient) {
        this.parallaxCoefficient = parallaxCoefficient;
        this.distanceCoefficient = distanceCoefficient;
        this.layer = layer;
    }

    @Override
    public void transformPage(View page, float position) {
        float scrollXOffset = page.getWidth() * parallaxCoefficient;

        // ...
        // layer is the id collection of views in this page
        for (int id : layer) {
            View view = page.findViewById(id);
            if (view != null) {
                view.setTranslationX(scrollXOffset * position);
            }
            scrollXOffset *= distanceCoefficient;
        }
    }
}
