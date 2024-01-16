package com.soft.multithreading.completablefuture;


import com.soft.multithreading.domain.*;
import com.soft.multithreading.service.InventoryService;
import com.soft.multithreading.service.ProductInfoService;
import com.soft.multithreading.service.ReviewService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.soft.multithreading.util.CommonUtil.stopWatch;
import static com.soft.multithreading.util.LoggerUtil.log;

public class ProductServiceUsingCompletableFuture {
    private ProductInfoService productInfoService;
    private ReviewService reviewService;

    private InventoryService inventoryService;

    public ProductServiceUsingCompletableFuture(ProductInfoService productInfoService,
                                                ReviewService reviewService,InventoryService inventoryService) {
        this.productInfoService = productInfoService;
        this.reviewService = reviewService;
        this.inventoryService=inventoryService;
    }

    public Product retrieveProductDetails(String productId) {
        stopWatch.start();
        CompletableFuture<ProductInfo> cfProductInfo= CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> cfReview= CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        Product product=cfProductInfo
                .thenCombine(cfReview,(productInfo,review)->new Product(productId, productInfo, review))
                        .join();//block the main thread to complete both reservice.

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    public CompletableFuture<Product> retrieveProductDetails_approach2(String productId) {

        CompletableFuture<ProductInfo> cfProductInfo= CompletableFuture.supplyAsync(()->productInfoService.retrieveProductInfo(productId));
        CompletableFuture<Review> cfReview= CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        return cfProductInfo
                .thenCombine(cfReview,(productInfo,review)->new Product(productId, productInfo, review));//NOT blocking the main thread to complete both service.
    }

    public Product retrieveProductDetailsWithInventory(String productId) {
        stopWatch.start();
        CompletableFuture<ProductInfo> cfProductInfo= CompletableFuture
                .supplyAsync(()->productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    List<ProductOption> productOptions=updateInventory(productInfo);
                    productInfo.setProductOptions(productOptions);
                    return productInfo;
                });

        CompletableFuture<Review> cfReview= CompletableFuture.supplyAsync(()->reviewService.retrieveReviews(productId));
        Product product=cfProductInfo
                .thenCombine(cfReview,(productInfo,review)->new Product(productId, productInfo, review))
                .join();//block the main thread to complete both reservice.

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    public Product retrieveProductDetailsWithInventory_approach2(String productId) {
        stopWatch.start();
        CompletableFuture<ProductInfo> cfProductInfo= CompletableFuture
                .supplyAsync(()->productInfoService.retrieveProductInfo(productId))
                .thenApply(productInfo -> {
                    List<ProductOption> productOptions=updateInventory_approach2(productInfo);
                    productInfo.setProductOptions(productOptions);
                    return productInfo;
                });

        CompletableFuture<Review> cfReview= CompletableFuture
                .supplyAsync(()->reviewService.retrieveReviews(productId))
                .exceptionally(e->{
                    log("Review service failure log:"+e.getMessage());
                    return Review.builder()
                            .noOfReviews(0)
                            .overallRating(0)
                            .build();//provide dummy recoverable value.
                });
        Product product=cfProductInfo
                .thenCombine(cfReview,(productInfo,review)->new Product(productId, productInfo, review))
                .whenComplete((product1,ex)->{//It means the product service is failed so in this no need to
                    //execute other services (like inventoryService,ReviewService..) and there is no point
                    //recovery hence we are using whenComplete instead of handle(-,-) and exceptionally(e)
                    log("Inside WhenComplete "+product1 +" and exception is:"+ex);
                })
                .join();//block the main thread to complete both reservice.

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return product;
    }

    private List<ProductOption> updateInventory(ProductInfo productInfo) {
        return productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    Inventory inventory=inventoryService.addInventory(productOption);
                    productOption.setInventory(inventory);
                    return productOption;
                }).collect(Collectors.toList());
    }
    private List<ProductOption> updateInventory_approach2(ProductInfo productInfo) {

        List <CompletableFuture<ProductOption>> completableFutureList= productInfo.getProductOptions()
                .stream()
                .map(productOption -> {
                    return CompletableFuture.supplyAsync(()->inventoryService.addInventory(productOption))
                            .exceptionally(ex->{
                                log("Exception occured in inventory service:"+ ex.getMessage());
                                return Inventory.builder()
                                        .count(0).build();//provide recoverable value. as inventoryService
                                //is intermediate operation..
                            })
                                    .thenApply(inventory -> {
                                        productOption.setInventory(inventory);
                                        return productOption;
                                    });
                }).collect(Collectors.toList());

        return completableFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static void main(String[] args) {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        InventoryService inventoryService1=new InventoryService();
        ProductServiceUsingCompletableFuture productService = new ProductServiceUsingCompletableFuture(productInfoService, reviewService,inventoryService1);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

    }
}
