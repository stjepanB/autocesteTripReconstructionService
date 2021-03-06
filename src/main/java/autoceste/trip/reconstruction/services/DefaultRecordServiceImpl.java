package autoceste.trip.reconstruction.services;

import autoceste.trip.reconstruction.models.DefaultRecord;
import autoceste.trip.reconstruction.models.Trip;
import autoceste.trip.reconstruction.models.VehicleRecords;
import autoceste.trip.reconstruction.repositories.DefaultRecordRepository;
import autoceste.trip.reconstruction.repositories.VehicleRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DefaultRecordServiceImpl implements DefaultRecordService {

    private final DefaultRecordRepository recordRepository;
    private final VehicleRecordsRepository vehicleRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final Map<String, List<DefaultRecord>> activeRecords;

    private final List<List<String>> roads;
    private final CommunicationService communicationService;

    @Autowired
    public DefaultRecordServiceImpl(DefaultRecordRepository recordRepository, VehicleRecordsRepository vehicleRepository, SequenceGeneratorService sequenceGeneratorService,
                                    CommunicationService communicationService) {
        this.recordRepository = recordRepository;
        this.vehicleRepository = vehicleRepository;
        this.communicationService = communicationService;
        this.sequenceGeneratorService = sequenceGeneratorService;
        activeRecords = Collections.synchronizedMap(new HashMap<>());

        List<String> sections = communicationService.getSections();

        roads = new ArrayList<>();

        roads.add(new ArrayList<>());
        boolean RIJEKA_DIRECTION = true;

        for (String s : sections) {

            if (RIJEKA_DIRECTION) {
                roads.get(0).add(s);
            } else {
                roads.get(1).add(s);
            }

            if (s.equals("Bosiljevo")) {
                roads.add(new ArrayList<>(roads.get(0)));
            }
            if (s.equals("Rijeka")) {
                RIJEKA_DIRECTION = false;
            }
        }
        roads.forEach(e -> e.forEach(System.out::println));

    }

    @Override
    public boolean addRecord(DefaultRecord record) {
        record.setId(sequenceGeneratorService.generateSequence(DefaultRecord.SEQUENCE_NAME));
        if (activeRecords.containsKey(record.getPlateMark())) {
            activeRecords.get(record.getPlateMark()).add(record);
        } else {
            activeRecords.put(record.getPlateMark(), new ArrayList<>());
            activeRecords.get(record.getPlateMark()).add(record);
        }
        return recordRepository.save(record).getId() != null;
    }

    @Override
    public boolean addRecords(List<DefaultRecord> recordList) {
        recordList.forEach(e -> e.setId(sequenceGeneratorService.generateSequence(DefaultRecord.SEQUENCE_NAME)));

        recordList.stream()
                .filter(e -> !activeRecords.containsKey(e.getPlateMark()))
                .forEach(e -> activeRecords.put(e.getPlateMark(), new ArrayList<>()));

        recordList.forEach(e -> activeRecords.get(e.getPlateMark()).add(e));
        recordList.forEach(this::addRecordToVehicle);
        return recordRepository.saveAll(recordList).get(0).getId() != null;
    }

    private void addRecordToVehicle(DefaultRecord record){
        VehicleRecords r = vehicleRepository.findByPlate(record.getPlateMark());
        if(r == null){
            r = new VehicleRecords();
            r.setPlate(record.getPlateMark());
            r.setId(sequenceGeneratorService.generateSequence(VehicleRecords.SEQUENCE_NAME));
            List<DefaultRecord> records = new ArrayList<>();
            records.add(record);
            r.setRecords(records);
        }else {
            r.getRecords().add(record);
        }
        vehicleRepository.save(r);
    }

    @Scheduled(cron = "0 0/2 0-23 * * *")
    public void reconstructTrips() {
        Set<String> keys = activeRecords.keySet();
        List<Trip> trips = new ArrayList<>();
        System.out.println(LocalDateTime.now());

        for (String key : keys) {
            List<DefaultRecord> records = activeRecords.get(key);

            LocalDateTime recent = findNewestRecord(records);
            if (recent.isBefore(LocalDateTime.now().minusHours(5L))) {
                trips.add(createTrip(records));
            }
        }

        if (trips.isEmpty()) {
            return;
        }

        if (communicationService.saveTrips(trips)) {
            removeReconstructedTrips(trips);
        } else {
            logUnsucessfulSavedTrips(trips);
        }
    }

    private void logUnsucessfulSavedTrips(List<Trip> trips) {
        trips.forEach(e -> System.out.println(e.toString()));
        removeReconstructedTrips(trips);
    }

    private LocalDateTime findNewestRecord(List<DefaultRecord> records) {
        LocalDateTime recent = records.get(0).getRecordedTime();

        for (DefaultRecord r : records) {
            if (r.getRecordedTime().isAfter(recent)) {
                recent = r.getRecordedTime();
            }
        }
        return recent;
    }

    public Trip createTrip(List<DefaultRecord> records) {
        Collections.sort(records);
        List<String> trip = new ArrayList<>();

        for (List<String> road : roads) {
            boolean wrongRoad = false;
            for (DefaultRecord r : records) {
                if (!road.contains(r.getLocation())) {
                    wrongRoad = true;
                    break;
                }
            }
            if (wrongRoad) {
                continue;
            }

            int index = road.indexOf(records.get(0).getLocation());
            int endIndex = road.indexOf(records.get(records.size() - 1).getLocation());

            if (index > endIndex) {
                for (int i = index; i >= endIndex; i--) {
                    trip.add(road.get(i));
                }
                return new Trip(records, trip);

            } else {
                for (int i = index; i <= endIndex; i++) {
                    trip.add(road.get(i));
                }
                return new Trip(records, trip);
            }
        }

        return new Trip(records, trip);
    }

    private void removeReconstructedTrips(List<Trip> trips) {
        for (Trip t : trips) {
            activeRecords.remove(t.getPlateMark());
        }
    }
}