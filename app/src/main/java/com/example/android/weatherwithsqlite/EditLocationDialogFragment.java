/*package com.example.android.weatherwithsqlite;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import static android.support.v4.os.LocaleListCompat.create;


/*public class EditLocationDialogFragment extends DialogFragment {
    public static EditLocationDialogFragment newInstance(int title) {
        EditLocationDialogFragment frag = new EditLocationDialogFragment();
        Bundle args = new Bundle();
        args.putInt("Add Location", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.activity_main, null));
            builder.setMessage(R.string.dialog_box)
                    .setView(R.id.et_location_entry)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((FragmentAlertDialog)getActivity()).doPositiveClick();
                        }

            // Create the AlertDialog object and return it
            return builder.create()
        }
    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    }*/



