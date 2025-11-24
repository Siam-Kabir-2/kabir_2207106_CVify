package com.example.learning;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {
    private static final String URL = "jdbc:sqlite:cvify.db";


    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS cvify" +
                    " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "full_name TEXT NOT NULL," +
                    "address TEXT," +
                    "phone TEXT," +
                    "email TEXT," +
                    "image_path TEXT," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");

            stmt.execute("CREATE TABLE IF NOT EXISTS education (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cvId INTEGER NOT NULL," +
                    "exam TEXT NOT NULL," +
                    "institute TEXT NOT NULL," +
                    "passing_year TEXT," +
                    "cg TEXT," +
                    "FOREIGN KEY (cvId) REFERENCES cvify" +
                    "(id) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS work_experience (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cvId INTEGER NOT NULL," +
                    "company TEXT NOT NULL," +
                    "position TEXT NOT NULL," +
                    "duration TEXT," +
                    "FOREIGN KEY (cvId) REFERENCES cvify" +
                    "(id) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS skills (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cvId INTEGER NOT NULL," +
                    "skill_name TEXT NOT NULL," +
                    "FOREIGN KEY (cvId) REFERENCES cvify" +
                    "(id) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS projects (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "cvId INTEGER NOT NULL," +
                    "project_name TEXT NOT NULL," +
                    "FOREIGN KEY (cvId) REFERENCES cvify" +
                    "(id) ON DELETE CASCADE)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int insertCvify(String name, String address, String phone, String email,
                            String imagePath, List<Education> eduList,
                            List<WorkExperience> expList, List<String> skills,
                            List<String> projects) throws SQLException {

        Connection conn = DriverManager.getConnection(URL);
        conn.setAutoCommit(false);

        try {
            String sql = "INSERT INTO cvify" +
                    " (full_name, address, phone, email, image_path) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, imagePath);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            int cvvId = rs.next() ? rs.getInt(1) : -1;

            for (Education edu : eduList) {
                sql = "INSERT INTO education (cvId, exam, institute, passing_year, cg) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, cvvId);
                pstmt.setString(2, edu.getExam());
                pstmt.setString(3, edu.getInstitute());
                pstmt.setString(4, edu.getPassingYear());
                pstmt.setString(5, edu.getCg());
                pstmt.executeUpdate();
            }


            for (WorkExperience exp : expList) {
                sql = "INSERT INTO work_experience (cvId, company, position, duration) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, cvvId);
                pstmt.setString(2, exp.getCompany());
                pstmt.setString(3, exp.getPosition());
                pstmt.setString(4, exp.getDuration());
                pstmt.executeUpdate();
            }


            for (String skill : skills) {
                sql = "INSERT INTO skills (cvId, skill_name) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, cvvId);
                pstmt.setString(2, skill);
                pstmt.executeUpdate();
            }

            for (String project : projects) {
                sql = "INSERT INTO projects (cvId, project_name) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, cvvId);
                pstmt.setString(2, project);
                pstmt.executeUpdate();
            }

            conn.commit();
            return cvvId;

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public List<dataSample> getAllCV() throws SQLException {
        List<dataSample> cvify
                = new ArrayList<>();
        String sql = "SELECT * FROM cvify" +
                " ORDER BY created_at DESC";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dataSample cv = new dataSample();
                cv.setId(rs.getInt("id"));
                cv.setFullName(rs.getString("full_name"));
                cv.setAddress(rs.getString("address"));
                cv.setPhone(rs.getString("phone"));
                cv.setEmail(rs.getString("email"));
                cv.setImagePath(rs.getString("image_path"));
                cvify
                        .add(cv);
            }
        }
        return cvify
                ;
    }

    public dataSample getCVById(int id) throws SQLException {
        dataSample cv = null;

        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "SELECT * FROM cvify" +
                    " WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                cv = new dataSample();
                cv.setId(rs.getInt("id"));
                cv.setFullName(rs.getString("full_name"));
                cv.setAddress(rs.getString("address"));
                cv.setPhone(rs.getString("phone"));
                cv.setEmail(rs.getString("email"));
                cv.setImagePath(rs.getString("image_path"));

                sql = "SELECT * FROM education WHERE cvId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                List<Education> eduList = new ArrayList<>();
                while (rs.next()) {
                    eduList.add(new Education(
                            rs.getString("exam"),
                            rs.getString("institute"),
                            rs.getString("passing_year"),
                            rs.getString("cg")
                    ));
                }
                cv.setEducationList(eduList);

                sql = "SELECT * FROM work_experience WHERE cvId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                List<WorkExperience> expList = new ArrayList<>();
                while (rs.next()) {
                    expList.add(new WorkExperience(
                            rs.getString("company"),
                            rs.getString("position"),
                            rs.getString("duration")
                    ));
                }
                cv.setExperienceList(expList);

                sql = "SELECT skill_name FROM skills WHERE cvId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                List<String> skills = new ArrayList<>();
                while (rs.next()) {
                    skills.add(rs.getString("skill_name"));
                }
                cv.setSkillList(skills);

                sql = "SELECT project_name FROM projects WHERE cvId = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                List<String> projects = new ArrayList<>();
                while (rs.next()) {
                    projects.add(rs.getString("project_name"));
                }
                cv.setProjectList(projects);
            }
        }
        return cv;
    }

    public void updateResume(int id, String name, String address, String phone,
                             String email, String imagePath, List<Education> eduList,
                             List<WorkExperience> expList, List<String> skills,
                             List<String> projects) throws SQLException {

        Connection conn = DriverManager.getConnection(URL);
        conn.setAutoCommit(false);

        try {
            String sql = "UPDATE cvify" +
                    " SET full_name=?, address=?, phone=?, email=?, image_path=? WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phone);
            pstmt.setString(4, email);
            pstmt.setString(5, imagePath);
            pstmt.setInt(6, id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("DELETE FROM education WHERE cvId=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("DELETE FROM work_experience WHERE cvId=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("DELETE FROM skills WHERE cvId=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("DELETE FROM projects WHERE cvId=?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            for (Education edu : eduList) {
                sql = "INSERT INTO education (cvId, exam, institute, passing_year, cg) VALUES (?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, edu.getExam());
                pstmt.setString(3, edu.getInstitute());
                pstmt.setString(4, edu.getPassingYear());
                pstmt.setString(5, edu.getCg());
                pstmt.executeUpdate();
            }

            for (WorkExperience exp : expList) {
                sql = "INSERT INTO work_experience (cvId, company, position, duration) VALUES (?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, exp.getCompany());
                pstmt.setString(3, exp.getPosition());
                pstmt.setString(4, exp.getDuration());
                pstmt.executeUpdate();
            }

            for (String skill : skills) {
                sql = "INSERT INTO skills (cvId, skill_name) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, skill);
                pstmt.executeUpdate();
            }

            for (String project : projects) {
                sql = "INSERT INTO projects (cvId, project_name) VALUES (?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.setString(2, project);
                pstmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void deleteResume(int id) throws SQLException {
        String sql = "DELETE FROM cvify" +
                " WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}