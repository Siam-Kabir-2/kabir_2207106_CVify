package com.example.learning;

public class Education {
    private final String exam;
    private final String institute;
    private final String passingYear;
    private final String cg;

    public Education(String exam, String institute, String passingYear, String cg) {
        this.exam = exam;
        this.institute = institute;
        this.passingYear = passingYear;
        this.cg = cg;
    }

    public String getExam() { return exam; }
    public String getInstitute() { return institute; }
    public String getPassingYear() { return passingYear; }
    public String getCg() { return cg; }

}
