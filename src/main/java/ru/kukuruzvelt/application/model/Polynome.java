package ru.kukuruzvelt.application.model;

public class Polynome{

    int[] coefficients;
    int maxPower;
    int offset;


    public static Polynome createNewRandomPolynome(int maxPower){
        int[] coefficients = new int[maxPower];
        for (int i = 0; i < coefficients.length; i++){
            coefficients[i] = java.util.concurrent.ThreadLocalRandom.current().nextInt(-9, 10);
        }
        int offset = java.util.concurrent.ThreadLocalRandom.current().nextInt(20);
        return new Polynome(maxPower, coefficients, offset);
    }

    public static Polynome createNewPredictedPolynome(int maxPower, int[] coefficients, int offset){
        return new Polynome(maxPower, coefficients, offset);
    }

    public int getValue(int variable){
        int value = 0;
        for (int i = 0; i < coefficients.length; i++){
            value = value + coefficients[i] * (int)java.lang.Math.pow(variable, maxPower - i);
        }
        value = value + offset;
        return value;
    }

    private Polynome(int maxPower, int[] coefficients, int offset){
        this.maxPower = maxPower;
        this.coefficients = coefficients;
        this.offset = offset;

    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("f(x) = ");
        for (int i = 0; i < coefficients.length; i++){
            str.append(coefficients[i]).append("x").append(maxPower - i).append("+");
        }
        str.append(offset);
        return str.toString();
    }
}
