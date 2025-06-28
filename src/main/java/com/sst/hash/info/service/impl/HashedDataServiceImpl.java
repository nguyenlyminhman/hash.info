package com.sst.hash.info.service.impl;

import com.sst.hash.info.common.CryptoUtils;
import com.sst.hash.info.entity.HashedDataEntity;
import com.sst.hash.info.model.EncryptedInfoModel;
import com.sst.hash.info.model.HashedInfoModel;
import com.sst.hash.info.repository.HashedDataRepository;
import com.sst.hash.info.service.IHashedDataService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class HashedDataServiceImpl implements IHashedDataService {
    private static final Logger logger = Logger.getLogger(HashedDataServiceImpl.class.getName());

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HashedDataRepository hashedDataRepository;
    @Value("${aes.secret.key}")
    private String AES_SECRET_KEY;


    @Override
    public List<HashedDataEntity> findAll() {
        return hashedDataRepository.findAll();
    }

    @Override
    public HashedInfoModel hashPassport(String passport) {
        HashedDataEntity entity = new HashedDataEntity();
        HashedInfoModel hashedInfoModel = new HashedInfoModel();
        try {
            String hash = CryptoUtils.hashSha256(passport);
            EncryptedInfoModel encrypted = new CryptoUtils().encrypt(passport, AES_SECRET_KEY);

            entity.setHashedPassport(hash);
            entity.setEncryptedPassport(encrypted);

            hashedDataRepository.save(entity);

            hashedInfoModel = modelMapper.map(entity, HashedInfoModel.class);
            hashedInfoModel.setPassport(passport);

        } catch (Exception e) {
            logger.info("hashPassport has an error: " + e.getMessage());
            return null;
        }

        return hashedInfoModel;
    }

    @Override
    public HashedInfoModel getPassport(String passport) {
        HashedInfoModel hashedInfoModel = new HashedInfoModel();
        try {
            String hashedPassport = CryptoUtils.hashSha256(passport);
            HashedDataEntity hashedDataEntity = hashedDataRepository.findByHashedPassport(hashedPassport);
            if (hashedDataEntity == null) return hashedInfoModel;

            EncryptedInfoModel encryptedInfoModel = hashedDataEntity.getEncryptedPassport();
            String decryptedPassport = CryptoUtils.decrypt(encryptedInfoModel, AES_SECRET_KEY);

            hashedInfoModel = modelMapper.map(hashedDataEntity, HashedInfoModel.class);
            hashedInfoModel.setPassport(decryptedPassport);

            return hashedInfoModel;
        } catch (Exception e) {
            logger.info("getPassport has an error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public HashedInfoModel getPassportByHash(String hash) throws Exception {
        HashedInfoModel hashedInfoModel = new HashedInfoModel();
        try {
            HashedDataEntity hashedDataEntity = hashedDataRepository.findByHashedPassport(hash);
            if (hashedDataEntity == null) return hashedInfoModel;

            EncryptedInfoModel encryptedInfoModel = hashedDataEntity.getEncryptedPassport();
            String decryptedPassport = CryptoUtils.decrypt(encryptedInfoModel, AES_SECRET_KEY);

            hashedInfoModel = modelMapper.map(hashedDataEntity, HashedInfoModel.class);

            hashedInfoModel.setPassport(decryptedPassport);

            return hashedInfoModel;
        } catch (Exception e) {
            logger.info("getPassportByHash has an error: " + e.getMessage());
            return null;
        }
    }
}
