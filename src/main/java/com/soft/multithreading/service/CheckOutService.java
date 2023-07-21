package com.soft.multithreading.service;

import com.soft.multithreading.domain.checkout.Cart;
import com.soft.multithreading.domain.checkout.CartItem;
import com.soft.multithreading.domain.checkout.CheckoutResponse;
import com.soft.multithreading.domain.checkout.CheckoutStatus;

import java.util.List;
import java.util.stream.Collectors;

import static com.soft.multithreading.util.CommonUtil.startTimer;
import static com.soft.multithreading.util.CommonUtil.timeTaken;

public class CheckOutService {

    public CheckOutService(PriceValidatorService priceValidatorService) {
        this.priceValidatorService = priceValidatorService;
    }

    private final PriceValidatorService priceValidatorService;
    public CheckoutResponse checkout(Cart cart){
        startTimer();
        List<CartItem> cartItemList= cart.getCartItemList()
                //.stream()
                .parallelStream()
                .map(cartItem -> {
                    boolean isValidPrice=priceValidatorService.isCartItemInvalid(cartItem);
                    cartItem.setExpired(isValidPrice);
                    return cartItem;
                })
                .filter(CartItem::isExpired)
                .collect(Collectors.toList());
        timeTaken();
        if(cartItemList.size()>0){
            return new CheckoutResponse(CheckoutStatus.FAILURE,cartItemList);
        }
        return new CheckoutResponse(CheckoutStatus.SUCCESS);
    }

}
