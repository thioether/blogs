package xebia.moviez.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.keyvalue.redis.connection.RedisConnection;
import org.springframework.data.keyvalue.redis.connection.RedisZSetCommands.Aggregate;
import org.springframework.data.keyvalue.redis.connection.RedisZSetCommands.Tuple;
import org.springframework.data.keyvalue.redis.core.BoundZSetOperations;
import org.springframework.data.keyvalue.redis.core.RedisCallback;
import org.springframework.data.keyvalue.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class UberDao {
	
	@Autowired
	private StringRedisTemplate srt;
	
	public void addRatings(String user, Map<String, Double> ratings) {
		srt.multi();
		
		srt.boundSetOps("Users").add(user);
		BoundZSetOperations<String, String> boundZSetOps = srt.boundZSetOps(user);
		
		for (Map.Entry<String, Double> mr : ratings.entrySet()) {
			srt.boundSetOps("Movies").add(mr.getKey());
			srt.boundZSetOps(mr.getKey()).add(user, mr.getValue());
			boundZSetOps.add(mr.getKey(), mr.getValue());
		}
		
		srt.exec();
	}
	
	
	
	public Map<String, Double> getScoreDiff(final String p1, final String p2) {
		Map<String, Double> mScoreMap = new HashMap<String, Double>();
		final String combinedKey = p1 + ":" + p2;

		Set<Tuple> movieAndScores = srt.execute(new RedisCallback<Set<Tuple>>() {
			@Override
			public Set<Tuple> doInRedis(RedisConnection con)
					throws DataAccessException {
				// emits a new zset ...
				con.zInterStore(combinedKey.getBytes(), Aggregate.SUM, new int[] {1,-1}, p1.getBytes(), p2.getBytes());
				con.expire(combinedKey.getBytes(), 120);
				return con.zRangeByScoreWithScore(combinedKey.getBytes(), 1, 20);
			}
			
		});
		
		for (Tuple t : movieAndScores) {
			mScoreMap.put(new String(t.getValue()), t.getScore());
		}
		
		return mScoreMap;
	}
	public Map<String, Double> getMovieScores(final String p) {
		return getScores(p);
	}
	
	public Map<String, Double> getScores(final String p) {
		Map<String, Double> mScoreMap = new HashMap<String, Double>();

		Set<Tuple> movieAndScores = srt.execute(new RedisCallback<Set<Tuple>>() {
			@Override
			public Set<Tuple> doInRedis(RedisConnection con)
					throws DataAccessException {
				Set<Tuple> zRangeByScoreWithScore = con.zRangeByScoreWithScore(p.getBytes(), 1, 10);
				return zRangeByScoreWithScore;
			}
			
		});
		
		for (Tuple t : movieAndScores) {
			mScoreMap.put(new String(t.getValue()), t.getScore());
		}
		
		return mScoreMap;
	}
	
	public Set<String> getAllUsers() {
		return srt.boundSetOps("Users").members();
	}
	
	public Set<String> getAllMovies() {
		return srt.boundSetOps("Movies").members();
	}
}
