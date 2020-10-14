package admin;

import connection.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import loginApp.LoginController;
import model.AdminModel;
import model.UserBookModel;
import model.UserModel;

import java.io.IOException;
import java.net.URL;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class AdminController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connected.getConnection();
    }
    DBConnection connected =new DBConnection();
    @FXML
    private Button rbbtn;//read book button
    @FXML
    private Button rubtn;//read user button
    @FXML
    private TableView<AdminModel> rbt;//read book table
    @FXML
    private TableView<UserModel> rut;//read user table
    @FXML
    private TextField iu_uit;//insert update user id textField
    @FXML
    private TextField iu_unt;
    @FXML
    private TextField iu_upt;
    @FXML
    private Button iuserbtn;
    @FXML
    private Button uuserbtn;
    @FXML
    private Button ruserbtn;
    @FXML
    private TextField d_uit;
    @FXML
    private TextField clear_uit;
    @FXML
    private Button clearuserbtn;
    @FXML
    private Button duserbtn;
    @FXML
    private TextField iu_isbn;
    @FXML
    private TextField iu_title;
    @FXML
    private TextField iu_py;
    @FXML
    private TextField iu_count;
    @FXML
    private TextField iu_pages;
    @FXML
    private TextField iu_an;
    @FXML
    private Button ibookbtn;
    @FXML
    private Button ubookbtn;
    @FXML
    private Button rbookbtn;
    @FXML
    private TextField d_isbn;
    @FXML
    private Button dbookbtn;
    @FXML
    private TextField deliver_isbn;
    @FXML
    private TextField deliver_userid;
    @FXML
    private DatePicker deliver_Date;
    @FXML
    private Button deliverbookbtn;
    @FXML
    private Button logOutbtn;
    @FXML
    private TableColumn<String,AdminModel> isbnColumn;
    @FXML
    private TableColumn<String,AdminModel> titleColumn;
    @FXML
    private TableColumn<String,AdminModel> pyColumn;
    @FXML
    private TableColumn<String,AdminModel> pageColumn;
    @FXML
    private TableColumn<String,AdminModel> countColumn;
    @FXML
    private TableColumn<String,AdminModel> anColumn;
    @FXML
    private TableColumn<String,AdminModel> capacityC;
    @FXML
    private TableColumn<String,UserModel> uiColumn;
    @FXML
    private TableColumn<String,UserModel> unColumn;

    public boolean checkNullBook(){
        if(iu_isbn.getText().equals("") ||iu_title.getText().equals("") ||iu_py.getText().equals("")
                ||iu_count.getText().equals("") ||iu_pages.getText().equals("") ||iu_an.getText().equals("")){
            return false;
        }
        return true;
    }
    public boolean checkExistBook(TextField textField) {
        try {
            List<AdminModel> ISBNList = connected.getBooksId();
            for (AdminModel adminModel : ISBNList) {
                if (textField.getText().equals(adminModel.getIsbn())) {
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkNullUser(){
        if(iu_uit.getText().equals("")||iu_unt.getText().equals("")||iu_upt.getText().equals("")){
            return true;
        }
        return false;
    }
    public boolean checkUserExist(TextField textField){
        try {
            List<UserModel> userList = connected.getUserList();
            for (UserModel usermodel : userList) {
                if (textField.getText().equals(usermodel.getUserId())) {
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public boolean checkConformity(String titleMessage, String contentMessage){
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(titleMessage);
        confirm.setContentText(contentMessage);
        confirm.getButtonTypes().clear();
        confirm.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();
        if(confirm.getResult()==ButtonType.YES){
            return true;
        }
        else{
            return false;
        }
    }
    public void printInfoMessage(String message){
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }
    public void printErrorMessage(String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }
    public void insertBookTab(ActionEvent event){
        try{
            if(checkNullBook()){
                if(!checkExistBook(iu_isbn)){
                    Statement stmnt=connected.connection.createStatement();
                    stmnt.executeUpdate("insert into book(isbn,title,count,pages,publish_year,author_name,maxCapacity) values ("+
                            iu_isbn.getText()+",'"+iu_title.getText()+"',"+iu_count.getText()+","+iu_pages.getText()+
                            ",'"+iu_py.getText()+"','"+iu_an.getText()+"',"+iu_count.getText()+");");
                    printInfoMessage("Inserted Successful:)");
                }
                else{
                    printErrorMessage("Book with this isbn is exist!");
                }
            }
            else{
                printInfoMessage("Complete input,some text fields are null!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void updateBookTab(){
        try {
            if (checkExistBook(iu_isbn)) {
                Statement stmnt = connected.connection.createStatement();
                if(!iu_title.getText().equals("")){
                    stmnt.executeUpdate("update book set title='"+iu_title.getText()+"' where isbn="+iu_isbn.getText()+";");
                }
                if(!iu_count.getText().equals("")){
                    if(parseInt(iu_count.getText())>parseInt(connected.getBookCapacity(iu_isbn.getText()))) {
                        stmnt.executeUpdate("update book set maxCapacity=" + iu_count.getText() + "where isbn=" + iu_isbn.getText() + ";");
                    }
                    else{
                        printErrorMessage("you can not to decrease capacity");
                    }
                }
                if(!iu_pages.getText().equals("")){
                    stmnt.executeUpdate("update book set pages="+iu_pages.getText()+" where isbn="+iu_isbn.getText()+";");
                }
                if(!iu_py.getText().equals("")){
                    stmnt.executeUpdate("update book set publish_year='"+iu_py.getText()+"' where isbn="+iu_isbn.getText()+";");
                }
                if(!iu_an.getText().equals("")){
                    stmnt.executeUpdate("update book set author_name='"+iu_an.getText()+"' where isbn="+iu_isbn.getText()+";");
                }
                printInfoMessage("Update Successful:)");
                iu_isbn.setText("");
                iu_title.setText("");
                iu_count.setText("");
                iu_pages.setText("");
                iu_py.setText("");
                iu_an.setText("");
            }
            else {
                printErrorMessage("Check isbn! book with this isbn is not exist!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void resetBookTab(){
        iu_py.setText("");
        iu_count.setText("");
        iu_pages.setText("");
        iu_title.setText("");
        iu_an.setText("");
        iu_isbn.setText("");
    }
    public void deleteBookTab(){
        try {
            if (checkExistBook(d_isbn)) {
                if(checkConformity("Delete book","Do you want to remove book?")) {
                    Statement stmnt = connected.connection.createStatement();
                    stmnt.executeUpdate("Delete from book where isbn=" +d_isbn.getText()+";");
                    printInfoMessage("Deleted book");
                    d_isbn.setText("");
                }
                else{
                    d_isbn.setText("");
                }
            } else {
                printErrorMessage("Book with this isbn is not exist!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String converterToString(long num){
        String result="";
        while(num>0){
            result+=((char)(num%10+48));
            num/=10;
        }
        if(result.equals("")){
            result="0";
        }
        return result;
    }
    public void deliverBookTab(){
        try {
            int flag=0;
            List<UserBookModel> borrowList = connected.getBorrowList();
            for(UserBookModel list:borrowList){
                if(deliver_isbn.getText().equals(list.getBookId())&&
                        deliver_userid.getText().equals(list.getUserId()))
                {
                    if(deliver_Date.getValue()==null){
                        printInfoMessage("Please select date of delivery");
                    }
                    else {
                        String date = deliver_Date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        Statement stmn = connected.connection.createStatement();
                        stmn.executeUpdate("update user_book set giveDate='"+date+
                                "' where userId="+deliver_userid.getText()+" and bookId="+deliver_isbn.getText()+";");
                        String oldCount=connected.getBookCount(deliver_isbn.getText());
                        long newCount=parseInt(oldCount);
                        newCount++;
                        stmn.executeUpdate("update user_book set count='"+converterToString(newCount)+
                                "' where userId="+deliver_userid.getText()+" and bookId="+deliver_isbn.getText()+";");
                        printInfoMessage("Successful deliver ;)");
                    }
                    flag++;
                }
            }
            if(flag==0){
                printErrorMessage("Information is not correct!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void insertUserTab(){
        try {
            if(!checkNullUser()){
                if(!checkUserExist(iu_uit)){
                    Statement stmn=connected.connection.createStatement();
                    stmn.executeUpdate("insert into userlibrary(id,name,password,AccessLevel)values" +
                            "("+iu_uit.getText()+",'"+iu_unt.getText()+"','"+iu_upt.getText()+"','User');");
                    printInfoMessage("Sign in new user!");
                }
                else{
                    printErrorMessage("Before user sign in library");
                }
            }
            else{
                printInfoMessage("please complete information!");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void updateUserTab(){
        try {
            if (checkUserExist(iu_uit)) {
                Statement stmnt = connected.connection.createStatement();
                if (!iu_unt.getText().equals("")) {
                    stmnt.executeUpdate("update userlibrary set name='"+iu_unt.getText()+"' where id="+iu_uit.getText()+";");
                }
                if (!iu_upt.getText().equals("")) {
                    stmnt.executeUpdate("update userlibrary set password='"+iu_upt.getText()+"' where id="+iu_uit.getText()+";");
                }
                printInfoMessage("Successfully in updating");
                iu_unt.setText("");
                iu_upt.setText("");
                iu_uit.setText("");
            } else {
                printErrorMessage("Check user id ! User not sign in library with this id ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void resetUserTab(){
        iu_uit.setText("");
        iu_unt.setText("");
        iu_upt.setText("");
    }
    public void deleteUserTab(){
        try {
            if (checkUserExist(d_uit)) {
                if(checkConformity("Delete User","Do you want to delete user?")) {
                    Statement stmnt = connected.connection.createStatement();
                    stmnt.executeUpdate("Delete from userlibrary where id="+d_uit.getText()+";");
                    printInfoMessage("Deleted User");
                    d_uit.setText("");
                }
                else{
                    d_uit.setText("");
                }
            }
            else {
                printErrorMessage("Check user id");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void clearUserTab(){
        try {
            if (checkUserExist(clear_uit)) {
                int flag=0;
                List<UserBookModel> userBookModels=connected.getBorrowList();
                for(UserBookModel borrowing:userBookModels){
                    if(borrowing.getUserId().equals(clear_uit.getText())){
                        LoginController loginController=new LoginController();
                        loginController.userPage();
                        flag++;
                        break;
                    }
                }
                if(flag==0){
                    printInfoMessage("User with this id has not borrow book");
                }
            } else {
                printErrorMessage("Check user id to be correct.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void displayBook(){
        rbt.getItems().clear();
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        pyColumn.setCellValueFactory(new PropertyValueFactory<>("publishYear"));
        pageColumn.setCellValueFactory(new PropertyValueFactory<>("pages"));
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        anColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        capacityC.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        try {
            List<AdminModel> booksList = connected.getBooksId();
            for(AdminModel listBooks:booksList){
                rbt.getItems().add(new AdminModel(listBooks.getIsbn(),listBooks.getTitle(),listBooks.getCount(),listBooks.getPages(),listBooks.getPublishYear(),listBooks.getAuthorName(),listBooks.getCapacity()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void displayUser(){
        rut.getItems().clear();
        uiColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        unColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        try {
            List<UserModel> userList = connected.getUserList();
            for(UserModel listUser:userList){
                rut.getItems().add(new UserModel(listUser.getUserId(),listUser.getUserName(),listUser.getUserPassword(),listUser.getAccessLevel(),listUser.getImageUser()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void logOutAdmin() throws IOException {
        if(checkConformity("Confirm log out","Do you want to log out from system?")) {
            Stage stage = (Stage) this.logOutbtn.getScene().getWindow();
            stage.close();
            FXMLLoader loader = new FXMLLoader();
            AnchorPane pane = (AnchorPane) loader.load(getClass().getResource("/loginApp/login.fxml").openStream());
            Scene scene = new Scene(pane, 700, 475);
            Stage stage2 = new Stage();
            stage.setTitle("Library");
            stage.setScene(scene);
            stage.show();
        }
    }
}
