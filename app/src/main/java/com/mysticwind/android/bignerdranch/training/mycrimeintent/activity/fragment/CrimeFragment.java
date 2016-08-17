package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog.DatePickerDialogFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog.TimePickerDialogFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.application.CrimeIntentApplication;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.Time;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

public class CrimeFragment extends Fragment {

    private static final String CRIME_ID_KEY = "crimeId";
    private static final String CRIME_ID_EXTRA_KEY = "crimeId";
    private static final String DATE_PICKER_DIALOG_TAG = "datePickerDialog";
    private static final String TIME_PICKER_DIALOG_TAG = "timePickerDialog";
    public static final int REQUEST_DATE_CODE = 0xFF01;
    public static final int REQUEST_TIME_CODE = 0xFF02;

    @Inject
    CrimeLab crimeLab;

    private EditText crimeTitleEditText;
    private Button dateButton;
    private Button timeButton;
    private CheckBox checkBox;

    private CrimeRecord crimeRecord;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID_KEY, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE_CODE) {
            Date date = DatePickerDialogFragment.extractDate(data);
            crimeRecord.updateDateTime(date);
            updateDate(date);
        } else if (requestCode == REQUEST_TIME_CODE) {
            Time time = TimePickerDialogFragment.extractTime(data);
            crimeRecord.updateDateTime(time);
            updateTime(crimeRecord.getDateTime());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    private void updateDate(Date date) {
        // Wednesday, Jul 22, 2015
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
        dateButton.setText(simpleDateFormat.format(date));
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
        updateDate(crimeRecord.getDateTime());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTablet()) {
                    FragmentManager manager = getFragmentManager();
                    DatePickerDialogFragment dialog = DatePickerDialogFragment.newInstance(crimeRecord.getDateTime());
                    dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE_CODE);
                    dialog.show(manager, DATE_PICKER_DIALOG_TAG);
                } else {
                    Intent intent = DatePickerActivity.newLaunchIntent(getActivity(), crimeRecord.getDateTime());
                    startActivityForResult(intent, REQUEST_DATE_CODE);
                }
                setCrimeChanged();
            } });

        timeButton = (Button) view.findViewById(R.id.crime_time);
        updateTime(crimeRecord.getDateTime());
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerDialogFragment dialog = TimePickerDialogFragment.newInstance(crimeRecord.getDateTime());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME_CODE);
                dialog.show(fragmentManager, TIME_PICKER_DIALOG_TAG);
                setCrimeChanged();
            }
        });

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

    // TODO put this in DI
    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                crimeLab.deleteCrimeRecord(crimeRecord.getId());
                setCrimeChanged();
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateTime(Date dateTime) {
        timeButton.setText(
                String.format("%02d:%02d",
                        dateTime.getHours(),
                        dateTime.getMinutes()));
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
