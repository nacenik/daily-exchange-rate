package net.oleksin.parser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.oleksin.deserializer.ObservationDeserializer;
import net.oleksin.model.jsonmodel.Observation;
import net.oleksin.model.jsonmodel.ObservationsBySeriesData;
import net.oleksin.parser.Parser;

public class JsonParser implements Parser<String, ObservationsBySeriesData> {
    private final Gson gson;

    public JsonParser() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Observation.class, new ObservationDeserializer())
                .create();
    }

    @Override
    public ObservationsBySeriesData parse(String json) {
        return gson.fromJson(json, ObservationsBySeriesData.class);
    }
}
