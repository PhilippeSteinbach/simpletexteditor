package com.philippesteinbach.control;

import com.philippesteinbach.crypto.AESEncryption;

public class CryptoFactory {

    public AESEncryption getAESEncryption(String blockMode, String padding) {
        return new AESEncryption(blockMode, padding);
    }
}
