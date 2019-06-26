package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Machine {

    public static final int MACHINE_ID = 1;

    public int get_id() {
        return _id;
    }

    private final int _id =1;

    private List<Beverage> beverages;

    private double moneyAmount;

    public Machine() {
        beverages = new ArrayList<>();
        moneyAmount = 0.0;
    }

    public double getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(double moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public List<Beverage> getBeverages() {
        return beverages;
    }

    public void setBeverages(List<Beverage> beverages) {
        this.beverages = beverages;
    }


    @Override
    public String toString() {
        return "Machine{" +
                "beverages=" + beverages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return Objects.equals(beverages, machine.beverages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beverages);
    }
}
