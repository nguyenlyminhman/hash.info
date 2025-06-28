package com.sst.hash.info.controller;

import com.sst.hash.info.entity.HashedDataEntity;
import com.sst.hash.info.dto.PassportRequestDto;
import com.sst.hash.info.model.HashedInfoModel;
import com.sst.hash.info.service.IHashedDataService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HashedDataController {
    @Autowired
    private IHashedDataService iHashedDataService;

    @GetMapping("/list-all")
    public ResponseEntity<List<HashedDataEntity>> findAll() {
        List<HashedDataEntity> hashedDataEntityList = iHashedDataService.findAll();
        return ResponseEntity.ok(hashedDataEntityList) ;
    }

    @RequestMapping(value = "/hash", produces = "application/json;charset=utf-8", method = RequestMethod.POST)
    public ResponseEntity<HashedInfoModel> save(@RequestBody PassportRequestDto request) {
        HashedInfoModel rs = iHashedDataService.hashPassport(request.getPassport());
        return ResponseEntity.ok(rs);
    }

    @RequestMapping(value = "/find-by", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    public ResponseEntity<HashedInfoModel> getByHash(@RequestParam("hash") String hash) throws Exception {
        HashedInfoModel rs = iHashedDataService.getPassportByHash(hash);
        return ResponseEntity.ok(rs);
    }

    @RequestMapping(value = "/find-passport", produces = "application/json;charset=utf-8", method = RequestMethod.GET)
    public ResponseEntity<HashedInfoModel> getByPassport(@RequestParam("number") String number) throws Exception {
        HashedInfoModel rs = iHashedDataService.getPassport(number);
        return ResponseEntity.ok(rs);
    }

}