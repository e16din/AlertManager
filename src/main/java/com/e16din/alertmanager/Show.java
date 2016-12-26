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

    private Runnable mOnPositive = null;
    private Runnable mOnNegative = null;


    private Show() {
    }


    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Show onPositive(Runnable callback) {
        mOnPositive = callback;
        return Holder.HOLDER_INSTANCE;
    }

    public Show onAction(Runnable callback) {
        mOnPositive = callback;
        return Holder.HOLDER_INSTANCE;
    }

    public Show onNegative(Runnable callback) {
        mOnNegative = callback;
        return Holder.HOLDER_INSTANCE;
    }

    public void dialog() {//todo: change OnClickListener on AlertDialogCallback
        AlertManager.manager(mActivity).showAlert(mMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mOnPositive != null) {
                    mOnPositive.run();
                }
                freeCallbacks();
            }
        });
        freeActivityWithMessage();
    }

    public void dialogYesNo() {
        AlertManager.manager(mActivity).showAlertYesNo(mMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mOnPositive != null) {
                    mOnPositive.run();
                }
                freeCallbacks();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (mOnNegative != null) {
                    mOnNegative.run();
                }
                freeCallbacks();
            }
        });
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
        snackbar(mActivity.findViewById(android.R.id.content), length, null);
    }

    public void snackbarOk(int length) {
        snackbar(mActivity.findViewById(android.R.id.content), length, android.R.string.ok);
    }

    public void snackbarOk() {
        snackbar(mActivity.findViewById(android.R.id.content), Snackbar.LENGTH_INDEFINITE, android.R.string.ok);
    }

    public void snackbarCancel(int length) {
        snackbar(mActivity.findViewById(android.R.id.content), length, android.R.string.cancel);
    }

    public void snackbarCancel() {
        snackbar(mActivity.findViewById(android.R.id.content), Snackbar.LENGTH_INDEFINITE, android.R.string.cancel);
    }

    public void snackbar(View v, int length, @Nullable final String action) {
        final Snackbar snackbar = Snackbar.make(v, mMessage, length);
        if (action != null) {
            snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (action.equals(mActivity.getString(android.R.string.cancel))) {
                        if (mOnNegative != null) {
                            mOnNegative.run();
                        }
                    } else {
                        if (mOnPositive != null) {
                            mOnPositive.run();
                        }
                    }

                    freeCallbacks();
                }
            });
        }

        freeActivityWithMessage();
    }

    public void snackbar(View v, int length, @StringRes int action) {
        snackbar(v, length, mActivity.getString(action));
    }


    private void freeActivityWithMessage() {
        mActivity = null;
        mMessage = null;
    }

    private void freeCallbacks() {
        mOnPositive = null;
        mOnNegative = null;
    }
}
