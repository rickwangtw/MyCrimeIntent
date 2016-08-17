package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.application.CrimeIntentApplication;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

public class CrimeListFragment extends Fragment {

    private static final String TAG =  CrimeListFragment.class.getSimpleName();

    public interface Callbacks {
        void onCrimeSelected(UUID crimeId);
    }

    private static final String IS_SUBTITLE_VISIBLE_KEY = "isSubtitleVisible";
    private static final int VIEW_CRIME_RECORD_REQUEST_CODE = 0xFF00;

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CrimeRecord crimeRecord;
        private final TextView titleTextView;
        private final TextView dateTextView;
        private final CheckBox solvedCheckBox;

        public CrimeHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.list_item_crime_title_text_view);
            dateTextView = (TextView) itemView.findViewById(R.id.list_item_crime_date_text_view);
            solvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_crime_solved_check_box);
            itemView.setOnClickListener(this);
        }

        public void bindCrime(CrimeRecord crime) {
            crimeRecord = crime;
            titleTextView.setText(crime.getTitle());
            dateTextView.setText(crime.getDateTime().toString());
            solvedCheckBox.setChecked(crime.isSolved());
        }

        @Override
        public void onClick(View view) {
            callbacks.onCrimeSelected(crimeRecord.getId());
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        private List<CrimeRecord> crimeRecords;

        public CrimeAdapter(List<CrimeRecord> crimeRecords) {
            this.crimeRecords = crimeRecords;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeHolder(view);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            CrimeRecord crime = crimeRecords.get(position);
            holder.bindCrime(crime);
        }

        @Override
        public int getItemCount() {
            return crimeRecords.size();
        }

        public void notifyCrimeChanged(UUID crimeId) {
            int index = getIndexOfCrimeId(crimeId);
            if (index < 0) {
                return;
            }
            notifyItemChanged(index);
        }

        private int getIndexOfCrimeId(UUID crimeId) {
            for (int index = 0 ; index < crimeRecords.size() ; ++index) {
                if (crimeRecords.get(index).getId().equals(crimeId)) {
                    return index;
                }
            }
            return -1;
        }

        public void updateCrimeRecords(List<CrimeRecord> crimeRecordList) {
            this.crimeRecords = crimeRecordList;
        }
    }

    @Inject
    public CrimeLab crimeLab;

    private RecyclerView crimeRecyclerView;
    private TextView noCrimeTextView;
    private CrimeAdapter crimeAdapter;
    private boolean subtitleVisible = false;

    private Callbacks callbacks;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode != VIEW_CRIME_RECORD_REQUEST_CODE) {
            return;
        }

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        UUID crimeId = CrimeFragment.extractCrimeId(data);
        crimeAdapter.notifyCrimeChanged(crimeId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Callbacks) {
            callbacks = (Callbacks) context;
        } else {
            Log.d(TAG, "context is not instance of Callbacks: " + context.getClass().getSimpleName());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            subtitleVisible = savedInstanceState.getBoolean(IS_SUBTITLE_VISIBLE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        CrimeIntentApplication.component(this.getActivity()).inject(this);

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        crimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        noCrimeTextView = (TextView) view.findViewById(R.id.no_crime_text_view);

        updateUi();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUi();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(IS_SUBTITLE_VISIBLE_KEY, subtitleVisible);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        callbacks = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (subtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_new_crime:
                CrimeRecord crimeRecord = new CrimeRecord();
                crimeLab.addCrimeRecord(crimeRecord);
                updateUi();
                callbacks.onCrimeSelected(crimeRecord.getId());
                return true;
            case R.id.menu_item_show_subtitle:
                subtitleVisible = !subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUi() {
        List<CrimeRecord> crimeRecordList = crimeLab.getCrimeRecordList();

        if (crimeRecordList.isEmpty()) {
            noCrimeTextView.setVisibility(View.VISIBLE);
            crimeRecyclerView.setVisibility(View.GONE);
        } else {
            noCrimeTextView.setVisibility(View.GONE);
            crimeRecyclerView.setVisibility(View.VISIBLE);
        }

        if (crimeAdapter == null) {
            crimeAdapter = new CrimeAdapter(crimeRecordList);
        } else {
            crimeAdapter.updateCrimeRecords(crimeRecordList);
            crimeAdapter.notifyDataSetChanged();
        }
        crimeRecyclerView.setAdapter(crimeAdapter);

        updateSubtitle();
    }

    private void updateSubtitle() {
        String subtitle;
        if (!subtitleVisible) {
            subtitle = null;
        } else {
            int crimeCount = crimeLab.getCrimeRecordList().size();
            subtitle = MessageFormat.format(getString(R.string.numberOfCrimes), crimeCount);
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
