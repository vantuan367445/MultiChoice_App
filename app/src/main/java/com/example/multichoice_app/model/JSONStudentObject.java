package com.example.multichoice_app.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JSONStudentObject {

    private String url_Photo;

    public String getUrl_Photo() {
        return url_Photo;
    }

    public void setUrl_Photo(String url_Photo) {
        this.url_Photo = url_Photo;
    }

    public Boolean getExisted() {
        return isExisted;
    }

    public void setExisted(Boolean existed) {
        isExisted = existed;
    }

    @SerializedName("is_existed")
    @Expose
    private Boolean isExisted;
    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;

    public Boolean getIsExisted() {
        return isExisted;
    }

    public void setIsExisted(Boolean isExisted) {
        this.isExisted = isExisted;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}