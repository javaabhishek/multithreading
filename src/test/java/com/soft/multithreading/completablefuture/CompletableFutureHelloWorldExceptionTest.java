package com.soft.multithreading.completablefuture;

import com.soft.multithreading.service.HelloWorldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletableFutureHelloWorldExceptionTest {

    @Mock
    private HelloWorldService hws;

    @InjectMocks
    private CompletableFutureHelloWorldException cfhw;
    @Test
    public void helloWorld_3_async_call_handle() {
        when(hws.hello()).thenThrow(new RuntimeException("Test exception"));
        when(hws.world()).thenCallRealMethod();
        String helloWorldWithHi=cfhw.helloWorld_3_async_call_handle();
        assertEquals(" WORLD!HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }

    @Test
    public void helloWorld_3_async_call_exceptionally() {
        when(hws.hello()).thenThrow(new RuntimeException("Test hello exception"));
        when(hws.world()).thenThrow(new RuntimeException("Test world exception"));
        String helloWorldWithHi=cfhw.helloWorld_3_async_call_exceptionally();
        assertEquals("HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }
    @Test
    public void helloWorld_3_async_call_exceptionally_sucess() {
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();
        String helloWorldWithHi=cfhw.helloWorld_3_async_call_exceptionally();
        assertEquals("HELLO WORLD!HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }

    @Test
    public void helloWorld_3_async_call_whenComplete_success() {
        when(hws.hello()).thenCallRealMethod();
        when(hws.world()).thenCallRealMethod();
        String helloWorldWithHi=cfhw.helloWorld_3_async_call_whenComplete();
        assertEquals("HELLO WORLD!HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }
    @Test
    public void helloWorld_3_async_call_whenComplete_failure() {
        when(hws.hello()).thenThrow(new RuntimeException("Test hello exception"));
        when(hws.world()).thenCallRealMethod();
        String helloWorldWithHi=cfhw.helloWorld_3_async_call_whenComplete();
        assertEquals("HI COMPLETABLEFUTURE!",helloWorldWithHi);
    }
}