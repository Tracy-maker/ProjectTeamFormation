package PTF.Controller;

import PTF.Model.Team;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;

public class TeamGroupController implements Initializable {

    @FXML
    private VBox background;

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

    @FXML
    private RadioButton radioButton0;

    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;

    private RadioButton[] radioButtons;
    private TextField[] textFields;
    private Team team;
    private int selectedIndex = -1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFields = new TextField[]{textField0, textField1, textField2, textField3};
        radioButtons = new RadioButton[]{radioButton0, radioButton1, radioButton2, radioButton3};

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
    private void handleRadioButtonClick(javafx.event.ActionEvent event) {
        RadioButton source = (RadioButton) event.getSource();
        String id = source.getId();
        selectedIndex = Integer.parseInt(id);
    }

    public String getSelectedStudentId() {
        if (selectedIndex < 0 || selectedIndex > 3) {
            return null;
        }
        String studentId = textFields[selectedIndex].getText();
        if (studentId.isEmpty()) {
            return null;
        }
        return studentId;
    }

    public boolean isEmptyChoose(){
        if( selectedIndex < 0||selectedIndex>3){
            return false;
        }
        String studentId = textFields[selectedIndex].getText();
        return studentId.isEmpty();
    }

    public void clearSection() {
        for (RadioButton button : radioButtons) {
            button.setSelected(false);
        }
        selectedIndex = -1;
    }

    public Team getTeam(){
        return team;
    }

    public void setBackgroundColor(String color){
        background.setStyle("-fx-background-color: "+color);
    }

}
