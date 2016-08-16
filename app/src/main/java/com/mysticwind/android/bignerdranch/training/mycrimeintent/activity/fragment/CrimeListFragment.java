package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

public class CrimeListFragment extends Fragment {

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
            Intent intent = CrimePagerActivity.newLaunchIntent(getActivity(), crimeRecord.getId());
            startActivityForResult(intent, VIEW_CRIME_RECORD_REQUEST_CODE);
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
    }

    @Inject
    public CrimeLab crimeLab;

    private RecyclerView crimeRecyclerView;
    private CrimeAdapter crimeAdapter;
    private boolean subtitleVisible = false;

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

        CrimeIntentApplication.component(this.getActivity()).inject(this);

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        crimeRecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUi();

        return view;
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
                Intent intent = CrimePagerActivity.newLaunchIntent(getActivity(), crimeRecord.getId());
                startActivity(intent);
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
        if (crimeAdapter == null) {
            crimeAdapter = new CrimeAdapter(crimeLab.getCrimeRecordList());
        } else {
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
            subtitle = getString(R.string.subtitle_format, crimeCount);
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }
}
