package com.maverick.tcs.innovations.talentengagment.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.maverick.tcs.innovations.talentengagment.beans.MenuBean;
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
public class ThingsToKnowActivity extends AppCompatActivity {
    private final int id=6;
    private ArrayList<MenuBean> data = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Things To Know");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_main);
        setupMenuItems();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ThingsToKnowActivity.this));
        recyclerView.setAdapter(new CustomAdapter(data));

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

    private void setupMenuItems() {
        data = new ArrayList<>();
        MenuBean menuBean = new MenuBean(1, "HMRC requirements - On going formalities.");
        MenuBean menuBean1 = new MenuBean(2, "TCS India Expat Pay Model - UK");
        MenuBean menuBean2 = new MenuBean(3, "Leave policy - Expats ");
        MenuBean menuBean3 = new MenuBean(4, "Additional Housing(Rental and Council Tax) allowance");
        MenuBean menuBean4 = new MenuBean(5, "Pro Tenant: Low cost home search programme");
        MenuBean menuBean5 = new MenuBean(6, "TCS UK Employee Discount Scheme");
        MenuBean menuBean6 = new MenuBean(7, "Employee Assistance Program(EAP)");
        MenuBean menuBean7 = new MenuBean(8, "HR for you");
        MenuBean menuBean8 = new MenuBean(9, "Communication");


        data.add(menuBean);
        data.add(menuBean1);
        data.add(menuBean2);
        data.add(menuBean3);
        data.add(menuBean4);
        data.add(menuBean5);
        data.add(menuBean6);
        data.add(menuBean7);
        data.add(menuBean8);


    }


    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
        ArrayList<MenuBean> data;

        public CustomAdapter(ArrayList<MenuBean> data) {
            this.data = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v;
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.title.setText(i + 1 + " - " + data.get(i).getMenuItem());
            setupImages(viewHolder, data.get(i).getId());


        }


        private void setupImages(ViewHolder viewHolder, int id) {
            switch (id) {
                case 1:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color1));
                    break;
                case 2:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color2));
                    break;

                case 3:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color3));
                    break;

                case 4:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color4));
                    break;

                case 5:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color5));
                    break;

                case 6:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color1));
                    break;

                case 7:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color2));
                    break;

                case 8:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color3));
                    break;
                case 9:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color4));
                    break;

            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView title;


            public ViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.menuTitle);

            }

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
