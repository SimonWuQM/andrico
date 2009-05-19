/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/

package org.andrico.andrico;

import android.os.Parcel;
import android.os.Parcelable;

class UserInfo implements Parcelable {
    public static final Parcelable.Creator<UserInfo> CREATOR = new Parcelable.Creator<UserInfo>() {
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    final String mFirstName;
    final String mLastName;
    final String mLocation;
    final String mBirthday;
    final String mBirthdayDate;
    final String mProfileUrl;
  

    UserInfo(Parcel in) {
        mFirstName = in.readString();
        mLastName = in.readString();
        mLocation = in.readString();
        mBirthday = in.readString();
        mBirthdayDate = in.readString();
        mProfileUrl = in.readString();
    }

    UserInfo(String firstName, String lastName, String location,
    		String birthday, String birthday_date, String profile_url) {
        if (firstName == null || lastName == null ) {
            throw new IllegalArgumentException();
        }
        mFirstName = firstName;
        mLastName = lastName;
        mLocation = location;
        mBirthday = birthday;
        mBirthdayDate = birthday_date;
        mProfileUrl = profile_url;
    }

    public int describeContents() {
        return 0;
    }

    String getFirstName() {
        return mFirstName;
    }

    String getLastName() {
        return mLastName;
    }
    
    String getLocation(){
    	return mLocation;
    }
    
    String getBirthday() {
    	return mBirthday;
    }
    String getBirthdayDate() {
    	return mBirthdayDate;
    }
    String getProfileUrl() {
    	return mProfileUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mLocation);
        dest.writeString(mBirthday);
        dest.writeString(mBirthdayDate);
        dest.writeString(mProfileUrl);
    }

}