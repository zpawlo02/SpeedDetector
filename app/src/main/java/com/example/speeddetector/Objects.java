package com.example.speeddetector;

import java.util.ArrayList;

public class Objects {

    private String[] obj = new String[]{"Car", "Person", "Bike", "Animal","Plane"};
    private Integer[] maxes = new Integer[]{330, 32, 60, 70, 1300};
    private Integer[] min = new Integer[]{0, 0, 0, 5, 600};
    public Objects() {
    }

    public Integer[] getMaxes() {
        return maxes;
    }

    public Integer[] getMin() {
        return min;
    }

    public String[] getObj() {
        return obj;
    }
}
