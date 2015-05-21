package com.yunhuirong.jpayapp;

import java.io.File;

import util.FileHelper;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import application.JPayApplication;

public class CtimeActivity extends Activity {
	EditText et;
	JPayApplication app;
	TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ctime);
		app = (JPayApplication) getApplication();
		et = (EditText) findViewById(R.id.editText1);
		Button create = (Button) findViewById(R.id.button1);
		Button state = (Button) findViewById(R.id.button2);
		Button check = (Button) findViewById(R.id.button3);
		text = (TextView) findViewById(R.id.text);

		create.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String file = app.getFilesDir() + File.separator
						+ et.getText().toString().trim();
				FileHelper.writeFile(file, "");
			}
		});
		state.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String file = app.getFilesDir() + File.separator
						+ et.getText().toString();
				text.setText(app.getCtime(file));
			}
		});

		check.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String file = app.getFilesDir() + File.separator
						+ et.getText().toString();
				boolean flag = app.getCtime(file).equals(
						text.getText().toString());

				Toast.makeText(getApplicationContext(), app.getCtime(file) + flag,
						Toast.LENGTH_LONG).show();
			}
		});

	}

}
