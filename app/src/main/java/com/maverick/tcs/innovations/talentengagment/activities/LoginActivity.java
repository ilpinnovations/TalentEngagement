package com.maverick.tcs.innovations.talentengagment.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.maverick.tcs.innovations.talentengagment.R;
import com.maverick.tcs.innovations.talentengagment.ultilities.ConnectionDetector;
import com.maverick.tcs.innovations.talentengagment.ultilities.RegisterUserAsync;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 1007546 on 12-09-2016.
 */
public class LoginActivity extends AppCompatActivity {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@tcs.com", Pattern.CASE_INSENSITIVE);

    private EditText empId;
    private EditText empEmail;

    private Button submitButton;
    private View view1, view2;
    private TextView messageText;
    private CoordinatorLayout coordinatorLayout;


    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("Talent Engagement", MODE_PRIVATE);
        String employeeId = sharedPreferences.getString("emp_id", "");

        if (employeeId != "") {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.login_activity);
        getSupportActionBar().setTitle("Talent Engagement");


        empId = (EditText) findViewById(R.id.employeeId);
        empEmail = (EditText) findViewById(R.id.employeeEmail);
        submitButton = (Button) findViewById(R.id.submitButton);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        messageText = (TextView) findViewById(R.id.messageText);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validData()) {
                    if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
                        performRegistration();
                    } else {
                        displaySnackbar();
                    }

                }
            }
        });


    }

    private void displaySnackbar() {
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please try again later.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("Retry", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new ConnectionDetector(LoginActivity.this).isConnectingToInternet()) {
                    performRegistration();
                    snackbar.dismiss();
                } else {
                    displaySnackbar();
                }
            }
        });
        snackbar.show();
    }


    private void performRegistration() {

        final String employeeId = empId.getText().toString().trim();
        final String employeeEmail = empEmail.getText().toString().trim();
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("Registering...");

        RegisterUserAsync registerUserAsync = new RegisterUserAsync(LoginActivity.this, new RegisterUserAsync.OnService() {
            @Override
            public void onService(String string) {
                pd.cancel();

                if (string != null) {
                    Log.d("Registration Response", "Hello " + string);

                    if (string.equals("0")) {
                        view2.setVisibility(View.GONE);
                        view1.setVisibility(View.VISIBLE);

                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Registration unsuccessful, unauthorized user.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        return;
                    } else if (string.equals("1")) {

                        SharedPreferences sharedPreferences = getSharedPreferences("Talent Engagement", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("emp_id", employeeId);
                        //Log.d("Employee id", employeeId);
                        editor.putString("emp_email", employeeEmail);
                        //Log.d("Employee email", employeeEmail);
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();


                    } else {
                        view2.setVisibility(View.GONE);
                        view1.setVisibility(View.VISIBLE);
                        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connectivity,please try again later.", Snackbar.LENGTH_INDEFINITE);
                        Toast.makeText(getApplicationContext(), "No internet connection, please try again later", Toast.LENGTH_LONG).show();
                    }

                } else {
                    //Log.d("My Tag", "Inside else");
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "No internet connection, please try again later", Toast.LENGTH_LONG).show();
                }


            }
        });
        registerUserAsync.execute(employeeId, employeeEmail);

        pd.show();

    }


    private boolean validData() {
        String employeeId = empId.getText().toString().trim();
        String employeeEmail = empEmail.getText().toString().trim();

        if (employeeId.length() == 0) {
            empId.setError("Please enter a valid employee id.");
            return false;
        } else if (!(employeeEmail.length() > 0 && validate(employeeEmail))) {
            empEmail.setError("Please enter a valid email address.");
            return false;
        }

        return true;
    }

}

