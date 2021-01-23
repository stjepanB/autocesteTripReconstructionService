package autoceste.trip.reconstruction.services;

import autoceste.trip.reconstruction.models.Trip;

import java.util.List;

public interface CommunicationService {

    List<String> getSections();

    boolean saveTrips(List<Trip> trips);
}
