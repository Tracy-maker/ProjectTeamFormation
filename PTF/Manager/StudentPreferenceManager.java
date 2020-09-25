package PTF.Manager;

import PTF.Model.DBHelper;
import PTF.Model.StudentPreference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class StudentPreferenceManager {
//    HashMap<String, TreeSet<StudentPreference>> preferences = new HashMap<String, TreeSet<StudentPreference>>();
//
//    public void loadStudentPreferenceFromFile() throws IOException {
//        try {
//            //1.open the  file
//            BufferedReader reader = new BufferedReader(new FileReader("preferences.txt"));
//            //2.read lines
//            ArrayList<String> lines = new ArrayList<>();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                lines.add(line);
//            }
//
//            int nextLineNumber = 0;
//            while (nextLineNumber < lines.size()) {
//                String studentId = lines.get(nextLineNumber);
//                String preferencesString = lines.get(nextLineNumber + 1);
//                TreeSet<StudentPreference> pref = parsePreferenceString(preferencesString);
//                this.preferences.put(studentId, pref);
//                nextLineNumber += 2;
//
//            }
//        } catch (FileNotFoundException e) {
//
//            // Do nothing
//        }
//
//    }
//
//    private TreeSet<StudentPreference> parsePreferenceString(String line) {
//
//        String[] components = line.split(" ");
//        int nextIndex = 0;
//        TreeSet<StudentPreference> preferences = new TreeSet<>(
//                (StudentPreference pref1, StudentPreference pref2) -> pref2.getGrade() - pref1.getGrade()
//        );
//
//        while (nextIndex < components.length) {
//            String projectId = components[nextIndex];
//            int grade = Integer.parseInt(components[nextIndex + 1]);
//            StudentPreference pref = new StudentPreference();
//            pref.setProjectId(projectId);
//            pref.setGrade(grade);
//            preferences.add(pref);
//            nextIndex += 2;
//        }
//        return preferences;
//    }
//
//    public void saveToFile() throws IOException {
//        BufferedWriter writer = new BufferedWriter(new FileWriter("preferences.txt"));
//        for (String studentId : preferences.keySet()) {
//            TreeSet<StudentPreference> value = preferences.get(studentId);
//
//            StringBuilder sb = new StringBuilder();
//            for (StudentPreference preference : value) {
//                sb.append(preference.getProjectId()).append(" ").append(preference.getGrade()).append(" ");
//            }
//            writer.write(studentId);
//            writer.newLine();
//            writer.write(sb.toString());
//            writer.newLine();
//        }
//        writer.flush();
//        writer.close();
//    }


    public void addPreference(String studentId, StudentPreference preference) {
        Connection connection = DBHelper.connection();

        try {
            String sql = "INSERT INTO student_preferences(student_id,project_id,grade)VALUES(?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, studentId);
            statement.setString(2, preference.getProjectId());
            statement.setInt(3, preference.getGrade());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
           try{
               connection.close();
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }
        }
}

    public Collection<StudentPreference> getPreferencesByStudentId(String id) {
        Connection connection =DBHelper.connection();
        ArrayList<StudentPreference> preferences=new ArrayList<>();
        try{
            String sql="SELECT * FROM student_preferences sp WHERE sp.student_id = ? ORDER BY sp.grade DESC;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,id);
            ResultSet result=statement.executeQuery();
            while (result.next()){
                String projectId=result.getString("project_id");
                int grade = result.getInt("grade");
                preferences.add(new StudentPreference(projectId,grade));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try{
                connection.close();
            }catch (SQLException throwables){
                throwables.printStackTrace();
            }
        }
        return preferences;
    }

    public HashMap<String, Integer> getProjectPreferenceStats() {
        HashMap<String, Integer> stats = new HashMap<>();
        Connection connection =DBHelper.connection();

        try{
            String sql="SELECT sp.project_id,SUM(sp.grade) as sum_grade FROM student_preferences sp GROUP BY sp.project_id;";
       PreparedStatement statement =connection.prepareStatement(sql);
       ResultSet resultSet=statement.executeQuery();
       while(resultSet.next()){
           String projectId =resultSet.getString("project_id");
           int grade = resultSet.getInt("sum_grade");
           stats.put(projectId, grade);
       }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try{
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return stats;
    }

//    public HashMap<String, TreeSet<StudentPreference>> getAllPreferences() {
//        return this.preferences;
//    }
}
