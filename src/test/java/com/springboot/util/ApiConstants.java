package com.springboot.util;

/**
 * Constants for API responses used across test automation.
 * 
 * This class centralizes commonly used API response messages, error codes,
 * and other constants to improve maintainability and avoid hardcoded strings
 * scattered throughout test files.
 */
public final class ApiConstants {
    
    // Prevent instantiation
    private ApiConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    // ================================
    // API RESPONSE MESSAGES
    // ================================
    
    /**
     * Standard success response message from offer creation API
     */
    public static final String SUCCESS_RESPONSE = "success";
    
    /**
     * Standard error response message for validation failures
     */
    public static final String ERROR_RESPONSE = "error";
    
    // ================================
    // HTTP STATUS CODES
    // ================================
    
    /**
     * HTTP 200 OK - Successful request
     */
    public static final int HTTP_OK = 200;
    
    /**
     * HTTP 400 Bad Request - Client error
     */
    public static final int HTTP_BAD_REQUEST = 400;
    
    /**
     * HTTP 415 Unsupported Media Type - Missing or invalid Content-Type
     */
    public static final int HTTP_UNSUPPORTED_MEDIA_TYPE = 415;
    
    /**
     * HTTP 500 Internal Server Error - Server error
     */
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;
    
    /**
     * HTTP 404 Not Found - Resource not found
     */
    public static final int HTTP_NOT_FOUND = 404;
    
    // ================================
    // USER SEGMENT CONSTANTS
    // ================================
    
    /**
     * P1 customer segment (premium customers)
     */
    public static final String SEGMENT_P1 = "p1";
    
    /**
     * P2 customer segment (regular customers)
     */
    public static final String SEGMENT_P2 = "p2";
    
    /**
     * P3 customer segment (basic customers)
     */
    public static final String SEGMENT_P3 = "p3";
    
    // ================================
    // OFFER TYPE CONSTANTS
    // ================================
    
    /**
     * FLATX offer type - flat discount amount
     */
    public static final String OFFER_TYPE_FLATX = "FLATX";
    
    /**
     * PERCENT offer type - percentage discount
     */
    public static final String OFFER_TYPE_PERCENT = "PERCENT";
}
