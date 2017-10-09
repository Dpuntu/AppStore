package com.seuic.app.store.install;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.utils.Loger;
import com.seuic.smartpos.cloudsdk.app.IAppInstallObserver;
import com.seuic.smartpos.cloudsdk.app.IAppManager;
import com.seuic.smartpos.cloudsdk.app.InstallParam;
import com.seuic.smartpos.cloudsdk.main.ICloudService;
import com.seuic.smartpos.cloudsdk.main.ManagerType;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */

public class CloudServiceManager {
    private final static String cloudServicePackage = "com.seuic.service.CLOUD_MANAGER";
    private final static String cloudSdkPackage = "com.seuic.smartpos.service";

    private static CloudServiceManager mCloudServiceManager = new CloudServiceManager();

    private BindCloudListener mBindCloudListener;

    private boolean connected = false;

    private IAppManager mAppManager;

    public static CloudServiceManager getInstance() {
        return mCloudServiceManager;
    }

    public boolean isConnected() {
        return connected;
    }

    /**
     * 绑定服务
     *
     * @param mBindCloudListener
     *         绑定结果回调
     */
    public void bindCloudService(BindCloudListener mBindCloudListener) {
        Loger.d("开始 CloudService 绑定");
        this.mBindCloudListener = mBindCloudListener;
        Intent intent = new Intent(cloudServicePackage);
        intent.setPackage(cloudSdkPackage);
        boolean result = AppStoreApplication.getApp().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        if (!result) {
            mBindCloudListener.bindFail("绑定 CloudService 失败");
        }
    }

    /**
     * 解绑服务
     */
    public void unBindCloudService() {
        if (connected) {
            Loger.d("解除 CloudService 绑定");
            AppStoreApplication.getApp().unbindService(mServiceConnection);
            connected = false;
            mAppManager = null;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ICloudService cloudService = ICloudService.Stub.asInterface(service);
            if (cloudService == null) {
                connected = false;
            } else {
                try {
                    mAppManager = IAppManager.Stub.asInterface(cloudService.getManager(ManagerType.APP_MANAGER));
                    connected = true;
                } catch (RemoteException e) {
                    Loger.e(android.util.Log.getStackTraceString(e));
                    connected = false;
                }
            }

            if (connected) {
                mBindCloudListener.bindSuccess(mCloudServiceManager);
            } else {
                mBindCloudListener.bindFail("绑定 CloudService 失败");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBindCloudListener.bindFail("onServiceDisconnected");
            if (connected) {
                connected = false;
                mAppManager = null;
            }
        }
    };

    /**
     * 安装应用
     *
     * @param filePath
     *         下载
     * @param mInstallListener
     *         安装结果监听
     */
    public void installApp(final String filePath, final InstallListener mInstallListener) {
        final InstallParam mInstallParam = new InstallParam();
        mInstallParam.setRebootEnable(false);
        if (mAppManager != null) {
            try {
                mAppManager.installAppWithParams(
                        filePath,
                        new IAppInstallObserver.Stub() {
                            @Override
                            public void onInstallFinished(String packageName, int returnCode, String errorMsg) throws RemoteException {
                                if (mInstallListener != null) {
                                    if (returnCode != 1) {
                                        mInstallListener.onInstallError(errorMsg);
                                    } else {
                                        mInstallListener.onInstallSuccess();
                                    }
                                }
                            }
                        }, mInstallParam);
            } catch (RemoteException e) {
                if (mInstallListener != null) {
                    mInstallListener.onInstallError("安装连接服务失败");
                }
            }
        } else {
            mInstallListener.onInstallError("未连接服务");
        }
    }

    interface InstallListener {
        void onInstallSuccess();

        void onInstallError(String errorMsg);
    }

    public interface BindCloudListener {
        void bindFail(String msg);

        void bindSuccess(CloudServiceManager serviceManager);
    }

}
