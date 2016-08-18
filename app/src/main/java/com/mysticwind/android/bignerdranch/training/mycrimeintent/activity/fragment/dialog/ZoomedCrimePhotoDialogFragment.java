package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.net.URI;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ZoomedCrimePhotoDialogFragment extends DialogFragment {

    private static final String PHOTO_URI_ARGUMENT_KEY = "photoUri";

    public static ZoomedCrimePhotoDialogFragment newInstance(URI photoUri) {
        Bundle args = new Bundle();
        args.putSerializable(PHOTO_URI_ARGUMENT_KEY, photoUri);
        ZoomedCrimePhotoDialogFragment fragment = new ZoomedCrimePhotoDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        ImageView view = new ImageView(getActivity());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        view.setLayoutParams(layoutParams);

        URI photoUri = (URI) getArguments().getSerializable(PHOTO_URI_ARGUMENT_KEY);

        view.setImageURI(Uri.parse(photoUri.toString()));

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
