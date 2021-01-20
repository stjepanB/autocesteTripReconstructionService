package autoceste.trip.reconstruction.models;

import com.mongodb.lang.NonNull;

import java.time.LocalDateTime;

public class Trip {

    private String plateMark;
    private LocalDateTime recordedTimeEntry;
    private LocalDateTime recordedTimeExit;
    private String locationEntry;
    private String locationExit;
    private Direction direction;

    public Trip(DefaultRecord entry, DefaultRecord exit) {
        this.plateMark = entry.getPlateMark();
        this.recordedTimeEntry = entry.getRecordedTime();
        this.recordedTimeExit = exit.getRecordedTime();
        this.locationEntry = entry.getLocation();
        this.locationExit = exit.getLocation();
        this.direction = exit.getDirection();
    }

    public String getPlateMark() {
        return plateMark;
    }

    public void setPlateMark(String plateMark) {
        this.plateMark = plateMark;
    }

    public LocalDateTime getRecordedTimeEntry() {
        return recordedTimeEntry;
    }

    public void setRecordedTimeEntry(LocalDateTime recordedTimeEntry) {
        this.recordedTimeEntry = recordedTimeEntry;
    }

    public LocalDateTime getRecordedTimeExit() {
        return recordedTimeExit;
    }

    public void setRecordedTimeExit(LocalDateTime recordedTimeExit) {
        this.recordedTimeExit = recordedTimeExit;
    }

    public String getLocationEntry() {
        return locationEntry;
    }

    public void setLocationEntry(String locationEntry) {
        this.locationEntry = locationEntry;
    }

    public String getLocationExit() {
        return locationExit;
    }

    public void setLocationExit(String locationExit) {
        this.locationExit = locationExit;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
