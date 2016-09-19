/**
 * GalleryActivity.java
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.inclass06;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GalleryActivity extends Activity {
	private String baseUrl = "https://api.500px.com/v1/photos/search?consumer_key=ldyg2oFiNqDiDDcfjhU4aT2fPeBkwhMnRjgHfdtq&image_size=4&rpp=50&term=";
	private ProgressDialog progressDialogue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);
		Intent intent = getIntent();
		if (intent.getExtras() != null) {
			String searchTerm = intent.getExtras().getString(MainActivity.SEARCH_KEY);
			new FetchImagesTask().execute(searchTerm);
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

	private class FetchImagesTask extends AsyncTask<String, Void, List<Photo>> {

		@Override
		protected void onPreExecute() {
			if (!isNetworkConnected()) {
				Toast.makeText(GalleryActivity.this, "No internet connectivity", Toast.LENGTH_LONG).show();
				this.cancel(true);
				return;
			}
			progressDialogue = new ProgressDialog(GalleryActivity.this);
			progressDialogue.setTitle("Loading Results");
			progressDialogue.setCancelable(false);
			progressDialogue.show();
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(final List<Photo> result) {
			progressDialogue.dismiss();
			super.onPostExecute(result);
			ListView listView = (ListView) findViewById(R.id.listView1);
			ArrayAdapter<Photo> adapter = new ArrayAdapter<Photo>(GalleryActivity.this,
					android.R.layout.simple_list_item_1, result);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Photo photo = result.get(position);
					Intent intent = new Intent(GalleryActivity.this, DetailsActivity.class);
					intent.putExtra(MainActivity.PHOTO_KEY, photo);
					startActivity(intent);
					Log.d(MainActivity.LOGGING_KEY, photo.toString());
				}
			});
		}

		@Override
		protected List<Photo> doInBackground(String... params) {
			BufferedReader breader = null;
			URL url;
			List<Photo> photos = null;
			try {
				// GalleryActivity.this.getResources().getString(R.string.baseUrl)
				url = new URL(baseUrl + URLEncoder.encode(params[0], "UTF-8"));
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.connect();
				int statusCode = connection.getResponseCode();
				if (statusCode == HttpURLConnection.HTTP_OK) {
					InputStream inStream = connection.getInputStream();
					breader = new BufferedReader(new InputStreamReader(inStream));
					StringBuilder sb = new StringBuilder();
					String line;
					while ((line = breader.readLine()) != null) {
						sb.append(line);
					}
					Log.d(MainActivity.LOGGING_KEY, sb.toString());
					photos = PhotoParseHelper.parse(sb.toString());
					Log.d(MainActivity.LOGGING_KEY, photos.size() + "");
				} else {
					Log.d(MainActivity.LOGGING_KEY, "Http status not OK");
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return photos;
		}
	}
}
