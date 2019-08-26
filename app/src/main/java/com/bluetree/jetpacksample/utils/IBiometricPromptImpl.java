package com.bluetree.jetpacksample.utils;

import android.hardware.biometrics.BiometricPrompt;
import android.support.v4.os.CancellationSignal;

public interface IBiometricPromptImpl {
    public void authenticate(CancellationSignal cancellationSignal, BiometricPromptManager.OnBiometricIdentifyCallBack callBack);
}
