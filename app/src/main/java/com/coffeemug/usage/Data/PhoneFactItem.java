package com.coffeemug.usage.Data;

import android.graphics.drawable.Drawable;

/**
 * Created by aditya on 04/08/15.
 */
public class PhoneFactItem {

    private String phoneFact, appLabel, packageName;
    private Drawable logo;

    public String getPhoneFact() {
        return phoneFact;
    }

    public void setPhoneFact(String phoneFact) {
        this.phoneFact = phoneFact;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }
}
