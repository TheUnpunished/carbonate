package Controller;

import DAO.PhoneTypeDAO;
import DAO.PhoneTypeDAOImpl;
import Model.PhoneType;
import Util.DbWork;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddCodeController implements Initializable {

    private Stage dialogStage;
    private DbWork dbWork=DbWork.getInstance();
    private final ObservableList<String> typeBoxList = FXCollections.observableArrayList();


    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private TextField codeField;
    @FXML
    private void handleCancelAdd(){
        dialogStage.close();
    }
    @FXML
    private void handleAdd() throws ClassNotFoundException {
        PhoneTypeDAO phoneTypeDAO = new PhoneTypeDAOImpl(dbWork);
        PhoneType phoneType = new PhoneType();
        phoneType.setCode(codeField.getText());
        phoneType.setName(typeBox.getSelectionModel().getSelectedItem());
        phoneType.setFullName(typeBox.getSelectionModel().getSelectedItem());
        phoneTypeDAO.save(phoneType);
        dialogStage.close();
    }

    public void setDialogStage(Stage stage){
        this.dialogStage=stage;
    }
    public void setDbWork(DbWork dbWork){
        this.dbWork=dbWork;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PhoneTypeDAO phoneTypeDAO = new PhoneTypeDAOImpl(dbWork);
        List<String> typeStrings = new ArrayList<>();
        List<PhoneType> phoneTypes = null;
        try {
            phoneTypes = phoneTypeDAO.findAll();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        for(PhoneType phoneType : phoneTypes){
            String name = phoneType.getFullName();
            if(!typeStrings.contains(name)){
                typeStrings.add(name);
            }
        }
        typeBoxList.addAll(typeStrings);
        typeBox.setItems(typeBoxList);
    }
}
