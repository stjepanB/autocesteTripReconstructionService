package autoceste.trip.reconstruction.service;

import autoceste.trip.reconstruction.models.DefaultRecord;
import autoceste.trip.reconstruction.repositories.DefaultRecordRepository;
import autoceste.trip.reconstruction.repositories.VehicleRecordsRepository;
import autoceste.trip.reconstruction.services.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class DefaultRecordServiceTest {

    private DefaultRecordServiceImpl defaultRecordService;
    private List<DefaultRecord> backward;
    private List<String> backward_result;
    private List<DefaultRecord> forward;
    private List<String> forward_result;
    private List<DefaultRecord> discRoad;
    private List<String> discRoad_result;
    private List<List<String>> roads;
    private List<String> sections;

    @MockBean
    private DefaultRecordRepository repository;

    @MockBean
    private VehicleRecordsRepository vehicleRepository;

    @MockBean
    private SequenceGeneratorService sequenceGeneratorService;

    @MockBean
    private CommunicationService communicationService;

    @BeforeEach
    void setUp() {
        roads = Arrays.asList(
                Arrays.asList("Lučko", "Zdenčina", "Jastrebarsko", "Karlovac", "Novigrad",
                        "Bosiljevo", "Vrbovsko", "Ravna", "Delnice", "Vrata", "Oštrovica", "Rijeka"),
                Arrays.asList("Lučko", "Zdenčina", "Jastrebarsko", "Karlovac", "Novigrad", "Bosiljevo",
                        "Ogulin", "Brinje", "Žuta", "Otočac", "Perušić", "Gospić", "Gornja", "Sveti", "Maslenica",
                        "Posedarje", "Zadar Zapad", "Zadar Istok", "Benkovac", "Pirovac", "Skradin", "Šibenik",
                        "Vrpolje", "Prgomet", "Vučevica", "Dugopolje", "Bisko", "Blato", "Šestanovac", "Zagvozd",
                        "Ravča", "Vrgorac", "Karamatići")
        );
        this.sections = Arrays.asList(
                "Lučko", "Zdenčina", "Jastrebarsko", "Karlovac", "Novigrad",
                "Bosiljevo", "Vrbovsko", "Ravna", "Delnice", "Vrata", "Oštrovica", "Rijeka",
                "Ogulin", "Brinje", "Žuta", "Otočac", "Perušić", "Gospić", "Gornja", "Sveti", "Maslenica",
                "Posedarje", "Zadar Zapad", "Zadar Istok", "Benkovac", "Pirovac", "Skradin", "Šibenik",
                "Vrpolje", "Prgomet", "Vučevica", "Dugopolje", "Bisko", "Blato", "Šestanovac", "Zagvozd",
                "Ravča", "Vrgorac", "Karamatići"

        );

        backward = Arrays.asList(
                new DefaultRecord(
                        LocalDateTime.of(2020, 11, 11, 12, 12),
                        "ST1233NB",
                        "Jastrebarsko"),
                new DefaultRecord(
                        LocalDateTime.of(2020, 11, 11, 13, 0),
                        "ST1233NB",
                        "Zdenčina"),
                new DefaultRecord(
                        LocalDateTime.of(2020, 11, 11, 13, 20),
                        "ST1233NB",
                        "Lučko")
        );

        backward_result = Arrays.asList(
                "Jastrebarsko", "Zdenčina", "Lučko"
        );
        forward = Arrays.asList(
                new DefaultRecord(
                        LocalDateTime.of(2020, 9, 13, 10, 12),
                        "ZG1233NB",
                        "Lučko"),
                new DefaultRecord(
                        LocalDateTime.of(2020, 11, 11, 13, 0),
                        "ZG1233NB",
                        "Zdenčina"),
                new DefaultRecord(
                        LocalDateTime.of(2020, 11, 11, 13, 20),
                        "ZG1233NB",
                        "Novigrad"),
                new DefaultRecord(
                        LocalDateTime.of(2020, 11, 11, 13, 20),
                        "ZG1233NB",
                        "Bosiljevo")
        );

        forward_result = Arrays.asList(
                "Lučko", "Zdenčina", "Jastrebarsko", "Karlovac", "Novigrad", "Bosiljevo"
        );
        discRoad = Arrays.asList(
                new DefaultRecord(
                        LocalDateTime.of(2020, 10, 9, 9, 1),
                        "ZG1133NB",
                        "Zdenčina"),
                new DefaultRecord(
                        LocalDateTime.of(2020, 10, 9, 9, 20),
                        "ZG1133NB",
                        "Karlovac"),
                new DefaultRecord(
                        LocalDateTime.of(2020, 10, 9, 10, 20),
                        "ZG1133NB",
                        "Bosiljevo"),
                new DefaultRecord(
                        LocalDateTime.of(2020, 10, 9, 13, 20),
                        "ZG1133NB",
                        "Vrbovsko")
        );
        discRoad_result = Arrays.asList(
                "Zdenčina", "Jastrebarsko", "Karlovac", "Novigrad", "Bosiljevo", "Vrbovsko"
        );
        Map<String, List<DefaultRecord>> activeMap = Map.of("ST1233NB", backward, "ZG1233NB", forward, "ZG1133NB", discRoad);
        doReturn(this.sections).when(communicationService).getSections();
        defaultRecordService = new DefaultRecordServiceImpl(repository,
                vehicleRepository, sequenceGeneratorService, communicationService);
    }

    @Test
    @DisplayName("Basic functionality test")
    public void createTrip_test() {
        Assertions.assertArrayEquals(forward_result.toArray(), defaultRecordService.createTrip(forward).getLocations().toArray());
    }

    @Test
    @DisplayName("Backward trip test")
    public void createBackwardTrip_test() {
        Assertions.assertArrayEquals(defaultRecordService.createTrip(backward).getLocations().toArray(), backward_result.toArray());
    }

    @Test
    @DisplayName("Multiple road possibilities test")
    public void createRoadTrip_test() {
        Assertions.assertArrayEquals(defaultRecordService.createTrip(discRoad).getLocations().toArray(), discRoad_result.toArray());
    }

}
