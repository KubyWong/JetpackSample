package com.bluetree.jetpacksample.utils;

import android.app.Activity;
import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.bluetree.jetpacksample.activity.SampleFingerPrinterActivity;

public class BiometricPromptManager {
    public Activity mActivity;
    private IBiometricPromptImpl mImpl;

    public BiometricPromptManager(Activity activity) {
        this.mActivity = activity;
        if (isAboveApi28()) {
            mImpl = new BiometricPromptApi28();
        } else if (isAboveApi23()) {
            mImpl = new BiometricPromptApi23();
        }
    }

    public IBiometricPromptImpl getmImpl() {
        return mImpl;
    }

    private boolean isAboveApi23() {
        return Build.VERSION.SDK_INT>Build.VERSION_CODES.M;
    }

    private boolean isAboveApi28() {
        return Build.VERSION.SDK_INT>Build.VERSION_CODES.P;
    }

    private boolean isBiometricEnable() {
        return isAboveApi23()
                && isHarewareDetected()
                && hasEnrolledFingerprints()
                && isKeyguardSecrure()
                ;
    }

    private boolean isHarewareDetected() {
        if (isAboveApi23()) {
            FingerprintManager fm = mActivity.getSystemService(FingerprintManager.class);
            return fm != null && fm.isHardwareDetected();
        }
        return false;
    }

    private boolean isKeyguardSecrure() {
        if (isAboveApi23()) {

            KeyguardManager keyguardManager = mActivity.getSystemService(KeyguardManager.class);
            return keyguardManager.isKeyguardSecure();
        }

        return false;
    }

    private boolean hasEnrolledFingerprints() {
        if (isAboveApi23()) {
            FingerprintManager fm = mActivity.getSystemService(FingerprintManager.class);
            return fm != null && fm.hasEnrolledFingerprints();
        }
        return false;
    }


    public interface OnBiometricIdentifyCallBack {
        void onDialogDismiss();

        void onUsePassword();

        void onCancel();
    }
}
