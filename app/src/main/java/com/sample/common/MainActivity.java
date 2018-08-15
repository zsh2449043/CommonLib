package com.sample.common;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.f2prateek.rx.preferences2.Preference;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okrx2.adapter.ObservableBody;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.orhanobut.logger.Logger;
import com.sample.common.base.PermissionActivity;
import com.sample.common.database.EffectInfo;
import com.sample.common.database.EffectInfoDao;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends PermissionActivity {

    private static final String DOWN_URL0 = "http://play.bustherm.com/download/download1526529188910.dat";
    private static final String DOWN_URL1 = "http://play.bustherm.com/download/download1526528719467.dat";
    private static final String DOWN_URL2 = "http://play.bustherm.com/download/download1526529070263.dat";

    @BindView(R.id.txtView)
    TextView mTxtView;
    @BindView(R.id.add)
    Button mAdd;
    @BindView(R.id.delete)
    Button mDelete;
    @BindView(R.id.modify)
    Button mModify;
    @BindView(R.id.query)
    Button mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPermissionGranted() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        CommonApp.initGreenDao(this);
        CommonApp.initGreenDao(this);
        initView();
//        testRxPreference();
//        testHttp(PAGE_URL);
//        testDownload();
    }

    private void initView() {
        click(mAdd, new Runnable() {
            @Override
            public void run() {
                //Long id, String tableThumbUri, String tableMode,
                //String uriKey, String uriValue, String gpSuk, boolean isHot,
                //boolean isNew, String subType, String price, String name
                String tag = String.valueOf(System.currentTimeMillis());
                EffectInfo effectInfo = new EffectInfo(null, "thumbUrl1", "mode1",
                        "uriKey1", "uriValue_" + tag, "suk_" + tag,
                        false, false, "subType1", "0.0", "name1");
                CommonApp.getEffectInfoDao().insert(effectInfo);
            }
        });
        click(mDelete, new Runnable() {
            @Override
            public void run() {
                EffectInfo effectInfo = queryFirst();
                if (effectInfo != null) {
                    CommonApp.getEffectInfoDao().delete(effectInfo);
                }
            }
        });
        click(mModify, new Runnable() {
            @Override
            public void run() {
                EffectInfo info = queryFirst();
                if (info != null) {
                    info.setGpSuk("suk_modified");
                }
            }
        });
        click(mQuery, new Runnable() {
            @Override
            public void run() {
                queryFirst();
            }
        });
    }

    private EffectInfo queryFirst() {
        List<EffectInfo> infoList = CommonApp.getEffectInfoDao()
                .queryBuilder()
                .where(EffectInfoDao.Properties.UriKey.like("uriKey%")).build().list();
        Logger.d("run: queryFirst list size=" + infoList.size());
        if (infoList.size() > 0) {
            Logger.d("run: queryFirst first info=" + infoList.get(0).toString());
            return infoList.get(0);
        }
        return null;
    }

    private void testDownload() {
        GetRequest<File> request0 = OkGo.get(DOWN_URL0);
        DownloadTask downloadTask0 = OkDownload.request(DOWN_URL0, request0);
        GetRequest<File> request1 = OkGo.get(DOWN_URL1);
        DownloadTask downloadTask1 = OkDownload.request(DOWN_URL1, request1);
        GetRequest<File> request2 = OkGo.get(DOWN_URL2);
        DownloadTask downloadTask2 = OkDownload.request(DOWN_URL2, request2);
        downloadTask0
                .save()
                .register(new DownloadListener(DOWN_URL0) {
                    @Override
                    public void onStart(Progress progress) {
                        Logger.d("onStart: ");
                    }

                    @Override
                    public void onProgress(Progress progress) {
                        Logger.d("onProgress: progress=" + progress);
                    }

                    @Override
                    public void onError(Progress progress) {
                        Logger.d("onError: ");
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        Logger.d("onFinish: ");
                    }

                    @Override
                    public void onRemove(Progress progress) {
                        Logger.d("onRemove: ");
                    }
                });
        downloadTask1
                .save()
                .register(new DownloadListener(DOWN_URL1) {
                    @Override
                    public void onStart(Progress progress) {
                        Logger.d("onStart: ");
                    }

                    @Override
                    public void onProgress(Progress progress) {
                        Logger.d("onProgress: progress=" + progress);
                    }

                    @Override
                    public void onError(Progress progress) {
                        Logger.d("onError: ");
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        Logger.d("onFinish: ");
                    }

                    @Override
                    public void onRemove(Progress progress) {
                        Logger.d("onRemove: ");
                    }
                });
        downloadTask2
                .save()
                .register(new DownloadListener(DOWN_URL2) {
                    @Override
                    public void onStart(Progress progress) {
                        Logger.d("onStart: ");
                    }

                    @Override
                    public void onProgress(Progress progress) {
                        Logger.d("onProgress: progress=" + progress);
                    }

                    @Override
                    public void onError(Progress progress) {
                        Logger.d("onError: ");
                    }

                    @Override
                    public void onFinish(File file, Progress progress) {
                        Logger.d("onFinish: ");
                    }

                    @Override
                    public void onRemove(Progress progress) {
                        Logger.d("onRemove: ");
                    }
                });
        Observable.fromArray(downloadTask0, downloadTask1, downloadTask2)
                .subscribe(new Observer<DownloadTask>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(DownloadTask downloadTask) {
                        Logger.d("onNext: downloadTask=" + downloadTask);
                        downloadTask.start();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError: ", e);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("onComplete: ");
                    }
                });
//        downloadTask.start();
    }

    private void testHttp(String url) {
        OkGo.<String>get(url)
                .converter(new StringConvert())
                .adapt(new ObservableBody<String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(String s) {
                        PageResponse response = JSON.parseObject(s, PageResponse.class);
                        int pageId = response.getPage().get(0).getId();
                        Logger.d("onNext: request success response str=" + s + ",pageId=" + pageId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError: request error " + e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    private void testRxPreference() {
        final Preference<Boolean> bp = CommonApp.getRxPreferences().getBoolean("test_key", true);
        bp.asObservable().subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
                addDisposable(d);
            }

            @Override
            public void onNext(Boolean aBoolean) {
                Logger.d("onNext: test bp default value=" + aBoolean);
                bp.set(false);
                Logger.d("onNext: after modified value=" + bp.get());
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                Logger.d("onComplete() called");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadTask task0 = OkDownload.getInstance().getTaskMap().get(DOWN_URL0);
        DownloadTask task1 = OkDownload.getInstance().getTaskMap().get(DOWN_URL1);
        DownloadTask task2 = OkDownload.getInstance().getTaskMap().get(DOWN_URL2);
        if (task0 != null) {
            task0.unRegister(DOWN_URL0);
        }
        if (task1 != null) {
            task1.unRegister(DOWN_URL1);
        }
        if (task2 != null) {
            task2.unRegister(DOWN_URL2);
        }
    }

}