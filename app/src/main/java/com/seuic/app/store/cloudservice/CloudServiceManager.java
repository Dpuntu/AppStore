package com.seuic.app.store.cloudservice;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.seuic.app.store.AppStoreApplication;
import com.seuic.app.store.utils.Logger;
import com.seuic.smartpos.cloudsdk.app.IAppInstallObserver;
import com.seuic.smartpos.cloudsdk.app.IAppManager;
import com.seuic.smartpos.cloudsdk.app.InstallParam;
import com.seuic.smartpos.cloudsdk.device.DeviceInfoContract;
import com.seuic.smartpos.cloudsdk.device.IDeviceManager;
import com.seuic.smartpos.cloudsdk.main.ICloudService;
import com.seuic.smartpos.cloudsdk.main.ManagerType;
import com.seuic.smartpos.cloudsdk.system.ISystemManager;

/**
 * Created on 2017/9/26.
 *
 * @author dpuntu
 */

public class CloudServiceManager {
    private final static String COM_SEUIC_SERVICE_CLOUD_MANAGER = "com.seuic.service.CLOUD_MANAGER";
    private final static String COM_SEUIC_SMARTPOS_SERVICE = "com.seuic.smartpos.service";

    private static CloudServiceManager mCloudServiceManager = new CloudServiceManager();

    private BindCloudListener mBindCloudListener;

    private boolean connected = false;

    private IAppManager mAppManager;
    private IDeviceManager mDeviceManager;
    private ISystemManager mSystemManager;

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
        Logger.d("开始 CloudService 绑定");
        this.mBindCloudListener = mBindCloudListener;
        Intent intent = new Intent(COM_SEUIC_SERVICE_CLOUD_MANAGER);
        intent.setPackage(COM_SEUIC_SMARTPOS_SERVICE);
        boolean result = AppStoreApplication.getApp().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        if (!result) {
            mBindCloudListener.bindFail("绑定 CloudService 失败");
        }
    }

    private void reSetManager() {
        mAppManager = null;
        mDeviceManager = null;
        mSystemManager = null;
    }

    /**
     * 解绑服务
     */
    public void unBindCloudService() {
        if (connected) {
            Logger.d("解除 CloudService 绑定");
            AppStoreApplication.getApp().unbindService(mServiceConnection);
            connected = false;
            reSetManager();
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
                    mDeviceManager = IDeviceManager.Stub.asInterface(cloudService.getManager(ManagerType.DEVICE_MANAGER));
                    mSystemManager = ISystemManager.Stub.asInterface(cloudService.getManager(ManagerType.SYSTEM_MANAGER));
                    connected = true;
                } catch (RemoteException e) {
                    Logger.e(android.util.Log.getStackTraceString(e));
                    connected = false;
                    reSetManager();
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
                reSetManager();
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

    /**
     * 获取设备信息
     */
    public void getTerminalInfo() {
        try {
            if (mDeviceManager != null) {
                boolean result = mDeviceManager.checkPosdServiceState();
                Logger.e("checkCloudServiceState, state = " + result);
                if (result) {
                    TerminalInfo terminalInfo = new TerminalInfo();
                    Bundle bundle = mDeviceManager.getDeviceInfo();
                    Bundle posdBundle = mDeviceManager.getPosdServiceInfo();
                    Bundle printerBundle = mDeviceManager.getPrinterInfo();
                    if (bundle != null) {
                        terminalInfo.setModelNo(bundle.getString(DeviceInfoContract.MODEL));
                        terminalInfo.setFacNo(bundle.getString(DeviceInfoContract.VENDOR));
                        terminalInfo.setSerialNo(bundle.getString(DeviceInfoContract.SN));
                        terminalInfo.setNativeVersion(bundle.getString(DeviceInfoContract.OS_VERSION));
                        terminalInfo.setCloudSdk(bundle.getString(DeviceInfoContract.SERVICE_APP_VERSION));
                        terminalInfo.setMcuInfo(posdBundle.getString(DeviceInfoContract.MCU_INFO));
                        terminalInfo.setMcuStatus(String.valueOf(posdBundle.getInt(DeviceInfoContract.MCU_STATUS)));
                        terminalInfo.setLackPaper(printerBundle.getBoolean(DeviceInfoContract.PRINTER_PAPER_STATUS));
                        terminalInfo.setLackPower(printerBundle.getBoolean(DeviceInfoContract.PRINTER_BATTERY_STATUS));
                    }
                    TerminalManager.getInstance().setTerminalInfo(terminalInfo);
                }
            } else {
                Logger.e("DeviceManager is Null");
            }
        } catch (RemoteException e) {
            Logger.e(android.util.Log.getStackTraceString(e));
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
