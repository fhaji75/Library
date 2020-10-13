package model;

import java.sql.Blob;

public class UserModel {
    private String userId;
    private String userName;
    private String userPassword;
    private String AccessLevel;
    private Blob imageUser;

    public Blob getImageUser() {
        return imageUser;
    }
    public void setImageUser(Blob imageUser) {
        this.imageUser = imageUser;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getAccessLevel() {
        return AccessLevel;
    }
    public void setAccessLevel(String accessLevel) {
        AccessLevel = accessLevel;
    }
    public UserModel(){}
   /* public UserModel(String idUser,String Name){
        setUserId(idUser);
        setUserName(Name);
    }
    */
    public UserModel(String id,String name,String password,String accessLevel,Blob userImage){
        setAccessLevel(accessLevel);
        setUserId(id);
        setUserName(name);
        setUserPassword(password);
        setImageUser(userImage);
    }
}
