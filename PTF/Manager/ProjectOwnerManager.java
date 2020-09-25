package PTF.Manager;

import PTF.Model.*;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ProjectOwnerManager {
//    private HashMap<String, ProjectOwner> projectOwners = new HashMap<String, ProjectOwner>();
//    public void loadOwnersFromFile() throws IOException {
//        try {
//            //1.open the file
//            BufferedReader reader = new BufferedReader(new FileReader("owners.txt"));
//            //2.read lines
//            String line = null;
//
//            while ((line = reader.readLine()) != null) {
//                ProjectOwner projectOwner = ProjectOwner.fromString(line);
//                this.projectOwners.put(projectOwner.getProjectOwnerID(), projectOwner);
//            }
//        } catch (FileNotFoundException e) {
//            // Do nothing
//        }
//    }
//
//    public void saveProjectOwnersToFile() throws IOException {
//        //Creates a character buffer output stream object
//        BufferedWriter bw = new BufferedWriter(new FileWriter("owners.txt"));
//
//        for (ProjectOwner p : this.projectOwners.values()) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(p.getProjectOwnerID()).append(",").append(p.getFirstName()).append(",").append(p.getSurname()).append(",").
//                    append(p.getRole()).append(",").append(p.getEmail()).append(",").append(p.getCompanyID());
//
//            //Write information about Project Owner on the files
//            bw.write(sb.toString());
//            bw.newLine();
//            bw.flush();
//        }
//        bw.close();
//    }

    public ProjectOwner findProjectOwnerById(String id) {
        try {
            Connection connection = DBHelper.connection();
            String sql = "SElECT * FROM project_owners WHERE id =?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String surname = resultSet.getString("surname");
                String role = resultSet.getString("role");
                String email = resultSet.getString("email");
                String companyId=resultSet.getString("company_id");
                connection.close();
                return new ProjectOwner(firstName,surname,id,role,email,companyId);

            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return null;
    }

    public void addProjectOwner(ProjectOwner projectOwner) throws Exception {
        if (this.findProjectOwnerById(projectOwner.getProjectOwnerID()) != null) {
            //Duplicate detected
            throw new Exception("Duplicate projectOwner id");
        }
        try {
            Connection connection = DBHelper.connection();
            String sql = "INSERT INTO project_owners(id,first_name,surname,role,email,company_id)VALUES(?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, projectOwner.getProjectOwnerID());
            statement.setString(2, projectOwner.getFirstName());
            statement.setString(3, projectOwner.getSurname());
            statement.setString(4, projectOwner.getRole());
            statement.setString(5, projectOwner.getEmail());
            statement.setString(6, projectOwner.getCompanyID());
            statement.executeUpdate();
            connection.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public Collection<ProjectOwner> getAllProjectOwners(){
        ArrayList<ProjectOwner> projectOwners = new ArrayList<>();
        try {
            Connection connection = DBHelper.connection();
            String sql = "SElECT * FROM project_owners";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                String id = result.getString("id");
                String firstName = result.getString("first_name");
                String surname = result.getString("surname");
                String role = result.getString("role");
                String email=result.getString("email");
                String companyId=result.getString("company_id");

                ProjectOwner po = new ProjectOwner(firstName,surname,id,role,email,companyId);
                projectOwners.add(po);
            }
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
        return projectOwners;
    }
}

