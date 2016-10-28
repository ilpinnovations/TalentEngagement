package com.maverick.tcs.innovations.talentengagment.activities;

import android.content.Context;
import android.content.Intent;
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
public class OnBoardFormalities extends AppCompatActivity {
    private ArrayList<MenuBean> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private final int id = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("On Boarding Formalities");
        }

        setContentView(R.layout.onboardformalities_activity);
        setupMenuItems();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(OnBoardFormalities.this));
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
        MenuBean menuBean = new MenuBean(1, "What's where on Ultimatix .");
        MenuBean menuBean1 = new MenuBean(2, "Onsite reporting - steps");
        MenuBean menuBean2 = new MenuBean(3, "Opening a bank account ");


        data.add(menuBean);
        data.add(menuBean1);
        data.add(menuBean2);

        String time = Timmings.getCurrentTime();
        SharedPreferences sharedPreferences = getSharedPreferences("Talent Engagement", MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");

        DBHelper dbHelper = new DBHelper(getApplicationContext());
        dbHelper.insertActivity(employeeId, String.valueOf(id), time);

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
            viewHolder.title.setText(data.get(i).getMenuItem());
            setupImages(viewHolder, data.get(i).getId());
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    manageItemClick(data.get(i).getId());
                }
            });

        }

        private void manageItemClick(int id) {
            switch (id) {
                case 1:
                    Intent intent = new Intent(getApplicationContext(), UltimatixDetailsActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    Intent intent1 = new Intent(getApplicationContext(), OnsiteReportingActivity.class);
                    startActivity(intent1);
                    break;
                case 3:
                    Intent intent2 = new Intent(getApplicationContext(), OpeningBankAccount.class);
                    startActivity(intent2);
                    break;
            }
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
