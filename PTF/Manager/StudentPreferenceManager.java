package PTF.Manager;

import PTF.Model.StudentPreference;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class StudentPreferenceManager {
    HashMap<String, TreeSet<StudentPreference>> preferences = new HashMap<String, TreeSet<StudentPreference>>();

    public void loadStudentPreferenceFromFile() throws IOException {
        try {
            //1.open the  file
            BufferedReader reader = new BufferedReader(new FileReader("preferences.txt"));
            //2.read lines
            ArrayList<String> lines = new ArrayList<>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

            int nextLineNumber = 0;
            while (nextLineNumber < lines.size()) {
                String studentId = lines.get(nextLineNumber);
                String preferencesString = lines.get(nextLineNumber + 1);
                TreeSet<StudentPreference> pref = parsePreferenceString(preferencesString);
                this.preferences.put(studentId, pref);
                nextLineNumber += 2;

            }
        } catch (FileNotFoundException e) {

            // Do nothing
        }

    }

    private TreeSet<StudentPreference> parsePreferenceString(String line) {

        String[] components = line.split(" ");
        int nextIndex = 0;
        TreeSet<StudentPreference> preferences = new TreeSet<>(
                (StudentPreference pref1, StudentPreference pref2) -> pref2.getGrade() - pref1.getGrade()
        );
       
        while (nextIndex < components.length) {
            String projectId = components[nextIndex];
            int grade = Integer.parseInt(components[nextIndex + 1]);
            StudentPreference pref = new StudentPreference();
            pref.setProjectId(projectId);
            pref.setGrade(grade);
            preferences.add(pref);
            nextIndex += 2;
        }
        return preferences;
    }

    public void saveToFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("preferences.txt"));
        for (String studentId : preferences.keySet()) {
            TreeSet<StudentPreference> value = preferences.get(studentId);

            StringBuilder sb = new StringBuilder();
            for (StudentPreference preference : value) {
                sb.append(preference.getProjectId()).append(" ").append(preference.getGrade()).append(" ");
            }
            writer.write(studentId);
            writer.newLine();
            writer.write(sb.toString());
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }


    public void addPreference(String studentId, StudentPreference preference) {
        TreeSet<StudentPreference> preferenceList = this.preferences.get(studentId);
        if (preferenceList == null) {
            preferenceList = new TreeSet<>(
                    (StudentPreference pref1, StudentPreference pref2) -> pref2.getGrade() - pref1.getGrade()
            );
        }
        preferenceList.add(preference);
        this.preferences.put(studentId, preferenceList);
    }

    public TreeSet<StudentPreference> getPreferencesByStudentId(String id) {
        return this.preferences.get(id);
    }

    public HashMap<String, Integer> getProjectPreferenceStats() {
        HashMap<String, Integer> stats = new HashMap<>();

        for(TreeSet<StudentPreference> prefForStudent : preferences.values()) {
            for (StudentPreference pref : prefForStudent) {
                String projId = pref.getProjectId();
                int grade = pref.getGrade();
                Integer previous = stats.get(projId);
                if (previous == null) {
                    previous = 0;
                }
                Integer next = previous + grade;
                stats.put(projId, next);
            }
        }

        return stats;
    }
    public HashMap<String,TreeSet<StudentPreference>>getAllPreferences(){
        return this.preferences;
    }
}
