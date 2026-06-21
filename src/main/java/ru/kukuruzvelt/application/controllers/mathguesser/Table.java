package ru.kukuruzvelt.application.controllers.mathguesser;

import java.util.HashMap;
import java.util.Map;

public class Table {
    Polynome pol;
    HashMap<Integer, Integer> table = new HashMap<Integer, Integer>();
    int floorIndex;
    int topIndex;

    public Table(Polynome pol, int floorIndex, int topIndex){
        this.pol = pol;
        this.floorIndex = floorIndex;
        this.topIndex = topIndex;
        for (int i = floorIndex; i <= topIndex; i++){
            table.put(i, pol.getValue(i));
        }

    }

    public void printToStdout(){
        System.out.println(table);
    }

    public Map<Integer, Integer> getMap(){
        return this.table;
    }
}
