package net.oleksin.model.jsonmodel;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ObservationsBySeriesData {
    private Terms terms;
    private Map<String, SeriesDetail> seriesDetail;
    @SerializedName("observations")
    private List<Observation> observations;
}
