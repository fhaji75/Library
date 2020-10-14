package connection;

import model.AdminModel;
import model.UserBookModel;
import model.UserModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    String dbName="Library";
    String userName="root";
    String password="@941041053f.";
    public Connection connection;
    public Statement stmn;
    public boolean getConnection(){
        try {
            connection= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
            stmn=connection.createStatement();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public UserModel getUserInfo(String userId) throws SQLException{
        connection= DriverManager.getConnection("jdbc:mysql://localhost/"+dbName,userName,password);
        stmn=connection.createStatement();
        try(ResultSet rs=stmn.executeQuery("select * from userlibrary where id="+userId+";")){
            rs.next();
                UserModel usermodel= new UserModel(rs.getString("id"),rs.getString("name"),rs.getString("password"),rs.getString("AccessLevel"),rs.getBlob("image"));
            return usermodel ;
        }
    }
    public List<UserModel> getUserList() throws SQLException {
        try(ResultSet rs=stmn.executeQuery("select * from userlibrary")){
            List<UserModel> userList = new ArrayList<>();
            while (rs.next()) {
                UserModel usermodel = new UserModel(rs.getString("id"),rs.getString("name"),rs.getString("password"),rs.getString("AccessLevel"),rs.getBlob("image"));
                userList.add(usermodel);
            }
            return userList ;
        }
    }
    public List<AdminModel> getBooksId() throws SQLException {
        try(ResultSet rs=stmn.executeQuery("select * from book")){
            List<AdminModel> booksId = new ArrayList<>();
            while (rs.next()) {
                AdminModel m = new AdminModel(rs.getString("isbn"),rs.getString("title"),rs.getString("count"),
                        rs.getString("pages"),rs.getString("publish_year"),rs.getString("author_name"));
                booksId.add(m);
            }
            return booksId ;

        }
    }
    public String getBookCapacity(String bookId) throws  SQLException{
        try(ResultSet rs=stmn.executeQuery("select maxCapacity from book where isbn="+bookId+";")){
            String capacity;
            rs.next();
            capacity=rs.getString("capacity");
            return  capacity;
        }
    }
    public String getBookCount(String bookId) throws  SQLException{
        try(ResultSet rs=stmn.executeQuery("select count from book where isbn="+bookId+";")){
            String count;
            rs.next();
            count=rs.getString("count");
            return  count;
        }
    }
    public String getBookName(String bookId) throws  SQLException{
        try(ResultSet rs=stmn.executeQuery("select title from book where isbn="+bookId+";")){
            String titleBook;
            rs.next();
            titleBook=rs.getString("title");
            return  titleBook;
        }
    }
    public List<UserBookModel> getBorrowList() throws SQLException {
        try(ResultSet rs=stmn.executeQuery("select * from user_book")){
            List<UserBookModel> borrowList=new ArrayList<>();
            while (rs.next()){
                UserBookModel list=new UserBookModel(rs.getString("userId"),rs.getString("bookId"),
                        rs.getString("takeDate"),rs.getString("giveDate"),rs.getString("latency"));
                borrowList.add(list);
            }
            return borrowList;
        }
    }
    public List<UserBookModel> getBorrowListUser(String idUser) throws SQLException{
        try(ResultSet rs=stmn.executeQuery("select * from user_book where userId="+idUser+";")){
            List<UserBookModel> list=new ArrayList<>();
            while(rs.next()) {
                UserBookModel borrowList=new UserBookModel(rs.getString("userId"), rs.getString("bookId"),
                        rs.getString("takeDate"), rs.getString("giveDate"), rs.getString("latency"));
                list.add(borrowList);
            }
            return list;
        }
    }
    public List<UserBookModel> getDeliveredList(String idUser) throws SQLException{
        try(ResultSet rs=stmn.executeQuery("select * from user_book where userId="+idUser+" and giveDate IS NOT NULL;")){
            List<UserBookModel> list=new ArrayList<>();
            while(rs.next()) {
                UserBookModel borrowList=new UserBookModel(rs.getString("userId"), rs.getString("bookId"),
                        rs.getString("takeDate"), rs.getString("giveDate"), rs.getString("latency"));
                list.add(borrowList);
            }
            return list;
        }
    }
    public List<UserBookModel> getInBorrowList(String idUser) throws SQLException{
        try(ResultSet rs=stmn.executeQuery("select * from user_book where userId="+idUser+" and giveDate IS NULL;")){
            List<UserBookModel> list=new ArrayList<>();
            while(rs.next()) {
                UserBookModel borrowList=new UserBookModel(rs.getString("userId"), rs.getString("bookId"),
                        rs.getString("takeDate"), rs.getString("giveDate"), rs.getString("latency"));
                list.add(borrowList);
            }
            return list;
        }
    }
}



