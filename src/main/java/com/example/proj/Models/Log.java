package com.example.proj.Models;

import java.sql.Date;

public class Log {
     private String id;
     private java.sql.Date creationDate;
     private String bookId;

     public Log(String id, java.sql.Date creationDate, String bookId) {
          this.id = id;
          this.creationDate = creationDate;
          this.bookId = bookId;
     }

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public java.sql.Date getCreationDate() {
          return creationDate;
     }

     public void setCreationDate(Date creationDate) {
          this.creationDate = creationDate;
     }

     public String getBookId() {
          return bookId;
     }

     public void setBookId(String bookId) {
          this.bookId = bookId;
     }

     @Override
     public String toString() {
          return "Member " + id + " has borrowed book with the id of " + bookId + " in " + creationDate.toString();
     }

     @Override
     public boolean equals(Object obj) {
          if (obj == this) {
               return true;
          }
          if (!(obj instanceof Log)) {
               return false;
          }
          Log tmp = (Log) obj;
          if (tmp.getBookId().equals(this.bookId)
                  && this.id.equals(tmp.getId())
                  && this.creationDate.equals(tmp.getCreationDate())) {
               return true;
          }
          return false;
     }
}
