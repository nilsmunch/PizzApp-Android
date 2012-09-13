package dk.pizzapp.android.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
    @JsonProperty("response")
    private String response;
    @JsonProperty("result")
    private HashMap<String, Restaurant> result;

    public String getResponse() {
        return response;
    }

    public HashMap<String, Restaurant> getResult() {
        return result;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Restaurant {
        private String id;
        @JsonProperty("name")
        private String name;
        @JsonProperty("street")
        private String address;
        @JsonProperty("city")
        private String city;
        @JsonProperty("lat")
        private String latitude;
        @JsonProperty("long")
        private String longitude;
        private float distance;
        @JsonProperty("keys")
        private String tags;
        @JsonProperty("phone")
        private String phone;
        @JsonProperty("del_price")
        private String deliveryPrice;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getCity() {
            return city;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public float getDistance() {
            return distance;
        }

        public void setDistance(float distance) {
            this.distance = distance;
        }

        public String getTags() {
            return tags;
        }

        public String getPhone() {
            return phone;
        }

        public String getDeliveryPrice() {
            return deliveryPrice;
        }
    }
}
