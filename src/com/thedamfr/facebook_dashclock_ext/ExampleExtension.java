/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thedamfr.facebook_dashclock_ext;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ExampleExtension extends DashClockExtension {
    private static final String TAG = "ExampleExtension";

    public static final String PREF_LOGGED_IN = "loggedIn";


    @Override
    protected void onUpdateData(int reason) {
        Session fbSession = Session.getActiveSession();
        if (fbSession == null) {
            Resources resources = this.getResources();
            AssetManager assetManager = resources.getAssets();
            Properties properties = new Properties();
            // Read from the /assets directory
            try {
                InputStream inputStream = assetManager.open("facebook.properties");
                properties.load(inputStream);
                System.out.println("The properties are now loaded");
                //System.out.println("properties: " + properties);
            } catch (IOException e) {
                System.err.println("Failed to open facebook property file");
                e.printStackTrace();
            }

            Session session = new Session.Builder(this).setApplicationId(properties.getProperty("app_id", "")).build();
            Session.setActiveSession(session);
            fbSession = session;
        }
        if(fbSession != null && !fbSession.isOpened())   {
            Intent intent = new Intent(this, RefreshSessionActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (fbSession.isOpened()) {
            Request notificationsRequest = Request.newGraphPathRequest(fbSession, "me/notifications", new Request.Callback() {

                @Override
                public void onCompleted(Response response) {
                    GraphObject object = response.getGraphObject();
                    if (object != null) {
                        JSONArray notifications = (JSONArray) object.getProperty("data");
                        if (notifications.length() >= 1) {
                            // Publish the extension data update.
                            String title = null;
                            String link = null;
                            try {
                                title = ((JSONObject) notifications.get(0)).getString("title");
                                link = ((JSONObject) notifications.get(0)).getString("link");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                title = "Can't read title";
                            }
                            publishUpdate(new ExtensionData()
                                    .visible(true)
                                    .icon(R.drawable.icon)
                                    .status("New Content")
                                    .expandedTitle(notifications.length() + "  notifications")
                                    .expandedBody(title)
                                    .clickIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(link))));

                        } else {
                            publishUpdate(new ExtensionData()
                                    .visible(false)
                                    .status("No Content"));
                        }
                    } else {
                        publishUpdate(new ExtensionData()
                                .visible(false)
                                .status("No Content"));
                    }

                }
            });

            notificationsRequest.executeAsync();
        }


    }
}
