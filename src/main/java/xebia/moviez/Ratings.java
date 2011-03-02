package xebia.moviez;

import java.util.HashMap;
import java.util.Map;

public class Ratings {
	
	public static Map<String, Double> getRatings1() {
		Map<String, Double> ratings = new HashMap<String, Double>();
		ratings.put("Finding Nemo", 8d);
		ratings.put("Ice Age", 6d);
		ratings.put("MegaMind", 8d);
		ratings.put("Matrix", 8d);
		ratings.put("Shutter Island", 6d);
		return ratings;
	}
	
	
	public static Map<String, Double> getRatings2() {
		Map<String, Double> ratings = new HashMap<String, Double>();
		ratings.put("Despicable Me", 8d);
		ratings.put("Ice Age", 6d);
		ratings.put("Inception", 6d);
		ratings.put("Matrix", 8d);
		return ratings;
	}
	
	
	public static Map<String, Double> getRatings3() {
		Map<String, Double> ratings = new HashMap<String, Double>();
		ratings.put("Finding Nemo", 10d);
		ratings.put("Ice Age", 6d);
		ratings.put("MegaMind", 10d);
		ratings.put("Inception", 8d);
		return ratings;
	}
	
	public static Map<String, Double> getRatings4() {
		Map<String, Double> ratings = new HashMap<String, Double>();
		ratings.put("MegaMind", 8d);
		ratings.put("Inception", 6d);
		ratings.put("Shutter Island", 8d);
		return ratings;
	}
}
