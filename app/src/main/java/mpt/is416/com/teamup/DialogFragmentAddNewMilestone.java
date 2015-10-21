package mpt.is416.com.teamup;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Elyza on 7/10/2015.
 */
public class DialogFragmentAddNewMilestone extends DialogFragment implements FetchUpdatesTask.AsyncResponse {
    private final String TAG = DialogFragmentAddNewMilestone.class.getSimpleName();
    private final String ANDROID_ID = "android_id";
    private DialogResponse mListener;

    public interface DialogResponse {
        void processFinish(Milestone output);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_add_new_milestone, null);
        final EditText datePickerET = (EditText) view.findViewById(R.id.new_milestone_date);
        datePickerET.setInputType(InputType.TYPE_NULL);
        datePickerET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the current date as the default date in the picker
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new
                        DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                String dd, mm;
                                if (day < 10)
                                    dd = "0" + day;
                                else
                                    dd = Integer.toString(day);
                                if (month < 10)
                                    mm = "0" + month;
                                else
                                    mm = Integer.toString(month);
                                datePickerET.setText(new StringBuilder().append(dd).append("/")
                                        .append(mm).append("/").append(year));
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        final EditText timePickerET = (EditText) view.findViewById(R.id.new_milestone_time);
        timePickerET.setInputType(InputType.TYPE_NULL);
        timePickerET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the current time as the default values for the picker
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new
                        TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String hh, mm;
                                if (hourOfDay < 10)
                                    hh = "0" + hourOfDay;
                                else
                                    hh = Integer.toString(hourOfDay);
                                if (minute < 10)
                                    mm = "0" + minute;
                                else
                                    mm = Integer.toString(minute);
                                timePickerET.setText(new StringBuilder().append(hh).append(":").append(mm));
                            }
                        }, hour, minute, DateFormat.is24HourFormat(getActivity()));
                timePickerDialog.show();
            }
        });
        builder.setView(view)
                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Milestone newMilestone = validateNewMilestone(view);
                        if (newMilestone.getCreatedBy() != null) {
                            // Send to database
                            String[] fetchInfo = {"insertMilestone", /* TODO: chat_id */"2",
                                    newMilestone.getTitle(), newMilestone.getDescription(),
                                    Integer.toString(newMilestone.getWeek()),
                                    Long.toString(newMilestone.getDatetime().getTime()),
                                    newMilestone.getLocation(), /* TODO: newMilestone.getCreatedBy() */"2",
                                    /* TODO: Remove last modified by in add */"2"};
                            FetchUpdatesTask fetchUpdatesTask = new FetchUpdatesTask();
                            fetchUpdatesTask.delegate = null;
                            fetchUpdatesTask.execute(fetchInfo);
                            mListener.processFinish(newMilestone);
                        } else {
                            // TODO: Dialog with error message to edit
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Milestone newMilestone = validateNewMilestone(view);
                        if (newMilestone.getCreatedBy() != null) {
                            // TODO: Prompt confirm cancel
                        } else {
                            DialogFragmentAddNewMilestone.this.getDialog().cancel();
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        mListener = (DialogResponse) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        mListener = null;
        super.onDetach();
    }

    public void processFinish(String output) {
    }

    private Milestone validateNewMilestone(View view) {
        final String regex = "(^(((0[1-9]|1[0-9]|2[0-8])[\\/](0[1-9]|1[012]))|((29|30|31)[\\/](0[13578]|1[02]))|((29|30)[\\/](0[4,6,9]|11)))[\\/](19|[2-9][0-9])\\d\\d$)|(^29[\\/]02[\\/](19|[2-9][0-9])(00|04|08|12|16|20|24|28|32|36|40|44|48|52|56|60|64|68|72|76|80|84|88|92|96)$)";
        Milestone newMilestone = new Milestone();
        Boolean isValid = true;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String errorMsg = "";
        try {
            // Validate new milestone
            Date formattedDate = null;
            String title = ((EditText) view
                    .findViewById(R.id.new_milestone_title)).getText().toString();
            String date = ((EditText) view
                    .findViewById(R.id.new_milestone_date)).getText().toString();
            String time = ((EditText) view
                    .findViewById(R.id.new_milestone_time)).getText().toString();
            String week = ((EditText) view
                    .findViewById(R.id.new_milestone_week)).getText().toString();
            if (title.isEmpty()) {
                errorMsg += "Please provide a title. ";
            }
            if (date.matches(regex)) {
                formattedDate = sdf.parse(new StringBuilder().append(date)
                        .append(" ").append(time).toString());
            } else if ((!date.isEmpty() && !date.matches(regex)) || (date.isEmpty() && !time.isEmpty())) {
                // In case date is not valid OR time is given without date
                errorMsg += "Please provide a valid date.";
            }
            if (time.isEmpty()) {
                time = "00:00";
            }
            if (week.isEmpty()) {
                week = "0";
            }
            newMilestone.setTitle(title);
            newMilestone.setDescription(((EditText) view
                    .findViewById(R.id.new_milestone_description)).getText().toString());
            newMilestone.setDatetime(formattedDate);
            newMilestone.setWeek(Integer.parseInt(week));
            newMilestone.setLocation(((EditText) view
                    .findViewById(R.id.new_milestone_location)).getText().toString());
            if (!errorMsg.isEmpty()) {
                throw new Exception(errorMsg.trim());
            }
            newMilestone.setCreatedBy(PreferenceManager
                    .getDefaultSharedPreferences(getActivity())
                    .getString(ANDROID_ID, null));
        } catch (Exception e) {
            isValid = false;
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            if (isValid) {
                Log.i(TAG, "successfully validated");
                Log.i(TAG, newMilestone.toString());
            } else {
                Log.i(TAG, "error with data");
                Log.i(TAG, newMilestone.toString());
            }
        }
        return newMilestone;
    }
}
