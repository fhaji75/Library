package model;

public class AdminModel {
    private String isbn;
    private String title;
    private String count;
    private String pages;
    private String publishYear;
    private String authorName;
    private String capacity;

    public String getCapacity() {
        return capacity;
    }
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }
    public String getPages() {
        return pages;
    }
    public void setPages(String pages) {
        this.pages = pages;
    }
    public String getPublishYear() {
        return publishYear;
    }
    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }
    public String getAuthorName() {
        return authorName;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public AdminModel(){}
    public AdminModel(String isbnb){
        setIsbn(isbnb);
    }
    public AdminModel(String isbnb,String bookName){
        setTitle(bookName);
        setIsbn(isbnb);
    }
    public AdminModel(String isbnb,String titleb,String countb,String pagesb,String publishYearb,String authorNameb){
        setIsbn(isbnb);
        setTitle(titleb);
        setCount(countb);
        setPages(pagesb);
        setPublishYear(publishYearb);
        setAuthorName(authorNameb);

    }
    public AdminModel(String isbnb,String titleb,String countb,String pagesb,String publishYearb,String authorNameb,String maxCapacity){
        setIsbn(isbnb);
        setTitle(titleb);
        setCount(countb);
        setPages(pagesb);
        setPublishYear(publishYearb);
        setAuthorName(authorNameb);
        setCapacity(maxCapacity);
    }
}
