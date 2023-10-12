package application;

import java.util.Date;

import db.DB;
import model.dao.DaoBase;
import model.dao.DaoFactory;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

public class App {
    public static void main(String[] args) throws Exception {
        Department dp = new Department(1, "Lib");

        // Create connection
        DaoBase<Seller> sellerDao = DaoFactory.createSellerDao(DB.getConnection(false));

        System.out.println("Find by id:");
        System.out.println(sellerDao.findById(29));

        System.out.println("Find all:");
        sellerDao.findAll().forEach(System.out::println);

        System.out.println("Find all by department Id:");
        ((SellerDaoJDBC) sellerDao).findByDepartmentId(1).forEach(System.out::println);

        System.out.println("Inserting...");
        sellerDao.insert(new Seller(null, "Emanuel", "emanuel@gmail.com", new Date(), 1200.0, dp));

        System.out.println("Updating...");
        sellerDao.update(new Seller(3, "Emanuel", "emanuel@gmail.com", new Date(), 1200.0, dp));

        // sellerDao.deleteById(4);
        DB.closeConnection();
    }
}
