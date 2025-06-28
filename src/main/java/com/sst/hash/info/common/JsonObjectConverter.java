package com.sst.hash.info.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sst.hash.info.model.EncryptedInfoModel;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class JsonObjectConverter implements AttributeConverter<EncryptedInfoModel, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(EncryptedInfoModel attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert Map to JSON String", e);
        }
    }

    @Override
    public EncryptedInfoModel convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, EncryptedInfoModel.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot deserialize JSON to EncryptedInfoModel", e);
        }
    }
}

