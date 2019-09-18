package http;

import java.util.Map;

import org.javalite.http.Get;
import org.javalite.http.Http;
import org.javalite.http.Post;

public class HttpUtils {
	private static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0";
	public static String post(String url, String content) {
		Post post = Http.post(url, content)
				.header("User-Agent", USER_AGENT)
				.header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Aceept-Language", "en-US,en;q=0.5");
		return post.text();

	}
	public static String get(String url, Map<String,String> values) {
		Get get = Http.get(url);
		return get.text();
	}
}
