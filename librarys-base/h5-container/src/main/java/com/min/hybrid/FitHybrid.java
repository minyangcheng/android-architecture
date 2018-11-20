package com.min.hybrid;

import android.widget.ImageView;

/**
 * Created by minych on 18-11-20.
 */

public class FitHybrid {

    private static FitHybrid hybrid;

    private HybridImageLoader imageLoader;

    private FitHybrid() {
    }

    public static FitHybrid getInstance() {
        if (hybrid == null) {
            synchronized (FitHybrid.class) {
                if (hybrid == null) {
                    hybrid = new FitHybrid();
                }
            }
        }
        return hybrid;
    }

    public void setImageLoader(HybridImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    public HybridImageLoader getImageLoader() {
        return imageLoader;
    }

    public interface HybridImageLoader {
        void loadImage(ImageView view, String url);
    }

}
