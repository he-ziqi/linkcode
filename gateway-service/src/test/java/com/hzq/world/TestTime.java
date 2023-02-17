package com.hzq.world;

import org.junit.Test;

import java.time.ZonedDateTime;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/1/22 23:46
 */
public class TestTime {

    @Test
    public void testTime(){
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println(now);
    }
}
