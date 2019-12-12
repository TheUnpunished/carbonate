package ru.icmit.Yakovlev.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.icmit.Yakovlev.DAO.PhoneDAO;
import ru.icmit.Yakovlev.DAO.PhoneDAOImpl;
import ru.icmit.Yakovlev.Model.Phone;
import ru.icmit.Yakovlev.Util.DbWork;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PhonesController implements Initializable {
    DbWork dbWork = DbWork.getInstance();
    private ObservableList<Phone> Phones = FXCollections.observableArrayList();
    @FXML
    private ListView<Phone> PhoneList;
    @FXML
    private void handleDoubleClick(MouseEvent mouseEvent) throws IOException, ClassNotFoundException {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount()==2){
                int index = PhoneList.getSelectionModel().getSelectedIndex();
                if(index>-1){
                    Phone phone = Phones.get(index);
                    openDialog(phone,"view",dbWork);
                }
            }
        }
    }
    @FXML
    private void handleAddButton() throws IOException, ClassNotFoundException {
        Phone phone = new Phone();
        openDialog(phone, "save",dbWork);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            setData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void openDialog(Phone phone, String type, DbWork dbWork) throws IOException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/FXML/PhoneDialog.fxml"));
        AnchorPane root = (AnchorPane) fxmlLoader.load();
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        PhoneDialogController phoneDialogController = fxmlLoader.getController();
        phoneDialogController.setData(phone,type,dbWork,stage);
        stage.setTitle("Контакт");
        stage.showAndWait();
        setData();
        //update table upon closing
    }
    private void setData() throws ClassNotFoundException {
        try{
            dbWork.setConnection();
            PhoneDAO phoneDAO = new PhoneDAOImpl(dbWork);
            Phones.clear();
            Phones.addAll(phoneDAO.findAll());
            PhoneList.setItems(Phones);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
