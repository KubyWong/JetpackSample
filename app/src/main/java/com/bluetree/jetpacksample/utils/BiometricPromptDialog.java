package com.bluetree.jetpacksample.utils;


import android.app.DialogFragment;

public class BiometricPromptDialog extends DialogFragment {
    OnBiometicPromptDialogActionCallback onBiometicPromptDialogActionCallback;

    public static BiometricPromptDialog newInstance() {
        return new BiometricPromptDialog();
    }

    public void setOnBiometicPromptDialogActionCallback(OnBiometicPromptDialogActionCallback onBiometicPromptDialogActionCallback) {
        this.onBiometicPromptDialogActionCallback = onBiometicPromptDialogActionCallback;
    }

    public interface OnBiometicPromptDialogActionCallback {
        void onDialogDismiss();

        void onUsePassword();

        void onCancel();
    }


}
