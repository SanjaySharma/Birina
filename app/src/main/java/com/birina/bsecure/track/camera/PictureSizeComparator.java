package com.birina.bsecure.track.camera;

import android.hardware.Camera;

import java.util.Comparator;


class PictureSizeComparator implements Comparator<Camera.Size> {
    // Used for sorting in ascending order of
    // roll name
    public int compare(Camera.Size a, Camera.Size b) {
        return (b.height * b.width) - (a.height * a.width);
    }
}
