package PTF.Manager;

import PTF.Model.ProjectOwner;

import java.io.*;
import java.util.HashMap;

public class ProjectOwnerManager {
    private HashMap<String, ProjectOwner> projectOwners = new HashMap<String, ProjectOwner>();

    public void loadOwnersFromFile() throws IOException {
        try {
            //1.open the file
            BufferedReader reader = new BufferedReader(new FileReader("owners.txt"));
            //2.read lines
            String line = null;

            while ((line = reader.readLine()) != null) {
                ProjectOwner projectOwner = ProjectOwner.fromString(line);
                this.projectOwners.put(projectOwner.getProjectOwnerID(), projectOwner);
            }
        } catch (FileNotFoundException e) {
            // Do nothing
        }
    }

    public void saveProjectOwnersToFile() throws IOException {
        //Creates a character buffer output stream object
        BufferedWriter bw = new BufferedWriter(new FileWriter("owners.txt"));

        for (ProjectOwner p : this.projectOwners.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(p.getProjectOwnerID()).append(",").append(p.getFirstName()).append(",").append(p.getSurname()).append(",").
                    append(p.getRole()).append(",").append(p.getEmail()).append(",").append(p.getCompanyID());

            //Write information about Project Owner on the files
            bw.write(sb.toString());
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    public ProjectOwner findProjectOwnerById(String id) {
        return projectOwners.get(id);
    }

    public void addProjectOwner(ProjectOwner projectOwner) throws Exception {
        if (this.findProjectOwnerById(projectOwner.getProjectOwnerID()) != null) {
            //Duplicate detected
            throw new Exception("Duplicate project owner id");
        }
        this.projectOwners.put(projectOwner.getProjectOwnerID(), projectOwner);
    }
}

