package autoceste.trip.reconstruction.repositories;

import autoceste.trip.reconstruction.models.VehicleRecords;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleRecordsRepository extends MongoRepository<VehicleRecords, Long> {

    VehicleRecords findByPlate(String plate);
}
