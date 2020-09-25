package PTF.Model;

import java.util.Collection;

public class ProjectOwner {
    private String firstName;
    private String surname;
    private String projectOwnerID;
    private String role;
    private String email;
    private String companyID;

    public ProjectOwner(String firstName, String surname, String projectOwnerID, String role, String email, String companyID) {
        this.firstName = firstName;
        this.surname = surname;
        this.projectOwnerID = projectOwnerID;
        this.role = role;
        this.email = email;
        this.companyID = companyID;
    }


    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getProjectOwnerID() {
        return projectOwnerID;
    }

    public void setProjectOwnerID(String projectOwnerID) {
        this.projectOwnerID = projectOwnerID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static ProjectOwner fromString(String line) {
        String[] components = line.split(",");
        String projectOwnerId = components[0];
        String firstName = components[1];
        String surname = components[2];
        String role = components[3];
        String email = components[4];
        String companyId = components[5];
        return new ProjectOwner(firstName, surname, projectOwnerId, role, email, companyId);
    }


}