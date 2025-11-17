package com.example.learning;

public class WorkExperience {
    private final String company;
    private final String position;
    private final String duration;

    public WorkExperience(String company, String position, String duration) {
        this.company = company;
        this.position = position;
        this.duration = duration;
    }

    public String getPosition() { return position; }
    public String getDuration() { return duration; }
    public String getCompany() { return company; }

}
