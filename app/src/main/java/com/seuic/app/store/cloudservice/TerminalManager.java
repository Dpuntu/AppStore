package com.seuic.app.store.cloudservice;

import android.os.ConditionVariable;

import com.seuic.app.store.utils.AndroidUtils;
import com.seuic.app.store.utils.Logger;
import com.seuic.app.store.utils.SpUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2017/10/16.
 *
 * @author dpuntu
 *
 */

public class TerminalManager {
    private static TerminalManager mTerminalManager = new TerminalManager();
    private TerminalInfo mTerminalInfo;
    private Disposable mDisposable;

    public static TerminalManager getInstance() {
        return mTerminalManager;
    }

    public TerminalInfo getTerminalInfo() {
        return mTerminalInfo;
    }

    public void setTerminalInfo(TerminalInfo terminalInfo) {
        mTerminalInfo = terminalInfo;
    }

    public void checkTerminal() {
        if (mTerminalInfo != null) {
            return;
        }

        String serialNum = SpUtils.getInstance().getStr(SpUtils.SP_DEVICE_SN, "");
        if (serialNum != null && !serialNum.isEmpty()) {
            return;
        }

        Observable
                .create(new ObservableOnSubscribe<TerminalInfo>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<TerminalInfo> emitter) throws Exception {
                        TerminalUpdate mTerminalUpdate = new TerminalUpdate();
                        mTerminalUpdate.updateTerminal();
                        if (getTerminalInfo() != null) {
                            emitter.onNext(getTerminalInfo());
                        } else {
                            emitter.onError(new NullPointerException("获取失败"));
                        }
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TerminalInfo>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(@NonNull TerminalInfo info) {
                        SpUtils.getInstance().putStr(SpUtils.SP_DEVICE_SN, info.getSerialNo());
                        Logger.e("获得数据成功！" + info.getSerialNo());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Logger.e("获得数据失败！");
                    }

                    @Override
                    public void onComplete() {
                        if (mDisposable != null) {
                            mDisposable.dispose();
                        }
                    }
                });
    }

    private class TerminalUpdate implements CloudServiceManager.BindCloudListener {
        private final ConditionVariable mConditionVariable = new ConditionVariable();
        private boolean cloudServiceState = false;
        private CloudServiceManager mCloudServiceManager;

        public void updateTerminal() {
            CloudServiceManager.getInstance().bindCloudService(this);
            mConditionVariable.block();
            mConditionVariable.close();
            if (cloudServiceState) {
                AndroidUtils.sleepTime(3000);
                mCloudServiceManager.getTerminalInfo();
            }
            if (mCloudServiceManager != null) {
                mCloudServiceManager.unBindCloudService();
            }
            cloudServiceState = false;
        }

        @Override
        public void bindFail(String msg) {
            Logger.e("bindFail = " + msg);
            cloudServiceState = false;
            mConditionVariable.open();
        }

        @Override
        public void bindSuccess(CloudServiceManager serviceManager) {
            cloudServiceState = true;
            this.mCloudServiceManager = serviceManager;
            mConditionVariable.open();
        }
    }
}
