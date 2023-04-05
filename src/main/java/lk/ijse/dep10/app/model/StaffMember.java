package lk.ijse.dep10.app.model;

import javafx.scene.image.ImageView;
import lk.ijse.dep10.app.util.Gender;
import lk.ijse.dep10.app.util.Status;

import java.io.Serializable;
import java.sql.Blob;

public class StaffMember implements Serializable {
    private String id;
    private String name;
    private String address;
    private String contact;
    private Gender gender;
    private Status status;
    private ImageView image;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public StaffMember() {
    }

    public StaffMember(String id, String name, String address, String contact, Gender gender, Status status, ImageView image) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.gender = gender;
        this.status = status;
        this.image = image;
    }


}
