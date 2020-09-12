package PTF.Model;

import java.util.HashSet;
import java.util.Set;

public class Team {
    private String projectId;
    private HashSet<String> studentIds;

    public Team(String projectId, String student1, String student2, String student3, String student4) {
        this.projectId = projectId;
        this.studentIds = new HashSet<String>();
        this.studentIds.add(student1);
        this.studentIds.add(student2);
        this.studentIds.add(student3);
        this.studentIds.add(student4);

    }
//pro 01 02 03 04

    public static Team fromString(String line) {
        String[] components = line.split(" ");
        String project = components[0];
        String s1 = components[1];
        String s2 = components[2];
        String s3 = components[3];
        String s4 = components[4];
        return new Team(project, s1, s2, s3, s4);
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Set<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(Set<String> studentIds) {
        this.studentIds = (HashSet<String>) studentIds;
    }
}
