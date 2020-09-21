package PTF.Model;

import java.util.*;

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

    public Team(String projectId, List<String> studentIds) {
        this.projectId=projectId;
        this.studentIds=new HashSet<String>(studentIds);
    }
//pro 01 02 03 04

    public static Team fromString(String line) {
        String[] components = line.split(" ");
        String project = components[0];
        ArrayList<String>studentIds=new ArrayList<>(Arrays.asList(components));
        studentIds.remove(0);
        return new Team(project, studentIds);
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

    public void addStudent(String studentId){
        if(studentIds.size()>=4){
            return;
        }
        this.studentIds.add(studentId);
    }
}
