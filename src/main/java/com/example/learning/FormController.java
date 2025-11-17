package com.example.learning;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class FormController {

    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private TextField phone;
    @FXML
    private TextField email;
    @FXML
    private void onAddEduQ() {

    }
    @FXML
    private void onAddSkills() {

    }
    @FXML
    private void onSubmitBtn() {

    }

    @FXML
    private Button previewBtn;

    @FXML
    public void onPreviewBtn() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("preview.fxml"));
        Scene scene = new Scene(loader.load());

        PreviewController previewController = loader.getController();

        previewController.setFullName(name.getText());
        previewController.setAddress(address.getText());
        previewController.setPhone(phone.getText());
        previewController.setEmail(email.getText());

        Stage stage = (Stage) previewBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
