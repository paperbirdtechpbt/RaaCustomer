package com.hopetechno.raadarbar.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils implements AppConstant{

    public static final String convertDateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static final Date convertStringToDate(String date) {
        Date convertDate = null;
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_FORMAT);
        try {
            convertDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertDate;
    }


    public static final String converttimeToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static final Date convertStringTotime(String date) {
        Date convertDate = null;
        SimpleDateFormat format = new SimpleDateFormat(TIME_FORMAT);
        try {
            convertDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertDate;
    }

    public static final String convertDatetimeToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static final String convertDatetimeToStringAM(Date date) {
        DateFormat dateFormat = new SimpleDateFormat(TIME_FORMAT);
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static final Date convertStringToDatetime(String date) {
        Date convertDate = null;
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        try {
            convertDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertDate;
    }

}
