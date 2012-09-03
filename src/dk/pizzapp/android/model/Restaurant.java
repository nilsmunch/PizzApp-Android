package dk.pizzapp.android.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Restaurant {
    @JsonProperty("type") private String type;
    @JsonProperty("link") private String link;
    @JsonProperty("name") private String name;
    @JsonProperty("street") private String address;
    @JsonProperty("city") private String city;
    @JsonProperty("lat") private String latitude;
    @JsonProperty("long") private String longitude;
    @JsonProperty("keys") private String keys;
    @JsonProperty("image") private String image;
    @JsonProperty("phone") private String phone;

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
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

    public String getKeys() {
        return keys;
    }

    public String getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }
}
