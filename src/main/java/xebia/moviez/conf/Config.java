package xebia.moviez.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.keyvalue.redis.connection.RedisConnectionFactory;
import org.springframework.data.keyvalue.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.keyvalue.redis.core.StringRedisTemplate;

@Configuration
public class Config {
	
	@Bean
	public RedisConnectionFactory getConnectionFactory() {
		JedisConnectionFactory cf = new JedisConnectionFactory();
		return cf;
	}
	
	@Bean
	public StringRedisTemplate getRedisTemplate() {
		return new StringRedisTemplate(getConnectionFactory());
	}
}
