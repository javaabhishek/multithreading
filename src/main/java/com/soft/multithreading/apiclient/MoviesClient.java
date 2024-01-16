package com.soft.multithreading.apiclient;

import com.soft.multithreading.domain.movie.Movie;
import com.soft.multithreading.domain.movie.MovieInfo;
import com.soft.multithreading.domain.movie.Review;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.soft.multithreading.util.CommonUtil.startTimer;
import static com.soft.multithreading.util.CommonUtil.timeTaken;

public class MoviesClient {
    private final WebClient webClient;

    public MoviesClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Movie retrieveMovie(Long movieInfoId){
       MovieInfo movieInfo= invokeMoveInfoService(movieInfoId);
       List<Review> reviews=invokeReviewService(movieInfoId);
       return new Movie(movieInfo,reviews);
    }

    public List<Movie> retrieveMovies(List<Long> movieInfoIds){

        return movieInfoIds.stream()
                .map(this::retrieveMovie)
                .collect(Collectors.toList());

    }

    public List<Movie> retrieveMovies_async(List<Long> movieInfoIds){

        List<CompletableFuture<Movie>> completableFuture= movieInfoIds.stream()
                .map(this::retrieveMovie_async)
                .collect(Collectors.toList());

        return completableFuture
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    public CompletableFuture<Movie> retrieveMovie_async(Long movieInfoId){

        CompletableFuture<MovieInfo> movieInfoCf=CompletableFuture.supplyAsync(()->invokeMoveInfoService(movieInfoId));
        CompletableFuture<List<Review>> reviewsCf=CompletableFuture.supplyAsync(()->invokeReviewService(movieInfoId));
        //Movie movie=movieInfoCf.thenCombine(reviewsCf,(movieInfo,reviews)->new Movie(movieInfo,reviews)).join();

        return movieInfoCf
                .thenCombine(reviewsCf,Movie::new);
    }

    private MovieInfo invokeMoveInfoService(Long movieInfoId) {
        var serviceUrl="/v1/movie_infos/{movieInfoId}";

        return webClient
                .get()
                .uri(serviceUrl,movieInfoId)
                .retrieve()
                .bodyToMono(MovieInfo.class)//it means give me only one object
                .block();//must be join()

    }

    private List<Review> invokeReviewService(Long movieInfoId) {
        var serviceUrl="/v1/reviews?movieInfoId={movieInfoId}";

        String reviewServiceUri=UriComponentsBuilder.fromUriString("/v1/reviews")
                .queryParam("movieInfoId",movieInfoId)
                .buildAndExpand()
                .toUriString();

        return webClient
                .get()
                .uri(reviewServiceUri)
                .retrieve()
                .bodyToFlux(Review.class)//it means give me list of object
                .collectList()
                .block();//must be join()

    }
}
