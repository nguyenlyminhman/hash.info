package com.sst.hash.info.common;

import com.sst.hash.info.model.EncryptedInfoModel;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;

public class CryptoUtils {

    private static final String AES = "AES";
    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 16;  // bytes (128 bits)
    private static final int IV_LENGTH = 12;       // bytes (96 bits) — recommended for GCM

    private static SecretKey getSecretKey(String secretKey) {
        if (Objects.isNull(secretKey) || secretKey.length() != 64) {
            throw new IllegalArgumentException("AES_SECRET_KEY must be a 64-char hex string (32 bytes)");
        }
        byte[] keyBytes = hexStringToByteArray(secretKey);
        return new SecretKeySpec(keyBytes, AES);
    }

    public EncryptedInfoModel encrypt(String plainText, String secretKey) throws Exception {
        byte[] iv = new byte[IV_LENGTH];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv); // in bits
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(secretKey), spec);

        byte[] cipherTextWithTag = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        // Tách cipher text và auth tag
        int cipherTextLength = cipherTextWithTag.length - GCM_TAG_LENGTH;
        byte[] cipherText = new byte[cipherTextLength];
        byte[] authTag = new byte[GCM_TAG_LENGTH];
        System.arraycopy(cipherTextWithTag, 0, cipherText, 0, cipherTextLength);
        System.arraycopy(cipherTextWithTag, cipherTextLength, authTag, 0, GCM_TAG_LENGTH);

        return new EncryptedInfoModel(
                Base64.getEncoder().encodeToString(iv),
                Base64.getEncoder().encodeToString(authTag),
                Base64.getEncoder().encodeToString(cipherText)
        );
    }

    public static String decrypt(EncryptedInfoModel data, String secretKey) throws Exception {
        byte[] cipherText = Base64.getDecoder().decode(data.getEncryptedData());
        byte[] iv = Base64.getDecoder().decode(data.getIv());
        byte[] authTag = Base64.getDecoder().decode(data.getAuthTag());

        // Ghép lại cipherText + authTag
        ByteBuffer buffer = ByteBuffer.allocate(cipherText.length + authTag.length);
        buffer.put(cipherText);
        buffer.put(authTag);
        byte[] cipherTextWithTag = buffer.array();

        Cipher cipher = Cipher.getInstance(AES_GCM);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(secretKey), spec);

        byte[] decryptedBytes = cipher.doFinal(cipherTextWithTag);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static String hashSha256(String input) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    // ---------- Helper Classes & Methods ----------


    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        return d;
    }
}
