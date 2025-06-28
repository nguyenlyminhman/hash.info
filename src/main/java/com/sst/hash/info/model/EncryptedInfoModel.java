package com.sst.hash.info.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EncryptedInfoModel {
    private String iv;
    private String authTag;
    private String encryptedData;
}
