package com.michaelcgood.model;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;

public class RedisAtomicInteger {

    private final String key;
    private final ValueOperations<String, Integer> operations;
    private final RedisOperations<String, Integer> generalOps;

    public RedisAtomicInteger(String key, int initValue, RedisOperations<String, Integer> generalOps) {
        this.key = key;
        this.generalOps = generalOps;
        this.operations = generalOps.opsForValue();

        this.operations.set(key, initValue);
    }

    public int incrAndGet() {
        return this.operations.increment(key, 1).intValue();
    }

    public int get() {
        return this.operations.get(key).intValue();
    }
}
