package com.sample.common.base;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class PermissionActivity extends BaseActivity {

    private RxPermissions mRxPermissions;
    private List<String> mAllPermissions = new ArrayList<>();//所有权限
    private List<String> mLaunchPermissions = new ArrayList<>();//必要权限
    private List<String> mGrantedPermissions = new ArrayList<>();//已授予权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPermission();
        mRxPermissions = new RxPermissions(this);
        requestPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 需要动态申请的权限组：
     * group:android.permission-group.CONTACTS
     * group:android.permission-group.PHONE
     * group:android.permission-group.CALENDAR
     * group:android.permission-group.CAMERA
     * group:android.permission-group.LOCATION
     * group:android.permission-group.STORAGE
     * group:android.permission-group.MICROPHONE
     * group:android.permission-group.SMS
     */
    private void initPermission() {
        mAllPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        mAllPermissions.add(Manifest.permission.READ_PHONE_STATE);
        mLaunchPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void showPermissionConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage("缺少必要权限，真的不给我吗?")
                .setPositiveButton("赏你吧", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        requestPermission();
                    }
                }).setNegativeButton("就不给", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        }).show();
    }

    private void requestPermission() {

        mGrantedPermissions.clear();
        mRxPermissions.requestEach(mAllPermissions.toArray(new String[mAllPermissions.size()]))
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(Permission permission) {
                        Logger.d("requestPermission onNext: permission =" + permission.toString());
                        if (permission.granted) {
                            if (!mGrantedPermissions.contains(permission.name)) {
                                mGrantedPermissions.add(permission.name);
                            }
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            Logger.d("onNext: shouldShowRequestPermissionRationale");
                            if (mLaunchPermissions.contains(permission.name)) {
                                showPermissionConfirmDialog();
                            }
                        } else {
                            //拒绝并不再提示
                            if (mLaunchPermissions.contains(permission.name)) {
                                //拒绝了必要权限
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError: request permission", e);
                    }

                    @Override
                    public void onComplete() {
                        Logger.d("onComplete: ");
                        for (String launchPermission : mLaunchPermissions) {
                            if (mGrantedPermissions.contains(launchPermission)) {
                                Logger.d("onNext: all launch permission granted");
                                onPermissionGranted();
                            }
                        }
                    }
                });
    }

    protected abstract void onPermissionGranted();
}
