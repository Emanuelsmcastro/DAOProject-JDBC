package application;

import db.DB;
import model.dao.DaoBase;
import model.dao.DaoFactory;
import model.entities.Department;

public class App2 {

    public static void main(String[] args) {
        // Create connection
        DaoBase<Department> departmentDao = DaoFactory.createDepartmentDao(DB.getConnection(false));

        System.out.println("Find by id:");
        System.out.println(departmentDao.findById(1));

        System.out.println("Find all:");
        departmentDao.findAll().forEach(System.out::println);

        System.out.println("Inserting...");
        departmentDao.insert(new Department(null, "Music"));

        System.out.println("Updating...");
        departmentDao.update(new Department(1, "Delivery"));

        // System.out.println("Deleting...");
        // departmentDao.deleteById(4);

        DB.closeConnection();
    }
}
