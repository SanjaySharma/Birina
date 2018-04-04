package com.birina.bsecure.Base;

/**
 * Created by Admin on 7/12/2017.
 */
// It consist all common behaviour of your UI
 // We create a presenter and a view corresponding to a screen.

public interface BaseView<T> {
    void showDialog();
    void hideDialog();
    void setPresenter(T presenter);
}
