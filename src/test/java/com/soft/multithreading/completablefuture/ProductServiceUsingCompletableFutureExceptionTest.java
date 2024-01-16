package com.soft.multithreading.completablefuture;

import com.soft.multithreading.domain.Product;
import com.soft.multithreading.service.InventoryService;
import com.soft.multithreading.service.ProductInfoService;
import com.soft.multithreading.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceUsingCompletableFutureExceptionTest {

    @Mock
    private ProductInfoService productInfoService;
    @Mock
    private ReviewService reviewService;
    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private ProductServiceUsingCompletableFuture psucf;

    @Test
    void retrieveProductDetailsWithInventory_approach2() {
        String productId="ABC123";
        //given
        when(productInfoService.retrieveProductInfo(any()))
                .thenCallRealMethod();
        when(inventoryService.addInventory(any()))
                .thenCallRealMethod();
        when(reviewService.retrieveReviews(any()))
                .thenThrow(new RuntimeException("Review service Exception"));
        //when
        Product product= psucf.retrieveProductDetailsWithInventory_approach2(productId);

        //then
        assertNotNull(product);
        assertNotNull(product.getReview());
        assertEquals(0,product.getReview().getNoOfReviews());
    }

    @Test
    void retrieveProductDetailsWithInventory_approach2_Fail_first_service_fail() {
        String productId="ABC123";
        //given
        when(productInfoService.retrieveProductInfo(any()))
                .thenThrow(new RuntimeException("Product Service Failed with dummy exception"));
        when(reviewService.retrieveReviews(any()))
                .thenCallRealMethod();
        //when
        //Product product= psucf.retrieveProductDetailsWithInventory_approach2(productId);

        //then
        assertThrows(RuntimeException.class,()->psucf.retrieveProductDetailsWithInventory_approach2(productId));
        //assertNotNull(product.getReview());
        //assertEquals(0,product.getReview().getNoOfReviews());
    }

    @Test
    void retrieveProductDetailsWithInventory_approach2_intermediate_service_fail() {
        String productId="ABC123";
        //given
        when(productInfoService.retrieveProductInfo(any()))
                .thenCallRealMethod();
        when(inventoryService.addInventory(any()))
                .thenThrow(new RuntimeException("Inventory service dummy exception"));
        when(reviewService.retrieveReviews(any()))
                .thenCallRealMethod();
        //when
        Product product= psucf.retrieveProductDetailsWithInventory_approach2(productId);

        //then
        assertNotNull(product);
        assertNotNull(product.getReview());
        product.getProductInfo().getProductOptions()
                        .stream()
                                .forEach(productOption -> {
                                    assertNotNull(productOption.getInventory());
                                    assertEquals(0,productOption.getInventory().getCount());
                                });
        assertEquals(200,product.getReview().getNoOfReviews());
    }
}