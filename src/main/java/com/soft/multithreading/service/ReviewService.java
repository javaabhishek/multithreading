package com.soft.multithreading.service;


import com.soft.multithreading.domain.Review;

import static com.soft.multithreading.util.CommonUtil.delay;

public class ReviewService {

    public Review retrieveReviews(String productId) {
        delay(1000);
        return new Review(200, 4.5);
    }
}
