package com.michaelcgood.model;

import com.google.common.base.Stopwatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the different consuming-time between RedisAtomicInteger and AtomicInteger
 * On my computer, AtomicInteger is approximiately 500 times faster than RedisAtomicInteger
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisAtomicIntegerTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void incrAndGet() {
        assertNotNull(redisTemplate);
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();

        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Integer.class));
        redisTemplate.setExposeConnection(true);
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();
        RedisAtomicInteger test = new RedisAtomicInteger("test", 0, redisTemplate);

        Function func = i -> test.incrAndGet();
        Function<Void, Integer> result = Void -> test.get();
        run(10_000, func, result);
    }

    @Test
    public void incrAndGet2() {
        AtomicInteger atomicInteger = new AtomicInteger();
        Function func = i -> atomicInteger.incrementAndGet();
        Function<Void, Integer> result = Void -> atomicInteger.get();
        run(10_000, func, result);
    }

    private void run(int times, Function func, Function<Void, Integer> result) {

        Stopwatch stopwatch = Stopwatch.createStarted();

        IntStream.range(0, times).parallel().forEach(func::apply);

        long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        Integer r = result.apply(null);
        System.out.printf("result: %s  time: %s \n", result, elapsed);
        assertEquals(times, r.intValue());
    }
}