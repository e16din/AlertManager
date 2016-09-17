package com.e16din.alertmanager;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

public final class AlertManager {

    public static final String TAG_CALENDAR_DIALOG = "CalendarDialog";

    private AlertManager() {
    }

    private static final int INVALID_VALUE = -100500;
    public static final int MILLISECONDS_IN_THE_DAY = 86400;

    private static class Holder {
        public static final AlertManager HOLDER_INSTANCE = new AlertManager();
    }

    public static AlertManager manager(@NonNull Context context) {
        Holder.HOLDER_INSTANCE.setContext(context);
        return Holder.HOLDER_INSTANCE;
    }

    private static int sCustomAlertTitle = R.string.title_alert;
    private static int sCustomErrorTitle = R.string.title_error;

    private Context mContext = null;
    private ArrayList<String> mDisplayedAlerts = new ArrayList<>();

    private void setContext(@NonNull Context context) {
        mContext = context;
    }

    private MaterialDialog.Builder createAlertBuilder() {
        return new MaterialDialog.Builder(mContext);
    }

    public static int getCustomAlertTitle() {
        return sCustomAlertTitle;
    }

    public static void setCustomAlertTitle(int customAlertTitle) {
        AlertManager.sCustomAlertTitle = customAlertTitle;
    }

    public static int getCustomErrorTitle() {
        return sCustomErrorTitle;
    }

    public static void setCustomErrorTitle(int customErrorTitle) {
        AlertManager.sCustomErrorTitle = customErrorTitle;
    }

    public boolean isAlertDisplayed(String message) {
        return mDisplayedAlerts.contains(message);
    }

    public void showAlert(String message, boolean isCancelable) {
        showAlert(message, sCustomAlertTitle, isCancelable, null);
    }

    public void showErrorAlert(String message, boolean isCancelable) {
        showAlert(message, sCustomErrorTitle, isCancelable, null);
    }

    public void showErrorAlert(String message, boolean isCancelable,
                               DialogInterface.OnClickListener listener) {
        showAlert(message, sCustomErrorTitle, isCancelable, listener);
    }

    public void showAlert(String message) {
        showAlert(message, sCustomAlertTitle, false, null);
    }

    public void showAlert(int message) {
        showAlert(message, sCustomAlertTitle, false, null);
    }

    public void showAlert(String message, DialogInterface.OnClickListener listener) {
        showAlert(message, sCustomAlertTitle, false, listener);
    }

    public void showAlert(int message, DialogInterface.OnClickListener listener) {
        showAlert(message, sCustomAlertTitle, false, listener);
    }

    public void showErrorAlert(String message) {
        showAlert(message, sCustomErrorTitle, false, null);
    }

    public void showErrorAlert(String message, DialogInterface.OnClickListener listener) {
        showAlert(message, sCustomErrorTitle, false, listener);
    }

    public void showAlert(final String message, final int title, final boolean isCancelable,
                          final DialogInterface.OnClickListener listener) {

        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    MaterialDialog.Builder builder = createAlertBuilder();

                    String updatedTitle = mContext.getString(title);
                    if (!TextUtils.isEmpty(mContext.getString(title)))
                        builder.title(updatedTitle);

                    builder.content(message)
                            .positiveText(android.R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mDisplayedAlerts.remove(message);
                                    if (listener != null)
                                        listener.onClick(dialog, which.ordinal());

                                }
                            })
                            .cancelable(isCancelable)
                            .keyListener(new DialogInterface.OnKeyListener() {
                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode,
                                                     KeyEvent event) {
                                    mDisplayedAlerts.remove(message);
                                    if (keyCode == KeyEvent.KEYCODE_BACK ||
                                            event.getAction() == KeyEvent.ACTION_UP) {
                                        if (listener != null)
                                            listener.onClick(dialog, INVALID_VALUE);
                                    }


                                    return false;
                                }
                            })
                            .show();
                    mDisplayedAlerts.add(message);
                } catch (WindowManager.BadTokenException e) {
                    Log.e("debug", "error: ", e);
                } finally {
                    mContext = null;
                }
            }
        }, 500);
    }

    public void showAlert(final int message, int title, boolean isCancelable,
                          final DialogInterface.OnClickListener listener) {
        showAlert(mContext.getString(message), title, isCancelable, listener);
    }

    public void showAlert(final String message, boolean isCancelable,
                          final DialogInterface.OnClickListener listener) {
        try {
            MaterialDialog.Builder builder = createAlertBuilder();

            final String customAlertTitle = mContext.getString(AlertManager.sCustomAlertTitle);

            if (!TextUtils.isEmpty(customAlertTitle))
                builder.title(customAlertTitle);

            builder.content(message)
                    .positiveText(android.R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mDisplayedAlerts.remove(message);
                            if (listener != null)
                                listener.onClick(dialog, which.ordinal());
                        }
                    })
                    .cancelable(isCancelable)
                    .keyListener(
                            new DialogInterface.OnKeyListener() {

                                @Override
                                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                    if (keyCode == KeyEvent.KEYCODE_BACK ||
                                            event.getAction() == KeyEvent.ACTION_UP) {
                                        mDisplayedAlerts.remove(message);
                                        if (listener != null)
                                            listener.onClick(dialog, INVALID_VALUE);
                                    }

                                    return false;
                                }
                            })
                    .show();
            mDisplayedAlerts.add(message);
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        } finally {
            mContext = null;
        }
    }


    public void showAlertYesNo(@StringRes int message,
                               @NonNull final DialogInterface.OnClickListener yesListener) {
        showAlertYesNo(mContext.getString(message), yesListener, null);
    }

    public void showAlertYesNo(final String message,
                               @NonNull final DialogInterface.OnClickListener yesListener) {
        showAlertYesNo(message, yesListener, null);
    }

    public void showAlertYesNo(int message,
                               DialogInterface.OnClickListener yesListener,
                               DialogInterface.OnClickListener noListener) {
        showAlertYesNo(mContext.getString(message), yesListener, noListener);
    }

    public void showAlertYesNo(final String message,
                               final DialogInterface.OnClickListener yesListener,
                               final DialogInterface.OnClickListener noListener) {
        try {
            MaterialDialog.Builder builder = createAlertBuilder();

            final String customAlertTitle = mContext.getString(AlertManager.sCustomAlertTitle);

            if (!TextUtils.isEmpty(customAlertTitle))
                builder.title(customAlertTitle);

            builder.content(message)
                    .positiveText(android.R.string.yes)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mDisplayedAlerts.remove(message);
                            if (yesListener != null)
                                yesListener.onClick(dialog, which.ordinal());
                        }
                    })
                    .negativeText(android.R.string.no)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            mDisplayedAlerts.remove(message);
                            if (noListener != null)
                                noListener.onClick(dialog, which.ordinal());
                        }
                    })
                    .show();
            mDisplayedAlerts.add(message);
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        } finally {
            mContext = null;
        }
    }

    public void showDialogList(final String title, final CharSequence[] items, final TextView tv,
                               final DialogInterface.OnClickListener listener) {
        try {
            MaterialDialog.Builder builder = createAlertBuilder();

            if (!TextUtils.isEmpty(title))
                builder.title(title);

            builder.items(items)
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                            if (tv != null && position >= 0)
                                tv.setText(items[position]);

                            mDisplayedAlerts.remove(title);
                            if (listener != null)
                                listener.onClick(dialog, position);
                        }
                    })
                    .show();
            mDisplayedAlerts.add(title);
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        } finally {
            mContext = null;
        }
    }

    public void showRadioList(final String title, final CharSequence[] items,
                              final DialogInterface.OnClickListener listener) {
        showRadioList(title, items, null, listener);
    }

    public void showRadioList(final CharSequence[] items,
                              final DialogInterface.OnClickListener listener) {
        showRadioList(null, items, null, listener);
    }

    public void showRadioList(final CharSequence[] items, final TextView tv,
                              final DialogInterface.OnClickListener listener) {
        showRadioList(null, items, tv, listener);
    }

    public void showRadioList(final String title, final CharSequence[] items, final TextView tv,
                              final DialogInterface.OnClickListener listener) {
        try {
            MaterialDialog.Builder builder = new MaterialDialog.Builder(mContext);

            if (!TextUtils.isEmpty(title))
                builder.title(title);

            builder.items(items)
                    .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/

                            if (tv != null && which >= 0) {
                                tv.setText(items[which]);
                            }

                            if (listener != null) {
                                listener.onClick(dialog, which);
                            }

                            dialog.dismiss();
                            dialog.cancel();
                            return true;
                        }
                    })
                    .alwaysCallSingleChoiceCallback()
                    .positiveText(android.R.string.cancel)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        } finally {
            mContext = null;
        }
    }

    //TODO: show list (see SimpleListDialogs in https://github.com/afollestad/material-dialogs)

    @Deprecated
    public void showDialogList(final CharSequence[] items, DialogInterface.OnClickListener listener) {
        showDialogList(mContext.getString(sCustomAlertTitle), items, null, listener);
    }

    @Deprecated
    public void showDialogList(final CharSequence[] items, final TextView tv,
                               DialogInterface.OnClickListener listener) {
        showDialogList(mContext.getString(sCustomAlertTitle), items, tv, listener);
    }

    //TODO: use better picker

    public void showTimePicker(int hours, int minutes,
                               final TimePickerDialog.OnTimeSetListener onTimeSetListener) {
        try {
            final TimePicker timePicker = new TimePicker(mContext);
            timePicker.setIs24HourView(true);
            timePicker.setCurrentHour(hours);
            timePicker.setCurrentMinute(minutes);

            MaterialDialog.Builder builder = createAlertBuilder();

            final String title = mContext.getString(R.string.set_time);
            if (!TextUtils.isEmpty(title))
                builder.title(title);

            builder.positiveText(android.R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            onTimeSetListener.onTimeSet(timePicker, timePicker.getCurrentHour(),
                                    timePicker.getCurrentMinute());
                        }
                    })
                    .negativeText(android.R.string.cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .customView(timePicker, true)
                    .show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        } finally {
            mContext = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showDatePicker(final String title, final int year, final int month, final int day,
                               long maxDate,
                               final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        try {
            final android.widget.DatePicker datePicker = new android.widget.DatePicker(mContext);
            datePicker.updateDate(year, month - 1, day);
            datePicker.setCalendarViewShown(false);
            if (maxDate > 0)
                datePicker.setMaxDate(maxDate);

            MaterialDialog.Builder builder = createAlertBuilder();

            if (!TextUtils.isEmpty(title))
                builder.title(title);

            builder.positiveText(android.R.string.ok)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Log.d("AlertManager", "year: " + datePicker.getYear() + " month: " +
                                    datePicker.getMonth() + 1 + " day: " + datePicker.getDayOfMonth());
                            onDateSetListener.onDateSet(datePicker,
                                    datePicker.getYear(),
                                    datePicker.getMonth() + 1,
                                    datePicker.getDayOfMonth());
                        }
                    })
                    .negativeText(android.R.string.cancel)
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .customView(datePicker, true)
                    .show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        } finally {
            mContext = null;
        }
    }

    public void showDatePicker(final String title, final int year, final int month, final int day,
                               final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(title, year, month, day, DateTime.now().plusYears(1).getMillis(),
                onDateSetListener);
    }

    public void showDatePicker(final int year, final int month, final int day,
                               final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(mContext.getString(R.string.check_date), year, month, day,
                DateTime.now().plusYears(1).getMillis(), onDateSetListener);
    }

    public void showBirthDatePicker(final String title, final int year, final int month,
                                    final int day,
                                    final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(title, year, month, day, DateTime.now().getMillis(), onDateSetListener);
    }

    public void showBirthDatePicker(final int year, final int month, final int day,
                                    final android.app.DatePickerDialog.OnDateSetListener onDateSetListener) {
        showDatePicker(mContext.getString(R.string.check_date), year, month, day,
                DateTime.now().getMillis(), onDateSetListener);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void showCalendarPicker(@NonNull FragmentManager fm,
                                   boolean showYearPickerFirst,
                                   boolean dismissOnPause,
                                   @NonNull final AlertDialogCallback<DateTime> callback) {
        try {
            final Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = DatePickerDialog.newInstance(
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            mContext = null;
                            DateTime dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
                            callback.onPick(dateTime);
                        }
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            dpd.showYearPickerFirst(showYearPickerFirst);
            dpd.dismissOnPause(dismissOnPause);

            dpd.show(fm, TAG_CALENDAR_DIALOG);
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        } finally {
            mContext = null;
        }
    }

    public void showMessageEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), null, message, -1, false, callback);
    }

    public void showMessageEditor(String message, int inputType, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), null, message, inputType, false, callback);
    }

    public void showMessageEditor(String hint, String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), hint, message, -1, false, callback);
    }

    public void showSingleLineMessageEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), null, message, -1, true, callback);
    }

    public void showSingleLineMessageEditor(String message, int inputType, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), null, message, inputType, true, callback);
    }

    public void showSingleLineMessageEditor(String hint, String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), hint, message, -1, true, callback);
    }

    public void showTextPasswordEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), mContext.getString(R.string.enter_password), message,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, true, callback);
    }

    public void showTextPasswordEditor(String message, String hint, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), hint, message,
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD, true, callback);
    }

    public void showNumberPasswordEditor(String message, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), mContext.getString(R.string.enter_password), message,
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD, true, callback);
    }

    public void showNumberPasswordEditor(String message, String hint, final AlertDialogCallback<String> callback) {
        showMessageEditor(mContext.getString(sCustomAlertTitle), hint, message,
                InputType.TYPE_CLASS_NUMBER | InputType.TYPE_TEXT_VARIATION_PASSWORD, true, callback);
    }

    //TODO: create Builder: setTitle, setMessage, setListener

    public void showMessageEditor(String title, String hint, String message, int inputType, boolean singleLine,
                                  final AlertDialogCallback<String> callback) {

        try {
            final View customView = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_message,
                    null);

            final TextInputLayout tilMessage = (TextInputLayout) customView.findViewById(R.id.tilMessage);
            final EditText etMessage = (EditText) customView.findViewById(R.id.etMessage);

            //etMessage.setHint(hint);
            etMessage.setText(message);
            etMessage.setSelection(etMessage.length());

            tilMessage.setHint(hint);

            if (inputType >= 0)
                etMessage.setInputType(inputType);

            if (singleLine)
                etMessage.setSingleLine();

            new MaterialDialog.Builder(mContext)
                    .title(title)
                    .customView(customView, false)
                    .positiveText("Готово")
                    .negativeText("Отмена")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            String text = etMessage.getText().toString();
                            if (callback != null) {
                                callback.onPositive(text);
                            }
                        }
                    })
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            mContext = null;
                        }
                    }).show();
        } catch (WindowManager.BadTokenException e) {
            Log.e("debug", "error: ", e);
        } finally {
            mContext = null;
        }
    }
}
