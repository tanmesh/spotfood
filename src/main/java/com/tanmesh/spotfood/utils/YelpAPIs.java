package com.tanmesh.spotfood.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanmesh.spotfood.entity.LatLong;
import com.tanmesh.spotfood.entity.Restaurant;

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
    public static List<Restaurant> getNearbyRestaurant(Properties props, double[] coordinates) throws Exception {
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
                throw new Exception("Something went wrong when accessing Yelp API");
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

        List<Restaurant> restuarants = new ArrayList<>();
        for (JsonNode node : jsonResponse.get("businesses")) {
            Restaurant data = new Restaurant();
            data.setName(node.get("name").asText());
            data.setId(node.get("id").asText());
            restuarants.add(data);
        }
        return restuarants;
    }

    public static Restaurant getRestaurant(Properties props, String restaurantId) throws Exception {
        JsonNode jsonResponse;

        try {
            String urlString = "https://api.yelp.com/v3/businesses/" + URLEncoder.encode(restaurantId, StandardCharsets.UTF_8.toString());
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + props.getProperty("YELP_API_KEY"));

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new Exception("Something went wrong when accessing Yelp API");
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

        Restaurant restaurant = new Restaurant();
        restaurant.setId(jsonResponse.get("id").asText());
        restaurant.setName(jsonResponse.get("name").asText());
        restaurant.setPhoneNumber(jsonResponse.get("display_phone").asText());

        // handle address
        JsonNode addressNode = jsonResponse.get("location").get("display_address");
        StringBuilder address = new StringBuilder();
        for (JsonNode node_ : addressNode) {
            address.append(" ").append(node_.asText());
        }
        restaurant.setAddress(address.toString());

        // handle imgUrls
        JsonNode photosNode = jsonResponse.get("photos");
        List<String> photos = new ArrayList<>();
        for(JsonNode node_: photosNode) {
            photos.add(node_.asText());
            System.out.println(node_.asText());
        }
        restaurant.setImgUrls(photos);

        double lat = jsonResponse.get("coordinates").get("latitude").asDouble();
        double lng = jsonResponse.get("coordinates").get("longitude").asDouble();
        LatLong latLong = new LatLong(lat, lng);
        restaurant.setLatLong(latLong);

        restaurant.setUrl(jsonResponse.get("url").asText());
        restaurant.setRating(jsonResponse.get("rating").asDouble());

        return restaurant;
    }
}
