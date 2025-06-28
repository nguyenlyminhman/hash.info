package com.sst.hash.info.repository;

import com.sst.hash.info.entity.HashedDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashedDataRepository extends JpaRepository<HashedDataEntity, Long> {
    HashedDataEntity findByHashedPassport(String hashedPassport);
}
