package com.soft.multithreading.completablefuture;

import com.soft.multithreading.domain.Product;
import com.soft.multithreading.service.InventoryService;
import com.soft.multithreading.service.ProductInfoService;
import com.soft.multithreading.service.ReviewService;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceUsingCompletableFutureTest {

    private ProductInfoService productInfoService=new ProductInfoService();
    private ReviewService reviewService=new ReviewService();
    private InventoryService inventoryService=new InventoryService();
    private ProductServiceUsingCompletableFuture psucf=new ProductServiceUsingCompletableFuture(productInfoService,reviewService,inventoryService);
    @Test
    void retrieveProductDetails() {
        Product product=psucf.retrieveProductDetails("1");
        assertEquals("1",product.getProductId());
    }

    @Test
    void retrieveProductDetails_approach2() {
       CompletableFuture<Product> cfProduct= psucf.retrieveProductDetails_approach2("1");
       Product product= cfProduct.join();
       assertEquals("1",product.getProductId());
    }

    @Test
    void retrieveProductDetailsWithInventory() {
        Product product=psucf.retrieveProductDetailsWithInventory("1");
        assertEquals("1",product.getProductId());
        product.getProductInfo().getProductOptions()
                .stream()
                .forEach(productOption -> assertEquals(2,productOption.getInventory().getCount()));
    }

    @Test
    void retrieveProductDetailsWithInventory_approach2() {//inventoryService asych
        Product product=psucf.retrieveProductDetailsWithInventory_approach2("1");
        assertEquals("1",product.getProductId());
        product.getProductInfo().getProductOptions()
                .stream()
                .forEach(productOption -> assertEquals(2,productOption.getInventory().getCount()));
    }
}