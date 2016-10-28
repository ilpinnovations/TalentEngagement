package com.maverick.tcs.innovations.talentengagment.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.maverick.tcs.innovations.talentengagment.R;
import com.maverick.tcs.innovations.talentengagment.beans.MenuBean;
import com.maverick.tcs.innovations.talentengagment.db.DBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<MenuBean> data = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupMenuItems();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(new CustomAdapter(data));
    }


    private void setupMenuItems() {
        data = new ArrayList<>();
        MenuBean menuBean = new MenuBean(1, "TCS in UK & I");
        MenuBean menuBean1 = new MenuBean(2, "On boarding formalities");
        MenuBean menuBean2 = new MenuBean(3, "Things to know");
        MenuBean menuBean3 = new MenuBean(4, "Key contacts");
        MenuBean menuBean4 = new MenuBean(5, "Health & Safety in the UK");
        MenuBean menuBean5 = new MenuBean(6, "Workplace behaviours");


        data.add(menuBean);
        data.add(menuBean1);
        data.add(menuBean2);
        data.add(menuBean3);
        data.add(menuBean4);
        data.add(menuBean5);

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

        private void manageItemClick(int i) {
            switch (i) {
                case 1:
                    Intent i1 = new Intent(MainActivity.this, TcsUkDescription.class);
                    i1.putExtra("type", "learningCalendar");
                    startActivity(i1);
                    break;
                case 2:
                    Intent i2 = new Intent(MainActivity.this, OnBoardFormalities.class);
                    startActivity(i2);
                    break;

                case 3:
                    Intent i3 = new Intent(MainActivity.this, ThingsToKnowActivity.class);
                    startActivity(i3);
                    break;

                case 4:
                    Intent i4 = new Intent(MainActivity.this, ContactsActivity.class);
                    startActivity(i4);
                    break;

                case 5:
                    Intent i5 = new Intent(MainActivity.this, HealthSafetyActivity.class);
                    startActivity(i5);
                    break;

                case 6:
                    Intent i6 = new Intent(MainActivity.this, WorkplaceBehaviour.class);
                    startActivity(i6);
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
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color6));

                    break;
                case 5:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color7));

                    break;


                case 6:
                    viewHolder.itemView.setBackgroundColor(getResources().getColor(R.color.color8));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Are you sure you want to logout?");

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    SharedPreferences sharedPreferences = getSharedPreferences("Talent Engagement", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear().apply();

                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    dbHelper.deleteAll();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setIcon(R.mipmap.ic_launcher);
            alertDialog.show();

        }


        return super.onOptionsItemSelected(item);
    }
}
