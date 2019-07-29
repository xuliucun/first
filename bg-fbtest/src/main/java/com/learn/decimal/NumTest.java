package com.learn.decimal;

import java.math.BigDecimal;

public class NumTest {
    public static void main(String[] args) {
        String a = "301353.0499999999883584678173065185546875";
        double c = 301353.0499999999883584678173065185546875d;
        BigDecimal sa = new BigDecimal(a);
        BigDecimal sc = new BigDecimal(String.valueOf(c));
        BigDecimal dc = new BigDecimal(Double.toString(c));
        
        System.out.println("sa : "+ sa);
        System.out.println("sc : "+ sc);
        System.out.println("dc : "+ dc);
        
    }
}