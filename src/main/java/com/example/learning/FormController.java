package com.example.learning;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
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

public class FormController {

    private final DatabaseService dbService = new DatabaseService();
    private Integer currentCvId = null;
    private boolean isEditMode = false;

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

    @FXML
    private Image uploadedImage;

    private String imagePath;
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
            imagePath = file.getAbsolutePath();
            image.setText("Uploaded");
        }
    }

    @FXML
    private Button saveBtn;

    @FXML
    public void initialize() {
        DB.initDatabase();
    }

    @FXML
    public void onSave() {
        if (name.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Please enter a name");
            return;
        }

        saveBtn.setDisable(true);

        if (currentCvId == null) {
            dbService.saveCvAsync(
                    name.getText(),
                    address.getText(),
                    phone.getText(),
                    email.getText(),
                    imagePath,
                    new ArrayList<>(eduList),
                    new ArrayList<>(expList),
                    new ArrayList<>(skillList),
                    new ArrayList<>(projectList),
                    cvId -> {
                        currentCvId = cvId;
                        saveBtn.setDisable(false);
                        showAlert("Success", "CV saved successfully! ID: " + cvId);
                        try {
                            openPreview();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    },
                    ex -> {
                        saveBtn.setDisable(false);
                        showAlert("Error", "Failed to save CV: " + ex.getMessage());
                        ex.printStackTrace();
                    }
            );
        } else {
            dbService.updateCvAsync(
                    currentCvId,
                    name.getText(),
                    address.getText(),
                    phone.getText(),
                    email.getText(),
                    imagePath,
                    new ArrayList<>(eduList),
                    new ArrayList<>(expList),
                    new ArrayList<>(skillList),
                    new ArrayList<>(projectList),
                    () -> {
                        saveBtn.setDisable(false);
                        showAlert("Success", "CV updated successfully!");
                        try {
                            goToHomeView();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    },
                    ex -> {
                        saveBtn.setDisable(false);
                        showAlert("Error", "Failed to update CV: " + ex.getMessage());
                        ex.printStackTrace();
                    }
            );
        }
    }

    public void loadCvForEdit(dataSample cv) {
        currentCvId = cv.getId();
        isEditMode = true;

        clearFormUI();

        dbService.loadCvByIdAsync(
                cv.getId(),
                fullCv -> {
                    name.setText(fullCv.getFullName());
                    address.setText(fullCv.getAddress());
                    phone.setText(fullCv.getPhone());
                    email.setText(fullCv.getEmail());

                    if (fullCv.getImagePath() != null && !fullCv.getImagePath().isEmpty()) {
                        File imgFile = new File(fullCv.getImagePath());
                        if (imgFile.exists()) {
                            uploadedImage = new Image(imgFile.toURI().toString());
                            imagePath = fullCv.getImagePath();
                            image.setText("Uploaded");
                        }
                    }

                    eduList.clear();
                    eduList.addAll(fullCv.getEducationList());
                    for (Education edu : eduList) {
                        Label label = new Label(edu.getExam() + " - " + edu.getInstitute() +
                                " (" + edu.getPassingYear() + ", GPA: " + edu.getCg() + ")");
                        eduQualifications.getChildren().add(label);
                    }

                    expList.clear();
                    expList.addAll(fullCv.getExperienceList());
                    for (WorkExperience exp : expList) {
                        Label label = new Label(exp.getPosition() + " - " + exp.getCompany() +
                                " (" + exp.getDuration() + ")");
                        workExp.getChildren().add(label);
                    }

                    skillList.clear();
                    skillList.addAll(fullCv.getSkillList());
                    for (String skill : skillList) {
                        Label label = new Label(skill);
                        skillBox.getChildren().add(label);
                        Separator separator = new Separator();
                        separator.setOrientation(Orientation.VERTICAL);
                        skillBox.getChildren().add(separator);
                    }

                    projectList.clear();
                    projectList.addAll(fullCv.getProjectList());
                    for (String proj : projectList) {
                        Label label = new Label(proj);
                        projectBox.getChildren().add(label);
                        Separator separator = new Separator();
                        separator.setOrientation(Orientation.VERTICAL);
                        projectBox.getChildren().add(separator);
                    }
                },
                ex -> {
                    showAlert("Error", "Failed to load CV: " + ex.getMessage());
                    ex.printStackTrace();
                }
        );
    }

    private void openPreview() throws IOException {
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

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void goToHomeView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void clearFormUI() {
        eduQualifications.getChildren().clear();
        workExp.getChildren().clear();
        skillBox.getChildren().clear();
        projectBox.getChildren().clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private Button backBtn;
    public void onBackBtn() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
