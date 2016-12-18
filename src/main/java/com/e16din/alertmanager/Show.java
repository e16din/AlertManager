package com.e16din.alertmanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public final class Show {

    private static class Holder {
        public static final Show HOLDER_INSTANCE = new Show();
    }

    public static Show message(@NonNull Activity activity, String message) {
        Holder.HOLDER_INSTANCE.setActivity(activity);
        Holder.HOLDER_INSTANCE.setMessage(message);
        return Holder.HOLDER_INSTANCE;
    }

    public static Show message(@NonNull Activity activity, @StringRes int messageId) {
        Holder.HOLDER_INSTANCE.setActivity(activity);
        Holder.HOLDER_INSTANCE.setMessage(activity.getString(messageId));
        return Holder.HOLDER_INSTANCE;
    }

    private Activity mActivity = null;
    private String mMessage = null;
    private AlertDialogCallback<?> mCallback = null;


    private Show() {
    }


    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public <T> Show callback(AlertDialogCallback<T> callback) {
        mCallback = callback;
        return Holder.HOLDER_INSTANCE;
    }

    public void dialog() {//todo: change OnClickListener on AlertDialogCallback
        if (mCallback == null) {
            AlertManager.manager(mActivity).showAlert(mMessage);
        } else {
            AlertManager.manager(mActivity).showAlert(mMessage, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mCallback.onPositive();
                    mCallback = null;
                }
            });
        }
        freeActivityWithMessage();
    }

    public void toast() {
        toast(Toast.LENGTH_LONG);
    }

    public void toast(int length) {
        Toast.makeText(mActivity, mMessage, length).show();
        freeActivityWithMessage();
    }

    public void snackbar() {
        snackbar(Snackbar.LENGTH_LONG);
    }

    public void snackbar(int length) {
        snackbar(mActivity.findViewById(android.R.id.content), length, null, null);
    }

    public void snackbarOk(int length, @Nullable View.OnClickListener onClick) {
        snackbar(mActivity.findViewById(android.R.id.content),
                length,
                android.R.string.ok,
                onClick);
    }

    public void snackbarOk(@Nullable View.OnClickListener onClick) {
        snackbar(mActivity.findViewById(android.R.id.content),
                Snackbar.LENGTH_INDEFINITE,
                android.R.string.ok,
                onClick);
    }

    public void snackbarCancel(int length, @Nullable View.OnClickListener onClick) {
        snackbar(mActivity.findViewById(android.R.id.content),
                length,
                android.R.string.cancel,
                onClick);
    }

    public void snackbarCancel(@Nullable View.OnClickListener onClick) {
        snackbar(mActivity.findViewById(android.R.id.content),
                Snackbar.LENGTH_INDEFINITE,
                android.R.string.cancel,
                onClick);
    }

    public void snackbar(View v, int length,
                         @Nullable String action,
                         @Nullable View.OnClickListener onClick) {
        final Snackbar snackbar = Snackbar.make(v, mMessage, length);
        if (action != null) {
            snackbar.setAction(action, onClick);
        }
        snackbar.show();
        freeActivityWithMessage();
    }

    public void snackbar(View v, int length,
                         @StringRes int action,
                         @Nullable View.OnClickListener onClick) {
        snackbar(v, length, mActivity.getString(action), onClick);
    }


    private void freeActivityWithMessage() {
        mActivity = null;
        mMessage = null;
    }
}
