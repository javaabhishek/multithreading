package com.soft.multithreading.service;

import com.soft.multithreading.domain.checkout.Cart;
import com.soft.multithreading.domain.checkout.CartItem;
import com.soft.multithreading.domain.checkout.CheckoutResponse;
import com.soft.multithreading.domain.checkout.CheckoutStatus;
import com.soft.multithreading.util.DataSet;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CheckOutServiceTest {

    PriceValidatorService priceValidatorService=new PriceValidatorService();
    CheckOutService checkOutService=new CheckOutService(priceValidatorService);
    @Test
    void checkout() {
        Cart cart= DataSet.createCart(6);
        //CheckoutResponse checkoutResponse=checkOutService.checkout(null);
        CheckoutResponse checkoutResponse=checkOutService.checkout(cart);
        assertEquals(CheckoutStatus.SUCCESS,checkoutResponse.getCheckoutStatus());
    }

    @Test
    void noOfCoresOnCurrentMachine(){
        System.out.println("Total no of cores:"+Runtime.getRuntime().availableProcessors());
    }
}