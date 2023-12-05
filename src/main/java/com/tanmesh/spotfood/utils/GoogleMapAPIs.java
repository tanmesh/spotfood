package com.tanmesh.spotfood.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanmesh.spotfood.exception.APINotWorkingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class GoogleMapAPIs {
    public static double[] getLatLong(Properties props, String address) {
        double[] coordinates = new double[2];

        try {
            address = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String urlString = "https://maps.googleapis.com/maps/api/geocode/json?" + "address=" + address + "&key=" + props.getProperty("GEOCODE_API_KEY");

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode != 200) {
                throw new APINotWorkingException("Google Map API giving error");
            }

            // Read the response from the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response using Jackson ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.toString());

            // Close the connection
            connection.disconnect();

            coordinates[0] = Double.parseDouble(jsonResponse.get("results").get(0).get("geometry").get("location").get("lat").asText());
            coordinates[1] = Double.parseDouble(jsonResponse.get("results").get(0).get("geometry").get("location").get("lng").asText());
        } catch (IOException | APINotWorkingException e) {
            e.printStackTrace();
        }

        return coordinates;
    }
}
