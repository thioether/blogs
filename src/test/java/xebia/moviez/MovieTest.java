package xebia.moviez;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import xebia.moviez.conf.Config;
import xebia.moviez.dao.UberDao;
import xebia.moviez.service.Recommend;
import xebia.moviez.service.Recommend.NameScoreTuple;

public class MovieTest {

	private AnnotationConfigApplicationContext ctx;
	private UberDao dao;
	private Recommend recomender;
	
	@Before
	public void init() {
		ctx = new AnnotationConfigApplicationContext(Config.class);
		ctx.scan("xebia.moviez.dao");
		ctx.scan("xebia.moviez.service");
		dao = ctx.getBean(UberDao.class);
		recomender = ctx.getBean(Recommend.class);
	}
	
	@Test
	public void populateData() {
		dao.addRatings("poo", Ratings.getRatings1());
		dao.addRatings("shifu", Ratings.getRatings2());
		dao.addRatings("tailong", Ratings.getRatings3());
		dao.addRatings("guru", Ratings.getRatings4());
	}
	
	@Test
	public void similarPeopleUsingDistance() {
		System.out.println(recomender.similarity1("poo", "shifu"));
		System.out.println(recomender.similarity1("tailong", "guru"));
		System.out.println(recomender.similarity1("shifu", "guru"));
		
		System.out.println(recomender.similarity("poo", "shifu"));
		System.out.println(recomender.similarity("tailong", "guru"));
		System.out.println(recomender.similarity("shifu", "guru"));
	}

	
	@Test
	public void bestMatches() {
		String p1 = "tailong";
		List<NameScoreTuple> topMatches = recomender.topMatches(p1);
		for (NameScoreTuple match : topMatches) {
			System.out.println(match.getName() + match.getScore());
		}
	}
	
	@Test
	public void getRecommendations() {
		String p1 = "tailong";
		List<NameScoreTuple> topMatches = recomender.getRecommendations(p1);
		for (NameScoreTuple match : topMatches) {
			System.out.println(match.getName() + match.getScore());
		}
	}
	
	@Test
	public void getRecommendations2() {
		String p1 = "shifu";
		List<NameScoreTuple> topMatches = recomender.getRecommendations(p1);
		for (NameScoreTuple match : topMatches) {
			System.out.println(match.getName() + match.getScore());
		}
	}
		
}
