package autoceste.trip.reconstruction.services;

import autoceste.trip.reconstruction.models.HighwaySection;
import autoceste.trip.reconstruction.models.HighwaySectionDto;
import autoceste.trip.reconstruction.models.Trip;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CommunicationInterfaceImpl implements CommunicationService {

    private final String BACKEND_URL;
    private ObjectMapper objectMapper;

    public CommunicationInterfaceImpl(@Value("${backend.url}") String BACKEND_URL, ObjectMapper objectMapper) {
        this.BACKEND_URL = BACKEND_URL;
        this.objectMapper = objectMapper;
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
        ResponseEntity<String> response;

        try {
            String userJsonList = objectMapper.writeValueAsString(trips);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(userJsonList, headers);
            restTemplate.postForEntity(BACKEND_URL + "/billing", entity, String.class);
            response = restTemplate.postForEntity(BACKEND_URL + "/billing", entity, String.class);

        } catch (RestClientException | JsonProcessingException e) {
            return false;
        }

        return response != null && response.getStatusCode() == HttpStatus.OK;
    }
}
