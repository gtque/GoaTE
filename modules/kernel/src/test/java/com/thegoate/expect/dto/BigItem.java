package com.thegoate.expect.dto;

import com.thegoate.utils.fill.serialize.Kid;

public class BigItem extends Kid {

    public int a = 0;
    public int b = 0;
    public int c = 0;
    public int d = 0;
    public int e = 0;
    public int f = 0;
    public int g = 0;
    public int h = 0;
    public String i = "a";
    public String j = "a";
    public String k = "a";
    public String l = "a";
    public String m = "a";
    public String n = "a";
    public String o = "a";
    public String p = "a";
    public String q = "a";
    public String r = "a";
    public String s = "a";
    public String t = "a";
    public String u = "a";
    public String v = "a";
    public String w = "a";
    public String x = "a";
    public String y = "a";
    public String z = "a";

    public BigItem() {
        a = (int)(Math.random()*42);
        i = ""+"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt((int)(Math.random()*52)) + System.currentTimeMillis();
    }
    public String toString() {
        return "i: " + i + "; a: " + a;
    }
}
