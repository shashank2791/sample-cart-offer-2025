package com.springboot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.ApiResponse;
import com.springboot.controller.ApplyOfferRequest;
import com.springboot.controller.ApplyOfferResponse;
import com.springboot.controller.OfferRequest;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class CartOfferRestClient {
    
    private final String baseUrl;
    private final ObjectMapper objectMapper;
    
    public CartOfferRestClient() {
        this.baseUrl = "http://localhost:9001";
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Create a new offer for a restaurant (for E2E testing)
     * @param offerRequest The offer details
     * @return HttpResponseWrapper with status code and response
     * @throws Exception if API call fails
     */
    public HttpResponseWrapper<ApiResponse> createOffer(OfferRequest offerRequest) throws Exception {
        String endpoint = baseUrl + "/api/v1/offer";
        return postRequestWithStatus(endpoint, offerRequest, ApiResponse.class);
    }

    /**
     * Apply an offer to a user's cart
     * @param applyRequest The cart and user details
     * @return HttpResponseWrapper with status code and response
     * @throws Exception if API call fails
     */
    public HttpResponseWrapper<ApplyOfferResponse> applyOffer(ApplyOfferRequest applyRequest) throws Exception {
        String endpoint = baseUrl + "/api/v1/cart/apply_offer";
        return postRequestWithStatus(endpoint, applyRequest, ApplyOfferResponse.class);
    }
    
    /**
     * Send raw JSON string to apply offer endpoint (for testing malformed JSON)
     * @param rawJson The raw JSON string to send
     * @return HttpResponseWrapper with status code and response
     * @throws Exception if request fails
     */
    public HttpResponseWrapper<ApplyOfferResponse> applyOfferWithRawJson(String rawJson) throws Exception {
        String endpoint = baseUrl + "/api/v1/cart/apply_offer";
        return postRawJsonWithStatus(endpoint, rawJson, ApplyOfferResponse.class);
    }
    
    /**
     * Send null request body (for testing null body validation)
     * @return HttpResponseWrapper with status code and response
     * @throws Exception if request fails
     */
    public HttpResponseWrapper<ApplyOfferResponse> applyOfferWithNullBody() throws Exception {
        String endpoint = baseUrl + "/api/v1/cart/apply_offer";
        return postNullBodyWithStatus(endpoint, ApplyOfferResponse.class);
    }
    
    /**
     * Send request without Content-Type header (for testing header validation)
     * @param applyRequest The cart and user details
     * @return HttpResponseWrapper with status code and response
     * @throws Exception if API call fails
     */
    public HttpResponseWrapper<ApplyOfferResponse> applyOfferWithoutContentType(ApplyOfferRequest applyRequest) throws Exception {
        String endpoint = baseUrl + "/api/v1/cart/apply_offer";
        return postRequestWithoutContentType(endpoint, applyRequest, ApplyOfferResponse.class);
    }
    
    /**
     * Simple POST request with JSON body
     * @param urlString The endpoint URL
     * @param requestBody The request payload
     * @param responseType The expected response class
     * @return HttpResponseWrapper with status code and parsed response
     * @throws Exception if request fails
     */
    private <T, R> HttpResponseWrapper<R> postRequestWithStatus(String urlString, T requestBody, Class<R> responseType) throws Exception {
        HttpURLConnection con = createConnection(urlString, "POST");
        con.setRequestProperty("Content-Type", "application/json");
        
        sendJsonPayload(con, objectMapper.writeValueAsString(requestBody));
        return processResponse(con, responseType);
    }
    
    /**
     * Simple POST request with raw JSON string
     * @param urlString The endpoint URL
     * @param rawJson The raw JSON string to send
     * @param responseType The expected response class
     * @return HttpResponseWrapper with status code and parsed response
     * @throws Exception if request fails
     */
    private <R> HttpResponseWrapper<R> postRawJsonWithStatus(String urlString, String rawJson, Class<R> responseType) throws Exception {
        HttpURLConnection con = createConnection(urlString, "POST");
        con.setRequestProperty("Content-Type", "application/json");
        
        sendJsonPayload(con, rawJson);
        return processResponse(con, responseType);
    }
    
    /**
     * Simple POST request without Content-Type header (for testing header validation)
     * @param urlString The endpoint URL
     * @param requestBody The request payload
     * @param responseType The expected response class
     * @return HttpResponseWrapper with status code and parsed response
     * @throws Exception if request fails
     */
    private <T, R> HttpResponseWrapper<R> postRequestWithoutContentType(String urlString, T requestBody, Class<R> responseType) throws Exception {
        HttpURLConnection con = createConnection(urlString, "POST");
        // Intentionally NOT setting Content-Type header
        
        sendJsonPayload(con, objectMapper.writeValueAsString(requestBody));
        return processResponse(con, responseType);
    }
    
    /**
     * POST request method with null body (for testing null body validation)
     * @param urlString The endpoint URL
     * @param responseType The expected response class
     * @return HttpResponseWrapper with status code and parsed response
     * @throws Exception if request fails
     */
    private <R> HttpResponseWrapper<R> postNullBodyWithStatus(String urlString, Class<R> responseType) throws Exception {
        HttpURLConnection con = createConnection(urlString, "POST");
        con.setRequestProperty("Content-Type", "application/json");
        
        sendNullPayload(con);
        return processResponse(con, responseType);
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Create and configure HTTP connection
     */
    private HttpURLConnection createConnection(String urlString, String method) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        con.setDoOutput(true);
        return con;
    }
    
    /**
     * Send JSON payload to connection
     */
    private void sendJsonPayload(HttpURLConnection con, String jsonPayload) throws Exception {
        try (OutputStream os = con.getOutputStream()) {
            os.write(jsonPayload.getBytes());
            os.flush();
        }
    }
    
    /**
     * Send empty/null body (for testing null body validation)
     */
    private void sendNullPayload(HttpURLConnection con) throws Exception {
        // Intentionally don't write anything to the output stream
        // This tests how the server handles empty/null request bodies
    }
    
    /**
     * Process HTTP response and create wrapper
     */
    private <R> HttpResponseWrapper<R> processResponse(HttpURLConnection con, Class<R> responseType) throws Exception {
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);
        
        String responseBody = readResponseBody(con, responseCode);
        System.out.println("Response Body: " + responseBody);
        
        HttpResponseWrapper<R> wrapper = new HttpResponseWrapper<>();
        wrapper.setStatusCode(responseCode);
        wrapper.setResponseBody(responseBody);
        
        if (responseCode >= 200 && responseCode < 300 && !responseBody.isEmpty()) {
            try {
                wrapper.setResponse(objectMapper.readValue(responseBody, responseType));
            } catch (Exception e) {
                System.out.println("Failed to parse response: " + e.getMessage());
            }
        } else {
            wrapper.setErrorMessage(responseBody);
        }
        
        return wrapper;
    }
    
    /**
     * Read response body from connection
     */
    private String readResponseBody(HttpURLConnection con, int responseCode) throws Exception {
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                responseCode >= 400 ? con.getErrorStream() : con.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        return response.toString();
    }
    
    /**
     * Wrapper class for HTTP responses with status code
     */
    public static class HttpResponseWrapper<T> {
        private int statusCode;
        private T response;
        private String responseBody;
        private String errorMessage;
        
        // Getters and setters
        public int getStatusCode() { return statusCode; }
        public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
        
        public T getResponse() { return response; }
        public void setResponse(T response) { this.response = response; }
        
        public String getResponseBody() { return responseBody; }
        public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        
        public boolean isSuccess() { return statusCode >= 200 && statusCode < 300; }
        public boolean isBadRequest() { return statusCode == 400; }
        public boolean isUnsupportedMediaType() { return statusCode == 415; }
        public boolean isInternalServerError() { return statusCode == 500; }
        
        @Override
        public String toString() {
            return "HttpResponseWrapper{" +
                    "statusCode=" + statusCode +
                    ", response=" + response +
                    ", errorMessage='" + errorMessage + '\'' +
                    '}';
        }
    }
}
