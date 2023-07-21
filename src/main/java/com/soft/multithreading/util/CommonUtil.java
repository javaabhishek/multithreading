package com.soft.multithreading.util;

import org.apache.commons.lang3.time.StopWatch;

import static com.soft.multithreading.util.LoggerUtil.log;
import static java.lang.Thread.sleep;

public class CommonUtil {

    public static StopWatch stopWatch = new StopWatch();

    public static void startTimer(){
        stopWatch.start();
    }

    public static void timeTaken(){
        stopWatch.stop();
        log("Total Time Taken : " +stopWatch.getTime());
        stopWatch.reset();
    }

    public static void delay(long delayMilliSeconds)  {
        try{
            sleep(delayMilliSeconds);
        }catch (Exception e){
            log("Exception is :" + e.getMessage());
        }
    }

    public static void stopWatchReset(){
        stopWatch.reset();
    }

}