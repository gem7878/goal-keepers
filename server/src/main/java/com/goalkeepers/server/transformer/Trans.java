package com.goalkeepers.server.transformer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Trans {
	
	public static String token(String rtn) {
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(rtn, JsonObject.class);
		return jsonObject.get("access_token").getAsString();
	}

}
