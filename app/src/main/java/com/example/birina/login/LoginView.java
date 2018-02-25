package com.example.birina.login;


import com.example.birina.Base.BaseView;

/**
 * Created by Admin on 7/12/2017.
 */

public interface LoginView extends BaseView {

    void onSuccess();
    void onFailure(String error);

    void setData(String t);

}
