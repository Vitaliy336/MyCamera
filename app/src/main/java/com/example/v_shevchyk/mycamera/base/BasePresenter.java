package com.example.v_shevchyk.mycamera.base;

/**
 * Created by v_shevchyk on 09.03.18.
 */

public interface BasePresenter<T> {
    void attachView(T view);

    void detachView();
}