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
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {


    @FXML
    private BarChart<?, ?> satisfactoryChart;

    @FXML
    private BarChart<?, ?> averageCompetencyChart;

    @FXML
    private BarChart<?, ?> skillGapChart;


    @Override
    public void initialize(URL location, ResourceBundle resource) {
        refreshMetrics();
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
