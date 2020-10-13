package loginApp;

import admin.AdminController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import connection.DBConnection;
import model.UserModel;
import user.UserController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    DBConnection connected=new DBConnection();
    @FXML
    private Label status;
    @FXML
    private Label userLabel;
    @FXML
    private Label passLabel;
    @FXML
    private Label comboLabel;
    @FXML
    public TextField userText;
    @FXML
    private TextField passText;
    @FXML
    private ComboBox<Option> combo;
    @FXML
    public Button submitbtn;
    @FXML
    private Button resetbtn;
    @FXML
    private Label notifyLabel;
    @FXML
    private Button exitbtn;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (this.connected.getConnection()) {
            this.status.setText("Connected");
        } else {
            this.status.setText("Not Connected");
        }
        combo.setItems(FXCollections.observableArrayList(Option.values()));
    }
    @FXML
    public void Login(){
        try {
            notifyLabel.setText("");
            int flag=0;
            List<UserModel> userList = connected.getUserList();
            for (UserModel mm : userList) {
                if (mm.getUserId().equals(userText.getText()) &&
                        mm.getUserPassword().equals(passText.getText()) &&
                        mm.getAccessLevel().equals(combo.getValue().toString()))//inja
                {
                    flag++;
                    Stage stage = (Stage)this.submitbtn.getScene().getWindow();
                    stage.close();
                    switch (combo.getValue().toString()) {
                        case "Admin": {
                            adminPage();
                            break;
                        }
                        case "User": {
                            userPage();
                        }
                    }
                }
            }
            if(flag==0){
                notifyLabel.setText("incorrect information!");
            }
        } catch (NullPointerException e1) {
            notifyLabel.setText("select access level!");
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }
    @FXML
    public void Reset(){
        userText.setText("");
        passText.setText("");
        combo.getItems().clear();
        combo.setItems(FXCollections.observableArrayList(Option.values()));
        notifyLabel.setText("");
    }
    @FXML
    public void exitSystem(){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Exist System");
        confirm.setContentText("Are you sure exit from system?");
        confirm.getButtonTypes().clear();
        confirm.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if(confirm.getResult()==ButtonType.YES){
            Stage stage1=(Stage)submitbtn.getScene().getWindow();
            stage1.close();
        }
    }
    public void adminPage() throws IOException {
        FXMLLoader loader=new FXMLLoader();
        AnchorPane pane=(AnchorPane) loader.load(getClass().getResource("/admin/admin.fxml").openStream());
        AdminController adminController = (AdminController) loader.getController();
        Scene scene=new Scene(pane,600,400);
        Stage stage=new Stage();
        stage.setTitle("Admin Page");
        stage.setScene(scene);
        stage.show();
    }
    public void userPage() throws IOException {
        FXMLLoader loader=new FXMLLoader();
        AnchorPane pane=(AnchorPane) loader.load(getClass().getResource("/user/user.fxml").openStream());
        UserController controller=loader.getController();
        controller.setUserId(userText.getText());
        Scene scene=new Scene(pane,600,400);
        Stage stage=new Stage();
        stage.setTitle("User Page");
        stage.setScene(scene);
        stage.show();
    }
}
