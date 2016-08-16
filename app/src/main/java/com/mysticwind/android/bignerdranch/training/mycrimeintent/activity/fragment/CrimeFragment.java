package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.CrimeActivity;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.application.CrimeIntentApplication;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.inject.Inject;

public class CrimeFragment extends Fragment {

    private static final String CRIME_ID_KEY = "crimeId";
    private static final String CRIME_ID_EXTRA_KEY = "crimeId";

    @Inject
    CrimeLab crimeLab;

    private EditText crimeTitleEditText;
    private Button dateButton;
    private CheckBox checkBox;

    private CrimeRecord crimeRecord;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID_KEY, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        CrimeIntentApplication.component(getActivity()).inject(this);

        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID_KEY);
        crimeRecord = crimeLab.getCrimeRecord(crimeId);

        crimeTitleEditText = (EditText) view.findViewById(R.id.crime_title);
        crimeTitleEditText.setText(crimeRecord.getTitle());
        crimeTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                crimeRecord.setTitle(charSequence.toString());
                setCrimeChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        dateButton = (Button) view.findViewById(R.id.crime_date);
        // Wednesday, Jul 22, 2015
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
        dateButton.setText(simpleDateFormat.format(crimeRecord.getDateTime()));
        dateButton.setEnabled(false);

        checkBox = (CheckBox) view.findViewById(R.id.crime_solved);
        checkBox.setChecked(crimeRecord.isSolved());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                crimeRecord.setSolved(isChecked);
                setCrimeChanged();
            }
        });

        return view;
    }

    private void setCrimeChanged() {
        Intent data = new Intent();
        data.putExtra(CRIME_ID_EXTRA_KEY, crimeRecord.getId());
        getActivity().setResult(Activity.RESULT_OK, data);
    }

    public static UUID extractCrimeId(Intent data) {
        return (UUID) data.getSerializableExtra(CRIME_ID_EXTRA_KEY);
    }
}
