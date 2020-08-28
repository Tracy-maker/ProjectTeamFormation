package PTF;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class StudentManager {
    private HashMap<String, Student> students = new HashMap<String, Student>();

    public void loadStudentsFromFile() throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("student.txt"));
            String line = null;
            while ((line = reader.readLine()) != null) {
                Student student = Student.fromString(line);
                students.put(student.getStudentID(), student);
            }
            try {
                //read student Information
                reader = new BufferedReader(new FileReader("studentInfo.txt"));

                while ((line = reader.readLine()) != null) {
                    String[] components = line.split(" ");
                    String studentId = components[0];
                    String personality = components[9];
                    ArrayList<String> studentCantWorkWith = new ArrayList<>();
                    for (int i = 10; i < components.length; i++) {
                        String studentNumber = components[i];
                        studentCantWorkWith.add(studentNumber);
                    }

                    Student s = students.get(studentId);
                    s.setCharacteristics(personality);
                    s.setNoMatchPeople(studentCantWorkWith);

                }
            } catch (FileNotFoundException notFound) {

            }
        } catch (FileNotFoundException notFound) {
            System.out.println("Error:student.txt not found !");
            throw notFound;
        }

    }

    public void saveStudentToFile() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("studentInfo.txt"));

        for (Student student : students.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(student.getStudentID()).append(" ").append(student.getTechnicalSkillCategories()).append(" ").
                    append(student.getCharacteristics()).append(" ").append(String.join(" ", student.getNoMatchPeople()));

            //Write information about Project on the files
            bw.write(sb.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    public Student getStudentById(String id) {
        return students.get(id);
    }

    public Collection<Student> getAllStudent() {
        return students.values();
    }

    public void addStudent(Student student) {
        students.put(student.getStudentID(), student);
    }

}