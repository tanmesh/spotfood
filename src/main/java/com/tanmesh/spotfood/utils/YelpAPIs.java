package com.tanmesh.spotfood.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class YelpAPIs {
    public static List<String> getNearbyRestaurant(Properties props, double[] coordinates) throws Exception {
        JsonNode jsonResponse;

        try {
            String urlString = "https://api.yelp.com/v3/businesses/search?" +
                    "latitude=" + URLEncoder.encode(String.valueOf(coordinates[0]), StandardCharsets.UTF_8.toString()) +
                    "&longitude=" + URLEncoder.encode(String.valueOf(coordinates[1]), StandardCharsets.UTF_8.toString()) +
                    "&radius=2&sort_by=best_match&limit=20";
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + props.getProperty("YELP_API_KEY"));

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Something went wrong");
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
            jsonResponse = objectMapper.readTree(response.toString());

            // Close the connection
            connection.disconnect();
        } catch (Exception e) {
            throw new Exception(e);
        }

        List<String> restuarants = new ArrayList<>();
        for (JsonNode node : jsonResponse.get("businesses")) {
            restuarants.add(node.get("name").asText());
        }
        return restuarants;
    }
}
