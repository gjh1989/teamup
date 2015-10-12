package mpt.is416.com.teamup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * Created by Elyza on 7/10/2015.
 */
public class DialogFragmentAddNewMilestone extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_new_milestone, null);
        Button datePicker = (Button) view.findViewById(R.id.new_milestone_date);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        Button timePicker = (Button) view.findViewById(R.id.new_milestone_time);
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        builder.setView(view)
                .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Save new milestone, send to database and add to list
                    }
                })
                .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DialogFragmentAddNewMilestone.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DialogFragmentDatePicker();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DialogFragmentTimePicker();
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }
}
