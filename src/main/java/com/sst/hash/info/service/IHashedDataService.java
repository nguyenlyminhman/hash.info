package com.sst.hash.info.service;

import com.sst.hash.info.entity.HashedDataEntity;
import com.sst.hash.info.model.HashedInfoModel;

import java.util.List;

public interface IHashedDataService {

    List<HashedDataEntity> findAll();

    HashedInfoModel hashPassport(String passport);

    HashedInfoModel getPassport(String passport) throws Exception;

    HashedInfoModel getPassportByHash(String hash) throws Exception;
}
