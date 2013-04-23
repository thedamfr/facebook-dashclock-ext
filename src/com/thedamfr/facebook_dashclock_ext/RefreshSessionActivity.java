package com.thedamfr.facebook_dashclock_ext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

import java.util.ArrayList;
import java.util.List;


public class RefreshSessionActivity extends Activity {

    private static final String TAG = "FB-EXT";
    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            if (state.isOpened()) {
                Log.i(TAG, "Logged in...");
                finish();
            } else if (state.isClosed()) {
                Log.i(TAG, "Logged out...");
                finish();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        Session session = Session.getActiveSession();
        if (session.getState() == SessionState.CREATED_TOKEN_LOADED) {
            Session.OpenRequest openRequest = new Session.OpenRequest(this);
            List<String> publish = new ArrayList<String>();
            publish.add("manage_notifications");
            openRequest.setPermissions(publish);
            openRequest.setCallback(callback);
            session.openForPublish(openRequest);
        }
        else {
            finish();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
}