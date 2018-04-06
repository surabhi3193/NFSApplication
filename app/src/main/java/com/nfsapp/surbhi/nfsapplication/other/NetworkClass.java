package com.nfsapp.surbhi.nfsapplication.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkClass {

    public static final String BASE_URL_NEW = "http://18.218.89.83/NFS/index.php/Webservice/";

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
