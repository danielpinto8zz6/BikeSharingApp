package com.bikesharing.app.onboarding;

public class OnboardingItem {

    private int nImage;
    private String szTitle;
    private String szDescription;

    public OnboardingItem(int nImage, String szTitle, String szDescription) {
        this.nImage = nImage;
        this.szTitle = szTitle;
        this.szDescription = szDescription;
    }

    public int getImage() {
        return nImage;
    }

    public void setImage(int nImage) {
        this.nImage = nImage;
    }

    public String getTitle() {
        return szTitle;
    }

    public void setTitle(String szTitle) {
        this.szTitle = szTitle;
    }

    public String getDescription() {
        return szDescription;
    }

    public void setDescription(String szDescription) {
        this.szDescription = szDescription;
    }
}
