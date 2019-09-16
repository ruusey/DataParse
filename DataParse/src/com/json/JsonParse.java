package json;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.owlike.genson.Genson;

public class JsonParse {
	static final Logger LOGGER = LogManager.getLogger(JsonParse.class);
	static Genson gen = new Genson();
	static List<String> log = new ArrayList<String>();

	public static void main(String[] args) {

	}

	public static List<String> mapJson(String path) {
		return log;
	}
	private static Object parseJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Object.class);
		
	}
	private static String parseJson(Object json) {
		Gson gson = new Gson();
		return gson.toJson(json);
		
	}
}
