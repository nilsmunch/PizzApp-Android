package dk.pizzapp.android.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Restaurant {
    private String id;
    private float distance;
    @JsonProperty("type")
    private String type;
    @JsonProperty("link")
    private String link;
    @JsonProperty("name")
    private String name;
    @JsonProperty("street")
    private String address;
    @JsonProperty("city")
    private String city;
    @JsonProperty("long")
    private String longitude;
    @JsonProperty("lat")
    private String latitude;
    @JsonProperty("keys")
    private String keys;
    @JsonProperty("image")
    private String image;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("del_price")
    private String delivery;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

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

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
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

    public String getDelivery() {
        return delivery;
    }
}
