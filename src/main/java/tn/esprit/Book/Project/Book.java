package tn.esprit.Book.Project;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class Book {

	
	@Pattern(regexp = "^\\d+$",
            message = "ID Number must be a non-negative integer!")
	 private int id;

	    @NotEmpty(message = "All book  must have a title!")
	    private String title;
	    private String description;
	    private String isbn;
	    private String publisher;
	    private String language;
	    private String author;
	    private float price;
	    private int pages;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getIsbn() {
			return isbn;
		}
		public void setIsbn(String isbn) {
			this.isbn = isbn;
		}
		public String getPublisher() {
			return publisher;
		}
		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}
		public String getLanguage() {
			return language;
		}
		public void setLanguage(String language) {
			this.language = language;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
		}
		public int getPages() {
			return pages;
		}
		public void setPages(int pages) {
			this.pages = pages;
		}
	    
	    
	    
	    
}



