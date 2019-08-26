package com.bluetree.jetpacksample.utils;

import android.app.Activity;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v4.os.CancellationSignal;

class BiometricPromptApi28 implements IBiometricPromptImpl {
    private BiometricPromptManager.OnBiometricIdentifyCallBack mManagerIdentifyCallBack;
    private BiometricPromptDialog mDialog;
    private CancellationSignal mCancellationSignal;
    private FingerprintManager mFingerPrintManager;


    @Override
    public void authenticate(CancellationSignal cancellationSignal, BiometricPromptManager.OnBiometricIdentifyCallBack callBack) {
        mCancellationSignal = cancellationSignal;
        mManagerIdentifyCallBack = callBack;
        mDialog = BiometricPromptDialog.newInstance();

        mDialog.setOnBiometicPromptDialogActionCallback(new BiometricPromptDialog.OnBiometicPromptDialogActionCallback() {
            @Override
            public void onDialogDismiss() {

                if (mCancellationSignal != null && !mCancellationSignal.isCanceled()) {
                    mCancellationSignal.cancel();
                    mFingerPrintManager = null;
                }
            }

            @Override
            public void onUsePassword() {
                if (mManagerIdentifyCallBack != null) {
                    mManagerIdentifyCallBack.onUsePassword();
                }
            }

            @Override
            public void onCancel() {
                if (mManagerIdentifyCallBack != null) {
                    mManagerIdentifyCallBack.onCancel();
                }

            }
        });

       // mDialog.show(mMa);
    }
}
