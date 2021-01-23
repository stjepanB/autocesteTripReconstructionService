package autoceste.trip.reconstruction.models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Trip {

    private String plateMark;
    private LocalDateTime recordedTimeEntry;
    private LocalDateTime recordedTimeExit;
    private String locationEntry;
    private String locationExit;
    private List<String> locations;
    private Direction direction;

    public Trip(List<DefaultRecord> records) {
        DefaultRecord entry = records.get(0);
        DefaultRecord exit = records.get(records.size() - 1);
        this.locations = records.stream()
                .map(DefaultRecord::getLocation)
                .collect(Collectors.toList());
        this.plateMark = entry.getPlateMark();
        this.recordedTimeEntry = entry.getRecordedTime();
        this.recordedTimeExit = exit.getRecordedTime();
        this.locationEntry = entry.getLocation();
        this.locationExit = exit.getLocation();
        this.direction = exit.getDirection();
    }

    public Trip(List<DefaultRecord> records, List<String> reconstructed) {
        DefaultRecord entry = records.get(0);
        DefaultRecord exit = records.get(records.size() - 1);
        this.locations = reconstructed;
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

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }
}
