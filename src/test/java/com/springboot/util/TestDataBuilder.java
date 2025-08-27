package com.springboot.util;

import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.OfferRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for creating test data objects
 * Provides convenient methods to build request objects for testing
 */
public class TestDataBuilder {
    
    // ==================== OFFER REQUEST BUILDERS ====================
    
    /**
     * Create a FLATX offer request
     * @param restaurantId The restaurant ID
     * @param value The flat discount amount
     * @param segments The target customer segments
     * @return OfferRequest object
     */
    public static OfferRequest createFlatXOffer(int restaurantId, int value, String... segments) {
        List<String> segmentList = Arrays.asList(segments);
        return new OfferRequest(restaurantId, ApiConstants.OFFER_TYPE_FLATX, value, segmentList);
    }
    
    /**
     * Create a percentage offer request
     * @param restaurantId The restaurant ID
     * @param value The percentage discount (e.g., 20 for 20%)
     * @param segments The target customer segments
     * @return OfferRequest object
     */
    public static OfferRequest createPercentOffer(int restaurantId, int value, String... segments) {
        List<String> segmentList = Arrays.asList(segments);
        return new OfferRequest(restaurantId, ApiConstants.OFFER_TYPE_PERCENT, value, segmentList);
    }
    
    // ==================== APPLY OFFER REQUEST BUILDERS ====================
    
    /**
     * Create an apply offer request
     * @param cartValue The cart value before discount
     * @param userId The user ID
     * @param restaurantId The restaurant ID
     * @return ApplyOfferRequest object
     */
    public static ApplyOfferRequest createApplyRequest(int cartValue, int userId, int restaurantId) {
        ApplyOfferRequest request = new ApplyOfferRequest();
        request.setCart_value(cartValue);
        request.setUser_id(userId);
        request.setRestaurant_id(restaurantId);
        return request;
    }

}
