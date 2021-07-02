package com.hopetechno.raadarbar.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.hopetechno.raadarbar.Fragment.CashFragment;
import com.hopetechno.raadarbar.Fragment.NetBankingFragment;

public class PaymentAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PaymentAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                CashFragment tab1 = new CashFragment();
                return tab1;
            case 1:
                NetBankingFragment tab2 = new NetBankingFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}