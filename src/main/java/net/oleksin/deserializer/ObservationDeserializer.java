package net.oleksin.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.oleksin.model.jsonmodel.Observation;
import net.oleksin.model.jsonmodel.Rate;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ObservationDeserializer implements JsonDeserializer<Observation> {
    private static final String D_FIELD = "d";
    private static final String V_FIELD = "v";

    @Override
    public Observation deserialize(JsonElement jsonElement,
                                   Type type,
                                   JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        Observation observation = new Observation();
        observation.setDate(LocalDate.parse(jsonObject.get(D_FIELD).getAsString()));
        Map<String, Rate> values = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (!entry.getKey().equals(D_FIELD)) {
                Rate rate = new Rate();
                rate.setCurrentRate(entry.getValue()
                        .getAsJsonObject()
                        .get(V_FIELD)
                        .getAsBigDecimal());
                values.put(entry.getKey(), rate);
            }
        }
        observation.setValue(values);
        return observation;
    }
}
