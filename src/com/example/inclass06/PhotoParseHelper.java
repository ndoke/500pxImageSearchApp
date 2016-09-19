/**
 * PhotoParseHelper.java
 * A Yang
 * Ajay Vijayakumaran Nair
 * Nachiket Doke
 * 
 */
package com.example.inclass06;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PhotoParseHelper {

	public static List<Photo> parse(String photosJson) throws JSONException{
		 List<Photo> photos = new ArrayList<Photo>();
		 JSONObject root = new JSONObject(photosJson);
		 JSONArray photoArrayJson = root.getJSONArray("photos");
		 for(int i=0; i<photoArrayJson.length(); i++){
			 JSONObject photoJson = photoArrayJson.getJSONObject(i);
			 Photo photo = new Photo();
			 photo.setName(photoJson.getString("name"));
			 JSONObject userJson = photoJson.getJSONObject("user");
			 photo.setOwner(userJson.getString("fullname"));
			 photo.setUrl(photoJson.getString("image_url"));
			 photos.add(photo);
		 }
		return photos;
		
	}
}
