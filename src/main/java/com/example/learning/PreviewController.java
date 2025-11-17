package com.example.learning;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PreviewController {

    @FXML
    private Label nameP;
    @FXML
    private Label addressP;
    @FXML
    private Label phoneP;
    @FXML
    private Label emailP;

    public void setFullName(String name) {
        nameP.setText(name);
    }
    public void setAddress(String address) {
        addressP.setText(address);
    }
    public void setPhone(String phone) {
        phoneP.setText(phone);
    }
    public void setEmail(String email) {
        emailP.setText(email);
    }
}
