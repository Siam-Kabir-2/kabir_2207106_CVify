package com.example.learning;

import java.util.List;

public class dataSample {
    private int id;
    private String fullName;
    private String address;
    private String phone;
    private String email;
    private String imagePath;
    private List<Education> educationList;
    private List<WorkExperience> experienceList;
    private List<String> skillList;
    private List<String> projectList;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public List<Education> getEducationList() { return educationList; }
    public void setEducationList(List<Education> educationList) { this.educationList = educationList; }

    public List<WorkExperience> getExperienceList() { return experienceList; }
    public void setExperienceList(List<WorkExperience> experienceList) { this.experienceList = experienceList; }

    public List<String> getSkillList() { return skillList; }
    public void setSkillList(List<String> skillList) { this.skillList = skillList; }

    public List<String> getProjectList() { return projectList; }
    public void setProjectList(List<String> projectList) { this.projectList = projectList; }

    @Override
    public String toString() {
        return fullName + " (" + email + ")";
    }
}