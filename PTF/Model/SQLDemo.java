package PTF.Model;

import PTF.DataEntryPoint;
import PTF.Manager.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;

public class SQLDemo {
    public static void main(String[] args) throws SQLException {
        //  insertCompany();
        // insertProjectOwners();
        //  insertProjects();
        //  insertStudentsAndConflict();
        // insertStudentPreferences();
      //  insertTeams();
    }

    public static void insertCompany() throws SQLException {
        CompanyManager companyManager = DataEntryPoint.getInstance().companyManager;
        Collection<Company> companies = companyManager.getAllCompanies();

        Connection connection = DBHelper.connection();

        for (Company c : companies) {
            String sql = "INSERT INTO companies(id,name,abn,url,address)VALUES(?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, c.getCompanyID());
            statement.setString(2, c.getCompanyName());
            statement.setString(3, c.getABNNumber());
            statement.setString(4, c.getURL());
            statement.setString(5, c.getAddress());
            statement.executeUpdate();
        }
        connection.close();
    }

    public static void insertProjectOwners() throws SQLException {
        ProjectOwnerManager projectOwnerManager = DataEntryPoint.getInstance().projectOwnerManager;
        Collection<ProjectOwner> pos = projectOwnerManager.getAllProjectOwners();

        Connection connection = DBHelper.connection();

        for (ProjectOwner p : pos) {
            String sql = "INSERT INTO project_owners(id,first_name,surname,role,email,company_id)VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, p.getProjectOwnerID());
            statement.setString(2, p.getFirstName());
            statement.setString(3, p.getSurname());
            statement.setString(4, p.getRole());
            statement.setString(5, p.getEmail());
            statement.setString(6, p.getCompanyID());
            statement.executeUpdate();
        }
        connection.close();
    }

    public static void insertProjects() throws SQLException {
        ProjectManager projectManager = DataEntryPoint.getInstance().projectManager;
        Collection<Project> pos = projectManager.getAllProject();

        Connection connection = DBHelper.connection();

        for (Project p : pos) {
            String sql = "INSERT INTO projects(id,title,description,project_owner_id,programming_level,web_level,network_level,analysis_level)VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, p.getProjectID());
            statement.setString(2, p.getTitle());
            statement.setString(3, p.getDescription());
            statement.setString(4, p.getProjectOwnerID());
            statement.setDouble(5, p.getTechnicalSkillCategories().getProgramming());
            statement.setDouble(6, p.getTechnicalSkillCategories().getWeb());
            statement.setDouble(7, p.getTechnicalSkillCategories().getNetworking());
            statement.setDouble(8, p.getTechnicalSkillCategories().getAnalytics());
            statement.executeUpdate();
        }
        connection.close();
    }

    public static void insertStudentsAndConflict() throws SQLException {
        StudentManager studentManager = DataEntryPoint.getInstance().studentManager;
        Collection<Student> students = studentManager.getAllStudent();

        Connection connection = DBHelper.connection();

        for (Student s : students) {
            String sql = "INSERT INTO students(id,characteristics,programming_level,web_level,network_level,analysis_level)VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, s.getStudentID());
            statement.setString(2, s.getCharacteristics());
            statement.setDouble(3, s.getTechnicalSkillCategories().getProgramming());
            statement.setDouble(4, s.getTechnicalSkillCategories().getWeb());
            statement.setDouble(5, s.getTechnicalSkillCategories().getNetworking());
            statement.setDouble(6, s.getTechnicalSkillCategories().getAnalytics());
            statement.executeUpdate();

            for (String conflictStudentId : s.getNoMatchPeople()) {
                sql = "INSERT INTO student_conflicts(student_id,conflict_student_id)VALUES(?,?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, s.getStudentID());
                statement.setString(2, conflictStudentId);
                statement.executeUpdate();
            }
        }
        connection.close();
    }

    public static void insertStudentPreferences() throws SQLException {
        StudentPreferenceManager studentPreferenceManager = DataEntryPoint.getInstance().studentPreferenceManager;
        HashMap<String, TreeSet<StudentPreference>> pos = studentPreferenceManager.getAllPreferences();

        Connection connection = DBHelper.connection();
        for (String studentId : pos.keySet()) {
            TreeSet<StudentPreference> preferences = pos.get(studentId);
            for (StudentPreference p : preferences) {
                String sql = "INSERT INTO student_preferences(student_id,project_id,grade)VALUES(?,?,?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, studentId);
                statement.setString(2, p.getProjectId());
                statement.setInt(3, p.getGrade());
                statement.executeUpdate();
            }
        }
        connection.close();
    }

    public static void insertTeams() throws SQLException {
        TeamManager teamManager = DataEntryPoint.getInstance().teamManager;
        Collection<Team> allTeams = teamManager.getAllTeams();
        Connection connection = DBHelper.connection();
        for (Team t : allTeams) {
            String projId = t.getProjectId();
            String sql = "INSERT INTO teams(project_id)VALUES(?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, projId);
            statement.executeUpdate();

            for (String studentId : t.getStudentIds()) {
                sql = "UPDATE students SET teamId= ? WHERE students.id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, projId);
                statement.setString(2, studentId);
                statement.executeUpdate();
            }
        }
        connection.close();
    }

}