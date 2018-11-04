package com.birina.bsecure.junkcleaner.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppsListItem implements Serializable {

    private long mCacheSize;
    private String mPackageName, mApplicationName;
    private Drawable mIcon;
    private int current;
    private int max;
    public AppsListItem(String packageName, String applicationName, Drawable icon, long cacheSize, int current,int max ) {
        mCacheSize = cacheSize;
        mPackageName = packageName;
        mApplicationName = applicationName;
        mIcon = icon;
        this.current = current;
        this.max = max;
    }


    public Drawable getApplicationIcon() {
        return mIcon;
    }

    public String getApplicationName() {
        return mApplicationName;
    }

    public long getCacheSize() {
        return mCacheSize;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public int getCurrent() {
        return current;
    }

    public int getMax() {
        return max;
    }
}
