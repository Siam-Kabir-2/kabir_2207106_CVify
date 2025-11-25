package com.example.learning;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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

    @FXML
    private VBox eduBox;

    public void setEducationList(List<Education> list) {
        eduBox.getChildren().clear();

        for (Education edu : list) {
            VBox entry = new VBox();
            entry.getChildren().add(new Label("Exam: " + edu.getExam()));
            entry.getChildren().add(new Label("Institute: " + edu.getInstitute()));
            entry.getChildren().add(new Label("Year: " + edu.getPassingYear()));
            entry.getChildren().add(new Label("CGPA: " + edu.getCg()));
            entry.getChildren().add(new Separator());

            eduBox.getChildren().add(entry);
        }
    }

    @FXML
    private VBox expBox;
    public void setExperienceList(List<WorkExperience> list) {
        expBox.getChildren().clear();

        for (WorkExperience exp : list) {
            VBox entry = new VBox();
            entry.getChildren().add(new Label("Position: " + exp.getPosition()));
            entry.getChildren().add(new Label("Company: " + exp.getCompany()));
            entry.getChildren().add(new Label("Duration: " + exp.getDuration()));
            entry.getChildren().add(new Separator());

            expBox.getChildren().add(entry);
        }
    }

    @FXML
    private VBox skillBox;
    public void setSkillList(List<String> list) {
        skillBox.getChildren().clear();

        for (String skill : list) {
            skillBox.getChildren().add(new Label(skill));

        }
        skillBox.getChildren().add(new Separator());
    }

    @FXML
    private VBox projectBox;
    public void setProjectList(List<String> list) {
        projectBox.getChildren().clear();

        for (String project : list) {
            projectBox.getChildren().add(new Label(project));

        }
        projectBox.getChildren().add(new Separator());
    }

    @FXML
    private ImageView imageBox;
    public void setProfileImage(Image img) {
        imageBox.setImage(img);
    }

    @FXML
    private Button homeBtn;
    public void redirectHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) homeBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



}
