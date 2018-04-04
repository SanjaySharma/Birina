package com.birina.bsecure.login.model;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Admin on 7/13/2017.
 */

public class TaskInteractorImp implements TaskInteractor {

    private  final String TAG = TaskInteractorImp.class.getName();
    @Override
    public void fetchList(TaskInteractorCallBack taskInteractorCallBack) {

        Log.d(TAG," Enter in fetchList() of TaskInteractorImp.java ");

        taskInteractorCallBack.onSuccess(new ArrayList<TaskModel>());
    }
}
