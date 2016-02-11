package me.zipstream.criminalintent.activity;

import android.support.v4.app.Fragment;

import me.zipstream.criminalintent.fragment.CrimeListFragment;

/**
 * Crime 列表的 Activity , host CrimeListFragment
 */
public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
