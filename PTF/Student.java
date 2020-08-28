package PTF;

import java.util.ArrayList;

public class Student {
    private String studentID;
    private TechnicalSkillCategories technicalSkillCategories;
    private String characteristics;
    private ArrayList<String> noMatchPeople;


    public Student(String studentID, TechnicalSkillCategories technicalSkillCategories, String characteristics, ArrayList<String> noMatchPeople) {
        this.studentID = studentID;
        this.technicalSkillCategories = technicalSkillCategories;
        this.characteristics = characteristics;
        this.noMatchPeople = noMatchPeople;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public TechnicalSkillCategories getTechnicalSkillCategories() {
        return technicalSkillCategories;
    }

    public void setTechnicalSkillCategories(TechnicalSkillCategories technicalSkillCategories) {
        this.technicalSkillCategories = technicalSkillCategories;
    }

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public ArrayList<String> getNoMatchPeople() {
        return noMatchPeople;
    }

    public void setNoMatchPeople(ArrayList<String> noMatchPeople) {
        this.noMatchPeople = noMatchPeople;
    }

    public static Student fromString(String line) {
        String[] components = line.split(" ");
        String id = components[0];
        int w = Integer.parseInt(components[2]);
        int p = Integer.parseInt(components[4]);
        int n = Integer.parseInt(components[6]);
        int a = Integer.parseInt(components[8]);
        TechnicalSkillCategories categories = new TechnicalSkillCategories(p, n, a, w);
        return new Student(id,categories,null,null);
    }

}