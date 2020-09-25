package PTF.Manager;

import PTF.Model.DBHelper;
import PTF.Model.Student;
import PTF.Model.TechnicalSkillCategories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class StudentManager {
//    private HashMap<String, Student> students = new HashMap<String, Student>();
//
//    public void loadStudentsFromFile() throws IOException {
//        try {
//            BufferedReader reader = new BufferedReader(new FileReader("student.txt"));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                Student student = Student.fromString(line);
//                students.put(student.getStudentID(), student);
//            }
//            try {
//                //read student Information
//                reader = new BufferedReader(new FileReader("studentInfo.txt"));
//
//                while ((line = reader.readLine()) != null) {
//                    String[] components = line.split(" ");
//                    String studentId = components[0];
//                    String personality = components[9];
//                    ArrayList<String> studentCantWorkWith = new ArrayList<>();
//                    for (int i = 10; i < components.length; i++) {
//                        String studentNumber = components[i];
//                        studentCantWorkWith.add(studentNumber);
//                    }
//
//                    Student s = students.get(studentId);
//                    s.setCharacteristics(personality);
//                    s.setNoMatchPeople(studentCantWorkWith);
//
//                }
//            } catch (FileNotFoundException notFound) {
//
//            }
//        } catch (FileNotFoundException notFound) {
//            System.out.println("Error:student.txt not found !");
//            throw notFound;
//        }
//
//    }
//
//    public void saveStudentToFile() throws IOException {
//        BufferedWriter bw = new BufferedWriter(new FileWriter("studentInfo.txt"));
//
//        for (Student student : students.values()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(student.getStudentID()).append(" ").append(student.getTechnicalSkillCategories()).append(" ").
//                    append(student.getCharacteristics()).append(" ").append(String.join(" ", student.getNoMatchPeople()));
//
//            //Write information about Project on the files
//            bw.write(sb.toString());
//            bw.newLine();
//            bw.flush();
//        }
//        bw.close();
//    }

    public Student getStudentById(String id) {

        Student student = null;
        try {
            Connection connection = DBHelper.connection();
            String sql = "SELECT * FROM students WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String characteristic = result.getString("characteristics");
                double p = result.getDouble("programming_level");
                double w = result.getDouble("web_level");
                double n = result.getDouble("network_level");
                double a = result.getDouble("analysis_level");
                TechnicalSkillCategories tech = new TechnicalSkillCategories(p, n, a, w);

                sql = "SELECT * FROM student_conflicts WHERE student_id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, id);
                ResultSet conflictsResult = statement.executeQuery();
                ArrayList<String> conflictStudentIds = new ArrayList<>();
                while (conflictsResult.next()) {
                    String conflictId = conflictsResult.getString("conflict_student_Id");
                    conflictStudentIds.add(conflictId);
                }
                student = new Student(id, tech, characteristic, conflictStudentIds);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return student;
    }

    public Collection<Student> getAllStudent() {
        ArrayList<Student> students = new ArrayList<>();
        try {
            Connection connection = DBHelper.connection();
            String sql = "SELECT * FROM students";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String id = result.getString("id");
                String characteristic = result.getString("characteristics");
                double p = result.getDouble("programming_level");
                double w = result.getDouble("web_level");
                double n = result.getDouble("network_level");
                double a = result.getDouble("analysis_level");
                TechnicalSkillCategories tech = new TechnicalSkillCategories(p, n, a, w);

                sql = "SELECT * FROM student_conflicts WHERE student_id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, id);
                ResultSet conflictsResult = statement.executeQuery();
                ArrayList<String> conflictStudentIds = new ArrayList<>();
                while (conflictsResult.next()) {
                    String conflictId = conflictsResult.getString("conflict_student_Id");
                    conflictStudentIds.add(conflictId);
                }
                Student student = new Student(id, tech, characteristic, conflictStudentIds);
                students.add(student);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return students;
    }

    public void addStudent(Student student) {
        Connection connection = DBHelper.connection();
        try {
            String sql = "INSERT INTO students(id,characteristics,programming_level,web_level,network_level,analysis_level)VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, student.getStudentID());
            statement.setString(2, student.getCharacteristics());
            statement.setDouble(3, student.getTechnicalSkillCategories().getProgramming());
            statement.setDouble(4, student.getTechnicalSkillCategories().getWeb());
            statement.setDouble(5, student.getTechnicalSkillCategories().getNetworking());
            statement.setDouble(6, student.getTechnicalSkillCategories().getAnalytics());
            statement.executeUpdate();

            for (String conflictStudentId : student.getNoMatchPeople()) {
                sql = "INSERT INTO student_conflicts(student_id,conflict_student_id)VALUES(?,?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, student.getStudentID());
                statement.setString(2, conflictStudentId);
                statement.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}