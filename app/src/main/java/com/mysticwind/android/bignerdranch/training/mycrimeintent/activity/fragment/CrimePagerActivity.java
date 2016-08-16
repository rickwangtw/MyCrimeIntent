package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.application.CrimeIntentApplication;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.manager.CrimeLab;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.model.CrimeRecord;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

public class CrimePagerActivity extends FragmentActivity {

    private static final String CRIME_ID_EXTRA_KEY = "crimeId";

    // package private for Dagger
    @Inject
    CrimeLab crimeLab;
    private ViewPager viewPager;

    public static Intent newLaunchIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(CRIME_ID_EXTRA_KEY, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        viewPager = (ViewPager) findViewById(R.id.activity_crime_pager);

        CrimeIntentApplication.component(this).inject(this);

        final List<CrimeRecord> crimeRecords = crimeLab.getCrimeRecordList();
        FragmentManager fragmentManager = getSupportFragmentManager();

        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                CrimeRecord crimeRecord = crimeRecords.get(position);
                return CrimeFragment.newInstance(crimeRecord.getId());
            }

            @Override
            public int getCount() {
                return crimeRecords.size();
            }
        });

        UUID crimeId = (UUID) getIntent().getSerializableExtra(CRIME_ID_EXTRA_KEY);
        flipViewPagerToPageForCrimeId(crimeRecords, crimeId);
    }

    private void flipViewPagerToPageForCrimeId(List<CrimeRecord> crimeRecords, UUID crimeId) {
        for (int index = 0; index < crimeRecords.size(); index++) {
            if (crimeRecords.get(index).getId().equals(crimeId)) {
                viewPager.setCurrentItem(index);
                return;
            }
        }
    }
}
