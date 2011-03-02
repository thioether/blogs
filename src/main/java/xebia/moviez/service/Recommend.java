package xebia.moviez.service;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import xebia.moviez.dao.UberDao;

@Component
public class Recommend {
	
	@Autowired
	private UberDao dao;
	
	// on server 
	public double similarity(String p1, String p2) {
		double similarity = 0;
		// get p1 scores 
		Map<String, Double> diffScore = dao.getScoreDiff(p1, p2);
		double sumOfSquares = 0;
		
		for (Map.Entry<String, Double> m : diffScore.entrySet()) {
			sumOfSquares += Math.pow(m.getValue(), 2);
		}
		
		similarity = 1/(1+Math.sqrt(sumOfSquares));
		return similarity;
	}
	
	// on client 
	public double similarity1(String p1, String p2) {
		double similarity = 0;
		// get p1 scores 
		Map<String, Double> p1Score = dao.getScores(p1);
		Map<String, Double> p2Score = dao.getScores(p2);
		double sumOfSquares = 0;
		
		for (Map.Entry<String, Double> m : p1Score.entrySet()) {
			if (p2Score.containsKey(m.getKey()) && p2Score.get(m.getKey())>0 ) {
				sumOfSquares += Math.pow((m.getValue() - p2Score.get(m.getKey())), 2);
			}
		}
		
		similarity = 1/(1+Math.sqrt(sumOfSquares));
		return similarity;
	}
	
	public double similarity_pearson(String p1, String p2) {
		// get p1 scores 
		Map<String, Double> p1Score = dao.getScores(p1);
		Map<String, Double> p2Score = dao.getScores(p2);
		double sum1 = 0;
		double sumsq1 = 0;
		double sum2 = 0;
		double sumsq2 = 0;
		double sump = 0;
		int len = 0;
		
		for (Map.Entry<String, Double> m : p1Score.entrySet()) {
			if (m.getValue()> 0 && p2Score.containsKey(m.getKey()) && p2Score.get(m.getKey())>0 ) {
				len++;
				sum1 += m.getValue();
				sumsq1 += Math.pow(m.getValue(), 2);
				sum2 += p2Score.get(m.getKey());
				sumsq2 += Math.pow(p2Score.get(m.getKey()), 2);
				
				sump += m.getValue() * p2Score.get(m.getKey());
			}
		}
		
		double num = sump - ((sum1*sum2)/len);
		double den = sqrt((sumsq1-pow(sum1,2)/len) *  (sumsq2-pow(sum2,2)/len)  );
		
		return den!=0?num/den:0;
	}
	
	public List<NameScoreTuple> topMatches(String p) {
		List<NameScoreTuple> topMatches = new ArrayList<NameScoreTuple>();
		Set<String> allUsers = dao.getAllUsers();
		for (String user : allUsers) {
			if (!user.equals(p)) {
				topMatches.add(new NameScoreTuple(user, similarity(p, user)));
			}
		}
		Collections.sort(topMatches);
		return topMatches;
	}
	
	public List<NameScoreTuple> getRecommendations(String p) {
		// find similarity score for movies that you have not watched.
		List<NameScoreTuple> topMatches = new ArrayList<NameScoreTuple>();
		Map<String, Double> p1Scores = dao.getScores(p);
		
		Set<String> allMovies = dao.getAllMovies();
		for (String movie : allMovies) {
			if (!p1Scores.containsKey(movie)) {
				int num = 0;
				double sum = 0;				
				
				for (Map.Entry<String, Double> m : dao.getMovieScores(movie).entrySet()) {
					if (m.getValue() > 0  ) {
						String user = m.getKey();
						double simWithUser = similarity(p, user);
						sum += simWithUser * m.getValue();
						num++;
					}
				}
				topMatches.add(new NameScoreTuple(movie, num!=0?(sum/num):0));
			}
		}
		Collections.sort(topMatches);
		return topMatches;
	}
	
	
	
	
	public static class NameScoreTuple implements Comparable<NameScoreTuple>{
		private String name;
		private Double score;
		public NameScoreTuple(String person, Double match) {
			super();
			this.name = person;
			this.score = match;
		}
		public String getName() {
			return name;
		}
		public Double getScore() {
			return score;
		}
		@Override
		public int compareTo(NameScoreTuple o) {
			return (this.score - o.score)>0?-1:1;
		}
	}
	
}
