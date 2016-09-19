/**
 * DetailsActivity.java
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.inclass06;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends Activity {

	private Photo photo;
	private ProgressDialog progressDialogue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		if (getIntent().getExtras() != null) {
			photo = (Photo) getIntent().getExtras().get(MainActivity.PHOTO_KEY);
			((TextView) findViewById(R.id.textView1)).setText(photo.getName());
			((TextView) findViewById(R.id.textView2)).setText(photo.getOwner());
			if (photo.getUrl() == null || photo.getUrl().isEmpty()) {
				((ImageView) findViewById(R.id.imageView1)).setImageDrawable(getResources().getDrawable(
						R.drawable.photo_not_found));
			} else {
				new LoadPhotoTask().execute(photo);
			}
		} else {
			Log.d(MainActivity.LOGGING_KEY, "No data received");
		}

	}

	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	private class LoadPhotoTask extends AsyncTask<Photo, Void, Bitmap> {

		@Override
		protected void onPreExecute() {
			if (!isNetworkConnected()) {
				Toast.makeText(DetailsActivity.this, "No internet connectivity", Toast.LENGTH_LONG).show();
				this.cancel(true);
				return;
			}
			progressDialogue = new ProgressDialog(DetailsActivity.this);
			progressDialogue.setTitle("Loading Photo");
			progressDialogue.setCancelable(false);
			progressDialogue.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			progressDialogue.dismiss();
			ImageView imageView = (ImageView) findViewById(R.id.imageView1);
			imageView.setImageBitmap(result);
			super.onPostExecute(result);
		}

		@Override
		protected Bitmap doInBackground(Photo... params) {
			Photo photoUrl = params[0];
			Bitmap bmp = null;
			URL url;
			try {
				url = new URL(photoUrl.getUrl());
				bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return bmp;
		}

	}
}
