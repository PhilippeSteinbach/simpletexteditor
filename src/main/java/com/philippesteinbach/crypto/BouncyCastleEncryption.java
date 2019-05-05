package com.philippesteinbach.crypto;

public interface BouncyCastleEncryption{
    byte[] encrypt(byte[] plainText);
    byte[] decrypt(byte[] cipherText);
}
