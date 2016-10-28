package com.maverick.tcs.innovations.talentengagment.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.maverick.tcs.innovations.talentengagment.R;
import com.maverick.tcs.innovations.talentengagment.beans.ActivityBean;
import com.maverick.tcs.innovations.talentengagment.beans.JsonDataBean;
import com.maverick.tcs.innovations.talentengagment.db.DBHelper;
import com.maverick.tcs.innovations.talentengagment.ultilities.AppConstants;
import com.maverick.tcs.innovations.talentengagment.ultilities.ConnectionDetector;
import com.maverick.tcs.innovations.talentengagment.ultilities.JsonHelper;
import com.maverick.tcs.innovations.talentengagment.ultilities.Timmings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by abhi on 3/30/2016.
 */
public class TcsUkDescription extends AppCompatActivity {
    private final int id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tcs_uk_desc_activity);
        setTitle("TCS In UK & I");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        String time = Timmings.getCurrentTime();
        SharedPreferences sharedPreferences = getSharedPreferences("Talent Engagement", MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.insertActivity(employeeId, String.valueOf(id), time);
        Log.d("MY TAG", String.valueOf(dbHelper.numberOfRows()));

        if (new ConnectionDetector(getApplicationContext()).isConnectingToInternet()){
            uploadData(getApplicationContext());
        }
    }

    private void uploadData(final Context context) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        String url = AppConstants.url + "upload_activities.php";
        final String TAG = "UPLOAD TAG";

        ArrayList<ActivityBean> activities = new DBHelper(context).getAllActivities();
        JsonDataBean jsonDataBean = new JsonDataBean(activities);
        final String toUpload = JsonHelper.serialize(jsonDataBean);
        Log.d("My JSON: ", toUpload);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                if (response.equals("success")) {
                    new DBHelper(context).deleteAll();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }


        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Registration_Data", toUpload);
                return params;
            }
        };

        mRequestQueue.add(strReq);
    }
}
