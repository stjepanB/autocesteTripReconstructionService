package autoceste.trip.reconstruction.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "vehicles")
public class VehicleRecords {

    @Transient
    public static final String SEQUENCE_NAME = "vehicle_records_sequence";

    @Id
    private Long id;

    private List<DefaultRecord> records;

    private String plate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DefaultRecord> getRecords() {
        return records;
    }

    public void setRecords(List<DefaultRecord> records) {
        this.records = records;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}
