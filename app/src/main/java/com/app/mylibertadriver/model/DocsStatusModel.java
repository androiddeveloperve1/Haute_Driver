package com.app.mylibertadriver.model;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class DocsStatusModel {

    private Docs driverlicense;
    private Docs insurance;
    private String is_document_verify;
    private String is_document_upload;


    public String getIs_document_upload() {
        return is_document_upload;
    }

    public void setIs_document_upload(String is_document_upload) {
        this.is_document_upload = is_document_upload;
    }

    public String getIs_document_verify() {
        return is_document_verify;
    }

    public void setIs_document_verify(String is_document_verify) {
        this.is_document_verify = is_document_verify;
    }

    public Docs getDriverlicense() {
        return driverlicense;
    }

    public void setDriverlicense(Docs driverlicense) {
        this.driverlicense = driverlicense;
    }

    public Docs getInsurance() {
        return insurance;
    }

    public void setInsurance(Docs insurance) {
        this.insurance = insurance;
    }
}
