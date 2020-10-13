package user;

import connection.DBConnection;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.AdminModel;
import model.UserBookModel;
import model.UserModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;
import static javafx.scene.paint.Color.RED;

public class UserController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connected.getConnection();
    }
    public String userId;
    public void setUserId(String idUser){
        userId=idUser;
    }
    public String getUserId(){
        return userId;
    }
    DBConnection connected =new DBConnection();
    @FXML
    private ImageView imageView;
    @FXML
    private Label info_namelabel;
    @FXML
    private Label info_idlabel;
    @FXML
    private Button info_upimage;
    @FXML
    private Label info_llabel;
    @FXML
    private Label info_stclabel;
    @FXML
    private Button info_uplatencybtn;
    @FXML
    private TextField info_passtext;
    @FXML
    private TextField info_rpasstext;
    @FXML
    private Button info_subbtn;
    @FXML
    private TextField borrow_sisbnt;
    @FXML
    private TextField borrow_stitlet;
    @FXML
    private TextField borrow_sant;
    @FXML
    private Button borrow_ssubbtn;
    @FXML
    private TextField borrow_bisbnt;
    @FXML
    private Button borrow_bdisplaybtn;
    @FXML
    private Button borrow_bresetbtn;
    @FXML
    private Button borrow_bbtn;
    @FXML
    private Label borrow_nlabel;
    @FXML
    private Label borrow_existlabel;
    @FXML
    private TableView borrow_tabel;
    @FXML
    private TableView clearing_tabel;
    @FXML
    private Label clearing_llabel;
    @FXML
    private Button clearing_cbtn;
    @FXML
    private TableView activity_tabel;
    @FXML
    private TableView delivery_tabel;
    @FXML
    private TableView inb_tabel;
    @FXML
    private Button user_logOutbtn;
    @FXML
    private TableColumn<String,UserBookModel> isbnC;
    @FXML
    private TableColumn<String,AdminModel>titleC;
    @FXML
    private TableColumn<String,UserBookModel>startC;
    @FXML
    private TableColumn<String,UserBookModel>endC;
    @FXML
    private TableColumn<String,UserBookModel>latencyC;
    @FXML
    private TableColumn<String,UserBookModel> isbnCD;
    @FXML
    private TableColumn<String,AdminModel>titleCD;
    @FXML
    private TableColumn<String,UserBookModel>startCD;
    @FXML
    private TableColumn<String,UserBookModel>endCD;
    @FXML
    private TableColumn<String,UserBookModel>latencyCD;
    @FXML
    private TableColumn<String,UserBookModel> isbnCB;
    @FXML
    private TableColumn<String,AdminModel>titleCB;
    @FXML
    private TableColumn<String,UserBookModel>startCB;
    @FXML
    private TableColumn<String,UserBookModel>endCB;
    @FXML
    private TableColumn<String,UserBookModel>latencyCB;
    @FXML
    private TableColumn<String,String>expectedC;
    @FXML
    private TableColumn<String,AdminModel> borrowIsbn;
    @FXML
    private TableColumn<String,AdminModel> borrowTitle;
    @FXML
    private TableColumn<String,AdminModel> borrowAname;
    @FXML
    private TableColumn<String,AdminModel> borrowPage;
    @FXML
    private TableColumn<String,AdminModel> borrowPublish;
    @FXML
    private TableColumn<String,AdminModel> borrowCount;
    @FXML
    private TableColumn<String,AdminModel> titleCC;
    @FXML
    private TableColumn<String,AdminModel> startCC;
    @FXML
    private TableColumn<String,AdminModel> endCC;
    @FXML
    private TableColumn<String,AdminModel> latencyCC;

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
    public void showInfoMessage(String message){
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }
    public void showErrorMessage(String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(message);
        errorAlert.showAndWait();
    }
    private void showImage() throws IOException {
        try {
            UserModel userModelList =connected.getUserInfo(userId);
            int len = (int) userModelList.getImageUser().length();
            byte[] buf = userModelList.getImageUser().getBytes(1, len);
            ByteArrayInputStream bis = new ByteArrayInputStream(buf);
            BufferedImage read = ImageIO.read(bis);
            Image image = SwingFXUtils.toFXImage(read, null);
            imageView.setImage(image);
            imageView.setFitHeight(116);
            imageView.setFitWidth(154);

        }catch (NullPointerException | SQLException e){
            showInfoMessage("Please upload image!");
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
    public void showInfo(){
        try {
            showImage();
            UserModel userModelList =connected.getUserInfo(userId);
            info_namelabel.setText(userModelList.getUserName());
            info_idlabel.setText(userModelList.getUserId());
            info_llabel.setText(converterToString(updateLatency()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public long updateLatency(){
        //ino button nakonm ino hamintori tabe konim ke lazem shod farakhani beshe
        try {
            long local=0,newLatency=0;
            List<UserBookModel> list = connected.getBorrowListUser(userId);
            for(UserBookModel bookModel:list){
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date realTime= simpleDateFormat.parse(bookModel.getBorrowDate());
                java.sql.Date time=new Date(realTime.getTime());
                LocalDate takeTime=time.toLocalDate();//tarikh gereftan
                if(bookModel.getDeliverDate()==null){//bargasht nadare
                    LocalDate today=LocalDate.now();//emroz
                    Period difference=Period.between(today,takeTime);
                    newLatency=difference.getDays();
                }
                else{//bargasht dare
                    java.util.Date deliverTime= simpleDateFormat.parse(bookModel.getDeliverDate());
                    java.sql.Date timeDeliver=new Date(deliverTime.getTime());//tarikh bargasht
                    Period difference=Period.between(timeDeliver.toLocalDate(),takeTime);
                    newLatency=difference.getDays();
                }
                if(newLatency>14){
                    newLatency=newLatency-14;
                }
                else{
                    newLatency=0;
                }
                //jam va update satr
                local+=newLatency;
                if(newLatency>0){
                    Statement stmn=connected.connection.createStatement();
                    stmn.executeUpdate("update user_book set latency="+newLatency+
                            " where userId="+bookModel.getUserId()+" and bookId="+bookModel.getBookId()+";");
                }
            }
            return local;
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }
    public void uploadImage(){//age kas bozorgtar bod taklif chie
        try {
            if(checkConformity("conformity","Are you sure?")) {
                FileChooser chooser = new FileChooser();
                chooser.setTitle("Select image location");
                File file = chooser.showOpenDialog(info_upimage.getScene().getWindow());
                PreparedStatement psmnt;
                psmnt = connected.connection.prepareStatement("update userlibrary set image=(?) where id="
                        + userId + ";");
                FileInputStream fis = new FileInputStream(file);
                psmnt.setBinaryStream(1, (InputStream) fis, (int) (file.length()));
                psmnt.executeUpdate();
                showImage();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void submitPassword(){
        if(!info_passtext.getText().equals("")&&!info_rpasstext.getText().equals("")){
            if(info_passtext.getText().equals(info_rpasstext.getText())){
                if(info_passtext.getText().length()>6){
                    try {
                        Statement stmnt = connected.connection.createStatement();
                        stmnt.executeUpdate("update userlibrary set password="+info_passtext.getText()+
                                " where id="+userId+";");
                        showInfoMessage("Password changed!");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    showErrorMessage("Password should be more than 6 character");
                }
            }
            else{
                showErrorMessage("Check two box have same content!");
            }
        }
        else{
            showInfoMessage("password and repeat password should be not null!");
        }

    }
    private void showData(String idBook,String title,String authorName,String page,String publishYear,String count){
        borrow_tabel.getItems().clear();
        borrowIsbn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        borrowTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowAname.setCellValueFactory(new PropertyValueFactory<>("count"));
        borrowPage.setCellValueFactory(new PropertyValueFactory<>("pages"));
        borrowPublish.setCellValueFactory(new PropertyValueFactory<>("publishYear"));
        borrowCount.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        borrow_tabel.getItems().add(new AdminModel(idBook,title,authorName,page,publishYear,count));
    }
    public void submitSearch() throws SQLException {
        if(!borrow_sisbnt.getText().equals("")){
            int flag=0;
            List<AdminModel> bookList=connected.getBooksId();
            for(AdminModel listBook:bookList){
                if(listBook.getIsbn().equals(borrow_sisbnt.getText())){
                    flag++;
                    showData(listBook.getIsbn(),listBook.getTitle(),listBook.getAuthorName(),listBook.getPages(),listBook.getPublishYear(),listBook.getCount());
                }
            }
            if(flag==0){
                showErrorMessage("Not exist book with this isbn.");
            }
        }
        else{
            if(!borrow_stitlet.getText().equals("")){
                int flag=0;
                List<AdminModel> bookList=connected.getBooksId();
                for(AdminModel listBook:bookList){
                    if(listBook.getTitle().subSequence(0,listBook.getTitle().length()).equals(borrow_stitlet.getText())){
                        flag++;
                        showData(listBook.getIsbn(),listBook.getTitle(),listBook.getAuthorName(),listBook.getPages(),listBook.getPublishYear(),listBook.getCount());
                    }
                }

                if(flag==0){
                    showErrorMessage("Not exist book with this title.");
                }
            }
            else{
                if(!borrow_sant.getText().equals("")){
                    int flag=0;
                    List<AdminModel> bookList=connected.getBooksId();
                    for(AdminModel listBook:bookList){
                        if(listBook.getAuthorName().subSequence(0,listBook.getAuthorName().length()).equals(borrow_sant.getText())){
                            flag++;
                            showData(listBook.getIsbn(),listBook.getTitle(),listBook.getAuthorName(),listBook.getPages(),listBook.getPublishYear(),listBook.getCount());
                        }
                    }
                    if(flag==0){
                        showErrorMessage("Not exist book with this author name.");
                    }
                }
                else{
                    showInfoMessage("Please write least one of information of book");
                }
            }
        }
    }
    public void displayBorrow(){
        //inja bayad ezafeh konam age ketab nabod to label exsit ina bayad benevise ke
        //ketab hast ya na
        try {
            if(!borrow_bisbnt.getText().equals("")) {
                List<AdminModel> bookList = connected.getBooksId();
                for (AdminModel listBook : bookList) {
                    if (listBook.getIsbn().equals(borrow_bisbnt.getText())) {
                        showData(listBook.getIsbn(), listBook.getTitle(), listBook.getAuthorName(), listBook.getPages(), listBook.getPublishYear(), listBook.getCount());
                    }
                }
            }
            else{
                showInfoMessage("Please write isbn of book");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void resetBorrow(){
        borrow_bisbnt.setText("");
    }
    public void borrowBook(){
        //vagti ino zad aval check mikoneh ke aya bishtar az 10 ta ketab garz
        //gerefte ya na age gerefteh bashe kadr notigy germez mishe va ye neveshte chap mishe ke nemitoni
        //age na aval mige motmaeni age goft bale bad barash sabt mishe va to kadr notify ba galam sabz
        //mige taha la chan ta ketab bardashti
        try {
            if(!borrow_bisbnt.getText().equals("")) {
                int flag=0;
                List<AdminModel> bookList=connected.getBooksId();
                for(AdminModel listBook:bookList){
                    if(listBook.getIsbn().equals(borrow_bisbnt.getText())){
                        flag++;
                        break;
                    }
                }
                if(flag!=0) {
                    List<UserBookModel> borrow = connected.getBorrowListUser(userId);
                    if (borrow.size() < 10) {
                        //check konim ketab hast be tedad manzor
                        String bookCount=connected.getBookCount(borrow_bisbnt.getText());
                        if(parseInt(bookCount)>0) {
                            LocalDate today=LocalDate.now();
                            java.sql.Date date=java.sql.Date.valueOf(today);
                            if (checkConformity("Conformity", "Do you wnat to get borrow book?")) {
                                Statement stmn = connected.connection.createStatement();
                                stmn.executeUpdate("insert into user_book(userId,bookId,takeDate) values" +
                                        "("+userId+","+borrow_bisbnt.getText()+",'"+date+"');");
                                showInfoMessage("Borrowed  book successfully");
                            }
                        }
                        else{
                            borrow_existlabel.setText("Book not exist in storage");
                        }
                    } else {
                        borrow_nlabel.setText("You had borrow 10 book  so you can not to borrow another book");
                        borrow_nlabel.setTextFill(RED);
                    }
                }
                else{
                    showErrorMessage("Not exist book with this isbn");
                }
            }
            else{
                showInfoMessage("please enter isbn of book in borrow box");
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    public void showHistory(){
        clearing_tabel.getItems().clear();
        try {
            clearing_llabel.setText(converterToString(updateLatency()));
            List<UserBookModel> listBorrow = connected.getBorrowListUser(userId);
            for(UserBookModel borrowList:listBorrow){
                titleCC.setCellValueFactory(new PropertyValueFactory<>("title"));
                startCC.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
                endCC.setCellValueFactory(new PropertyValueFactory<>("deliverDate"));
                latencyCC.setCellValueFactory(new PropertyValueFactory<>("latency"));
                clearing_tabel.getItems().addAll(new UserBookModel(userId,borrowList.getBookId(),
                        borrowList.getBorrowDate(),borrowList.getDeliverDate(),borrowList.getLatency(),connected.getBookName(borrowList.getBookId())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void clearHistory(){
        try {
            if (checkConformity("Conformity", "Are you sure ro clear history of user?")) {
                Statement statement = connected.connection.createStatement();
                LocalDate today=LocalDate.now();
                java.sql.Date date=java.sql.Date.valueOf(today);
                List<UserBookModel> listBorrow=connected.getBorrowListUser(userId);
                for(UserBookModel borrowList:listBorrow){
                    if(borrowList.getDeliverDate()==null) {
                        statement.executeUpdate("update user_book set latency=0 where userId=" + userId + " and bookId="+borrowList.getBookId()+"");
                        statement.executeUpdate("update user_book set  giveDate='"+date.toString()+"' where userId=" + userId + " and bookId="+borrowList.getBookId()+"");
                    }
                    else{
                        System.out.println(borrowList.getBookId());
                        statement.executeUpdate("update user_book set latency=0 where userId="+ userId +" and bookId="+borrowList.getBookId()+"");
                    }
                }
                showInfoMessage("Successful clearing!");
                updateLatency();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showActivity(){
        activity_tabel.getItems().clear();
        try {
            clearing_llabel.setText(converterToString(updateLatency()));
            List<UserBookModel> listBorrow = connected.getBorrowListUser(userId);
            for(UserBookModel borrowList:listBorrow){
                isbnC.setCellValueFactory(new PropertyValueFactory<>("BookId"));
                titleC.setCellValueFactory(new PropertyValueFactory<>("title"));
                startC.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
                endC.setCellValueFactory(new PropertyValueFactory<>("deliverDate"));
                latencyC.setCellValueFactory(new PropertyValueFactory<>("latency"));
                activity_tabel.getItems().addAll(new UserBookModel(userId,borrowList.getBookId(),
                        borrowList.getBorrowDate(),borrowList.getDeliverDate(),borrowList.getLatency(),connected.getBookName(borrowList.getBookId())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showDelivery(){
        delivery_tabel.getItems().clear();
        try {
            updateLatency();
            List<UserBookModel> listBorrow = connected.getDeliveredList(userId);
            for(UserBookModel borrowList:listBorrow){
                isbnCD.setCellValueFactory(new PropertyValueFactory<>("BookId"));
                titleCD.setCellValueFactory(new PropertyValueFactory<>("title"));
                startCD.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
                endCD.setCellValueFactory(new PropertyValueFactory<>("deliverDate"));
                latencyCD.setCellValueFactory(new PropertyValueFactory<>("latency"));
                delivery_tabel.getItems().addAll(new UserBookModel(userId,borrowList.getBookId(),
                        borrowList.getBorrowDate(),borrowList.getDeliverDate(),borrowList.getLatency(),connected.getBookName(borrowList.getBookId())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void showBorrowing(){
        inb_tabel.getItems().clear();
        try {
            updateLatency();
            List<UserBookModel> listBorrow = connected.getInBorrowList(userId);
            String expectedDate;
            for(UserBookModel borrowList:listBorrow){
                expectedDate=borrowList.getBorrowDate();
                Calendar c=Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                c.setTime(sdf.parse(expectedDate));
                c.add(Calendar.DAY_OF_MONTH,14);
                expectedDate = sdf.format(c.getTime());
                isbnCB.setCellValueFactory(new PropertyValueFactory<>("BookId"));
                titleCB.setCellValueFactory(new PropertyValueFactory<>("title"));
                startCB.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
                expectedC.setCellValueFactory(new PropertyValueFactory<>("deliverDate"));
                latencyCB.setCellValueFactory(new PropertyValueFactory<>("latency"));
                inb_tabel.getItems().addAll(new UserBookModel(userId,borrowList.getBookId(),
                        borrowList.getBorrowDate(),expectedDate,borrowList.getLatency(),connected.getBookName(borrowList.getBookId())));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void logOutUser() throws IOException {
        if(checkConformity("Confirm log out","Do you want to log out from system?")) {
            Stage stage = (Stage) this.user_logOutbtn.getScene().getWindow();
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
