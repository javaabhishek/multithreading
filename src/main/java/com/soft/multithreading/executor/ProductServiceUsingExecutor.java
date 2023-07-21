package com.soft.multithreading.executor;


import com.soft.multithreading.domain.Product;
import com.soft.multithreading.domain.ProductInfo;
import com.soft.multithreading.domain.Review;
import com.soft.multithreading.service.ProductInfoService;
import com.soft.multithreading.service.ReviewService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.soft.multithreading.util.CommonUtil.stopWatch;
import static com.soft.multithreading.util.LoggerUtil.log;

public class ProductServiceUsingExecutor {

    static ExecutorService executorService= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingExecutor(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws ExecutionException, InterruptedException {
        stopWatch.start();

        Future<ProductInfo> productInfoFuture=  executorService.submit(()->
            productInfoService.retrieveProductInfo(productId)//submtting task into workqueue
        );
        Future<Review> reviewFuture=  executorService.submit(()->
            reviewService.retrieveReviews(productId)//submtting task into workqueue
        );

        ProductInfo productInfo = productInfoFuture.get(); // get the result from queue, .get() will block the main thread
        Review review = reviewFuture.get(); // get the result from queue

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingExecutor productService = new ProductServiceUsingExecutor(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);
        executorService.shutdown();
    }
}
