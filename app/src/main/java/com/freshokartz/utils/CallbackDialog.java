package com.freshokartz.utils;

import android.app.Dialog;

public interface CallbackDialog {

    void onPositiveClick(Dialog dialog);

    void onNegativeClick(Dialog dialog);
}
