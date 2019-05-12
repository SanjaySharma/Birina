package com.birina.bsecure.junkcleaner.model;

import android.annotation.TargetApi;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Process;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TaskScanO extends AsyncTask<Void, List<AppsListItem> , List<AppsListItem>> {

    private int mAppCount = 0;
    private CleanerService.OnActionListener mOnActionListener;
    private CleanerService cleanerService;
    private List<ApplicationInfo> packages;

    private long mCacheSize;


    public long getCacheSize() {
        return mCacheSize;
    }

    public TaskScanO(CleanerService.OnActionListener mOnActionListener, CleanerService cleanerService) {
        this.mOnActionListener = mOnActionListener;
        this.cleanerService = cleanerService;
    }

    @Override
    protected void onPreExecute() {
        if (mOnActionListener != null) {
            mOnActionListener.onScanStarted(cleanerService);
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected List<AppsListItem> doInBackground(Void... params) {
        cleanerService.setmCacheSize(0);
        final List<AppsListItem> apps = new ArrayList<>();

        PackageManager packageManager = cleanerService.getPackageManager();
        final StorageStatsManager storageStatsManager = (StorageStatsManager) cleanerService.getSystemService(Context.STORAGE_STATS_SERVICE);
        final StorageManager storageManager = (StorageManager) cleanerService.getSystemService(Context.STORAGE_SERVICE);
        if (storageManager == null || storageStatsManager == null) {
            return new ArrayList<>(apps);
        }
        final List<StorageVolume> storageVolumes = storageManager.getStorageVolumes();
        final UserHandle user = Process.myUserHandle();

        try {
            packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
            apps.clear();
            for (ApplicationInfo pkg : packages) {
                long cacheSize = 0;
                for (StorageVolume storageVolume : storageVolumes) {
                    final String uuidStr = storageVolume.getUuid();
                    final UUID uuid = uuidStr == null ? StorageManager.UUID_DEFAULT : UUID.fromString(uuidStr);
                    try {
                        final StorageStats storageStats = storageStatsManager.queryStatsForPackage(uuid, pkg.packageName, user);
                        cacheSize += storageStats.getCacheBytes();

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                long mCacheSize = cleanerService.getmCacheSize();
                mCacheSize += addPackage(apps, pkg.packageName, ++mAppCount, packages.size(), cacheSize);
                cleanerService.setmCacheSize(mCacheSize);
                publishProgress(new ArrayList<>(apps));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>(apps);
    }



    @Override
    protected void onProgressUpdate(List<AppsListItem>...result) {
        if (mOnActionListener != null) {
            mOnActionListener.onScanProgressUpdated(cleanerService, result[0]);
        }
    }

    @Override
    protected void onPostExecute(List<AppsListItem> result) {
        if (mOnActionListener != null) {
            mOnActionListener.onScanCompleted(cleanerService, result);
        }
        cleanerService.setmIsScanning(false);
    }


    private long addPackage(List<AppsListItem> apps, String packageName, int current, int max, long cacheSize) {
        try {
            PackageManager packageManager = cleanerService.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(packageName,
                    PackageManager.GET_META_DATA);

            apps.add(new AppsListItem(packageName,
                    packageManager.getApplicationLabel(info).toString(),
                    packageManager.getApplicationIcon(packageName),
                    cacheSize, current, max));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return cacheSize;
    }
}