package autoceste.trip.reconstruction.controllers;

import autoceste.trip.reconstruction.models.DefaultRecord;
import autoceste.trip.reconstruction.services.DefaultRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DefaultRecordController {

    private DefaultRecordService recordService;

    @Autowired
    public DefaultRecordController(DefaultRecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/record")
    public HttpStatus addRecord(@RequestBody DefaultRecord record) {
        return recordService.addRecord(record) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    @PostMapping("/records")
    public HttpStatus addRecords(@RequestBody List<DefaultRecord> recordList) {
        return recordService.addRecords(recordList) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }
}
