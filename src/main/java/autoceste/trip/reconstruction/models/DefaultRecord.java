package autoceste.trip.reconstruction.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.lang.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Document(collection = "records")
public class DefaultRecord implements Comparable<DefaultRecord> {

    @Transient
    public static final String SEQUENCE_NAME = "records_sequence";

    @Id
    private Long id;
    @NonNull
    private LocalDateTime recordedTime;
    @NonNull
    private String plateMark;
    @NonNull
    private String location;

    public DefaultRecord() {
    }

    public DefaultRecord(@NonNull LocalDateTime recordedTime, @NonNull String plateMark, @NonNull String location) {
        this.recordedTime = recordedTime;
        this.plateMark = plateMark;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public LocalDateTime getRecordedTime() {
        return recordedTime;
    }

    public void setRecordedTime(@NonNull LocalDateTime recordedTime) {
        this.recordedTime = recordedTime;
    }

    @NonNull
    public String getPlateMark() {
        return plateMark;
    }

    public void setPlateMark(@NonNull String plateMark) {
        this.plateMark = plateMark;
    }

    @NonNull
    public String getLocation() {
        return location;
    }

    public void setLocation(@NonNull String location) {
        this.location = location;
    }

    @Override
    public int compareTo(DefaultRecord o) {
        return this.getRecordedTime().compareTo(o.getRecordedTime());
    }
}
