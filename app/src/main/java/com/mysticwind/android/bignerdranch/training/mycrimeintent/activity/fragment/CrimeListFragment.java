package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.CrimeActivity;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.application.CrimeIntentApplication;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.List;

import javax.inject.Inject;

public class CrimeListFragment extends Fragment {

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
            Intent intent = CrimeActivity.newLaunchIntent(getActivity(), crimeRecord.getId());
            startActivity(intent);
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
    }

    @Inject
    public CrimeLab crimeLab;

    private RecyclerView crimeRecyclerView;
    private CrimeAdapter crimeAdapter;

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
    public void onResume() {
        super.onResume();

        updateUi();
    }

    private void updateUi() {
        if (crimeAdapter == null) {
            crimeAdapter = new CrimeAdapter(crimeLab.getCrimeRecordList());
        } else {
            crimeAdapter.notifyDataSetChanged();
        }
        crimeRecyclerView.setAdapter(crimeAdapter);
    }
}
