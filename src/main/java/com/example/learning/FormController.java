package com.example.learning;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.separator;

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
    private TextField exam;
    @FXML
    private TextField institute;
    @FXML
    private TextField passingYear;
    @FXML
    private TextField cg;
    @FXML
    private VBox eduQualifications;

    private final List<Education> eduList=new ArrayList<>();
    @FXML
    private void onAddEduQ() {
        Education edu=new Education(
                exam.getText(),
                institute.getText(),
                passingYear.getText(),
                cg.getText()
        );
        eduList.add(edu);
        exam.clear();
        institute.clear();
        passingYear.clear();
        cg.clear();
        Label label = new Label(
                edu.getExam() + " - " + edu.getInstitute() +
                        " (" + edu.getPassingYear() + ", GPA: " + edu.getCg() + ")"
        );
        eduQualifications.getChildren().add(label);
    }


    @FXML
    private TextField company;
    @FXML
    private TextField position;
    @FXML
    private TextField duration;
    @FXML
    private VBox workExp;

    private final List<WorkExperience> expList=new ArrayList<>();
    @FXML
    private void onAddExp() {
        WorkExperience exp=new WorkExperience(
                company.getText(),
        position.getText(),
        duration.getText()
                );
        expList.add(exp);
        position.clear();
        company.clear();
        duration.clear();
        Label label = new Label(
                exp.getPosition() + " - " + exp.getCompany() +
                        " ( "+ exp.getDuration()+")"
        );
        workExp.getChildren().add(label);

    }
    @FXML
    private Button addSkills;
    @FXML
    private TextField skills;
    @FXML
    private HBox skillBox;

    private final List<String> skillList=new ArrayList<>();
    @FXML
    private void onAddSkills() {
        skillList.add(skills.getText());
        Label label = new Label(
                skills.getText()
        );
        skills.clear();
        skillBox.getChildren().add(label);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        skillBox.getChildren().add(separator);

    }

    @FXML
    private TextField project;
    @FXML
    private HBox projectBox;

    private final List<String> projectList=new ArrayList<>();
    @FXML
    private void onAddProjects() {
        projectList.add(project.getText());
        Label label = new Label(
                project.getText()
        );
        project.clear();
        projectBox.getChildren().add(label);
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        projectBox.getChildren().add(separator);

    }

    private Image uploadedImage;
    @FXML
    private Button image;
    @FXML
    private void onImageUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(image.getScene().getWindow());
        if (file != null) {
            Image img = new Image(file.toURI().toString());
            uploadedImage = img;
            image.setText("Uploaded");
        }
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

        previewController.setEducationList(eduList);
        previewController.setExperienceList(expList);
        previewController.setSkillList(skillList);
        previewController.setProjectList(projectList);

        if (uploadedImage != null) {
            previewController.setProfileImage(uploadedImage);
        }

        Stage stage = (Stage) previewBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


}
