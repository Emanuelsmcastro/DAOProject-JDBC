package application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class App {
    public static void main(String[] args) throws Exception {
        Department dp = new Department(1, "Lib");
        System.out.println(dp);
        Seller seller = new Seller(21, "Emanuel", "emanuel@gmail.com", new Date(), 3000.0, dp);
        System.out.println(seller);
    }
}
