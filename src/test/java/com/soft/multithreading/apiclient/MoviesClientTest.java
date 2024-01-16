package com.soft.multithreading.apiclient;

import com.soft.multithreading.domain.movie.Movie;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.soft.multithreading.util.CommonUtil.startTimer;
import static com.soft.multithreading.util.CommonUtil.timeTaken;
import static com.soft.multithreading.util.LoggerUtil.log;
import static org.junit.jupiter.api.Assertions.*;

class MoviesClientTest {

    private WebClient webClient=WebClient
            .builder()
            .baseUrl("http://localhost:8080/movies")
            .build();

    private MoviesClient moviesClient=new MoviesClient(webClient);
    @RepeatedTest(5)//first call will take some time: the reason is, WebClient API will take a time to make connection
    void retrieveMovie() {
        long movieId=1;
        startTimer();
        Movie movie=moviesClient.retrieveMovie(movieId);
        timeTaken();
        log("Movie:"+movie);
        assertNotNull(movie);
        assertEquals("Batman Begins",movie.getMovieInfo().getName());
        assert movie.getReviewList().size()!=0;
    }

    @RepeatedTest(5)//first call will take some time: the reason is, WebClient API will take a time to make connection
    void retrieveMovies() {
        List<Long> movieIds=List.of(1l,2l,3l,4l,5l,6l,7l);
        startTimer();
        List<Movie> movies=moviesClient.retrieveMovies(movieIds);
        timeTaken();
        log("Movie:"+movies.size());
        assertNotNull(movies);
    }

    @RepeatedTest(5)//first call will take some time: the reason is, WebClient API will take a time to make connection
    void retrieveMovie_async() {
        long movieId=1;
        startTimer();
        CompletableFuture<Movie> movieCf=moviesClient.retrieveMovie_async(movieId);
        Movie movie=movieCf.join();
        timeTaken();
        log("Movie:"+movie);
        assertNotNull(movie);
        assertEquals("Batman Begins",movie.getMovieInfo().getName());
        assert movie.getReviewList().size()!=0;
    }

    @RepeatedTest(5)//first call will take some time: the reason is, WebClient API will take a time to make connection
    void retrieveMovies_async() {
        List<Long> movieIds=List.of(1l,2l,3l,4l,5l,6l,7l);
        startTimer();
        List<Movie> movies=moviesClient.retrieveMovies_async(movieIds);
        timeTaken();
        log("Movie:"+movies);
        assertNotNull(movies);
    }
}