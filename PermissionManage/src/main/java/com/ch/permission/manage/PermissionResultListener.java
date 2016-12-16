package com.ch.permission.manage;

/**
 * Created by CH on 2016/12/14.
 */

public interface PermissionResultListener {


    void requestPermissionSuccess();

    void requestPermissionFail();

    void requestFailExplain();


}
