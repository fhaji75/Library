package model;

public class UserBookModel {
    String userId;
    String BookId;
    String borrowDate;
    String deliverDate;
    String latency;
    String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getBookId() {
        return BookId;
    }
    public void setBookId(String bookId) {
        BookId = bookId;
    }
    public String getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }
    public String getDeliverDate() {
        return deliverDate;
    }
    public void setDeliverDate(String deliverDate) {
        this.deliverDate = deliverDate;
    }
    public String getLatency() {
        return latency;
    }
    public void setLatency(String latency) {
        this.latency = latency;
    }
    public UserBookModel(){}
    public UserBookModel(String iduser,String idBook,String dateBorrow,String datedeliver,String latency){
        setUserId(iduser);
        setBookId(idBook);
        setBorrowDate(dateBorrow);
        setDeliverDate(datedeliver);
        setLatency(latency);
    }
    public UserBookModel(String idUser,String idBook,String dateBorrow,String datedeliver,String latency,String title){
        setUserId(idUser);
        setBookId(idBook);
        setBorrowDate(dateBorrow);
        setDeliverDate(datedeliver);
        setLatency(latency);
        setTitle(title);
    }
}
