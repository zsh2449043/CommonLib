package com.sample.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.clear();
    }

    protected void click(View view, final Runnable runnable) {
        RxView.clicks(view)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Object o) {
                        runnable.run();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.d("click onError: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    protected void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    protected void removeDisposable(Disposable disposable) {
        mCompositeDisposable.remove(disposable);
    }
}
