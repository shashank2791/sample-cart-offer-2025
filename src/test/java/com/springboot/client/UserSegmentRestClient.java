package com.springboot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.SegmentResponse;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class UserSegmentRestClient {
    
    private final String baseUrl;
    private final ObjectMapper objectMapper;
    
    public UserSegmentRestClient() {
        this.baseUrl = "http://localhost:1080";
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * HttpResponseWrapper to capture both status code and response
     */
    public static class HttpResponseWrapper<T> {
        private int statusCode;
        private T response;
        private String responseBody;
        private String errorMessage;

        public int getStatusCode() { return statusCode; }
        public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
        
        public T getResponse() { return response; }
        public void setResponse(T response) { this.response = response; }
        
        public String getResponseBody() { return responseBody; }
        public void setResponseBody(String responseBody) { this.responseBody = responseBody; }
        
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        
        public boolean isSuccess() { return statusCode >= 200 && statusCode < 300; }
    }
    
    /**
     * Get user segment from mock server
     * @param userId The user ID to get segment for
     * @return HttpResponseWrapper with status code and segment response
     * @throws Exception if API call fails
     */
    public HttpResponseWrapper<SegmentResponse> getUserSegment(int userId) throws Exception {
        String endpoint = baseUrl + "/api/v1/user_segment?user_id=" + userId;
        return getRequestWithStatus(endpoint, SegmentResponse.class);
    }
    
    /**
     * Simple GET request with status code capture
     */
    private <R> HttpResponseWrapper<R> getRequestWithStatus(String urlString, Class<R> responseType) throws Exception {
        System.out.println("Making GET request to: " + urlString);
        
        try {
            HttpURLConnection con = createGetConnection(urlString);
            return processGetResponse(con, responseType);
        } catch (Exception connectionException) {
            System.out.println("Error connecting to: " + urlString + " - " + connectionException.getMessage());
            return createErrorWrapper(500, "Connection failed: " + connectionException.getMessage());
        }
    }
    
    // ==================== HELPER METHODS ====================
    
    /**
     * Create and configure GET HTTP connection
     */
    private HttpURLConnection createGetConnection(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setConnectTimeout(5000); // 5 second connection timeout
        con.setReadTimeout(10000);   // 10 second read timeout
        return con;
    }
    
    /**
     * Process GET response and create wrapper
     */
    private <R> HttpResponseWrapper<R> processGetResponse(HttpURLConnection con, Class<R> responseType) throws Exception {
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        
        String responseBody = readResponseBody(con, responseCode);
        System.out.println("Response Body: " + responseBody);
        
        HttpResponseWrapper<R> wrapper = new HttpResponseWrapper<>();
        wrapper.setStatusCode(responseCode);
        wrapper.setResponseBody(responseBody);
        
        if (responseCode >= 200 && responseCode < 300 && !responseBody.isEmpty()) {
            try {
                wrapper.setResponse(objectMapper.readValue(responseBody, responseType));
            } catch (Exception parseException) {
                System.out.println("Failed to parse response as " + responseType.getSimpleName() + ": " + parseException.getMessage());
                wrapper.setErrorMessage("JSON parsing failed: " + parseException.getMessage());
            }
        } else {
            wrapper.setErrorMessage(responseBody.isEmpty() ? "HTTP " + responseCode + " error with no response body" : responseBody);
        }
        
        return wrapper;
    }
    
    /**
     * Read response body from connection
     */
    private String readResponseBody(HttpURLConnection con, int responseCode) throws Exception {
        StringBuilder response = new StringBuilder();
        
        try {
            java.io.InputStream inputStream = responseCode >= 400 ? con.getErrorStream() : con.getInputStream();
            
            if (inputStream != null) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
            } else {
                System.out.println("Warning: No response stream available for status code: " + responseCode);
            }
        } catch (Exception streamException) {
            System.out.println("Error reading response stream: " + streamException.getMessage());
        }
        
        return response.toString();
    }
    
    /**
     * Create error wrapper for connection failures
     */
    private <R> HttpResponseWrapper<R> createErrorWrapper(int statusCode, String errorMessage) {
        HttpResponseWrapper<R> errorWrapper = new HttpResponseWrapper<>();
        errorWrapper.setStatusCode(statusCode);
        errorWrapper.setErrorMessage(errorMessage);
        errorWrapper.setResponseBody("");
        return errorWrapper;
    }
}
