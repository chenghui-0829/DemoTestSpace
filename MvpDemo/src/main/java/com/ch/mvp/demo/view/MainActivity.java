package com.ch.mvp.demo.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ch.mvp.demo.R;
import com.ch.mvp.demo.presenter.UserPresenter;

public class MainActivity extends Activity implements UserViewImpl {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.result_text);
        UserPresenter presenter = new UserPresenter(this);
        presenter.getInfo();

    }

    @Override
    public void getInfo(String result) {
        System.out.println("========result======" + result);
        textView.setText(result);
    }

}
