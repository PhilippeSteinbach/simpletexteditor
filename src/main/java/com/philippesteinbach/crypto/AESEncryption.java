package com.philippesteinbach.crypto;

import javafx.scene.control.Alert;

import javax.crypto.*;
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
            cipher = Cipher.getInstance("AES/" + blockMode + "/" + padding, "BC");
            this.blockMode = blockMode;
            this.iv = cipher.getIV();
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
     *
     * @param plainText The text to be encrypted
     * @return AES encrypted cipher text
     */
    public byte[] encrypt(byte[] plainText) {
        try {

            if(blockMode.equals("ECB")){
                cipher.init(Cipher.ENCRYPT_MODE, key);
            } else {
                AlgorithmParameters ivParams = cipher.getParameters();
                cipher.init(Cipher.ENCRYPT_MODE, key, ivParams); // notwendig? JA! Ansonsten IV = 0 (=>ECB)
            }
            byte[] encrypted = cipher.doFinal(plainText);
            System.out.println("encrypted with " + cipher.getAlgorithm());
            System.out.println("plain text length  = " + plainText.length);
            System.out.println("cipher text length  = " + encrypted.length);
            return encrypted;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidAlgorithmParameterException e) {
            System.out.println(e.getMessage());

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occured. Please try again with different cipher configuration"); //TODO more concrete information about error (do not catch 4 exceptions at once)
            alert.showAndWait();

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
            if(blockMode.equals("ECB")){
                cipher.init(Cipher.DECRYPT_MODE, key);
            } else {
                AlgorithmParameters ivParams = cipher.getParameters();
                cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            }
            byte[] decrypted = cipher.doFinal(cipherText);
            System.out.println("decrypted with " + cipher.getAlgorithm());
            System.out.println("cipher text length  = " + cipherText.length);
            System.out.println("plain text length  = " + decrypted.length);
            return decrypted;
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            System.out.println(e.getMessage());
            //TODO Alert if exception thrown
            return null;
        }
    }
}
