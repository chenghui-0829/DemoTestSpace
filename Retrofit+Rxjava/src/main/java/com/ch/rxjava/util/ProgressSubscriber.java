package com.ch.rxjava.util;


import android.content.Context;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import rx.Subscriber;

/**
 * Created by helin on 2016/10/10 15:49.
 */

public abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {


    private SimpleLoadDialog dialogHandler;

    public ProgressSubscriber(Context context) {
        dialogHandler = new SimpleLoadDialog(context, this, true);
    }

    @Override
    public void onCompleted() {

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                dismissProgressDialog();
            }
        }, 5000);


    }


    /**
     * 显示Dialog
     */
    public void showProgressDialog() {
        if (dialogHandler != null) {
            dialogHandler.show();
        }
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    /**
     * 隐藏Dialog
     */
    private void dismissProgressDialog() {
        if (dialogHandler != null) {
            dialogHandler.dismiss();
            dialogHandler = null;
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        _onError("请求失败，请稍后再试...");
        dismissProgressDialog();
    }


    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    protected abstract void _onNext(T t);

    protected abstract void _onError(String message);
}
