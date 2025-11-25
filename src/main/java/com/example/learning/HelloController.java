package com.example.learning;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class HelloController {

    private final DatabaseService dbService = new DatabaseService();

    @FXML
    private Button createBtn;
    @FXML
    private ListView<dataSample> cvListView;
    @FXML
    private Button viewBtn;
    @FXML
    private Button editBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button refreshBtn;

    @FXML
    public void initialize() {
        loadAllCvs();

        viewBtn.setDisable(true);
        editBtn.setDisable(true);
        deleteBtn.setDisable(true);

        cvListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean hasSelection = newVal != null;
            viewBtn.setDisable(!hasSelection);
            editBtn.setDisable(!hasSelection);
            deleteBtn.setDisable(!hasSelection);
        });
    }

    private void loadAllCvs() {
        dbService.loadAllCvsAsync(
                cvList -> {
                    cvListView.getItems().clear();
                    cvListView.getItems().addAll(cvList);
                },
                ex -> {
                    showAlert("Error", "Failed to load CVs: " + ex.getMessage());
                    ex.printStackTrace();
                }
        );
    }

    @FXML
    public void onRefresh() {
        loadAllCvs();
    }

    @FXML
    public void onCreateBtnClk() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("form-view.fxml"));
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) createBtn.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onView() {
        dataSample selectedCv = cvListView.getSelectionModel().getSelectedItem();
        if (selectedCv == null) {
            showAlert("No Selection", "Please select a CV to view");
            return;
        }

        viewBtn.setDisable(true);

        dbService.loadCvByIdAsync(
                selectedCv.getId(),
                fullCv -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("preview.fxml"));
                        Scene scene = new Scene(loader.load());

                        PreviewController previewController = loader.getController();

                        previewController.setFullName(fullCv.getFullName());
                        previewController.setAddress(fullCv.getAddress());
                        previewController.setPhone(fullCv.getPhone());
                        previewController.setEmail(fullCv.getEmail());

                        previewController.setEducationList(fullCv.getEducationList());
                        previewController.setExperienceList(fullCv.getExperienceList());
                        previewController.setSkillList(fullCv.getSkillList());
                        previewController.setProjectList(fullCv.getProjectList());

                        if (fullCv.getImagePath() != null && !fullCv.getImagePath().isEmpty()) {
                            File imgFile = new File(fullCv.getImagePath());
                            if (imgFile.exists()) {
                                previewController.setProfileImage(new javafx.scene.image.Image(imgFile.toURI().toString()));
                            }
                        }

                        Stage stage = (Stage) viewBtn.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
                        viewBtn.setDisable(false);
                        showAlert("Error", "Failed to open preview: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                ex -> {
                    viewBtn.setDisable(false);
                    showAlert("Error", "Failed to load CV data: " + ex.getMessage());
                    ex.printStackTrace();
                }
        );
    }

    @FXML
    public void onEdit() {
        dataSample selectedCv = cvListView.getSelectionModel().getSelectedItem();
        if (selectedCv == null) {
            showAlert("No Selection", "Please select a CV to edit");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("form-view.fxml"));
            Scene scene = new Scene(loader.load());

            FormController formController = loader.getController();
            formController.loadCvForEdit(selectedCv);

            Stage stage = (Stage) editBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            showAlert("Error", "Failed to open edit form: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void onDelete() {
        dataSample selectedCv = cvListView.getSelectionModel().getSelectedItem();
        if (selectedCv == null) {
            showAlert("No Selection", "Please select a CV to delete");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete CV");
        confirmation.setHeaderText("Are you sure you want to delete this CV?");
        confirmation.setContentText(selectedCv.getFullName() + " (" + selectedCv.getEmail() + ")");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteBtn.setDisable(true);

            dbService.deleteCvAsync(
                    selectedCv.getId(),
                    () -> {
                        deleteBtn.setDisable(false);
                        showAlert("Success", "CV deleted successfully!");
                        loadAllCvs();
                    },
                    ex -> {
                        deleteBtn.setDisable(false);
                        showAlert("Error", "Failed to delete CV: " + ex.getMessage());
                        ex.printStackTrace();
                    }
            );
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
