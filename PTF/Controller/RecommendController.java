package PTF.Controller;

import PTF.DataEntryPoint;
import PTF.Model.Team;
import PTF.Recommender;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class RecommendController implements Initializable {

    @FXML
    private Button applyButton;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private HBox contentBox;

    private Thread currentThread;

    private Stage stage;

    private Collection<Team>result;

    private MainSceneController parentController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        applyButton.setDisable(true);
        currentThread = new Thread(() -> {
            Recommender recommender = new Recommender(DataEntryPoint.getInstance().teamManager);
            try {
                Collection<Team> result = recommender.recommend();
                Platform.runLater(()->{
                    handleResult(result);
            });
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        currentThread.start();
    }

    public void setStage(Stage stage){
        this.stage=stage;
        stage.setOnCloseRequest((event -> {
            currentThread.interrupt();
        }));
    }

    public void setParent(MainSceneController parent){
        this.parentController = parent;

    }


    private void handleResult(Collection<Team> result) {
        this.result=result;

        applyButton.setDisable(false);

        contentBox.getChildren().remove(progressIndicator);

        if(result!=null){
            TextArea text = new TextArea();
            StringBuilder sb = new StringBuilder();
            sb.append("A better combination would be: \n");
            for (Team t : result) {
                sb.append((t.getProjectId()));
                sb.append("\n");
                sb.append(String.join(", ", t.getStudentIds()));
                sb.append("\n");
            }
            text.setText(sb.toString());
            contentBox.getChildren().add(text);
        }

    }

    @FXML
    private void handleCancel() {
        currentThread.interrupt();
        stage.close();
    }

    @FXML
    private void handleApply(){
        try {
            DataEntryPoint.getInstance().teamManager.reassignTeam(result);
            if(parentController != null){
                parentController.refreshMetrics();
                parentController.refreshTeamBoxes();
            }
            stage.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
