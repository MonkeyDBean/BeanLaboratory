package com.monkeybean.rl.component.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 当前仅测试Redis, 测试时, 将注释解开
 * EnableRedisHttpSession: 将session放置Redis存储
 * windows下Redis可视化工具：RedisDesktopManager
 * <p>
 * Created by MonkeyBean on 2019/7/28.
 */
@Component
@EnableRedisHttpSession
public class RedisConfig {
    private final RedisCon redisCon;

    @Autowired
    public RedisConfig(RedisCon redisCon) {
        this.redisCon = redisCon;
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisCon.getAddress());
        redisStandaloneConfiguration.setPort(redisCon.getPort());
        redisStandaloneConfiguration.setDatabase(redisCon.getDbIndex());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisCon.getPassword()));
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(redisCon.getConnectTimeout()));
        jedisClientConfiguration.readTimeout(Duration.ofMillis(redisCon.getReadTimeout()));
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        RedisSerializer stringRedisSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        //key的序列化采用StringRedisSerializer
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        //value的序列化采用GenericJackson2JsonRedisSerializer
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        template.setConnectionFactory(jedisConnectionFactory());
        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
        cookieSerializer.setUseBase64Encoding(false);
        cookieSerializer.setUseHttpOnlyCookie(true);

        //取消跨域限制
        cookieSerializer.setSameSite(null);

        //设值cookie作用范围
        cookieSerializer.setCookiePath("/");
        return cookieSerializer;
    }

}
