package ru.icmit.Yakovlev.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.icmit.Yakovlev.DAO.*;
import ru.icmit.Yakovlev.Model.Contact;
import ru.icmit.Yakovlev.Model.Phone;
import ru.icmit.Yakovlev.Model.PhoneType;
import ru.icmit.Yakovlev.Util.DbWork;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class PhoneDialogController implements Initializable {
    private boolean editClicked=false;
    private final ObservableList<String> yesNoList = FXCollections.observableArrayList();
    private final ObservableList<String> typesList = FXCollections.observableArrayList();
    private final ObservableList<String> codeList = FXCollections.observableArrayList();
    private DbWork dbWork = DbWork.getInstance();
    private Stage dialogStage;
    private Phone phone;
    private String type;
    @FXML
    private Label fNameLabel;
    @FXML
    private Label lNameLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label phoneTypeLabel;
    @FXML
    private Label codeLabel;
    @FXML
    private Label inBlackListLabel;
    @FXML
    private TextField fNameField;
    @FXML
    private TextField lNameField;
    @FXML
    private TextField phoneField;
    @FXML
    private ComboBox<String> codeBox;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private ComboBox<String> inBlackListBox;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private ImageView imageView;

    private String resolveInBlackList(boolean inBlackList){
        if(inBlackList)
            return "Да";
        else
            return "Нет";
    }
    private boolean resolveInBlackList(String inBlacklist) {
        switch (inBlacklist) {
            case "Да":
                return true;
            case "Нет":
                return false;
        }
        return false;
    }
    public void setData(Phone phone, String type, DbWork dbWork, Stage dialogStage){
        this.phone=phone;
        this.type=type;
        this.dialogStage=dialogStage;
        this.dbWork=dbWork;
        switch (type){
            case "view":{
                editButton.setText("Изменить");
                deleteButton.setText("Удалить");
                setLabelData(phone);
                setImageView(type);
                break;
            }
            case "save":
            case "edit":{
                changeWindowType(type);
                break;
            }
        }

    }
    public void handleDeleteButton() throws ClassNotFoundException {
        if(editClicked){
            switch (this.type){
                case "save":{
                    this.dialogStage.close();
                    break;
                }
                case "edit":{
                    setData(phone,"view",this.dbWork,this.dialogStage);
                    changeWindowType("view");
                    break;
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Удаление");
            alert.setHeaderText("Внимание!");
            alert.setContentText("Вы хотите удалить данный контакт?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()==ButtonType.OK){
                PhoneDAO phoneDAO = new PhoneDAOImpl(dbWork);
                phoneDAO.delete(phone);
                dialogStage.close();
            }
        }
    }
    public void handleEditButton() throws ClassNotFoundException {
        if(editClicked){
            switch(type) {
                case "save": {
                    //add new phone and close the window
                    Phone newPhone = new Phone();
                    Contact contact = new Contact();
                    contact.setFirstName(fNameField.getText());
                    contact.setLastName(lNameField.getText());
                    contact.setFullName(fNameField.getText()+" "+lNameField.getText());
                    contact.setInBlackList(resolveInBlackList(inBlackListBox.getSelectionModel().getSelectedItem()));
                    ContactDAO contactDAO = new ContactDAOImpl(dbWork);
                    contactDAO.save(contact);
                    PhoneType phoneType = new PhoneType();
                    PhoneTypeDAO phoneTypeDAO = new PhoneTypeDAOImpl(dbWork);
                    phoneType = phoneTypeDAO.getByNameAndCode(typeBox.getSelectionModel().getSelectedItem(),
                            codeBox.getSelectionModel().getSelectedItem()).get();
                    newPhone.setPhoneNumber(phoneField.getText());
                    newPhone.setContact(contact);
                    newPhone.setPhoneType(phoneType);
                    PhoneDAO phoneDAO = new PhoneDAOImpl(dbWork);
                    phoneDAO.save(newPhone);
                    dialogStage.close();
                    break;
                }

                case "edit": {
                    //edit phone and leave the window open
                    Phone newPhone = phone;
                    newPhone.getContact().setFirstName(fNameField.getText());
                    newPhone.getContact().setLastName(lNameField.getText());
                    newPhone.getContact().setFullName(fNameField.getText()+" "+lNameField.getText());
                    newPhone.getContact().setInBlackList(
                            resolveInBlackList(inBlackListBox.getSelectionModel().getSelectedItem()));
                    newPhone.setPhoneNumber(phoneField.getText());
                    PhoneTypeDAO phoneTypeDAO = new PhoneTypeDAOImpl(dbWork);
                    PhoneType phoneType = phoneTypeDAO.getByNameAndCode(typeBox.getSelectionModel().getSelectedItem(),
                            codeBox.getSelectionModel().getSelectedItem()).get();
                    newPhone.setPhoneType(phoneType);
                    PhoneDAO phoneDAO = new PhoneDAOImpl(dbWork);
                    ContactDAO contactDAO = new ContactDAOImpl(dbWork);
                    phoneDAO.update(newPhone);
                    contactDAO.update(newPhone.getContact());
                    changeWindowType("view");
                    break;
                }
            }
        }
        else {
            changeWindowType("edit");
        }
    }
    public void changeWindowType(String type){
        this.type=type;
        fNameLabel.setVisible(!fNameLabel.isVisible());
        fNameLabel.setDisable(!fNameLabel.isDisabled());
        lNameLabel.setVisible(!lNameLabel.isVisible());
        lNameLabel.setDisable(!fNameLabel.isDisabled());
        codeLabel.setVisible(!codeLabel.isVisible());
        codeLabel.setDisable(!codeLabel.isDisabled());
        phoneLabel.setVisible(!phoneLabel.isVisible());
        phoneLabel.setDisable(!phoneLabel.isDisabled());
        phoneTypeLabel.setVisible(!phoneTypeLabel.isVisible());
        phoneTypeLabel.setDisable(!phoneTypeLabel.isDisabled());
        inBlackListLabel.setVisible(!inBlackListLabel.isVisible());
        inBlackListLabel.setDisable(!inBlackListLabel.isDisabled());
        fNameField.setVisible(!fNameField.isVisible());
        fNameField.setDisable(!fNameField.isDisabled());
        lNameField.setVisible(!lNameField.isVisible());
        lNameField.setDisable(!lNameField.isDisable());
        codeBox.setVisible(!codeBox.isVisible());
        codeBox.setDisable(!codeBox.isDisabled());
        phoneField.setVisible(!phoneField.isVisible());
        phoneField.setDisable(!phoneField.isDisabled());
        inBlackListBox.setVisible(!inBlackListBox.isVisible());
        inBlackListBox.setDisable(!inBlackListBox.isDisabled());
        typeBox.setVisible(!typeBox.isVisible());
        typeBox.setDisable(!typeBox.isDisabled());
        switch (type){
            case "view": {
                editClicked=false;
                setImageView(type);
                titleLabel.setText(phone.getContact().getFullName());
                editButton.setText("Изменить");
                deleteButton.setText("Удалить");
                setLabelData(phone);
                break;
            }
            case "edit":{
                editClicked=true;
                setImageView(type);
                deleteButton.setText("Отмена");
                editButton.setText("Сохранить");
                titleLabel.setText("Изменить");
                setFieldData(phone);
                break;
            }
            case "save":{
                editClicked=true;
                setImageView(type);
                deleteButton.setText("Отмена");
                editButton.setText("Сохранить");
                titleLabel.setText("Добавить");
                codeBox.setDisable(true);
                setFieldData(phone);
                break;
            }
        }
    }
    private void setFieldData(Phone phone){
        if(phone.getId()!=null){
            fNameField.setText(phone.getContact().getFirstName());
            lNameField.setText(phone.getContact().getLastName());
            phoneField.setText(phone.getPhoneNumber());
            typeBox.setValue(phone.getPhoneType().getFullName());
            codeBox.setValue(phone.getPhoneType().getCode());
            inBlackListBox.setValue(resolveInBlackList(phone.getContact().getInBlackList()));
        }
    }
    private void setLabelData(Phone phone){
        titleLabel.setText(phone.getContact().getFullName());
        fNameLabel.setText(phone.getContact().getFirstName());
        lNameLabel.setText(phone.getContact().getLastName());
        codeLabel.setText(phone.getPhoneType().getCode());
        phoneTypeLabel.setText(phone.getPhoneType().getFullName());
        phoneLabel.setText(phone.getPhoneType().getCode()+phone.getPhoneNumber());
        inBlackListLabel.setText(resolveInBlackList(phone.getContact().getInBlackList()));
    }
    private void setImageView(String type){
        switch (type){
            case "view":{
                Image image = new Image(getClass().getResourceAsStream("/Static/agenda.png"));
                imageView.setImage(image);
                break;
            }
            case "edit":
            case "save":{
                Image image = new Image(getClass().getResourceAsStream("/Static/add-contact.png"));
                imageView.setImage(image);
                break;
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        yesNoList.add("Да");
        yesNoList.add("Нет");
        inBlackListBox.setItems(yesNoList);
        try {
            setTypeBoxData();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        typeBox.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    try {
                        setCodeBoxData(newValue);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                })
        );
        codeBox.getSelectionModel().selectedItemProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    try {
                        openAddDialog(newValue);
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                })
        );
    }
    public void setCodeBoxData(String name) throws ClassNotFoundException {
        PhoneTypeDAO phoneTypeDAO = new PhoneTypeDAOImpl(dbWork);
        List<PhoneType> phoneTypes = phoneTypeDAO.getByName(name);
        List<String> codeStrings = new ArrayList<>();
        for(PhoneType phoneType: phoneTypes){
            codeStrings.add(phoneType.getCode());
        }
        codeStrings.add("Добавить...");
        codeList.clear();
        codeList.addAll(codeStrings);
        codeBox.setItems(codeList);
        codeBox.setDisable(false);
    }
    public void openAddDialog(String code) throws IOException, ClassNotFoundException {
        if (code!=null&&code.equals("Добавить...")){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/FXML/AddCode.fxml"));
            AnchorPane anchorPane = (AnchorPane) loader.load();
            AddCodeController addCodeController = loader.getController();
            Scene scene = new Scene(anchorPane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            addCodeController.setDialogStage(stage);
            addCodeController.setDbWork(dbWork);
            stage.setTitle("Добавить код");
            stage.showAndWait();
            setCodeBoxData(typeBox.getSelectionModel().getSelectedItem());
        }
    }
    public void setTypeBoxData() throws ClassNotFoundException {
        PhoneTypeDAO phoneTypeDAO = new PhoneTypeDAOImpl(dbWork);
        List<PhoneType> phoneTypes = phoneTypeDAO.findAll();
        List<String> phoneTypeStrings = new ArrayList<>();
        for(PhoneType phoneType: phoneTypes){
            String name = phoneType.getFullName();
            if(!phoneTypeStrings.contains(name))
                phoneTypeStrings.add(name);
        }
        typesList.clear();
        typesList.addAll(phoneTypeStrings);
        typeBox.setItems(typesList);
    }
}
