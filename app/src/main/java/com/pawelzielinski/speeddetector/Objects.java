package com.pawelzielinski.speeddetector;

import android.content.Context;

import java.util.ArrayList;

public class Objects {



    private String[] obj = new String[]{};
    private Integer[] maxes = new Integer[]{330, 32, 60, 70, 1300};
    private Integer[] min = new Integer[]{0, 0, 0, 5, 600};
    public Objects(Context context) {
        obj = new String[]{context.getResources().getString(R.string.objCar).toUpperCase(),
                context.getResources().getString(R.string.objPerson).toUpperCase(),
                context.getResources().getString(R.string.objBike).toUpperCase(),
                context.getResources().getString(R.string.objAnimal).toUpperCase(),
                context.getResources().getString(R.string.objPlane).toUpperCase()};
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
