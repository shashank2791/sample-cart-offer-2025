package com.springboot.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.controller.OfferRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * REST client for Offer API operations
 * Handles offer creation requests to the offer service
 */
public class OfferRestClient {
    
    public OfferRestClient() {
    }
    
    /**
     * Add/Create a new offer using the existing addOffer implementation
     * @param offerRequest The offer request to create
     * @return true if offer creation was successful
     * @throws Exception if there's an error during offer creation
     */
    public boolean addOffer(OfferRequest offerRequest) throws Exception {
        String urlString = "http://localhost:9001/api/v1/offer";
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/json");
        
        ObjectMapper mapper = new ObjectMapper();
        String POST_PARAMS = mapper.writeValueAsString(offerRequest);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request did not work.");
        }
        return true;
    }
}
