/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classes;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Karsten
 */
public class Statistic {
    //Count-Methode 
    //Für jedes Element wird count hochgezählt
    public int getNumber(List listRs) {
        int count = 0;
        for (Object element : listRs) {
            Map<String, String> row = (Map<String, String>) element;
            count = count + 1;
        }
        return count;
    }

    //Methode zur Berechnung des Durchschnitts
    public double getAverage(List listRs) {
        double average = 0;
        double valueSum = 0;
        int count = 0;

        for (Object element : listRs) {
            Map<String, String> row = (Map<String, String>) element;
            double rate = new Double(row.get("UserRate"));
            if (rate != 0.0) {
                valueSum = rate + valueSum;
                count++;
            }
        }

        //Um auf die 2. Nachkommastelle zu runden (*100)/100
        average = Math.rint((valueSum / count)*100)/100;
        return average;
    }

}
