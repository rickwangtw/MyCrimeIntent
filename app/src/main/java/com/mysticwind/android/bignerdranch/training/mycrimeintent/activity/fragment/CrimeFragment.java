package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog.DatePickerDialogFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog.TimePickerDialogFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.dialog.ZoomedCrimePhotoDialogFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.application.CrimeIntentApplication;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.common.PictureUtils;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.Time;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

public class CrimeFragment extends Fragment {

    private static final String CRIME_ID_KEY = "crimeId";
    private static final String CRIME_ID_EXTRA_KEY = "crimeId";
    private static final String DATE_PICKER_DIALOG_TAG = "datePickerDialog";
    private static final String TIME_PICKER_DIALOG_TAG = "timePickerDialog";
    private static final String ZOOMED_CRIME_PHOTO_DIALOG_TAG = "zoomedCrimePhotoDialog";
    public static final int REQUEST_DATE_CODE = 0xFF01;
    public static final int REQUEST_TIME_CODE = 0xFF02;
    public static final int REQUEST_CONTACT_CODE = 0xFF03;
    public static final int REQUEST_PHOTO_CODE = 0xFF04;

    private static final Intent PICK_CONTACT_INTENT = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    private static final Intent CAPTURE_IMAGE_INTENT = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    public interface Callbacks {
        void onCrimeUpdated(UUID crimeId);
    }

    @Inject
    CrimeLab crimeLab;

    private EditText crimeTitleEditText;
    private ImageView photoView;
    private ImageButton cameraCaptureButton;
    private Button dateButton;
    private Button timeButton;
    private CheckBox checkBox;

    private CrimeRecord crimeRecord;
    private Button suspectButton;
    private Button callCrimeSuspectButton;
    private Button reportButton;

    private Callbacks callback;

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
            updateDate(date);
        } else if (requestCode == REQUEST_TIME_CODE) {
            Time time = TimePickerDialogFragment.extractTime(data);
            updateTime(time);
        } else if (requestCode == REQUEST_CONTACT_CODE && data != null) {
            Uri contactUri = data.getData();
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor cursor = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try {
                if (cursor.getCount() == 0) {
                    return;
                }
                cursor.moveToFirst();
                String suspect = cursor.getString(0);
                updateSuspect(suspect);
            } finally {
                cursor.close();
            }
        } else if (requestCode == REQUEST_PHOTO_CODE) {
            updatePhotoView();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CrimeFragment.Callbacks) {
            callback = (Callbacks) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
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
                updateTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        photoView = (ImageView) view.findViewById(R.id.crime_photo_view);
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File photoFile = getPhotoFile();
                if (photoFile != null && photoFile.exists()) {
                    ZoomedCrimePhotoDialogFragment fragment = ZoomedCrimePhotoDialogFragment.newInstance(photoFile.toURI());
                    fragment.show(getFragmentManager(), ZOOMED_CRIME_PHOTO_DIALOG_TAG);
                }
            }
        });
        updatePhotoView();

        cameraCaptureButton = (ImageButton) view.findViewById(R.id.crime_camera_capture_button);
        cameraCaptureButton.setEnabled(canTakePhoto());
        cameraCaptureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CAPTURE_IMAGE_INTENT.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getPhotoFile()));
                startActivityForResult(CAPTURE_IMAGE_INTENT, REQUEST_PHOTO_CODE);
            }
        });

        dateButton = (Button) view.findViewById(R.id.crime_date);
        updateDateDisplay(crimeRecord.getDateTime());
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
            } });

        timeButton = (Button) view.findViewById(R.id.crime_time);
        updateTimeDisplay(crimeRecord.getDateTime());
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerDialogFragment dialog = TimePickerDialogFragment.newInstance(crimeRecord.getDateTime());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME_CODE);
                dialog.show(fragmentManager, TIME_PICKER_DIALOG_TAG);
            }
        });

        checkBox = (CheckBox) view.findViewById(R.id.crime_solved);
        checkBox.setChecked(crimeRecord.isSolved());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                updateSolved(isChecked);
            }
        });

        suspectButton = (Button) view.findViewById(R.id.crime_suspect_button);
        if (crimeRecord.getSuspect() != null) {
            suspectButton.setText(crimeRecord.getSuspect());
        }
        suspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(PICK_CONTACT_INTENT, REQUEST_CONTACT_CODE);
            }
        });

        callCrimeSuspectButton = (Button) view.findViewById(R.id.call_crime_suspect_button);

        updateSuspectView(crimeRecord.getSuspect());

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(PICK_CONTACT_INTENT, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            suspectButton.setEnabled(false);
        }

        reportButton = (Button) view.findViewById(R.id.crime_report_button);
        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                        .setSubject(getString(R.string.crime_report_subject))
                        .setText(getCrimeReport())
                        .setChooserTitle(R.string.send_report)
                        .setType("text/plain").getIntent();
                startActivity(intent);
            }
        });

        return view;
    }

    private boolean canTakePhoto() {
        return getPhotoFile() != null &&
                CAPTURE_IMAGE_INTENT.resolveActivity(getActivity().getPackageManager()) != null;
    }

    private String getPhoneNumber(String name) {
        String contactId = getContactId(name);
        if (contactId == null || contactId.length() == 0) {
            return null;
        }
        final ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{ contactId },
                null);
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToNext();

            int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            return cursor.getString(phoneNumberIndex);
        } finally {
            cursor.close();
        }
    }

    private String getContactId(String name) {
        final ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{ ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME },
                ContactsContract.Contacts.DISPLAY_NAME + " = ?",
                new String[]{ name },
                null);
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToNext();

            int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            return cursor.getString(idColumnIndex);
        } finally {
            cursor.close();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        crimeLab.updateCrimeRecord(crimeRecord);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        callback = null;
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
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static UUID extractCrimeId(Intent data) {
        return (UUID) data.getSerializableExtra(CRIME_ID_EXTRA_KEY);
    }

    private String getCrimeReport() {
        String solvedString;
        if (crimeRecord.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, crimeRecord.getDateTime()).toString();
        String suspect = crimeRecord.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                crimeRecord.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    private File getPhotoFile() {
        File externalFilesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }
        return new File(externalFilesDir, getPhotoFileName());
    }

    private String getPhotoFileName() {
        return "IMG_" + crimeRecord.getId().toString() + ".jpg";
    }

    private void updatePhotoView() {
        photoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                File photoFile = getPhotoFile();
                if (photoFile == null || !photoFile.exists()) {
                    photoView.setImageDrawable(null);
                } else {
                    int height = photoView.getHeight();
                    int width = photoView.getWidth();

                    Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), width, height);
                    photoView.setImageBitmap(bitmap);
                }
            }
        });
    }

    private void updateTitle(String updatedTitle) {
        crimeRecord.setTitle(updatedTitle);
        crimeLab.updateCrimeRecord(crimeRecord);
        setCrimeChanged();

        updateTitleDisplay(updatedTitle);
    }

    private void updateTitleDisplay(String updatedTitle) {
        if (crimeTitleEditText.getText().toString().equals(updatedTitle)) {
            return;
        }
        crimeTitleEditText.setText(updatedTitle);
    }

    private void updateDate(Date date) {
        crimeRecord.updateDate(date);
        crimeLab.updateCrimeRecord(crimeRecord);
        setCrimeChanged();

        updateDateDisplay(date);
    }

    private void updateDateDisplay(Date date) {
        // Wednesday, Jul 22, 2015
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E, MMM dd, yyyy");
        dateButton.setText(simpleDateFormat.format(date));
    }

    private void updateTime(Time time) {
        crimeRecord.updateTime(time);
        crimeLab.updateCrimeRecord(crimeRecord);
        setCrimeChanged();

        updateTimeDisplay(crimeRecord.getDateTime());
    }

    private void updateTimeDisplay(Date dateTime) {
        timeButton.setText(
                String.format("%02d:%02d",
                        dateTime.getHours(),
                        dateTime.getMinutes()));
    }

    private void updateSolved(boolean isSolved) {
        crimeRecord.setSolved(isSolved);
        crimeLab.updateCrimeRecord(crimeRecord);
        setCrimeChanged();

        updateSolvedDisplay(isSolved);
    }

    private void updateSolvedDisplay(boolean isSolved) {
        if (checkBox.isChecked() == isSolved) {
            return;
        }
        checkBox.setChecked(isSolved);
    }

    private void updateSuspect(String suspect) {
        crimeRecord.setSuspect(suspect);
        crimeLab.updateCrimeRecord(crimeRecord);
        setCrimeChanged();

        updateSuspectView(suspect);
    }

    private void updateSuspectView(String suspect) {
        if (suspect == null || suspect.length() == 0) {
            callCrimeSuspectButton.setEnabled(false);
        } else {
            suspectButton.setText(suspect);

            final String phoneNumber = getPhoneNumber(suspect);
            if (phoneNumber == null || phoneNumber.length() == 0) {
                callCrimeSuspectButton.setEnabled(false);
            } else {
                callCrimeSuspectButton.setEnabled(true);
                callCrimeSuspectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Intent callContactIntent = new Intent(Intent.ACTION_DIAL);
                        callContactIntent.setData(Uri.parse("tel:" + phoneNumber));
                        startActivity(callContactIntent);
                    }
                });
            }
        }
    }

    private void setCrimeChanged() {
        if (callback != null) {
            callback.onCrimeUpdated(crimeRecord.getId());
        }
    }


}
