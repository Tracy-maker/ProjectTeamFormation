package PTF;

public class StudentPreference {
    private String projectId;
    private int grade;

    public StudentPreference() {
    }

    public StudentPreference(String projectName, int grade) {

        this.projectId = projectName;
        this.grade = grade;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectName) {
        this.projectId = projectName;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

}
