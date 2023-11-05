package com.tanmesh.splatter.scrachpad.dummyData;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
class Businesses {
    private List<Business> businesses;

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Business {
    private String name;

    private String image_url;
    private Location location;

    private Coordinates coordinates;

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Coordinates {
    private Double latitude;
    private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Location {
    @JsonProperty("display_address")
    private String[] display_address;

    private String address1;

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String[] getDisplayAddress() {
        return display_address;
    }

    public void setDisplayAddress(String[] address1) {
        this.display_address = address1;
    }
}


public class FillDummyData {

    public static void main(String[] args)  {
//        List<Set<String>> tags = getTags();
//        List<String> imgPath = readImages();
//        List<RestaurantInfo> restaurantInfos = getRestaurantInfo();
//        List<String> userEmailId = getEmailId();
//
//        for (int i = 0; i < 1; ++i) {
//            String emailId = userEmailId.get(i % 4);
//            String imgUrl = setImgUrl(imgPath.get(i), restaurantInfos.get(i).getName());
//
//            UserPostData userPostData = new UserPostData();
//            userPostData.setAuthorEmailId(emailId);
//            userPostData.setTagList(tags.get(i));
//            userPostData.setLatitude(restaurantInfos.get(i).getLatitude());
//            userPostData.setLongitude(restaurantInfos.get(i).getLongitude());
//            userPostData.setLocationName(restaurantInfos.get(i).getName());
//            userPostData.setImgUrl(imgUrl);
//
//            userPostService.addPost(userPostData, emailId);
//        }
    }

    public static List<String> getEmailId() {
        List<String> userEmailId = new ArrayList<>();

        userEmailId.add("tanmeshnm2@gmail.com");
        userEmailId.add("admin@gmail.com");
        userEmailId.add("jackinbox@gmail.com");
        userEmailId.add("happyjoe@gmail.com");

        return userEmailId;
    }

    public static List<RestaurantInfo> getRestaurantInfo() {
        List<RestaurantInfo> restaurantInfos = new ArrayList<>();

        String filePath = "src/main/java/com/tanmesh/splatter/scrachpad/dummyData/dummyDataFromYelp.json";
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            File jsonFile = new File(filePath);

            Businesses businesses = objectMapper.readValue(jsonFile, Businesses.class);

            for (Business business : businesses.getBusinesses()) {
                Location location = business.getLocation();
                Coordinates coordinates = business.getCoordinates();
                restaurantInfos.add(
                        new RestaurantInfo(
                                business.getName(),
                                String.join(", ", location.getDisplayAddress()),
                                coordinates.getLatitude(),
                                coordinates.getLongitude()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurantInfos;
    }

    public static List<String> readImages() {
        List<String> imgPath = new ArrayList<>();

        String folderPath = "src/main/java/com/tanmesh/splatter/scrachpad/dummyData/food-img";

        File folder = new File(folderPath);

        // Check if the path is a directory
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        imgPath.add(folderPath + "/" + file.getName());
                    }
                }
            }
        }

        return imgPath;
    }

    public static List<Set<String>> getTags() {
        List<Set<String>> tags = new ArrayList<>();

        // Hardcoded lists of 3 tags each
        tags.add(new HashSet<>(Arrays.asList("Vegetarian", "Vegan", "Keto")));
        tags.add(new HashSet<>(Arrays.asList("Gluten-Free", "Dessert", "Spicy")));
        tags.add(new HashSet<>(Arrays.asList("Seafood", "Keto", "Local")));
        tags.add(new HashSet<>(Arrays.asList("Paleo", "Organic", "Dessert")));
        tags.add(new HashSet<>(Arrays.asList("Vegan", "Spicy", "Local")));
        tags.add(new HashSet<>(Arrays.asList("Keto", "Organic", "Vegetarian")));
        tags.add(new HashSet<>(Arrays.asList("Spicy", "Dessert", "Gluten-Free")));
        tags.add(new HashSet<>(Arrays.asList("Local", "Seafood", "Paleo")));
        tags.add(new HashSet<>(Arrays.asList("Vegan", "Organic", "Keto")));
        tags.add(new HashSet<>(Arrays.asList("Spicy", "Vegetarian", "Gluten-Free")));
        tags.add(new HashSet<>(Arrays.asList("Dessert", "Paleo", "Seafood")));
        tags.add(new HashSet<>(Arrays.asList("Local", "Organic", "Vegetarian")));
        tags.add(new HashSet<>(Arrays.asList("Gluten-Free", "Spicy", "Keto")));
        tags.add(new HashSet<>(Arrays.asList("Seafood", "Dessert", "Vegan")));
        tags.add(new HashSet<>(Arrays.asList("Paleo", "Vegetarian", "Local")));
        tags.add(new HashSet<>(Arrays.asList("Keto", "Gluten-Free", "Organic")));
        tags.add(new HashSet<>(Arrays.asList("Spicy", "Seafood", "Dessert")));
        tags.add(new HashSet<>(Arrays.asList("Vegan", "Local", "Vegetarian")));
        tags.add(new HashSet<>(Arrays.asList("Organic", "Keto", "Paleo")));
        tags.add(new HashSet<>(Arrays.asList("Dessert", "Gluten-Free", "Spicy")));

        return tags;
    }
}
