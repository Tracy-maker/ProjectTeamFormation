package PTF.Manager;

import PTF.Model.DBHelper;
import PTF.Model.Project;
import PTF.Model.TechnicalSkillCategories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProjectManager {
    private HashMap<String, Project> projects = new HashMap<String, Project>();

    //    ArrayList<String> lines = new ArrayList<>();
//    String line = null;
//
//    public void loadProjectsFromFile() throws IOException {
//        try {
//            //1.open the  file
//            BufferedReader reader = new BufferedReader(new FileReader("projects.txt"));
//            //2.read lines
//            while ((line = reader.readLine()) != null) {
//                lines.add(line);
//            }
//            int nextLineNumber = 0;
//            while (nextLineNumber < lines.size()) {
//                ArrayList<String> linesForProject = new ArrayList<>();
//                for (int i = nextLineNumber; i < nextLineNumber + 5; i++) {
//                    linesForProject.add(lines.get(i));
//                }
//                Project project = Project.fromStrings(linesForProject);
//                this.projects.put(project.getProjectID(), project);
//                nextLineNumber+=5;
//            }
//        } catch (
//                FileNotFoundException e) {
//            // Do nothing
//        }
//    }
//    public void saveProjectsToFile() throws IOException {
//        //Create a character buffer output stream object
//        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("projects.txt"));
//        for (Project p : this.projects.values()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(p.getTitle()).append("\n").append(p.getProjectID()).append("\n").
//                    append(p.getDescription()).append("\n").append(p.getProjectOwnerID()).append("\n").
//                    append(p.getTechnicalSkillCategories());
//
//            //Write information about Project on the files
//            bufferedWriter.write(sb.toString());
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//        }
//        bufferedWriter.close();
//    }
    public Project findProjectById(String id) {
        Project p = null;
        try {
            Connection connection = DBHelper.connection();
            String sql = "SElECT * FROM projects WHERE id =?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String title = result.getString("title");
                String description = result.getString("description");
                String projectOwnerId = result.getString("project_owner_id");
                double programming = result.getDouble("programming_level");
                double web = result.getDouble("web_level");
                double network = result.getDouble("network_level");
                double analysis = result.getDouble("analysis_level");
                TechnicalSkillCategories tech = new TechnicalSkillCategories(programming, network, analysis, web);
                p = new Project(title, id, description, tech, projectOwnerId);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        }
        return p;
    }

    public void addProject(Project project) throws Exception {
        if (this.findProjectById(project.getProjectID()) != null) {
            //Duplicate detected
            throw new Exception("Duplicate project id");
        }
        try {
            Connection connection = DBHelper.connection();
            String sql = "INSERT INTO projects(id,title,description,project_owner_id," +
                    "programming_level,web_level,network_level,analysis_level)VALUES(?,?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, project.getProjectID());
            statement.setString(2, project.getTitle());
            statement.setString(3, project.getDescription());
            statement.setString(4, project.getProjectOwnerID());
            statement.setDouble(5, project.getTechnicalSkillCategories().getProgramming());
            statement.setDouble(6, project.getTechnicalSkillCategories().getWeb());
            statement.setDouble(7, project.getTechnicalSkillCategories().getNetworking());
            statement.setDouble(8, project.getTechnicalSkillCategories().getAnalytics());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deletedProjectId(String id) {
        try {
            Connection connection = DBHelper.connection();
            String sql = "DELETE FROM projects WHERE id=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Collection<Project> getAllProject() {
        ArrayList<Project> projects = new ArrayList<>();
        try {
            Connection connection = DBHelper.connection();
            String sql = "SElECT * FROM projects";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String id = result.getString("id");
                String title = result.getString("title");
                String description = result.getString("description");
                String projectOwnerId = result.getString("project_owner_id");
                double programming = result.getDouble("programming_level");
                double web = result.getDouble("web_level");
                double network = result.getDouble("network_level");
                double analysis = result.getDouble("analysis_level");
                TechnicalSkillCategories tech = new TechnicalSkillCategories(programming, network, analysis, web);
                connection.close();
                Project p = new Project( title,id, description, tech, projectOwnerId);
                projects.add(p);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return projects;
    }


    public Set<String> getAllProjectId() {
        Collection<Project>allProjects=getAllProject();
        Set<String>result=new HashSet<>();
        for(Project p:allProjects){
            result.add(p.getProjectID());
        }
        return result;
    }
}

