package autoceste.trip.reconstruction.services;

import autoceste.trip.reconstruction.models.HighwaySection;
import autoceste.trip.reconstruction.models.HighwaySectionDto;
import autoceste.trip.reconstruction.models.Trip;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommunicationInterfaceImpl implements CommunicationService {

    private final String BACKEND_URL;

    public CommunicationInterfaceImpl(@Value("${backend.url}") String BACKEND_URL) {
        this.BACKEND_URL = BACKEND_URL;
    }

    public List<String> getSections() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<HighwaySectionDto[]> response =
                restTemplate.getForEntity(
                        BACKEND_URL + "/sections",
                        HighwaySectionDto[].class);

        List<String> sections = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(HighwaySectionDto::toHighwaySections)
                .map(HighwaySection::getSectionStart)
                .collect(Collectors.toList());

        return sections;
    }

    public boolean saveTrips(List<Trip> trips) {
        RestTemplate restTemplate = new RestTemplate();

        var response = restTemplate.postForObject(
                BACKEND_URL + "/billing", trips, ResponseEntity.class);

        return response != null && response.getStatusCode() == HttpStatus.OK;
    }
}
