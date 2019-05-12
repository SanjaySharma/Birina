package com.birina.bsecure.junkcleaner.model;

import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.AsyncTask;
import android.os.Build;
import android.os.RemoteException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TaskScan extends AsyncTask<Void, List<AppsListItem> , List<AppsListItem>> {

    private int mAppCount = 0;
    private CleanerService.OnActionListener mOnActionListener;
    private CleanerService cleanerService;


    public TaskScan(CleanerService.OnActionListener mOnActionListener, CleanerService cleanerService) {
        this.mOnActionListener = mOnActionListener;
        this.cleanerService = cleanerService;
    }

    @Override
    protected void onPreExecute() {
        if (mOnActionListener != null) {
            mOnActionListener.onScanStarted(cleanerService);
        }
    }

    @Override
    protected List<AppsListItem> doInBackground(Void... params) {
        cleanerService.setmCacheSize(0);

        final List<ApplicationInfo> packages = cleanerService.getPackageManager().getInstalledApplications(
                PackageManager.GET_META_DATA);

        // publishProgress(0, packages.size());

        final CountDownLatch countDownLatch = new CountDownLatch(packages.size());

        final List<AppsListItem> apps = new ArrayList<>();

        try {

            for (ApplicationInfo pkg : packages) {
                cleanerService.getmGetPackageSizeInfoMethod().invoke(cleanerService.getPackageManager(), pkg.packageName,
                        new IPackageStatsObserver.Stub() {

                            @Override
                            public void onGetStatsCompleted(PackageStats pStats,
                                                            boolean succeeded)
                                    throws RemoteException {
                                synchronized (apps) {
                                    long mCacheSize = cleanerService.getmCacheSize();
                                    mCacheSize += addPackage(apps, pStats, succeeded, ++mAppCount, packages.size());
                                    cleanerService.setmCacheSize(mCacheSize);
                                    // publishProgress(++mAppCount, packages.size());

                                    publishProgress(new ArrayList<>(apps));
                                }

                                synchronized (countDownLatch) {
                                    countDownLatch.countDown();
                                }
                            }
                        }
                );
            }

            countDownLatch.await();
        } catch (InvocationTargetException | InterruptedException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(apps);
    }

    @Override
    protected void onProgressUpdate(List<AppsListItem>...result) {
        if (mOnActionListener != null) {
            mOnActionListener.onScanProgressUpdated(cleanerService, result[0]);

            // mOnActionListener.onScanProgressUpdated(CleanerService.this, values[0], values[1]);
        }
    }

    @Override
    protected void onPostExecute(List<AppsListItem> result) {
        if (mOnActionListener != null) {
            mOnActionListener.onScanCompleted(cleanerService, result);
        }
        cleanerService.setmIsScanning(false);
    }

    private long addPackage(List<AppsListItem> apps, PackageStats pStats, boolean succeeded, int current, int max) {
        long cacheSize = 0;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            cacheSize += pStats.cacheSize;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            cacheSize += pStats.externalCacheSize;
        }

        if (!succeeded || cacheSize <= 0) {
            return 0;
        }

        try {
            PackageManager packageManager = cleanerService.getPackageManager();
            ApplicationInfo info = packageManager.getApplicationInfo(pStats.packageName,
                    PackageManager.GET_META_DATA);

            apps.add(new AppsListItem(pStats.packageName,
                    packageManager.getApplicationLabel(info).toString(),
                    packageManager.getApplicationIcon(pStats.packageName),
                    cacheSize, current, max));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return cacheSize;
    }
}
