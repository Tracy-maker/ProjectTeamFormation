package PTF.Controller;

import PTF.DataEntryPoint;
import PTF.Manager.TeamManager;
import PTF.Model.Team;
import PTF.Model.TechnicalSkillCategories;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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


    @Override
    public void initialize(URL location, ResourceBundle resource) {
        refreshMetrics();

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
    @FXML
    private void handleSwap(){
        ArrayList<String>selectedIds=new ArrayList<>();
        if(teamGroup1Controller.getSelectedStudentId()!=null){
            selectedIds.add(teamGroup1Controller.getSelectedStudentId());
        }
        if(teamGroup2Controller.getSelectedStudentId()!=null){
            selectedIds.add(teamGroup2Controller.getSelectedStudentId());
        }
        if(teamGroup3Controller.getSelectedStudentId()!=null){
            selectedIds.add(teamGroup3Controller.getSelectedStudentId());
        }
        if(teamGroup4Controller.getSelectedStudentId()!=null){
            selectedIds.add(teamGroup4Controller.getSelectedStudentId());
        }
        if(teamGroup5Controller.getSelectedStudentId()!=null){
            selectedIds.add(teamGroup5Controller.getSelectedStudentId());
        }
       if(selectedIds.size()!=2){

       }
    }

    @FXML
    private void handleAdd() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "lalalal", ButtonType.OK);
        alert.show();
    }

    private void refreshMetrics() {
        drawSatisfactoryChart();
        drawAverageCompetencyChart();
        drawSkillGapChart();
    }

    private void drawSatisfactoryChart() {
        TeamManager teamManager = DataEntryPoint.getInstance().teamManager;
        Collection<Team> teams = teamManager.getAllTeams();

        XYChart.Series satisfactoryDataSet = new XYChart.Series();

        for (
                Team t : teams) {
            String projectId = t.getProjectId();
            double satisfactory = teamManager.satisfactoryPercentage(projectId);
            satisfactoryDataSet.getData().add(new XYChart.Data<>("Team " + projectId, satisfactory));
        }
        satisfactoryChart.getData().removeAll();
        satisfactoryChart.getData().addAll(satisfactoryDataSet);

    }

    private void drawAverageCompetencyChart() {
        TeamManager teamManager = DataEntryPoint.getInstance().teamManager;
        Collection<Team> teams = teamManager.getAllTeams();

        XYChart.Series programingDataSet = new XYChart.Series();
        programingDataSet.setName("programing");
        XYChart.Series webDataSet = new XYChart.Series();
        webDataSet.setName("web");
        XYChart.Series networkDataSet = new XYChart.Series();
        networkDataSet.setName("network");
        XYChart.Series analyticsDataSet = new XYChart.Series();
        analyticsDataSet.setName("analytics");

        for (Team t : teams) {
            String projectId = t.getProjectId();
            TechnicalSkillCategories averageCompetency = teamManager.averageTechnicalSkill(projectId);
            programingDataSet.getData().add(new XYChart.Data<>("Team " + projectId, averageCompetency.getProgramming()));
            webDataSet.getData().add(new XYChart.Data<>("Team " + projectId, averageCompetency.getWeb()));
            networkDataSet.getData().add(new XYChart.Data<>("Team " + projectId, averageCompetency.getNetworking()));
            analyticsDataSet.getData().add(new XYChart.Data<>("Team " + projectId, averageCompetency.getAnalytics()));
        }
        averageCompetencyChart.getData().removeAll();
        averageCompetencyChart.getData().addAll(programingDataSet, webDataSet, networkDataSet, analyticsDataSet);
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
        skillGapChart.getData().removeAll();
        skillGapChart.getData().addAll(skillGapSet);
    }
}
