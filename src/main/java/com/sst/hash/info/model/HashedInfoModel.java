package com.sst.hash.info.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HashedInfoModel {
    private Long id;
    private String passport;
    private String hashedPassport;
    private EncryptedInfoModel encryptedPassport;
}
