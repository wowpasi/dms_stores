/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.orien.dms.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Asus
 */
public class DateTime {

    private static SimpleDateFormat sft = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String getTime() {
        return sft.format(new Date());
    }
    public static String getDate() {
        return sfd.format(new Date());
    }
}
