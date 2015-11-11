package mpt.is416.com.teamup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Elyza on 27/10/2015.
 */
public class DialogFragmentError extends DialogFragment {
    private final String TAG = DialogFragmentError.class.getSimpleName();

    public DialogFragmentError() {
    }

    public static DialogFragmentError newInstance(String message) {
        DialogFragmentError dialogFragmentError = new DialogFragmentError();
        Bundle args = new Bundle();
        args.putString("message", message);
        dialogFragmentError.setArguments(args);
        return dialogFragmentError;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String error = getArguments().getString("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(error)
                .setTitle(R.string.title_dialog_error)
                .setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                    }
                })
                .setCancelable(false);
        return builder.create();
    }
}
