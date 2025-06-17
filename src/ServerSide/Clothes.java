package ServerSide;

import java.io.Serializable;

public class Clothes implements Serializable{
   
 private String label; 
 private String code;
 private int price;
 private char size;
 private String color;
 private int QuantityAvailable;

    public Clothes(String label, String code, int price, char size, String color, int QuantityAvailable) {
        this.label = label;
        this.code = code;
        this.price = price;
        this.size = size;
        this.color = color;
        this.QuantityAvailable = QuantityAvailable;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSize(char size) {
        this.size = size;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setQuantityAvailable(int QuantityAvailable) {
        this.QuantityAvailable = QuantityAvailable;
    }

    public String getLabel() {
        return label;
    }

    public String getCode() {
        return code;
    }

    public int getPrice() {
        return price;
    }

    public char getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public int getQuantityAvailable() {
        return QuantityAvailable;
    }

    @Override
    public String toString() {
        return "Clothes \n label:" + label + "\n code:" + code + "\n price:" + price + "\n size:" + size + "\n color:" + color + "\n QuantityAvailable:" + QuantityAvailable + '\n';
    }
 
 
  
  
}
