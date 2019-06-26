package model;

import java.util.Objects;

public class Beverage {

    private int id;
    private String type;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Beverage{" +
                ", type='" + type + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beverage beverage = (Beverage) o;
        return Double.compare(beverage.price, price) == 0 &&
                Objects.equals(type, beverage.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, price);
    }
}
