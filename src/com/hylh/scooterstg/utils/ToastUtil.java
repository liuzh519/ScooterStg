package com.hylh.scooterstg.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    private static Toast sToast = null;

    public ToastUtil(Context c) {
    }


    public static void setToast(Toast toast) {
        if (sToast != null)
            sToast.cancel();
        sToast = toast;
    }

    public static void cancelToast() {
        if (sToast != null)
            sToast.cancel();
        sToast = null;
    }

	public static void makeText(Context context, String string, int lengthShort) {
        if (sToast != null){
            sToast.cancel();
        }
		sToast = Toast.makeText(context, string, lengthShort);
		sToast.show();
	}

	public static void makeText(Context context, int res, int lengthShort) {
        if (sToast != null){
            sToast.cancel();
        }
		sToast = Toast.makeText(context, context.getResources().getString(res), lengthShort);
		sToast.show();
	}

}
