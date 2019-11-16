package com.minhtzy.moneytracker.entity;

import android.content.ContentValues;

import org.parceler.Parcel;

@Parcel
public class UserEntity
        extends EntityBase {
    public static final String USER_ID = "_id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String EMAIL = "email";
    public static final String PHONE = "phone";

    public UserEntity()
    {
        super();
    }

    public UserEntity(ContentValues contentValues)
    {
        super(contentValues);
    }

    public String getUserId() {
        return getString(USER_ID);
    }

    public void setUserId(String userId)
    {
        setString(USER_ID,userId);
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public void setFirstName(String firstName)
    {
        setString(FIRST_NAME,firstName);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public void setLastName(String lastName)
    {
        setString(LAST_NAME,lastName);
    }

    public String getMiddleName() {
        return getString(MIDDLE_NAME);
    }

    public void setMiddleName(String middleName)
    {
        setString(MIDDLE_NAME,middleName);
    }

    public String getEmail() {
        return getString(EMAIL);
    }

    public void setEmail(String email)
    {
        setString(EMAIL,email);
    }

    public String getPhone() {
        return getString(PHONE);
    }

    public void setPhone(String phone)
    {
        setString(PHONE,phone);
    }
}
