package autoceste.trip.reconstruction.repositories;

import autoceste.trip.reconstruction.models.DefaultRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DefaultRecordRepository extends MongoRepository<DefaultRecord, Long> {
}
