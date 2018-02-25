package com.example.birina.login.model;

import java.util.List;

/**
 * Created by Admin on 7/13/2017.
 */
// when we communicate to server.
public interface TaskInteractor {

    // Server request
    void fetchList(TaskInteractorCallBack callBack);


    // server response call back
    interface TaskInteractorCallBack{
        void onSuccess(List<TaskModel> taskModelList);
        void onFailure(String error);
    }
}
