package com.springboot;

import com.springboot.client.CartOfferRestClient;
import com.springboot.client.CartOfferRestClient.HttpResponseWrapper;
import com.springboot.client.UserSegmentRestClient;
import com.springboot.controller.ApiResponse;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.ApplyOfferResponse;
import com.springboot.controller.OfferRequest;
import com.springboot.controller.SegmentResponse;
import com.springboot.util.ApiConstants;
import com.springboot.util.TestDataBuilder;
import com.springboot.util.TestValidators;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Complete Test Suite for Cart Offer Application API
 * 
 * This class contains all test scenarios for POST /api/v1/cart/apply_offer endpoint.
 * Includes both positive and negative test cases in a single consolidated file.
 * 
 * Total Test Cases: 26 (11 positive + 15 negative)
 * API Focus: POST /api/v1/cart/apply_offer
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartOfferApplicationTests {

    private static CartOfferRestClient cartOfferClient;
    private static UserSegmentRestClient userSegmentClient;
    private static final String TEST_SEPARATOR = "################################################################################";
    private static final String STEP_SEPARATOR = "----------------------------------------";
    
    @BeforeClass
    public static void setUpTestSuite() {
        System.out.println("\n" + TEST_SEPARATOR);
        System.out.println(" CART OFFER APPLICATION API - COMPREHENSIVE TEST SUITE");
        System.out.println(TEST_SEPARATOR);
        System.out.println(" API Endpoint: POST /api/v1/cart/apply_offer");
        System.out.println(" Total Test Cases: 25 (10 positive + 15 negative)");
        System.out.println(" Test Coverage: Complete positive/negative scenarios with E2E workflows");
        System.out.println(TEST_SEPARATOR);
        
        cartOfferClient = new CartOfferRestClient();
        userSegmentClient = new UserSegmentRestClient();
        System.out.println(" CartOfferRestClient initialized");
        System.out.println(" UserSegmentRestClient initialized");
        System.out.println(" Starting comprehensive cart application API test execution...");
        System.out.println(TEST_SEPARATOR);
    }

    private void logTestStart(String testCase, String scenario, String expected) {
        System.out.println("\n" + TEST_SEPARATOR);
        System.out.println(" TEST CASE: " + testCase);
        System.out.println(" SCENARIO: " + scenario);
        System.out.println(" EXPECTED: " + expected);
        System.out.println(TEST_SEPARATOR);
    }

    private void logTestStep(int step, String description, Object data) {
        System.out.println(STEP_SEPARATOR);
        System.out.println(" STEP " + step + ": " + description);
        if (data != null) {
            System.out.println(" DATA: " + data);
        }
        System.out.println(STEP_SEPARATOR);
    }

    private void logTestResult(boolean passed, String message, Object response) {
        System.out.println(STEP_SEPARATOR);
        System.out.println(passed ? " TEST RESULT: PASSED" : " TEST RESULT: FAILED");
        System.out.println(" MESSAGE: " + message);
        if (response != null) {
            System.out.println(" RESPONSE: " + response);
        }
        System.out.println(STEP_SEPARATOR);
        System.out.println(TEST_SEPARATOR + "\n");
    }

    // ================================================================================
    //                           POSITIVE TEST CASES (10)
    // ================================================================================

    @Test // Test Case: FLATX_Application_Success - E2E FLATX offer application with real API calls
    public void testFlatXOfferApplication() throws Exception {
        logTestStart(
            "FLATX_Application_E2E",
            "E2E test: Create FLATX offer → Validate user segment → Apply offer to cart",
            "Complete E2E workflow validation with real API calls"
        );
        
        try {
            // Step 1: Validate user segment first (E2E requirement)
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create FLATX offer (E2E requirement)
            logTestStep(2, "Creating FLATX offer via API", null);
            OfferRequest offerRequest = TestDataBuilder.createFlatXOffer(101, 50, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            System.out.println(" Offer Created Successfully: " + offerRequest);
            
            // Step 3: Apply offer to cart (Main API under test)
            logTestStep(3, "Applying FLATX offer to cart via API", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(200, 1, 101);
            System.out.println(" Apply Request: " + applyRequest);
            
            // Step 4: Submit application request and validate status
            logTestStep(4, "Submitting offer application request", null);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 5: Validate E2E discount application
            logTestStep(5, "Validating E2E discount calculation", responseWrapper.getResponse());
            TestValidators.validateFlatXDiscount(responseWrapper, 200, 50);
            
            logTestResult(true, "E2E FLATX offer flow completed successfully - User segment validated, offer created, discount applied", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: PERCENT_Application_Success - E2E PERCENT offer application with real API calls
    public void testPercentOfferApplication() throws Exception {
        logTestStart(
            "PERCENT_Application_E2E",
            "E2E test: Create PERCENT offer → Validate user segment → Apply offer to cart",
            "Complete E2E workflow validation with real API calls for PERCENT discount"
        );
        
        try {
            // Step 1: Validate user segment first (E2E requirement)
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create PERCENT offer (E2E requirement)
            logTestStep(2, "Creating PERCENT offer via API", null);
            OfferRequest offerRequest = TestDataBuilder.createPercentOffer(102, 20, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            System.out.println(" Offer Created Successfully: " + offerRequest);
            
            // Step 3: Apply offer to cart (Main API under test)
            logTestStep(3, "Applying PERCENT offer to cart via API", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(500, 1, 102);
            System.out.println(" Apply Request: " + applyRequest);
            
            // Step 4: Submit application request and validate status
            logTestStep(4, "Submitting offer application request", null);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 5: Validate E2E percentage discount application
            logTestStep(5, "Validating E2E percentage discount calculation", responseWrapper.getResponse());
            TestValidators.validatePercentageDiscount(responseWrapper, 500, 20);
            
            logTestResult(true, "E2E PERCENT offer flow completed successfully - User segment validated, offer created, 20% discount applied", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: No_Offers_Available - Apply offer when no offers exist for restaurant
    public void testNoOffersAvailableScenario() throws Exception {
        logTestStart(
            "No_Offers_Available",
            "Apply offer to restaurant that has no offers configured",
            "Original cart value should be returned unchanged"
        );
        
        try {
            // Step 1: Attempt to apply offer to restaurant with no offers
            logTestStep(1, "Applying offer to restaurant without any offers", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(500, 1, 999);
            System.out.println(" Apply Request: Cart Value=500, User ID=1 (P1 segment), Restaurant ID=999 (no offers)");
            
            // Step 2: Submit application request
            logTestStep(2, "Submitting offer application for restaurant without offers", applyRequest);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 3: Validate no discount applied
            logTestStep(3, "Validating no discount applied", responseWrapper.getResponse());
            TestValidators.validateNoDiscountApplied(responseWrapper, 500, "when no offers available");
            
            logTestResult(true, "No offers scenario handled correctly - Original cart value ₹500 preserved for restaurant 999", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: User_Not_In_Segment_Scenario - E2E test for user segment mismatch
    public void testUserNotInSegmentScenario() throws Exception {
        logTestStart(
            "User_Not_In_Segment_E2E",
            "E2E test: Create P3 offer → Apply with P1 user → Validate no discount",
            "User segment mismatch should result in no discount applied"
        );
        
        try {
            // Step 1: Validate user segment first (P1 user)
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create offer for P3 segment only (mismatch with P1 user)
            logTestStep(2, "Creating P3-only offer via API", null);
            OfferRequest offerRequest = TestDataBuilder.createPercentOffer(104, 25, "p3");
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            
            // Step 3: Apply offer to cart (should not apply due to segment mismatch)
            logTestStep(3, "Applying P3 offer to P1 user", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(400, 1, 104);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 4: Validate no discount applied due to segment mismatch
            logTestStep(4, "Validating no discount due to segment mismatch", responseWrapper.getResponse());
            TestValidators.validateNoDiscountApplied(responseWrapper, 400, "due to segment mismatch");
            
            logTestResult(true, "E2E segment mismatch handled correctly - No discount applied for P1 user with P3 offer", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Multi_Segment_Offer_Application - E2E test for multi-segment offers
    public void testMultiSegmentOfferApplication() throws Exception {
        logTestStart(
            "Multi_Segment_Offer_E2E",
            "E2E test: Create multi-segment offer → Apply with eligible user → Validate discount",
            "Multi-segment offer should apply to eligible user segments"
        );
        
        try {
            // Step 1: Validate user segment (P1 user)
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create offer for both P1 and P2 segments
            logTestStep(2, "Creating multi-segment offer (P1,P2) via API", null);
            OfferRequest offerRequest = TestDataBuilder.createPercentOffer(105, 10, ApiConstants.SEGMENT_P1, ApiConstants.SEGMENT_P2);
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            
            // Step 3: Apply multi-segment offer to P1 user
            logTestStep(3, "Applying multi-segment offer to P1 user", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(300, 1, 105);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 4: Validate discount applied (10% of 300 = 30)
            logTestStep(4, "Validating multi-segment discount application", responseWrapper.getResponse());
            TestValidators.validatePercentageDiscount(responseWrapper, 300, 10);
            
            logTestResult(true, "E2E multi-segment offer applied successfully - 10% discount applied to P1 user", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: FLATX_Discount_Greater_Than_Cart - E2E test for discount exceeding cart value
    public void testFlatXDiscountGreaterThanCart() throws Exception {
        logTestStart(
            "FLATX_Discount_Greater_Than_Cart_E2E",
            "E2E test: Create large FLATX offer → Apply to small cart → Validate negative value",
            "FLATX discount greater than cart value should result in negative cart value"
        );
        
        try {
            // Step 1: Validate user segment
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create large FLATX offer (₹500 off)
            logTestStep(2, "Creating large FLATX offer (₹500) via API", null);
            OfferRequest offerRequest = TestDataBuilder.createFlatXOffer(106, 500, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            
            // Step 3: Apply large discount to small cart (₹100)
            logTestStep(3, "Applying ₹500 discount to ₹100 cart", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(100, 1, 106);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 4: Validate negative cart value (business rule)
            logTestStep(4, "Validating negative cart value result", responseWrapper.getResponse());
            TestValidators.validateNegativeCartValue(responseWrapper, -400);
            
            logTestResult(true, "E2E large discount handled correctly - Negative cart value (-₹400) when discount exceeds cart", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: 100_Percent_Discount - E2E test for 100% percentage discount
    public void test100PercentDiscount() throws Exception {
        logTestStart(
            "100_Percent_Discount_E2E",
            "E2E test: Create 100% offer → Apply to cart → Validate zero cart value",
            "100% discount should result in zero cart value"
        );
        
        try {
            // Step 1: Validate user segment
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create 100% discount offer
            logTestStep(2, "Creating 100% discount offer via API", null);
            OfferRequest offerRequest = TestDataBuilder.createPercentOffer(107, 100, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            
            // Step 3: Apply 100% discount to cart
            logTestStep(3, "Applying 100% discount to cart", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(150, 1, 107);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 4: Validate zero cart value
            logTestStep(4, "Validating zero cart value after 100% discount", responseWrapper.getResponse());
            TestValidators.validate100PercentDiscount(responseWrapper);
            
            logTestResult(true, "E2E 100% discount applied successfully - Cart value reduced to ₹0", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Zero_Cart_Value_Application - E2E test for zero cart value with discount
    public void testZeroCartValueApplication() throws Exception {
        logTestStart(
            "Zero_Cart_Value_Application_E2E",
            "E2E test: Create FLATX offer → Apply to zero cart → Validate negative value",
            "FLATX discount on zero cart should result in negative cart value"
        );
        
        try {
            // Step 1: Validate user segment
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create FLATX offer
            logTestStep(2, "Creating FLATX offer via API", null);
            OfferRequest offerRequest = TestDataBuilder.createFlatXOffer(108, 50, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            
            // Step 3: Apply FLATX discount to zero cart
            logTestStep(3, "Applying ₹50 discount to ₹0 cart", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(0, 1, 108);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 4: Validate negative cart value
            logTestStep(4, "Validating negative cart value from zero cart", responseWrapper.getResponse());
            TestValidators.validateNegativeCartValue(responseWrapper, -50);
            
            logTestResult(true, "E2E zero cart discount handled correctly - Negative cart value (-₹50) from zero cart", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Decimal_Percentage_Calculation - E2E test for decimal rounding
    public void testDecimalPercentageCalculation() throws Exception {
        logTestStart(
            "Decimal_Percentage_Calculation_E2E",
            "E2E test: Create 15% offer → Apply to ₹333 cart → Validate decimal rounding",
            "15% of ₹333 should be properly rounded in calculation"
        );
        
        try {
            // Step 1: Validate user segment
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create 15% discount offer
            logTestStep(2, "Creating 15% discount offer via API", null);
            OfferRequest offerRequest = TestDataBuilder.createPercentOffer(109, 15, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            
            // Step 3: Apply 15% discount to ₹333 cart (15% of 333 = 49.95)
            logTestStep(3, "Applying 15% discount to ₹333 cart", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(333, 1, 109);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 4: Validate decimal calculation (15% of 333 = 49.95, rounded to 50, result = 283)
            logTestStep(4, "Validating decimal percentage calculation", responseWrapper.getResponse());
            TestValidators.validatePercentageDiscountWithTolerance(responseWrapper, 333, 15, 1);
            
            logTestResult(true, "E2E decimal percentage calculation handled correctly - Cart value: ₹" + responseWrapper.getResponse().getCart_value(), responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Excessive_Percentage_Discount_E2E - Test >100% percentage offer
    public void testExcessivePercentageDiscount() throws Exception {
        logTestStart(
            "Excessive_Percentage_Discount_E2E",
            "E2E test: Create 150% offer → Apply to cart → Validate negative cart value",
            "150% discount should result in negative cart value (customer gets money back)"
        );
        
        try {
            // Step 1: Validate user segment
            logTestStep(1, "Validating user segment for User ID=1", null);
            com.springboot.client.UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentWrapper = userSegmentClient.getUserSegment(1);
            TestValidators.validateUserSegmentResponse(segmentWrapper, ApiConstants.SEGMENT_P1);
            
            // Step 2: Create 150% discount offer
            logTestStep(2, "Creating 150% discount offer via API", null);
            OfferRequest offerRequest = TestDataBuilder.createPercentOffer(111, 150, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            
            // Step 3: Apply 150% discount to cart
            logTestStep(3, "Applying 150% discount to cart", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(200, 1, 111);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 4: Validate negative cart value result
            logTestStep(4, "Validating negative cart value from excessive discount", responseWrapper.getResponse());
            TestValidators.validateExcessivePercentageDiscount(responseWrapper, 200, 150, "150% discount should result in negative cart value");
            
            logTestResult(true, "E2E excessive percentage discount handled correctly - Negative cart value from 150% discount", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Multiple_Offers_Same_Restaurant - E2E test for multiple offers behavior
    public void testMultipleOffersSameRestaurant() throws Exception {
        logTestStart(
            "Multiple_Offers_Same_Restaurant_E2E",
            "E2E test: Create multiple offers for same restaurant → Apply → Validate offer selection",
            "First matching offer should be applied when multiple offers exist"
        );
        
        try {
            // Step 1: Validate user segment
            logTestStep(1, "Validating user segment for User ID=1", null);
            UserSegmentRestClient.HttpResponseWrapper<SegmentResponse> segmentResponse = userSegmentClient.getUserSegment(1);
            TestValidators.validateUser1P1Segment(segmentResponse);
            
            // Step 2: Create first offer (FLATX ₹25)
            logTestStep(2, "Creating first offer (FLATX ₹25) via API", null);
            OfferRequest offer1 = TestDataBuilder.createFlatXOffer(110, 25, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> create1Wrapper = cartOfferClient.createOffer(offer1);
            TestValidators.validateOfferCreationSuccess(create1Wrapper);
            
            // Step 3: Create second offer (PERCENT 10%)
            logTestStep(3, "Creating second offer (PERCENT 10%) via API", null);
            OfferRequest offer2 = TestDataBuilder.createPercentOffer(110, 10, ApiConstants.SEGMENT_P1);
            HttpResponseWrapper<ApiResponse> create2Wrapper = cartOfferClient.createOffer(offer2);
            TestValidators.validateOfferCreationSuccess(create2Wrapper);
            
            // Step 4: Apply offers to cart (should use first matching offer)
            logTestStep(4, "Applying offers to cart - testing offer selection", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(300, 1, 110);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 5: Validate offer selection behavior
            logTestStep(5, "Validating offer selection behavior", responseWrapper.getResponse());
            TestValidators.validateMultipleOffersScenario(responseWrapper, 275, 270, "FLATX ₹25 vs PERCENT 10%");
            
            String appliedOffer = (responseWrapper.getResponse().getCart_value() == 275) ? "FLATX ₹25 discount" : "PERCENT 10% discount";
            logTestResult(true, "E2E multiple offers handled correctly - Applied: " + appliedOffer + ", Cart value: ₹" + responseWrapper.getResponse().getCart_value(), responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "E2E test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    // ================================================================================
    //                           NEGATIVE TEST CASES (16)
    // ================================================================================

    @Test // Test Case: Missing_Cart_Value - Test offer application with missing cart_value parameter
    public void testMissingCartValueParameter() throws Exception {
        logTestStart(
            "Missing_Cart_Value",
            "Attempt to apply offer with missing cart_value in JSON request",
            "Should handle missing mandatory parameters gracefully"
        );
        
        try {
            // Step 1: Send raw JSON with missing cart_value field to API
            logTestStep(1, "Sending request with missing cart_value field", null);
            String invalidJsonRequest = "{\"user_id\":1,\"restaurant_id\":1}";
            System.out.println(" Invalid JSON (missing cart_value): " + invalidJsonRequest);
            
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOfferWithRawJson(invalidJsonRequest);
            
            // Step 2: Validate API response to missing cart_value
            logTestStep(2, "Validating API response to missing cart_value parameter", responseWrapper);
            TestValidators.validateMissingParameterHandling(responseWrapper, "cart_value");
            
            logTestResult(true, "Missing cart_value parameter handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Invalid_User_ID_Zero - Test offer application with user ID = 0
    public void testInvalidUserIdZero() throws Exception {
        logTestStart(
            "Invalid_User_ID_Zero",
            "Test offer application with user_id = 0",
            "Should handle zero user ID appropriately"
        );
        
        try {
            // Step 1: Create request with zero user ID
            logTestStep(1, "Creating apply request with user_id = 0", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(200, 0, 1);
            System.out.println(" Apply Request: Cart Value=200, User ID=0 (invalid), Restaurant ID=1");
            
            // Step 2: Submit request with zero user ID
            logTestStep(2, "Submitting request with zero user_id", applyRequest);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            // Step 3: Validate response handling
            logTestStep(3, "Validating zero user_id handling", responseWrapper);
            TestValidators.validateInvalidRequestHandling(responseWrapper, "zero user_id");
            
            logTestResult(true, "Zero user_id handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
	}
}

    @Test // Test Case: Invalid_Parameter_Types - Test offer application with invalid data types
    public void testInvalidParameterTypes() throws Exception {
        logTestStart(
            "Invalid_Parameter_Types",
            "Test offer application with invalid parameter data types in JSON",
            "Should handle type validation errors gracefully"
        );
        
        try {
            logTestStep(1, "Sending request with invalid parameter types", null);
            String invalidJsonRequest = "{\"cart_value\":\"not_a_number\",\"user_id\":\"invalid\",\"restaurant_id\":true}";
            
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOfferWithRawJson(invalidJsonRequest);
            
            logTestStep(2, "Validating API response to invalid parameter types", responseWrapper);
            TestValidators.validateInvalidParameterTypesHandling(responseWrapper, "string values for numeric fields");
            
            logTestResult(true, "Invalid parameter types handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Invalid_User_ID_Negative - Test offer application with negative user ID
    public void testInvalidUserIdNegative() throws Exception {
        logTestStart(
            "Invalid_User_ID_Negative",
            "Test offer application with negative user_id",
            "Should handle negative user ID appropriately"
        );
        
        try {
            logTestStep(1, "Creating apply request with negative user_id", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(200, -1, 1);
            
            logTestStep(2, "Submitting request with negative user_id", applyRequest);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            logTestStep(3, "Validating negative user_id handling", responseWrapper);
            TestValidators.validateNegativeValuesHandling(responseWrapper, "negative user_id");
            
            logTestResult(true, "Negative user_id handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Invalid_Restaurant_ID_Zero - Test offer application with restaurant ID = 0
    public void testInvalidRestaurantIdZero() throws Exception {
        logTestStart(
            "Invalid_Restaurant_ID_Zero",
            "Test offer application with restaurant_id = 0",
            "Should handle zero restaurant ID appropriately"
        );
        
        try {
            logTestStep(1, "Creating apply request with restaurant_id = 0", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(200, 1, 0);
            
            logTestStep(2, "Submitting request with zero restaurant_id", applyRequest);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            logTestStep(3, "Validating zero restaurant_id handling", responseWrapper);
            TestValidators.validateInvalidRequestHandling(responseWrapper, "zero restaurant_id");
            
            logTestResult(true, "Zero restaurant_id handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: No_Offers_Available_Negative - Test no offers scenario as negative case
    public void testNoOffersAvailable() throws Exception {
        logTestStart(
            "No_Offers_Available_Negative",
            "Test offer application when no offers exist for restaurant (negative context)",
            "Should return original cart value when no offers available"
        );
        
        try {
            logTestStep(1, "Applying offer to restaurant without any offers", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(200, 1, 999);
            
            logTestStep(2, "Submitting application for restaurant without offers", applyRequest);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            logTestStep(3, "Validating no offers handling", responseWrapper);
            TestValidators.validateNoDiscountApplied(responseWrapper, 200, "when no offers available for restaurant");
            
            logTestResult(true, "No offers scenario handled correctly in negative context", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: User_Segment_Mismatch_Negative - Test segment mismatch as negative case
    public void testUserSegmentMismatch() throws Exception {
        logTestStart(
            "User_Segment_Mismatch_Negative",
            "Test offer application when user segment doesn't match offer (negative context)",
            "Should return original cart value when segment doesn't match"
        );
        
        try {
            // Step 1: Create offer for P3 segment
            logTestStep(1, "Creating P3 segment offer via API", null);
            OfferRequest offerRequest = TestDataBuilder.createPercentOffer(888, 25, "p3");
            HttpResponseWrapper<ApiResponse> createResponseWrapper = cartOfferClient.createOffer(offerRequest);
            TestValidators.validateOfferCreationSuccess(createResponseWrapper);
            
            // Step 2: Apply with P1 user (segment mismatch)
            logTestStep(2, "Applying P3 offer to P1 user", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(200, 1, 888);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            logTestStep(3, "Validating segment mismatch handling", responseWrapper);
            TestValidators.validateNoDiscountApplied(responseWrapper, 200, "due to segment mismatch");
            
            logTestResult(true, "Segment mismatch handled correctly in negative context", responseWrapper.getResponse());
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Extremely_Large_Cart_Value - Test with very large cart values
    public void testExtremelyLargeCart() throws Exception {
        logTestStart(
            "Extremely_Large_Cart_Value",
            "Test offer application with extremely large cart value",
            "Should handle large numbers gracefully"
        );
        
        try {
            logTestStep(1, "Creating apply request with extremely large cart value", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(999999999, 1, 1);
            
            logTestStep(2, "Submitting request with large cart value", applyRequest);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            logTestStep(3, "Validating large cart value handling", responseWrapper);
            TestValidators.validateInvalidRequestHandling(responseWrapper, "extremely large cart value");
            
            logTestResult(true, "Extremely large cart value handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Missing_User_ID_Parameter - Test with missing user_id
    public void testMissingUserIdParameter() throws Exception {
        logTestStart(
            "Missing_User_ID_Parameter",
            "Test offer application with missing user_id in JSON request",
            "Should handle missing user_id parameter gracefully"
        );
        
        try {
            logTestStep(1, "Sending request with missing user_id field", null);
            String invalidJsonRequest = "{\"cart_value\":200,\"restaurant_id\":1}";
            System.out.println(" Invalid JSON (missing user_id): " + invalidJsonRequest);
            
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOfferWithRawJson(invalidJsonRequest);
            
            logTestStep(2, "Validating API response to missing user_id parameter", responseWrapper);
            TestValidators.validateMissingParameterHandling(responseWrapper, "user_id");
            
            logTestResult(true, "Missing user_id parameter handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Missing_Restaurant_ID_Parameter - Test with missing restaurant_id
    public void testMissingRestaurantIdParameter() throws Exception {
        logTestStart(
            "Missing_Restaurant_ID_Parameter",
            "Test offer application with missing restaurant_id in JSON request",
            "Should handle missing restaurant_id parameter gracefully"
        );
        
        try {
            logTestStep(1, "Sending request with missing restaurant_id field", null);
            String invalidJsonRequest = "{\"cart_value\":200,\"user_id\":1}";
            System.out.println(" Invalid JSON (missing restaurant_id): " + invalidJsonRequest);
            
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOfferWithRawJson(invalidJsonRequest);
            
            logTestStep(2, "Validating API response to missing restaurant_id parameter", responseWrapper);
            TestValidators.validateMissingParameterHandling(responseWrapper, "restaurant_id");
            
            logTestResult(true, "Missing restaurant_id parameter handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Empty_JSON_Body - Test with completely empty JSON
    public void testEmptyJsonBody() throws Exception {
        logTestStart(
            "Empty_JSON_Body",
            "Test offer application with completely empty JSON request body",
            "Should handle empty JSON gracefully"
        );
        
        try {
            logTestStep(1, "Sending request with empty JSON body", null);
            String emptyJsonRequest = "{}";
            System.out.println(" Empty JSON: " + emptyJsonRequest);
            
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOfferWithRawJson(emptyJsonRequest);
            
            logTestStep(2, "Validating API response to empty JSON body", responseWrapper);
            TestValidators.validateInvalidRequestHandling(responseWrapper, "empty JSON body");
            
            logTestResult(true, "Empty JSON body handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Null_Request_Body_Apply - Test with null request body
    public void testNullRequestBodyApply() throws Exception {
        logTestStart(
            "Null_Request_Body_Apply",
            "Test offer application with null request body",
            "Should handle null request body gracefully"
        );
        
        try {
            logTestStep(1, "Sending request with null body", null);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOfferWithNullBody();
            
            logTestStep(2, "Validating API response to null request body", responseWrapper);
            TestValidators.validateNullRequestBodyHandling(responseWrapper);
            
            logTestResult(true, "Null request body handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Malformed_JSON_Apply - Test with malformed JSON
    public void testMalformedJsonApply() throws Exception {
        logTestStart(
            "Malformed_JSON_Apply",
            "Test offer application with malformed JSON syntax",
            "Should handle malformed JSON gracefully"
        );
        
        try {
            logTestStep(1, "Sending request with malformed JSON", null);
            String malformedJson = "{cart_value:200, user_id:1, restaurant_id:1";
            System.out.println(" Malformed JSON: " + malformedJson);
            
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOfferWithRawJson(malformedJson);
            
            logTestStep(2, "Validating API response to malformed JSON", responseWrapper);
            TestValidators.validateMalformedJsonHandling(responseWrapper, "malformed JSON with missing braces");
            
            logTestResult(true, "Malformed JSON handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Negative_Cart_Value - Test with negative cart value
    public void testNegativeCartValue() throws Exception {
        logTestStart(
            "Negative_Cart_Value",
            "Test offer application with negative cart value",
            "Should handle negative cart value appropriately"
        );
        
        try {
            logTestStep(1, "Creating apply request with negative cart value", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(-100, 1, 1);
            
            logTestStep(2, "Submitting request with negative cart value", applyRequest);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOffer(applyRequest);
            
            logTestStep(3, "Validating negative cart value handling", responseWrapper);
            TestValidators.validateInvalidRequestHandling(responseWrapper, "negative cart value");
            
            logTestResult(true, "Negative cart value handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }

    @Test // Test Case: Content_Type_Missing - Test without Content-Type header
    public void testContentTypeMissing() throws Exception {
        logTestStart(
            "Content_Type_Missing",
            "Test offer application without Content-Type header",
            "Should handle missing Content-Type header appropriately"
        );
        
        try {
            logTestStep(1, "Sending request without Content-Type header", null);
            ApplyOfferRequest applyRequest = TestDataBuilder.createApplyRequest(100, 1, 1);
            
            logTestStep(2, "Submitting request without Content-Type header", applyRequest);
            HttpResponseWrapper<ApplyOfferResponse> responseWrapper = cartOfferClient.applyOfferWithoutContentType(applyRequest);
            
            logTestStep(3, "Validating Content-Type handling", responseWrapper);
            TestValidators.validateMissingContentTypeHandling(responseWrapper);
            
            logTestResult(true, "Content-Type header handling validated", null);
            
        } catch (Exception e) {
            logTestResult(false, "Test failed with exception: " + e.getMessage(), null);
            throw e;
        }
    }
}