package PTF.Controller;

import PTF.Model.Team;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class TeamGroupController implements Initializable {

    @FXML
    private Label titleLabel;

    @FXML
    public TextField textField0;

    @FXML
    public TextField textField1;

    @FXML
    public TextField textField2;

    @FXML
    public TextField textField3;


    private TextField[] textFields;
    private Team team;
    private int selectedIndex = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFields = new TextField[]{textField0, textField1, textField2, textField3};
    }

    public void populate(Team team) {
        this.team = team;

        team.getStudentIds();

        Set<String> studentIds = team.getStudentIds();
        ArrayList<String> idArray = new ArrayList<>(studentIds);
        for (int i = 0; i < studentIds.size(); i++) {
            String studentId = idArray.get(i);
            textFields[i].setText(studentId);
        }
        titleLabel.setText("Team " + team.getProjectId());
    }

    @FXML
    private void handleRadioButtonClick(ActionEvent event) {
        RadioButton source = (RadioButton) event.getSource();
        String id = source.getId();
        selectedIndex = Integer.parseInt(id);
    }

    public String getSelectedStudentId() {
        if (selectedIndex < 0 || selectedIndex > 3) {
            return null;
        }
        String studentId=textFields[selectedIndex].getText();
        if(studentId.isEmpty()){
            return null;
        }
        return studentId;
    }
}
