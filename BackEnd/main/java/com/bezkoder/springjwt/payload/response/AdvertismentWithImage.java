package com.bezkoder.springjwt.payload.response;

import com.bezkoder.springjwt.models.Advertisment;

public class AdvertismentWithImage {
    private Advertisment advertisment;
    private byte[] imageBytes;

    // Constructor, getters, and setters
    public AdvertismentWithImage(Advertisment advertisment, byte[] imageBytes) {
        this.advertisment = advertisment;
        this.imageBytes = imageBytes;
    }

    public Advertisment getAdvertisment() {
        return advertisment;
    }

    public void setAdvertisment(Advertisment advertisment) {
        this.advertisment = advertisment;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
}

