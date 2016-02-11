package me.zipstream.criminalintent.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

import me.zipstream.criminalintent.R;
import me.zipstream.criminalintent.fragment.CrimeFragment;
import me.zipstream.criminalintent.model.Crime;
import me.zipstream.criminalintent.model.CrimeLab;

/**
 * CrimeList 中点击某项, 跳转到本类, ViewPager, 通过FragmentStatePagerAdapter host CrimeFragment
 */
public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID =
            "me.zipstream.criminalintent.crime_id";

    private ViewPager mViewPager;

    private List<Crime> mCrimes;

    /**
     * 交由 CrimeListFragment 调用来跳转到本类, 通过 Intent 传递 crimeId
     * @param packageContext
     * @param crimeId
     * @return
     */
    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        // 取出 CrimeListFragment 传递过来的 crimeId
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        // 根据传递过来的 crimeId 找到对应的位置, 并设置到 ViewPager 中, 点击哪项就进到哪项
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
