package com.example.learning;

import javafx.application.Platform;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DatabaseService {
    private final DB db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DatabaseService() {
        this.db = new DB();
        DB.initDatabase();
    }

    public void saveCvAsync(String name, String address, String phone, String email,
                           String imagePath, List<Education> eduList,
                           List<WorkExperience> expList, List<String> skills,
                           List<String> projects,
                           Consumer<Integer> onSuccess,
                           Consumer<Throwable> onError) {
        
        executor.submit(() -> {
            try {
                int cvId = db.insertCvify(name, address, phone, email, imagePath, 
                                         eduList, expList, skills, projects);
                Platform.runLater(() -> onSuccess.accept(cvId));
            } catch (Throwable ex) {
                Platform.runLater(() -> onError.accept(ex));
            }
        });
    }

    public void updateCvAsync(int id, String name, String address, String phone,
                             String email, String imagePath, List<Education> eduList,
                             List<WorkExperience> expList, List<String> skills,
                             List<String> projects,
                             Runnable onSuccess,
                             Consumer<Throwable> onError) {
        
        executor.submit(() -> {
            try {
                db.updateResume(id, name, address, phone, email, imagePath,
                              eduList, expList, skills, projects);
                Platform.runLater(onSuccess);
            } catch (Throwable ex) {
                Platform.runLater(() -> onError.accept(ex));
            }
        });
    }

    public void loadAllCvsAsync(Consumer<List<dataSample>> onSuccess,
                               Consumer<Throwable> onError) {
        
        executor.submit(() -> {
            try {
                List<dataSample> cvList = db.getAllCV();
                Platform.runLater(() -> onSuccess.accept(cvList));
            } catch (Throwable ex) {
                Platform.runLater(() -> onError.accept(ex));
            }
        });
    }

    public void loadCvByIdAsync(int id,
                               Consumer<dataSample> onSuccess,
                               Consumer<Throwable> onError) {
        
        executor.submit(() -> {
            try {
                dataSample cv = db.getCVById(id);
                Platform.runLater(() -> onSuccess.accept(cv));
            } catch (Throwable ex) {
                Platform.runLater(() -> onError.accept(ex));
            }
        });
    }

    public void deleteCvAsync(int id,
                             Runnable onSuccess,
                             Consumer<Throwable> onError) {
        
        executor.submit(() -> {
            try {
                db.deleteResume(id);
                Platform.runLater(onSuccess);
            } catch (Throwable ex) {
                Platform.runLater(() -> onError.accept(ex));
            }
        });
    }

    public void shutdown() {
        executor.shutdown();
    }
}
