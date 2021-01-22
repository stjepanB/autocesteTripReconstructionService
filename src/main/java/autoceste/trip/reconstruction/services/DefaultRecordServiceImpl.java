package autoceste.trip.reconstruction.services;

import autoceste.trip.reconstruction.models.DefaultRecord;
import autoceste.trip.reconstruction.models.HighwaySection;
import autoceste.trip.reconstruction.models.HighwaySectionDto;
import autoceste.trip.reconstruction.models.Trip;
import autoceste.trip.reconstruction.repositories.DefaultRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DefaultRecordServiceImpl implements DefaultRecordService {

    private final DefaultRecordRepository recordRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final Map<String, List<DefaultRecord>> activeRecords;
    @Value("${backend.url}")
    private final String BACKEND_URL;

    @Autowired
    public DefaultRecordServiceImpl(DefaultRecordRepository recordRepository, SequenceGeneratorService sequenceGeneratorService,
                                    @Value("${backend.url}") String url) {
        this.BACKEND_URL = url;
        this.recordRepository = recordRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        activeRecords = Collections.synchronizedMap(new HashMap<>());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HighwaySectionDto[]> response =
                restTemplate.getForEntity(
                        BACKEND_URL + "/sections",
                        HighwaySectionDto[].class);

        List<String> sections = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(HighwaySectionDto::toHighwaySections)
                .map(HighwaySection::getSectionStart)
                .collect(Collectors.toList());

        List<List<String>> roads = new ArrayList<>();

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
        recordList.forEach(e -> activeRecords.get(e.getPlateMark()).add(e));
        return recordRepository.saveAll(recordList).get(0).getId() != null;
    }

    @Scheduled(cron = "0 0 4/1 * * *")
    public void reconstructTrips() {
        Set<String> keys = activeRecords.keySet();
        List<Trip> trips = new ArrayList<>();

        for (String key : keys) {
            List<DefaultRecord> records = activeRecords.get(key);

            LocalDateTime recent = findNewestRecord(records);

            if (recent.isBefore(LocalDateTime.now().minusHours(12L))) {
                trips.add(createTrip(records));
            }
        }
        if (trips.isEmpty()) {
            return;
        }
        saveTrips(trips);
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

    private Trip createTrip(List<DefaultRecord> records) {
        Collections.sort(records);

        for (DefaultRecord r : records) {
            int i = 0;

        }
        return new Trip(records.get(0), records.get(records.size() - 1));
    }

    private boolean saveTrips(List<Trip> trips) {
        RestTemplate restTemplate = new RestTemplate();

        var response = restTemplate.postForObject(
                BACKEND_URL + "/billing", trips, ResponseEntity.class);

        return response != null && response.getStatusCode() == HttpStatus.OK;
    }

    private void removeReconstructedTrips(List<Trip> trips) {
        for (Trip t : trips) {
            activeRecords.remove(t.getPlateMark());
        }
    }
}