package com.soft.multithreading.completablefuture;

import com.soft.multithreading.service.HelloWorldService;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static com.soft.multithreading.util.CommonUtil.delay;
import static com.soft.multithreading.util.LoggerUtil.log;

public class CompletableFutureHelloWorldException {

    private final HelloWorldService hws;

    public CompletableFutureHelloWorldException(HelloWorldService helloWorldService) {
        this.hws = helloWorldService;
    }

    public String helloWorld_3_async_call_handle(){
        CompletableFuture<String> hello=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return "Hi CompletableFuture!";
        });
        return hello
                .handle((res,e)->{
                    if(e!=null){
                        log("Exception from hello service failure:"+e.getMessage());
                        return "";//return the recoverable value so that further operation won't impact
                    }
                    log("Result>>>>:"+res);
                    return res;
                })
                .thenCombine(world,
                        (previousFutureResult,currentFutureResult)->previousFutureResult+currentFutureResult)
                .thenCombine(hiFuture,(previous,current)->previous+current)
                .thenApply(String::toUpperCase)
                .join();
    }

    public String helloWorld_3_async_call_exceptionally(){
        CompletableFuture<String> hello=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return "Hi CompletableFuture!";
        });
        return hello
                .exceptionally((e)->{
                        log("Exception from hello service failure:"+e.getMessage());
                        return "";//return the recoverable value so that further operation won't impact
                })
                .thenCombine(world,
                        (previousFutureResult,currentFutureResult)->previousFutureResult+currentFutureResult)
                .exceptionally((e)->{
                    log("Exception from world service failure:"+e.getMessage());
                    return "";//return the recoverable value so that further operation won't impact
                })
                .thenCombine(hiFuture,(previous,current)->previous+current)
                .thenApply(String::toUpperCase)
                .join();
    }

    public String helloWorld_3_async_call_whenComplete(){
        CompletableFuture<String> hello=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return "Hi CompletableFuture!";
        });
        return hello
                .whenComplete((res,e)->{
                    if(e!=null){
                        log("Exception from hello service failure:"+e.getMessage());
                    }// whenComplete - does not support recoverable value, it just propogate the
                    //exception to next handler
                    log("Excecuted >>>>>>>>>>>..");
                })
                .thenCombine(world,
                        (previousFutureResult,currentFutureResult)->previousFutureResult+currentFutureResult)
                .whenComplete((res,e)->{
                    if(e!=null){
                        log("Exception from world service failure:"+e.getMessage());
                    }// whenComplete - does not support recoverable value, it just propogate the
                    //exception to next handler
                })
                .exceptionally(e->{
                    log("Exception: finally catching exception which is propogated from previous call");
                    return "";//provide a recoverable value
                })
                .thenCombine(hiFuture,(previous,current)->previous+current)
                .thenApply(String::toUpperCase)
                .join();
    }
}
