package com.ch.mvp.demo.model;

import com.ch.mvp.demo.utils.HttpResultListener;
import com.ch.mvp.demo.utils.HttpUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserViewModelImpl implements UserViewModel {

	String result = "";

	@Override
	public void getUserInfo(final HttpResultListener listener) {

		RequestParams params = new RequestParams();
		params.put("tokenId", "360fd97a-b65e-4eca-9f73-ab4322f037f5");
		HttpUtil.sendHttpByGet(HttpUtil.URL + "GetPersonInfo", params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, String arg1) {
						super.onSuccess(arg0, arg1);
						result = arg1;
						System.out.println("=======success======" + arg1);
						listener.successs(arg1);
					}

					@Override
					public void onFailure(Throwable arg0, String arg1) {
						super.onFailure(arg0, arg1);
						result = "-----erro-----" + arg1;
						System.out.println("=======erro======" + arg1);
						listener.failed(arg1);
					}

				});
	}
}
