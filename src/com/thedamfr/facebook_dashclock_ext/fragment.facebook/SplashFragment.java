package com.thedamfr.facebook_dashclock_ext.fragment.facebook;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.widget.LoginButton;
import com.thedamfr.facebook_dashclock_ext.R;

import java.util.Arrays;

public class SplashFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_f, container, false);
        LoginButton authButton = (LoginButton) view.findViewById(R.id.login_button);
        authButton.setPublishPermissions(Arrays.asList("manage_notifications"));
        return view;

    }
}
