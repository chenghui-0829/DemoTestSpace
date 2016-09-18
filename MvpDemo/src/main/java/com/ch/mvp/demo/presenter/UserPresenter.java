package com.ch.mvp.demo.presenter;

import android.os.Handler;
import android.os.Looper;
import com.ch.mvp.demo.model.UserViewModel;
import com.ch.mvp.demo.model.UserViewModelImpl;
import com.ch.mvp.demo.utils.HttpResultListener;
import com.ch.mvp.demo.view.UserViewImpl;

public class UserPresenter {

	private UserViewModel userViewModel;
	private UserViewImpl impl;
	Handler handler;

	public UserPresenter(UserViewImpl impl) {
		this.impl = impl;
		userViewModel = new UserViewModelImpl();
		handler = new Handler(Looper.getMainLooper());
	}

	public void getInfo() {

		userViewModel.getUserInfo(new HttpResultListener() {
			@Override
			public void successs(final String result) {

				System.out.println("=====getUserInfo()===" + result);
				// handler.postDelayed(new Runnable() {
				// @Override
				// public void run() {
				impl.getInfo(result);
				// }
				// }, 3000);
			}

			@Override
			public void failed(String result) {

			}
		});

	}
}
