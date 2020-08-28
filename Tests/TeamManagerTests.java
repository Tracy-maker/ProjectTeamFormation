package Tests;

import PTF.Exception.NoLeaderException;
import PTF.Exception.PersonalityImbalanceException;
import PTF.Exception.RepeatedMemberException;
import PTF.Exception.StudentConflictException;
import PTF.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;


public class TeamManagerTests {
    TeamManager teamManager;


    @Before
    public void setUp() throws Exception {


        ArrayList<String> conflictS1 = new ArrayList<>();
        conflictS1.add("S2");
        Student s1 = new Student("S1",
                new TechnicalSkillCategories(4, 3, 2, 1), "A", conflictS1);
        Student s2 = new Student("S2",
                new TechnicalSkillCategories(1, 3, 2, 4), "A", null);

        ArrayList<String> conflictS2 = new ArrayList<>();
        conflictS2.add("S1");
        Student s3 = new Student("S3",
                new TechnicalSkillCategories(3, 1, 1, 1), "B", conflictS2);
        Student s4 = new Student("S4",
                new TechnicalSkillCategories(2, 2, 4, 1), "C", new ArrayList<>());
        Student s5 = new Student("S5",
                new TechnicalSkillCategories(4, 4, 4, 4), "C", new ArrayList<>());
        Student s6 = new Student("S6",
                new TechnicalSkillCategories(4, 3, 2, 1), "B", null);

        StudentManager studentManager = new StudentManager();
        studentManager.addStudent(s1);
        studentManager.addStudent(s2);
        studentManager.addStudent(s3);
        studentManager.addStudent(s4);
        studentManager.addStudent(s5);
        studentManager.addStudent(s6);

        Project project = new Project("Project1", "proj1", null,
                new TechnicalSkillCategories(4, 3, 2, 1), "owner1");

        ProjectManager projectManager = new ProjectManager();
        projectManager.addProject(project);

        StudentPreferenceManager preferenceManager=new StudentPreferenceManager();
        preferenceManager.addPreference("S1",new StudentPreference("proj1",2));
        preferenceManager.addPreference("S1",new StudentPreference("proj2",4));
        preferenceManager.addPreference("S1",new StudentPreference("proj4",1));

        preferenceManager.addPreference("S3",new StudentPreference("proj1",3));
        preferenceManager.addPreference("S3",new StudentPreference("proj2",2));
        preferenceManager.addPreference("S1",new StudentPreference("proj4",1));
        preferenceManager.addPreference("S1",new StudentPreference("proj5",1));

        preferenceManager.addPreference("S4",new StudentPreference("proj2",2));
        preferenceManager.addPreference("S4",new StudentPreference("proj3",1));
        preferenceManager.addPreference("S5",new StudentPreference("proj4",4));
        preferenceManager.addPreference("S5",new StudentPreference("proj2",4));
        preferenceManager.addPreference("S6",new StudentPreference("proj5",2));

        this.teamManager = new TeamManager(studentManager, projectManager,preferenceManager );
    }


    @Test(expected = StudentConflictException.class)
    public void testValidateConflict() throws StudentConflictException {
        teamManager.validateConflict("S1", "S2", "S3", "S4");
    }


    @Test()
    public void testValidateConflict1() throws StudentConflictException {
        teamManager.validateConflict("S5", "S4", "S3", "S2");
    }

    @Test(expected = PersonalityImbalanceException.class)
    public void testValidPersonalityBalance() throws PersonalityImbalanceException {
        teamManager.validatePersonalityImbalance("S5", "S6", "S3", "S4");
    }

    @Test()
    public void testValidPersonalityBalance1() throws PersonalityImbalanceException {
        teamManager.validatePersonalityImbalance("S1", "S2", "S3", "S5");
    }

    @Test(expected = RepeatedMemberException.class)
    public void testValidRepeatedMember() throws RepeatedMemberException {
        teamManager.validateRepeatedMember("S1", "S1", "S2", "S3");
    }

    @Test()
    public void testValidRepeatedMember1() throws RepeatedMemberException {
        teamManager.validateRepeatedMember("S1", "S2", "S3", "S4");
    }

    @Test(expected = NoLeaderException.class)
    public void testValidNoLeaderException() throws NoLeaderException {
        teamManager.validateNoLeader("S6", "S3", "S4", "S5");
    }

    @Test()
    public void testValidNoLeaderException1() throws NoLeaderException {
        teamManager.validateNoLeader("S1", "S2", "S3", "S4");
    }

    @Test
    public void testAverageTestSkill() {
        Team t = new Team("proj1", "S1", "S2", "S3", "S4");
        teamManager.addTeam(t);

        TechnicalSkillCategories technicalSkillCategories = teamManager.averageTechnicalSkill("proj1");
        assertEquals((2 + 2 + 1 + 4) / (double) 4, technicalSkillCategories.getAnalytics(), 0.001);
        assertEquals((3 + 3 + 1 + 2) / (double) 4, technicalSkillCategories.getNetworking(), 0.001);
        assertEquals((4 + 1 + 3 + 2) / (double) 4, technicalSkillCategories.getProgramming(), 0.001);
        assertEquals((1 + 4 + 1 + 1) / (double) 4, technicalSkillCategories.getWeb(), 0.001);
    }

    @Test
    public void testShortFall() {
        Team t = new Team("proj1", "S1", "S2", "S3", "S4");
        teamManager.addTeam(t);
        //   p:2.5 n:2.25 a:2.25 w:1.75
        //    1.5+0.75
        assertEquals (2.25,teamManager.skillShortfall("proj1"), 0.001);
    }

    @Test
    public void testSatisfactoryPercentage(){
        Team t=new Team("proj1","S1","S2","S3","S4");
        teamManager.addTeam(t);
        assertEquals(0.5, teamManager.satisfactoryPercentage("proj1"),0.001);
    }
}
