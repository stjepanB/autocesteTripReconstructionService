package autoceste.trip.reconstruction.services;

import autoceste.trip.reconstruction.models.DefaultRecord;
import autoceste.trip.reconstruction.repositories.DefaultRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultRecordServiceImpl implements DefaultRecordService {

    private DefaultRecordRepository recordRepository;
    private SequenceGeneratorService sequenceGeneratorService;

    @Autowired
    public DefaultRecordServiceImpl(DefaultRecordRepository recordRepository, SequenceGeneratorService sequenceGeneratorService) {
        this.recordRepository = recordRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
    }

    @Override
    public boolean addRecord(DefaultRecord record) {
        record.setId(sequenceGeneratorService.generateSequence(DefaultRecord.SEQUENCE_NAME));
        return recordRepository.save(record).getId() != null;
    }

    @Override
    public boolean addRecords(List<DefaultRecord> recordList) {
        recordList.forEach(e -> e.setId(sequenceGeneratorService.generateSequence(DefaultRecord.SEQUENCE_NAME)));
        return recordRepository.saveAll(recordList).get(0).getId() != null;
    }
}
