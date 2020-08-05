package org.celstec.arlearn2.beans.api;

public class ConnectionInvitation {
    public String email;
    public String note;
    public boolean addpers;

    public ConnectionInvitation () {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isAddpers() {
        return addpers;
    }

    public void setAddpers(boolean addpers) {
        this.addpers = addpers;
    }
}
