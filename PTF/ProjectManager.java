package PTF;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ProjectManager {
    private HashMap<String, Project> projects = new HashMap<String, Project>();
    ArrayList<String> lines = new ArrayList<>();
    String line = null;

    public void loadProjectsFromFile() throws IOException {
        try {
            //1.open the  file
            BufferedReader reader = new BufferedReader(new FileReader("projects.txt"));
            //2.read lines
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            int nextLineNumber = 0;
            while (nextLineNumber < lines.size()) {
                ArrayList<String> linesForProject = new ArrayList<>();
                for (int i = nextLineNumber; i < nextLineNumber + 5; i++) {
                    linesForProject.add(lines.get(i));
                }
                Project project = Project.fromStrings(linesForProject);
                this.projects.put(project.getProjectID(), project);
                nextLineNumber+=5;
            }
        } catch (
                FileNotFoundException e) {
            // Do nothing
        }
    }
    public void saveProjectsToFile() throws IOException {
        //Create a character buffer output stream object
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("projects.txt"));
        for (Project p : this.projects.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(p.getTitle()).append("\n").append(p.getProjectID()).append("\n").
                    append(p.getDescription()).append("\n").append(p.getProjectOwnerID()).append("\n").
                    append(p.getTechnicalSkillCategories());

            //Write information about Project on the files
            bufferedWriter.write(sb.toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
        bufferedWriter.close();
    }
    public Project findProjectById(String id){
        return projects.get(id);
    }
        public void addProject(Project project) throws Exception {
        if (this.findProjectById(project.getProjectID()) != null) {
            //Duplicate detected
            throw new Exception("Duplicate project id");
        }
        this.projects.put(project.getProjectID(), project);
    }
    public void deletedProjectId(String id){
        projects.remove(id);
    }

    public Collection<Project> getAllProject(){
        return projects.values();
    }
    public Set<String> getAllProjectId(){
        return projects.keySet();
    }
}

