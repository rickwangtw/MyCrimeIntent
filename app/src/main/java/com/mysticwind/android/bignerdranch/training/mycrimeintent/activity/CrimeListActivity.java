package com.mysticwind.android.bignerdranch.training.mycrimeintent.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mysticwind.android.bignerdranch.training.mycrimeintent.R;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.CrimeFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.CrimeListFragment;
import com.mysticwind.android.bignerdranch.training.mycrimeintent.activity.fragment.CrimePagerActivity;

import java.util.UUID;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(UUID crimeId) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newLaunchIntent(this, crimeId);
            startActivity(intent);
        } else {
            Fragment crimeFragment = CrimeFragment.newInstance(crimeId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, crimeFragment)
                    .commit();
        }
    }
}
