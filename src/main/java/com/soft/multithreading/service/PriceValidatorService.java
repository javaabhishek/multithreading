package com.soft.multithreading.service;


import com.soft.multithreading.domain.checkout.CartItem;

import static com.soft.multithreading.util.CommonUtil.delay;

public class PriceValidatorService {

    public boolean isCartItemInvalid(CartItem cartItem){
        int cartId = cartItem.getItemId();
        delay(500);
        if (cartId == 7 || cartId == 9 || cartId == 11) {
            return true;
        }
        return false;
    }
}
