/**
 * MainActivity.java
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.inclass06;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String LOGGING_KEY = "inc6";
	public static final String PHOTO_KEY = "photo";
	public static final String SEARCH_KEY = "searchTerm";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//ldyg2oFiNqDiDDcfjhU4aT2fPeBkwhMnRjgHfdtq
				
				EditText editText = (EditText) findViewById(R.id.editText1);
				String value = editText.getText().toString();
				
				//If the user enters no value
				if(value.isEmpty()){
					Context context = getApplicationContext();
					CharSequence text = "Please enter a valid search Term!";
					int duration = Toast.LENGTH_SHORT;

					Toast toast = Toast.makeText(context, text, duration);
					toast.show(); 
				}
				else{
					Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
					intent.putExtra(SEARCH_KEY, value);
					startActivity(intent);
				}
				
			}
		});

	}
}
