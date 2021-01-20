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
@PropertySource("classpath:application.properties")
public class DefaultRecordServiceImpl implements DefaultRecordService {

    private final DefaultRecordRepository recordRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final Map<String, List<DefaultRecord>> activeRecords;
    @Value("${backend.url}")
    private String BACKEND_URL = "http://localhost:9000/api/service";
    private List<HighwaySection> sections;

    @Autowired
    public DefaultRecordServiceImpl(DefaultRecordRepository recordRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.recordRepository = recordRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        activeRecords = Collections.synchronizedMap(new HashMap<>());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HighwaySectionDto[]> response =
                restTemplate.getForEntity(
                        BACKEND_URL + "/sections",
                        HighwaySectionDto[].class);

        this.sections = Arrays.stream(response.getBody())
                .map(HighwaySectionDto::toHighwaySections)
                .collect(Collectors.toList());

    }

    @Override
    public boolean addRecord(DefaultRecord record) {
        record.setId(sequenceGeneratorService.generateSequence(DefaultRecord.SEQUENCE_NAME));
        activeRecords.get(record.getPlateMark()).add(record);
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

            LocalDateTime recent = findNewstRecord(records);

            if (recent.isBefore(LocalDateTime.now().minusHours(12L))) {
                trips.add(createTrip(records));
            }
        }
        saveTrips(trips);
        removeReconstructedTrips(trips);

    }

    private LocalDateTime findNewstRecord(List<DefaultRecord> records) {
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
        return new Trip(records.get(0), records.get(records.size() - 1));
    }

    private boolean saveTrips(List<Trip> trips) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity response = restTemplate.postForObject(
                BACKEND_URL + "/bill", trips, ResponseEntity.class);

        return response != null && response.getStatusCode() == HttpStatus.OK;
    }

    private void removeReconstructedTrips(List<Trip> trips) {
        for (Trip t : trips) {
            activeRecords.remove(t.getPlateMark());
        }
    }
}