package com.sst.hash.info.entity;

import com.sst.hash.info.common.JsonObjectConverter;
import com.sst.hash.info.model.EncryptedInfoModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "hashed_data", schema = "public")
public class HashedDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hashed_passport")
    private String hashedPassport;

    @Column(name = "encrypted_passport", columnDefinition = "jsonb")
    @Convert(converter = JsonObjectConverter.class)
    @org.hibernate.annotations.ColumnTransformer(write = "?::jsonb")
    private EncryptedInfoModel encryptedPassport;
}
