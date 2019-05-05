package com.philippesteinbach.crypto;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.*;

/**
 * This class represents our Bouncy Castle AES Cipher .
 * Create an instance of this class for AES encrypting/decrypting byte[]
 */
public class AESEncryption implements BouncyCastleEncryption{

    private Cipher cipher;
    private SecretKey key;
    private byte[] iv;
    private String blockMode;

    /**
     * Constructor.
     * Gets an instance of BC and creates a key
     */
    public AESEncryption(String blockMode, String padding) {
        try {
            this.blockMode = blockMode;
            cipher = Cipher.getInstance("AES/" + blockMode + "/" + padding, "BC");

            if(blockMode.equals("CBC") || blockMode.equals("CTS")){
                this.iv = cipher.getIV();
                System.out.println("IV generated (CBC,CTS)");
            }

            createKey();
        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a key for AES BC which will be used both for encrypting and decrypting
     */
    public void createKey() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyGenerator kGen = KeyGenerator.getInstance("AES", "BC");
        this.key = kGen.generateKey();
    }

    /**
     * Receives a plain text, processes it and returns the encrypted cipher text
     * @param plainText The text to be encrypted
     * @return AES encrypted cipher text
     */
    public byte[] encrypt(byte[] plainText) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            System.out.println("encrypted with " + cipher.getAlgorithm());
            return cipher.doFinal(plainText);
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Receives a cipher text, processes it and returns the decrypted plain text
     * @param cipherText The encrypted content to be decrypted
     * @return decrypted plain text
     */
    public byte[] decrypt(byte[] cipherText) {
        try {
            if(blockMode.equals("CBC") || blockMode.equals("CTS")){
                AlgorithmParameters ivParams = cipher.getParameters();
                cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            System.out.println("decrypted with " + cipher.getAlgorithm());
            return cipher.doFinal(cipherText);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
