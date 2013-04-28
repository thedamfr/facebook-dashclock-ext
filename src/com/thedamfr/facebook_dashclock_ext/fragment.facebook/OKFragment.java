package com.thedamfr.facebook_dashclock_ext.fragment.facebook;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.thedamfr.facebook_dashclock_ext.R;


public class OKFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_success_f, container, false);
        return view;
    }
}
