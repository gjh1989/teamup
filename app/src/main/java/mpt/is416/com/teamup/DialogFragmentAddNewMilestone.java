package mpt.is416.com.teamup;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
public class DialogFragmentAddNewMilestone extends DialogFragment {
    private final String TAG = DialogFragmentAddNewMilestone.class.getSimpleName();

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
                                datePickerET.setText(new StringBuilder().append(day).append("/")
                                        .append(month).append("/").append(year));
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
                                timePickerET.setText(new StringBuilder().append(hourOfDay).append(":").append(minute));
                            }
                        }, hour, minute, DateFormat.is24HourFormat(getActivity()));
                timePickerDialog.show();
            }
        });
        builder.setView(view)
                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Boolean isValid = true;
                        // Validate new milestone, error with how to get data...
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        try {
                            Date formattedDate = sdf.parse(new StringBuilder().append(((EditText) view
                                    .findViewById(R.id.new_milestone_date)).getText().toString())
                                    .append(" ").append(((EditText) view
                                            .findViewById(R.id.new_milestone_time)).getText().toString())
                                    .toString());
                            Milestone newMilestone = new Milestone();
                            newMilestone.setTitle(((EditText) view
                                    .findViewById(R.id.new_milestone_title)).getText().toString());
                            newMilestone.setDatetime(formattedDate);
                            newMilestone.setWeek(Integer.parseInt(((EditText) view
                                    .findViewById(R.id.new_milestone_week)).getText().toString()));
                            newMilestone.setLocation(((EditText) view
                                    .findViewById(R.id.new_milestone_location)).getText().toString());
                        } catch (Exception e) {
                            isValid = false;
                            Log.e(TAG, e.getMessage());
                            e.printStackTrace();
                        } finally {
                            if (isValid) {
                                // TODO: Add to list and send to database
                                Log.i(TAG, "successfully validated");
                            } else {
                                // TODO: Dialog with error message to edit
                                Log.i(TAG, "error with data");
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Check for any changes and prompt confirm cancel
                        DialogFragmentAddNewMilestone.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
