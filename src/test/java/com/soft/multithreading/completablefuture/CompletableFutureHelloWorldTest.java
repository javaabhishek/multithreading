package com.soft.multithreading.completablefuture;

import com.soft.multithreading.service.HelloWorldService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class CompletableFutureHelloWorldTest {

    HelloWorldService hws=new HelloWorldService();
    CompletableFutureHelloWorld cfhw=new CompletableFutureHelloWorld(hws);
    @Test
    void helloWorld() {
        CompletableFuture<String> helloWorld=cfhw.helloWorld();
        helloWorld.thenAccept(result-> assertEquals("HELLO WORLD",result))
                .join();
    }

    @Test
    void helloWorld_thenCombine() {
        String helloWorld=cfhw.helloWorld_thenCombine();
        assertEquals("HELLO WORLD!",helloWorld);
    }
    @Test
    void helloWorld_3_async_call() {
        String helloWorldWithHi=cfhw.helloWorld_3_async_call();
        assertEquals("HELLO WORLD!HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }

    @Test
    void helloWorld_3_async_call_async() {
        String helloWorldWithHi=cfhw.helloWorld_3_async_call();
        assertEquals("HELLO WORLD!HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }

    @Test
    void helloWorld_3_async_call_custom_threadPool() {
        String helloWorldWithHi=cfhw.helloWorld_3_async_call_custom_threadPool();
        assertEquals("HELLO WORLD!HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }

    @Test
    void helloWorld_3_async_call_custom_threadPool_async() {
        String helloWorldWithHi=cfhw.helloWorld_3_async_call_custom_threadPool_async();
        assertEquals("HELLO WORLD!HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }
}