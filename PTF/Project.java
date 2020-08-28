package PTF;

import java.util.ArrayList;

public class Project {
    private String title;
    private String projectID;
    private String description;
    private String projectOwnerID;
    private TechnicalSkillCategories technicalSkillCategories;


    public Project(String title, String projectID, String description, TechnicalSkillCategories technicalSkillCategories, String projectOwnerID) {
        this.title = title;
        this.projectID = projectID;
        this.description = description;
        this.technicalSkillCategories = technicalSkillCategories;
        this.projectOwnerID = projectOwnerID;
    }

    public String getProjectOwnerID() {
        return projectOwnerID;
    }

    public void setProjectOwnerID(String projectOwnerID) {
        this.projectOwnerID = projectOwnerID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TechnicalSkillCategories getTechnicalSkillCategories() {
        return technicalSkillCategories;
    }

    public void setTechnicalSkillCategories(TechnicalSkillCategories technicalSkillCategories) {
        this.technicalSkillCategories = technicalSkillCategories;
    }
    public static Project fromStrings(ArrayList<String>lines){

        String title = lines.get(0);
        String projectId = lines.get(1);
        String description = lines.get(2);
        String projectOwnerId = lines.get(3);
        String skills = lines.get(4);
        TechnicalSkillCategories categories = TechnicalSkillCategories.fromString(skills);

        return new Project(title, projectId, description, categories, projectOwnerId);
    }
}

