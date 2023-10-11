package application;

import model.entities.Department;

public class App {
    public static void main(String[] args) throws Exception {
        Department dp = new Department(1, "Lib");
        System.out.println(dp);
    }
}
