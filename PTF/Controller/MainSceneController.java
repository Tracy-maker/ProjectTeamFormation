package PTF.Controller;

import PTF.DataEntryPoint;
import PTF.MainGUI;
import PTF.Manager.TeamManager;
import PTF.Model.Team;
import PTF.Model.TechnicalSkillCategories;
import PTF.Recommender;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {


    @FXML
    private BarChart<?, ?> satisfactoryChart;

    @FXML
    private BarChart<?, ?> averageCompetencyChart;

    @FXML
    private BarChart<?, ?> skillGapChart;

    @FXML
    private TeamGroupController teamGroup1Controller;

    @FXML
    private TeamGroupController teamGroup2Controller;

    @FXML
    private TeamGroupController teamGroup3Controller;

    @FXML
    private TeamGroupController teamGroup4Controller;

    @FXML
    private TeamGroupController teamGroup5Controller;
    private TeamGroupController[] allTeamGroupControllers;

    @FXML
    private TextField addStudentIdTextField;

    @Override
    public void initialize(URL location, ResourceBundle resource) {
        refreshMetrics();

        refreshTeamBoxes();

        teamGroup1Controller.setBackgroundColor("#FFB6C1");
        teamGroup2Controller.setBackgroundColor("#53c0c9");
        teamGroup3Controller.setBackgroundColor("#ffe375");
        teamGroup4Controller.setBackgroundColor("#75baff");
        teamGroup5Controller.setBackgroundColor("#E6E6FA");

        allTeamGroupControllers = new TeamGroupController[]{
                teamGroup1Controller, teamGroup2Controller, teamGroup3Controller, teamGroup4Controller, teamGroup5Controller
        };
    }

    @FXML
    private void handleSwap() {
        ArrayList<String> selectedIds = new ArrayList<>();
        ArrayList<String> selectedProjId = new ArrayList<>();
        if (teamGroup1Controller.getSelectedStudentId() != null) {
            selectedIds.add(teamGroup1Controller.getSelectedStudentId());
            selectedProjId.add(teamGroup1Controller.getTeam().getProjectId());
        }
        if (teamGroup2Controller.getSelectedStudentId() != null) {
            selectedIds.add(teamGroup2Controller.getSelectedStudentId());
            selectedProjId.add(teamGroup2Controller.getTeam().getProjectId());
        }
        if (teamGroup3Controller.getSelectedStudentId() != null) {
            selectedIds.add(teamGroup3Controller.getSelectedStudentId());
            selectedProjId.add(teamGroup3Controller.getTeam().getProjectId());
        }
        if (teamGroup4Controller.getSelectedStudentId() != null) {
            selectedIds.add(teamGroup4Controller.getSelectedStudentId());
            selectedProjId.add(teamGroup4Controller.getTeam().getProjectId());
        }
        if (teamGroup5Controller.getSelectedStudentId() != null) {
            selectedIds.add(teamGroup5Controller.getSelectedStudentId());
            selectedProjId.add(teamGroup5Controller.getTeam().getProjectId());
        }
        teamGroup1Controller.clearSection();
        teamGroup2Controller.clearSection();
        teamGroup3Controller.clearSection();
        teamGroup4Controller.clearSection();
        teamGroup5Controller.clearSection();

        if (selectedIds.size() != 2) {
            Alert alert = new Alert(Alert.AlertType.NONE, "you should selected two student", ButtonType.OK);
            alert.show();
            return;
        }
        try {
            TeamManager teamManager = DataEntryPoint.getInstance().teamManager;
            String studentAId = selectedIds.get(0);
            String studentBId = selectedIds.get(1);
            String projAId = selectedProjId.get(0);
            String projBId = selectedProjId.get(1);

            // --- validate the new team ---

            // Get original students of projects

            Team projATeam = teamManager.getTeamByProjectId(projAId);
            Team projBTeam = teamManager.getTeamByProjectId(projBId);
            ArrayList<String> teamAStudents = new ArrayList<>(projATeam.getStudentIds());
            ArrayList<String> teamBStudents = new ArrayList<>(projBTeam.getStudentIds());

            // Swap locally

            teamAStudents.remove(studentAId);
            teamAStudents.add(studentBId);
            teamBStudents.remove(studentBId);
            teamBStudents.add(studentAId);
            teamManager.validateConflict(teamAStudents.get(0), teamAStudents.get(1), teamAStudents.get(2), teamAStudents.get(3));
            teamManager.validateNoLeader(teamAStudents.get(0), teamAStudents.get(1), teamAStudents.get(2), teamAStudents.get(3));
            teamManager.validatePersonalityImbalance(teamAStudents.get(0), teamAStudents.get(1), teamAStudents.get(2), teamAStudents.get(3));
            teamManager.validateRepeatedMember(teamAStudents.get(0), teamAStudents.get(1), teamAStudents.get(2), teamAStudents.get(3));
            teamManager.validateConflict(teamBStudents.get(0), teamBStudents.get(1), teamBStudents.get(2), teamBStudents.get(3));
            teamManager.validateNoLeader(teamBStudents.get(0), teamBStudents.get(1), teamBStudents.get(2), teamBStudents.get(3));
            teamManager.validatePersonalityImbalance(teamBStudents.get(0), teamBStudents.get(1), teamBStudents.get(2), teamBStudents.get(3));
            teamManager.validateRepeatedMember(teamBStudents.get(0), teamBStudents.get(1), teamBStudents.get(2), teamBStudents.get(3));

            // ---- validate end ----

            DataEntryPoint.getInstance().teamManager.swapStudent(studentAId, projAId, studentBId, projBId);
            refreshTeamBoxes();
            refreshMetrics();
        } catch (Exception e) {
            String message = e.getMessage();
            Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.OK);
            alert.show();
        }
    }

    @FXML
    private void handleAdd() {
        String newStudentId = addStudentIdTextField.getText();
        ArrayList<String> projectsSelectedByEmptyField = new ArrayList<>();
        for (TeamGroupController controller : allTeamGroupControllers) {
            if (controller.isEmptyChoose()) {
                projectsSelectedByEmptyField.add(controller.getTeam().getProjectId());
            }
        }
        if (projectsSelectedByEmptyField.size() != 1) {
            Alert alert = new Alert(Alert.AlertType.NONE, "you can only select one empty field", ButtonType.OK);
            alert.show();

        }
        String projectId = projectsSelectedByEmptyField.get(0);
        TeamManager teamManager = DataEntryPoint.getInstance().teamManager;
        Team t = teamManager.getTeamByProjectId(projectId);

        teamGroup1Controller.clearSection();
        teamGroup2Controller.clearSection();
        teamGroup3Controller.clearSection();
        teamGroup4Controller.clearSection();
        teamGroup5Controller.clearSection();

        try {
            DataEntryPoint.getInstance().teamManager.validateRepeatedMember(t, newStudentId);
            DataEntryPoint.getInstance().teamManager.validateConflict(t, newStudentId);
            DataEntryPoint.getInstance().teamManager.validateAvailability(newStudentId);
            DataEntryPoint.getInstance().teamManager.validateStudentId(newStudentId);
            if (t.getStudentIds().size() == 3) {
                ArrayList<String> existingStudentIds = new ArrayList<>(t.getStudentIds());
                String student1 = existingStudentIds.get(0);
                String student2 = existingStudentIds.get(1);
                String student3 = existingStudentIds.get(2);
                DataEntryPoint.getInstance().teamManager.validateNoLeader(student1, student2, student3, newStudentId);
                DataEntryPoint.getInstance().teamManager.validatePersonalityImbalance(student1, student2, student3, newStudentId);
            }

            teamManager.addStudentToTeam(newStudentId,t);
            refreshTeamBoxes();
            refreshMetrics();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.show();

        }

    }

    @FXML
    private void handleRecommend() throws IOException {
        FXMLLoader loader =new FXMLLoader(MainGUI.class.getResource("recommendScene.fxml"));
        Parent parent=loader.load();
        RecommendController recommendController =loader.getController();

        Scene scene = new Scene(parent,410,250);
        Stage stage = new Stage();
        recommendController.setStage(stage);
        recommendController.setParent(this);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();



    }

    public void refreshTeamBoxes() {
        Collection<Team> teams = DataEntryPoint.getInstance().teamManager.getAllTeams();
        ArrayList<Team> teamArray = new ArrayList<>(teams);

        if (teamArray.size() >= 1) {
            teamGroup1Controller.populate(teamArray.get(0));
        }
        if (teamArray.size() >= 2) {
            teamGroup2Controller.populate(teamArray.get(1));
        }
        if (teamArray.size() >= 3) {
            teamGroup3Controller.populate(teamArray.get(2));
        }
        if (teamArray.size() >= 4) {
            teamGroup4Controller.populate(teamArray.get(3));
        }
        if (teamArray.size() >= 5) {
            teamGroup5Controller.populate(teamArray.get(4));
        }

    }

    public void refreshMetrics() {

        drawSatisfactoryChart();

        drawAverageCompetencyChart();

        drawSkillGapChart();
    }

    private void drawSatisfactoryChart() {
        TeamManager teamManager = DataEntryPoint.getInstance().teamManager;
        Collection<Team> teams = teamManager.getAllTeams();

        XYChart.Series satisfactoryDataSet = new XYChart.Series();

        for (Team t : teams) {
            String projectId = t.getProjectId();
            double satisfactory = teamManager.satisfactoryPercentage(projectId);
            satisfactoryDataSet.getData().add(new XYChart.Data<>("Team " + projectId, satisfactory));
        }
        satisfactoryChart.getData().clear();
        satisfactoryChart.getData().addAll(satisfactoryDataSet);
        String title = "%Getting 1st and 2nd preferences";
        double std = DataEntryPoint.getInstance().teamManager.standardDeviationForSatisfactoryPercentage();
        String stdStr = String.format("%.2f", std);
        satisfactoryChart.setTitle(title + "\rStd Dev= " + stdStr);

    }

    private void drawAverageCompetencyChart() {
        TeamManager teamManager = DataEntryPoint.getInstance().teamManager;
        Collection<Team> teams = teamManager.getAllTeams();

        XYChart.Series averageCompetency = new XYChart.Series();
        averageCompetency.setName("Average Competency");

        for (Team t : teams) {
            String projectId = t.getProjectId();
            TechnicalSkillCategories technicalSkill = teamManager.averageTechnicalSkill(projectId);
            double teamTechnicalSkill = (technicalSkill.getAnalytics() + technicalSkill.getNetworking() +
                    technicalSkill.getWeb() + technicalSkill.getProgramming()) / 4;
            averageCompetency.getData().add(new XYChart.Data<>("Team " + projectId, teamTechnicalSkill));
        }
        averageCompetencyChart.getData().clear();
        averageCompetencyChart.getData().addAll(averageCompetency);
        String title = "Average Competency Level";
        double std = DataEntryPoint.getInstance().teamManager.standardDeviationForOverallSkillCompetency();
        String stdStr = String.format("%.2f", std);
        averageCompetencyChart.setTitle(title + "\rStd Dev= " + stdStr);
    }

    private void drawSkillGapChart() {
        TeamManager teamManager = DataEntryPoint.getInstance().teamManager;
        Collection<Team> teams = teamManager.getAllTeams();

        XYChart.Series skillGapSet = new XYChart.Series();
        skillGapSet.setName("Skill gap");

        for (Team t : teams) {
            String projectId = t.getProjectId();
            double skillGap = teamManager.skillShortfall(projectId);
            skillGapSet.getData().add(new XYChart.Data<>("Team " + projectId, skillGap));
        }
        skillGapChart.getData().clear();
        skillGapChart.getData().addAll(skillGapSet);

        String title = "Skill Gap";
        double std = DataEntryPoint.getInstance().teamManager.standardDeviationForShortfall();
        String stdStr = String.format("%.2f", std);
        skillGapChart.setTitle(title + "\rStd Dev= " + stdStr);
    }
}
