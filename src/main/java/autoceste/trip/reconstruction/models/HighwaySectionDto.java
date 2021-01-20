package autoceste.trip.reconstruction.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class HighwaySectionDto {

    @JsonProperty
    private Long key;
    @JsonProperty
    private String section;
    @JsonProperty(value = "IA")
    private HighwayCategory IA;
    @JsonProperty(value = "I")
    private HighwayCategory I;
    @JsonProperty(value = "II")
    private HighwayCategory II;
    @JsonProperty(value = "III")
    private HighwayCategory III;
    @JsonProperty(value = "IV")
    private HighwayCategory IV;

    public HighwaySectionDto(HighwaySection h) {
        this.key = h.getId();
        this.section = h.getSectionStart() + " - " + h.getSectionEnd();
        this.IA = new HighwayCategory(h.getInfrastructureCostIA(), h.getOutsideCostIA());
        this.I = new HighwayCategory(h.getInfrastructureCostI(), h.getOutsideCostI());
        this.II = new HighwayCategory(h.getInfrastructureCostII(), h.getOutsideCostII());
        this.III = new HighwayCategory(h.getInfrastructureCostIII(), h.getOutsideCostIII());
        this.IV = new HighwayCategory(h.getInfrastructureCostIV(), h.getOutsideCostIV());
    }

    public HighwaySectionDto() {
    }

    public HighwaySection toHighwaySections() {
        HighwaySection h = new HighwaySection();
        h.setId(this.key);
        String[] tmp = section.split(" - ");

        h.setSectionStart(tmp[0]);
        h.setSectionEnd(tmp[1]);

        h.setOutsideCostIA(IA.getOutside());
        h.setInfrastructureCostIA(IA.getInfrastructure());

        h.setOutsideCostI(I.getOutside());
        h.setInfrastructureCostI(I.getInfrastructure());

        h.setOutsideCostII(II.getOutside());
        h.setInfrastructureCostII(II.getInfrastructure());

        h.setOutsideCostIII(III.getOutside());
        h.setInfrastructureCostIII(III.getInfrastructure());

        h.setOutsideCostIV(IV.getOutside());
        h.setInfrastructureCostIV(IV.getInfrastructure());

        return h;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public HighwayCategory getIA() {
        return IA;
    }

    public void setIA(HighwayCategory IA) {
        this.IA = IA;
    }

    public HighwayCategory getI() {
        return I;
    }

    public void setI(HighwayCategory i) {
        I = i;
    }

    public HighwayCategory getII() {
        return II;
    }

    public void setII(HighwayCategory II) {
        this.II = II;
    }

    public HighwayCategory getIII() {
        return III;
    }

    public void setIII(HighwayCategory III) {
        this.III = III;
    }

    public HighwayCategory getIV() {
        return IV;
    }

    public void setIV(HighwayCategory IV) {
        this.IV = IV;
    }
}

