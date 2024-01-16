package com.soft.multithreading.completablefuture;

import com.soft.multithreading.service.HelloWorldService;

import java.util.concurrent.*;

import static com.soft.multithreading.util.CommonUtil.delay;
import static com.soft.multithreading.util.LoggerUtil.log;

public class CompletableFutureHelloWorld {

    private final HelloWorldService hws;

    public CompletableFutureHelloWorld(HelloWorldService helloWorldService) {
        this.hws = helloWorldService;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        HelloWorldService hws=new HelloWorldService();
        /*CompletableFuture<String> helloWorldFuture= CompletableFuture.supplyAsync(()->{
            return hws.helloWorld();
        });
        CompletableFuture<Void> endOfFuture=helloWorldFuture.thenAccept(result->{
            System.out.println("Result is:"+result);
        });
        endOfFuture.get(1050, TimeUnit.MILLISECONDS);*/
        //Or
        /*CompletableFuture
                .supplyAsync(hws::helloWorld)
                        .thenAccept(result->log("Result:"+result))
                                .join();
        */
        //thenApply
        CompletableFuture.supplyAsync(hws::helloWorld)
                        .thenApply(String::toUpperCase)
                                .thenAccept(result->log("Result:"+result))
                                        .join();

        log("done");
    }

    public CompletableFuture<String> helloWorld(){
        return CompletableFuture.supplyAsync(hws::helloWorld)
                .thenApply(String::toUpperCase);
    }

    public String helloWorld_thenCombine(){
        CompletableFuture<String> hello=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world=CompletableFuture.supplyAsync(hws::world);

        return hello.thenCombine(world,
                        (previousFutureResult,currentFutureResult)->previousFutureResult+currentFutureResult)
                .thenApply(String::toUpperCase)
                .join();
    }

    public String helloWorld_3_async_call(){
        CompletableFuture<String> hello=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            log("inside hi");
            return "Hi CompletableFuture!";
        });
        return hello
                .thenCombine(world,(previousFutureResult,currentFutureResult)->{
                    log("thenCombine: Hello");
                    return previousFutureResult+currentFutureResult;
                })
                .thenCombine(hiFuture,(previous,current)->{
                    log("thenCombine: hi");
                    return previous+current;
                })
                .thenApply(s -> {
                    log("thenApply: make upper case");
                    return s.toUpperCase();
                })
                .join();
    }

    public String helloWorld_3_async_call_async(){
        CompletableFuture<String> hello=CompletableFuture.supplyAsync(hws::hello);
        CompletableFuture<String> world=CompletableFuture.supplyAsync(hws::world);
        CompletableFuture<String> hiFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            log("inside hi");
            return "Hi CompletableFuture!";
        });
        //Notice that, even though we use thenCombineAsync,thenApplyAsync the only single theread will execute this pipe line
        //the reason is to void context switching.
        //And this is default behaviour in Common pool.
        return hello
                .thenCombineAsync(world,(previousFutureResult,currentFutureResult)->{
                    log("thenCombine: Hello");
                    return previousFutureResult+currentFutureResult;
                })
                .thenCombineAsync(hiFuture,(previous,current)->{
                    log("thenCombine: hi");
                    return previous+current;
                })
                .thenApplyAsync(s -> {
                    log("thenApply: make upper case");
                    return s.toUpperCase();
                })
                .join();
    }

    public String helloWorld_3_async_call_custom_threadPool(){
        ExecutorService executorService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletableFuture<String> hello=CompletableFuture.supplyAsync(hws::hello,executorService);
        CompletableFuture<String> world=CompletableFuture.supplyAsync(hws::world,executorService);
        CompletableFuture<String> hiFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return "Hi CompletableFuture!";
        },executorService);

        return hello
                .thenCombine(world,(previousFutureResult,currentFutureResult)->{
                    log("thenCombine: Hello");
                    return previousFutureResult+currentFutureResult;
                })
                .thenCombine(hiFuture,(previous,current)->{
                    log("thenCombine: hi");
                    return previous+current;
                })
                .thenApply(s -> {
                    log("thenApply: make upper case");
                    return s.toUpperCase();
                })
                .join();
    }

    public String helloWorld_3_async_call_custom_threadPool_async(){
        ExecutorService executorService=Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        CompletableFuture<String> hello=CompletableFuture.supplyAsync(hws::hello,executorService);
        CompletableFuture<String> world=CompletableFuture.supplyAsync(hws::world,executorService);
        CompletableFuture<String> hiFuture=CompletableFuture.supplyAsync(()->{
            delay(1000);
            return "Hi CompletableFuture!";
        },executorService);

        //passing executorService to every pipeline operation
        //allow us to execute every pipeline operation
        //in different threads.
        return hello
                .thenCombineAsync(world,(previousFutureResult,currentFutureResult)->{
                    log("thenCombine: Hello");
                    return previousFutureResult+currentFutureResult;
                },executorService)
                .thenCombineAsync(hiFuture,(previous,current)->{
                    log("thenCombine: hi");
                    return previous+current;
                },executorService)
                .thenApplyAsync(s -> {
                    log("thenApply: make upper case");
                    return s.toUpperCase();
                },executorService)
                .join();
    }
}
