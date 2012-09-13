package dk.pizzapp.android.api;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.Transformer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonTransformer implements Transformer {

    @Override
    public <T> T transform(String url, Class<T> type, String encoding, byte[] data, AjaxStatus status) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(data, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
