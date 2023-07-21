package com.soft.multithreading.thread;


import com.soft.multithreading.domain.Product;
import com.soft.multithreading.domain.ProductInfo;
import com.soft.multithreading.domain.Review;
import com.soft.multithreading.service.ProductInfoService;
import com.soft.multithreading.service.ReviewService;

import static com.soft.multithreading.util.CommonUtil.stopWatch;
import static com.soft.multithreading.util.LoggerUtil.log;

public class ProductServiceUsingThread {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    public ProductServiceUsingThread(ProductInfoService productInfoService, ReviewService reviewService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
    }

    public Product retrieveProductDetails(String productId) throws InterruptedException {
        stopWatch.start();

        ProductInfoRunnable productInfoRunnable=new ProductInfoRunnable(productId);
        Thread productInfoThread=new Thread(productInfoRunnable);

        ReviewRunnable reviewRunnable=new ReviewRunnable(productId);
        Thread reivewThread=new Thread(reviewRunnable);

        productInfoThread.start();
        reivewThread.start();

        productInfoThread.join();
        reivewThread.join();

        ProductInfo productInfo=productInfoRunnable.getProductInfo();
        Review review=reviewRunnable.getReview();
        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws Exception {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingThread productService = new ProductServiceUsingThread(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }

    private class ProductInfoRunnable implements Runnable {
        private ProductInfo productInfo;
        private String productId;
        public ProductInfoRunnable(String productId) {
            this.productId=productId;
        }

        public ProductInfo getProductInfo(){
            return productInfo;
        }

        @Override
        public void run() {
            productInfo=productInfoService.retrieveProductInfo(productId);
        }
    }

    private class ReviewRunnable implements Runnable {

        private String productId;
        private Review review;
        public ReviewRunnable(String productId) {
            this.productId=productId;
        }

        public Review getReview() {
            return review;
        }

        @Override
        public void run() {
            review=reviewService.retrieveReviews(productId);
        }
    }
}
