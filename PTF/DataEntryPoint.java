package PTF;

import PTF.Manager.*;

public class DataEntryPoint {
    private static DataEntryPoint shared;

    public CompanyManager companyManager = new CompanyManager();
    public ProjectOwnerManager projectOwnerManager = new ProjectOwnerManager();
    public ProjectManager projectManager = new ProjectManager();
    public StudentManager studentManager = new StudentManager();
    public StudentPreferenceManager studentPreferenceManager = new StudentPreferenceManager();
    public TeamManager teamManager = new TeamManager(studentManager, projectManager, studentPreferenceManager);

    public static DataEntryPoint getInstance() {
        if (shared == null) {
            shared = new DataEntryPoint();
        }
        return shared;
    }

    private DataEntryPoint() {
        try {
          //  companyManager.loadCompaniesFromFile();
          //  projectOwnerManager.loadOwnersFromFile();
          //  projectManager.loadProjectsFromFile();
          //  studentManager.loadStudentsFromFile();
            studentPreferenceManager.loadStudentPreferenceFromFile();
            teamManager.loadTeamsFromFile();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
