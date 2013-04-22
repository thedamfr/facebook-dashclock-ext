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

public class ExampleExtension extends DashClockExtension {
    private static final String TAG = "ExampleExtension";

    public static final String PREF_NAME = "pref_name";


    @Override
    protected void onUpdateData(int reason) {
        // Get preference value.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String name = sp.getString(PREF_NAME, getString(R.string.pref_name_default));
        Session fbSession = Session.getActiveSession();
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
