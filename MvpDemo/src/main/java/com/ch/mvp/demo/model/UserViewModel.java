package com.ch.mvp.demo.model;

import com.ch.mvp.demo.utils.HttpResultListener;

public interface UserViewModel {

	void getUserInfo(HttpResultListener listener);
}
