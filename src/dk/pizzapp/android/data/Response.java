package dk.pizzapp.android.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class Response {
    @JsonProperty("response")
    private String response;
    @JsonProperty("result")
    private HashMap<String, Restaurant> result;
    @JsonProperty("error")
    private String error;

    public String getResponse() {
        return response;
    }

    public HashMap<String, Restaurant> getResult() {
        return result;
    }

    public String getError() {
        return error;
    }
}
