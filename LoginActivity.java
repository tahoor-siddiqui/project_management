package com.android.task.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.task.bean.DeveloperBean;
import com.android.task.context.ApplicationContext;
import com.android.task.db.DBAdapter;
import com.example.androidtasksystem.R;

public class LoginActivity extends Activity {

	// Declare UI elements
	Button login;
	EditText username, password;
	Spinner spinnerLoginAs;
	String userRole;
	// Define roles
	private String[] userRoleString = new String[]{"admin", "developer"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		// Initialize UI elements
		login = (Button) findViewById(R.id.buttonlogin);
		username = (EditText) findViewById(R.id.editTextusername);
		password = (EditText) findViewById(R.id.editTextpassword);
		spinnerLoginAs = (Spinner) findViewById(R.id.spinnerloginas);

		// Set up the spinner for role selection
		spinnerLoginAs.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View view, int arg2, long arg3) {
				((TextView) arg0.getChildAt(0)).setTextColor(Color.WHITE);
				userRole = (String) spinnerLoginAs.getSelectedItem();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// Set up the spinner adapter
		ArrayAdapter<String> adapterRole = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, userRoleString);
		adapterRole.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerLoginAs.setAdapter(adapterRole);

		// Set up the login button click listener
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// Handle admin login
					if (userRole.equals("admin")) {
						String userName = username.getText().toString();
						String passWord = password.getText().toString();

						// Validate username and password fields
						if (TextUtils.isEmpty(userName)) {
							username.setError("Invalid User Name");
						} else if (TextUtils.isEmpty(passWord)) {
							password.setError("Enter password");
						} else {
							// Check admin credentials
							if (userName.equals("1") && passWord.equals("1")) {
								Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
								startActivity(intent);
								Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
							}
						}
					} else { // Handle developer login
						String userName = username.getText().toString();
						String passWord = password.getText().toString();

						// Validate username and password fields
						if (TextUtils.isEmpty(userName)) {
							username.setError("Invalid User Name");
						} else if (TextUtils.isEmpty(passWord)) {
							password.setError("Enter password");
						} else {
							// Validate developer credentials
							DBAdapter dbAdapter = new DBAdapter(LoginActivity.this);
							DeveloperBean developerBean = dbAdapter.validateDeveloper(userName, passWord);

							// Check if the developer is valid
							if (developerBean != null) {
								Intent intent = new Intent(LoginActivity.this, AddTaskSessionActivity.class);
								startActivity(intent);
								((ApplicationContext) LoginActivity.this.getApplicationContext()).setDeveloperBean(developerBean);
								Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_SHORT).show();
							}
						}
					}
				} catch (Exception e) {
					// Handle any unexpected exceptions
					Toast.makeText(getApplicationContext(), "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
