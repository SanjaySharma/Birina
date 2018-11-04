package com.birina.bsecure.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.birina.bsecure.backup.model.Request;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 3/29/2018.
 */

public class BirinaUtility {


    public static Request getBackUpData(Context mContext) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("compliance_score1.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Request request = null;

        Gson gson = new Gson();

        try {
            request = gson.fromJson(json, Request.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return request;
    }


    public static boolean isLicenseExpire(Context context) {

        boolean isExpire = false;

        try {

            String dtStart = BirinaPrefrence.getExpireDate(context);

            if(null != dtStart && !dtStart.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");


                Date endDate = sdf.parse(dtStart);

                Date currentDate = Calendar.getInstance().getTime();

                long different = endDate.getTime() - currentDate.getTime();

                if (different <= 0) {
                    isExpire = true;
                } else {
                    isExpire = false;
                }
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isExpire;
    }



    public static String getDateDifference ( String enDate) {

        Date endDate = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        try {
             endDate = sdf.parse(enDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date currentTime = Calendar.getInstance().getTime();

        //milliseconds
        long different = endDate.getTime() - currentTime.getTime();

        System.out.println("startDate : " + currentTime);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        StringBuilder timeRemaining = new StringBuilder(""+elapsedDays).append(" Days:")
                .append(""+elapsedHours).append(" Hours:").append(" Remaining");

   /*     StringBuilder timeRemaining = new StringBuilder(""+elapsedDays).append(" Days:")
                .append(""+elapsedHours).append(" Hours:").append(elapsedMinutes)
                .append(" Minutes").append(" Remaining");*/

        Log.d("timeRemaining "," is: "+timeRemaining);

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return timeRemaining.toString();
    }


    public static Bitmap decodeSampledBitmapFromResource(Context context, Bitmap bmp, String realPath) {

        int reqWidth = 500;
        int reqHeight = 500;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap img = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);

        try {
            img = rotateImageIfRequired(context, img, realPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
        // with both dimensions larger than or equal to the requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

        // This offers some additional logic in case the image has a strange
        // aspect ratio. For example, a panorama may have a much larger
        // width than height. In these cases the total pixels might still
        // end up being too large to fit comfortably in memory, so we should
        // be more aggressive with sample down the image (=larger inSampleSize).

        final float totalPixels = width * height;

        // Anything more than 2x the requested pixels we'll sample down further
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;

        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }


    /**
     * Rotate an image if required.
     *
     *
     * @param context
     * @param img           The image bitmap
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, String realPath) throws IOException {


        ExifInterface ei = new ExifInterface(realPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


}
