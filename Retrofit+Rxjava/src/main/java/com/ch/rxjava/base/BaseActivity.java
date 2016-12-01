package com.ch.rxjava.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by helin on 2016/11/11 10:41.
 */

public class BaseActivity extends AppCompatActivity {

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.START);
        super.onStart();
    }

    @Override
    protected void onResume() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.RESUME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
    }


    /**
     * 利用Observable.takeUntil()停止网络请求
     *
     * @param event
     * @param lifecycleSubject
     * @param <T>
     * @return
     */
    public static <T> Observable.Transformer<T, T> bindUntilEvent(final ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> sourceObservable) {
                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
                        lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
                            @Override
                            public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
                                return activityLifeCycleEvent.equals(event);
                            }
                        });
                return sourceObservable.takeUntil(compareLifecycleObservable);
            }
        };
    }


//    /**
//     * @param <T>
//     * @return
//     */
//    public static <T> Observable.Transformer<String, T> handleResult(final ActivityLifeCycleEvent event, final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject) {
//        return new Observable.Transformer<String, T>() {
//            @Override
//            public Observable<T> call(Observable<String> tObservable) {
//                Observable<ActivityLifeCycleEvent> compareLifecycleObservable =
//                        lifecycleSubject.takeFirst(new Func1<ActivityLifeCycleEvent, Boolean>() {
//                            @Override
//                            public Boolean call(ActivityLifeCycleEvent activityLifeCycleEvent) {
//                                return activityLifeCycleEvent.equals(event);
//                            }
//                        });
//                return tObservable.flatMap(new Func1<String, Observable<T>>() {
//                    @Override
//                    public Observable<T> call(String result) {
//                        if (result.getCount() != 0) {
//                            return createData(result.getSubjects());
//                        } else {
//                            return Observable.error(new ApiException(result.getCount()));
//                        }
//                    }
//                }).takeUntil(compareLifecycleObservable).subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }

    /**
     * 创建成功的数据
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(data);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });

    }


}
