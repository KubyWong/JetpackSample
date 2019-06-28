package com.bluetree.jetpacksample.utils;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToastUtils {

    public static void show(@NotNull Context context, @Nullable String toJson) {
        Toast.makeText(context, toJson, Toast.LENGTH_SHORT).show();
    }
}
