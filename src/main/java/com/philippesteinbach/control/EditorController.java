package com.philippesteinbach.control;

import com.philippesteinbach.crypto.AESEncryption;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import com.philippesteinbach.model.EditorModel;
import com.philippesteinbach.model.IOResult;
import com.philippesteinbach.model.TextFile;
import org.bouncycastle.util.encoders.Hex;
import java.io.*;
import java.util.Arrays;

/**
 * This is the controller for our text editor which receives all user inputs and processes the requests.
 * The controller usually prepares objects with the users data and forwards it to the model.
 */
public class EditorController {

    @FXML
    private TextArea areaText;
    private TextFile currentTextFile;
    private EditorModel model;
    private CryptoFactory cryptoFactory;
    private AESEncryption enc;
    private String blockMode = "ECB";
    private String padding = "PKCS5Padding";

    @FXML
    ToggleGroup modeGroup;
    @FXML
    ToggleGroup paddingGroup;


    //Constructor
    public EditorController(EditorModel model) {
        this.model = model;
        this.cryptoFactory = new CryptoFactory();
    }

    //TODO
    /**
     * This method is called when the user wants to create a new file.
     *
     * To decide later: choose path when creating new file or later when saving?
     */
    @FXML
    protected void onNew() {
        areaText.clear();
        currentTextFile = null;
    }

    /**
     * This method is called when the user wants to save the current file to the current path.
     */
    @FXML
    protected void onSave() {
        if (currentTextFile != null) {
            TextFile textFile = new TextFile(currentTextFile.getFile(), Arrays.asList(areaText.getText().split("\n")));

            IOResult<TextFile> io = model.save(textFile); // just save to the current path

            if (!io.isOk() && !io.hasData()) {
                System.out.println("failed to save file " + textFile.getFile()); // better: Alert
            }
        } else {
            onSaveAs(); // otherwise forward to save as method when currentFile is null (=no path available)
        }
    }

    /**
     * This method is called when the user wants to save the current file to another path than the current file.
     */
    @FXML
    protected void onSaveAs() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            // construct file with chosen path
            TextFile textFile = new TextFile(file.toPath(), Arrays.asList(areaText.getText().split("\n")));
            currentTextFile = textFile;
            IOResult<TextFile> io = model.save(textFile); // just save to the current path

            if (!io.isOk() && !io.hasData()) {
                System.out.println("failed to save file " + textFile.getFile()); // better: Alert
            }
        }
    }

    /**
     * This method is called when the user wants to open a textfile from the hard drive.
     * The user can choose a file via OS filepicker.
     *
     * if io.isOk(), currentTextFile is set to the opened file
     * The values which isOk() checks have been set in class EditorModel.
     */
    @FXML
    protected void onOpen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("./"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            IOResult<TextFile> io = model.open(file.toPath()); // loads all lines of file and returns IOResult object with ok parameter = true, if the operation succeeded
            if (io.isOk() && io.hasData()) {
                currentTextFile = io.getData();
                areaText.clear();
                currentTextFile.getContent().forEach(line -> areaText.appendText(line + "\n"));
            } else {
                System.out.println("failed to open file " + file); // better: Alert
            }
        }
    }

    /**
     * This method is called when the user wants to close the application via file -> close
     */
    @FXML
    protected void onClose() {
        model.close();
    }

    /**
     * This method is called when the user wants to view the ""about" information via help -> about
     */
    @FXML
    protected void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("About");
        alert.setContentText("This is a simple text editor.");
        alert.show();
    }

    /**
     * This method is called when the user wants to encrypt a text file.
     * Reads text from the applications textarea and encrypts it.
     * Refresh textarea with Base64 encoded cipher text.
     */
    @FXML
    public void onEncrypt() {
        //enc = cryptoFactory.getAESEncryption("ECB", "PKCS5Padding");
        enc = cryptoFactory.getAESEncryption(blockMode, padding);
        String plainText = areaText.getText();
        byte[] encrypted = enc.encrypt(Hex.encode(plainText.getBytes()));

        //update textarea
        areaText.clear();
        String cipherText = new String(Hex.encode(encrypted));
        areaText.setText(cipherText);
    }

    /**
     * This method is called when the user wants to decrypt a text file.
     * Reads text from the applications textarea and decrypts it.
     * Refresh textarea with Base64 decoded plain text.
     */
    @FXML
    public void onDecrypt() {
        String cipherText = areaText.getText();
        //enc = cryptoFactory.getAESEncryption(blockMode, padding); // BadPaddingException: pad block corrupted because key corrupted/modified (solution: new enc only on change)
        byte[] decrypted = enc.decrypt(Hex.decode(cipherText.getBytes()));

        //update textarea
        areaText.clear();
        String plainText = new String(Hex.decode(decrypted));
        areaText.setText(plainText);
    }

    /**
     * This method is called when the user changes the blockmode via menu.
     * Sets local variable blockMode which is used for initialising the encryption in method onEncrypt.
     */
    @FXML
    public void setMode(){
        RadioMenuItem selectedRadioButton = (RadioMenuItem) modeGroup.getSelectedToggle();
        String selectedValue = selectedRadioButton.getText();
        System.out.println("blockmode set to " + selectedValue);
        this.blockMode = selectedValue;
    }

    /**
     * This method is called when the user changes the padding via menu.
     * Sets local variable padding which is used for initialising the encryption in method onEncrypt.
     */
    @FXML
    public void setPadding(){
        RadioMenuItem selectedRadioButton = (RadioMenuItem) paddingGroup.getSelectedToggle();
        String selectedValue = selectedRadioButton.getText();
        System.out.println("padding set to " + selectedValue);
        this.padding = selectedValue;
    }
}