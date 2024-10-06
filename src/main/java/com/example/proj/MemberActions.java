package com.example.proj;

import java.util.Date;

public abstract class MemberActions {
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public MemberActions(Date creationDate) {
        this.creationDate = creationDate;
    }
}
