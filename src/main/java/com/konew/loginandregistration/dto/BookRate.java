package com.konew.loginandregistration.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class BookRate {
    @Min(1)
    @Max(10)
    private int rate;
    private long bookId;
    private long userID;

    public BookRate() {
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getUserID() {
        return userID;
    }
}
