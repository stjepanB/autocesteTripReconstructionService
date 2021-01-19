package autoceste.trip.reconstruction.models;

public class Trip {

    private String plateMark;
    private DefaultRecord entry;
    private DefaultRecord exit;

    public Trip(String plateMark, DefaultRecord entry, DefaultRecord exit) {
        this.plateMark = plateMark;
        this.entry = entry;
        this.exit = exit;
    }

    public String getPlateMark() {
        return plateMark;
    }

    public void setPlateMark(String plateMark) {
        this.plateMark = plateMark;
    }

    public DefaultRecord getEntry() {
        return entry;
    }

    public void setEntry(DefaultRecord entry) {
        this.entry = entry;
    }

    public DefaultRecord getExit() {
        return exit;
    }

    public void setExit(DefaultRecord exit) {
        this.exit = exit;
    }
}
