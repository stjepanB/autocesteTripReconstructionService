package autoceste.trip.reconstruction.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HighwayCategory {

    @JsonProperty
    private Double infrastructure;
    @JsonProperty
    private Double outside;

    public HighwayCategory(Double infrastructure, Double outside) {
        this.infrastructure = infrastructure;
        this.outside = outside;
    }

    public HighwayCategory() {
    }

    public Double getInfrastructure() {
        return infrastructure;
    }

    public void setInfrastructure(Double infrastructure) {
        this.infrastructure = infrastructure;
    }

    public Double getOutside() {
        return outside;
    }

    public void setOutside(Double outside) {
        this.outside = outside;
    }
}
