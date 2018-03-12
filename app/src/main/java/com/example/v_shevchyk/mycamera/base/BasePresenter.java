package com.example.v_shevchyk.mycamera.base;

public interface BasePresenter<T> {
    void attachView(T view);

    void detachView();
}