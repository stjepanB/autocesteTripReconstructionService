package autoceste.trip.reconstruction.services;

import autoceste.trip.reconstruction.models.DefaultRecord;

import java.util.List;

public interface DefaultRecordService {

    boolean addRecord(DefaultRecord record);

    boolean addRecords(List<DefaultRecord> recordList);
}
