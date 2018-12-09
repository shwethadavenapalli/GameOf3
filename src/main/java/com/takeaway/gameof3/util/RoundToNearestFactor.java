package com.takeaway.gameof3.util;

public class RoundToNearestFactor {

    public static Integer roundToNearestFactorOf3(Integer inputNumber) {

        int roundOfToNearestFactorOf3 = inputNumber % 3;
        if (roundOfToNearestFactorOf3 == 2) {
            inputNumber++;
        } else if (roundOfToNearestFactorOf3 == 1) {
            inputNumber--;
        }

        if  (inputNumber == 0)
            inputNumber = 3;

        return inputNumber;
    }
}