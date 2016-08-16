package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

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
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.text.SimpleDateFormat;

public class CrimeFragment extends Fragment {

    private EditText crimeTitleEditText;
    private Button dateButton;
    private CheckBox checkBox;

    private CrimeRecord crimeRecord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        crimeRecord = new CrimeRecord();

        crimeTitleEditText = (EditText) view.findViewById(R.id.crime_title);
        crimeTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                crimeRecord.setTitle(charSequence.toString());
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
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                crimeRecord.setSolved(isChecked);
            }
        });

        return view;
    }
}
