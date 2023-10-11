package application;

import db.DB;
import model.dao.DaoBase;
import model.dao.DaoFactory;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Seller;

public class App {
    public static void main(String[] args) throws Exception {
        // Department dp = new Department(1, "Lib");
        // System.out.println(dp);
        // Seller seller = new Seller(21, "Emanuel", "emanuel@gmail.com", new Date(),
        // 3000.0, dp);
        // System.out.println(seller);
        DaoBase<Seller> sellerDao = DaoFactory.createSellerDao(DB.getConnection(false));
        // System.out.println(sellerDao.findById(29));
        System.out.println(((SellerDaoJDBC) sellerDao).findByDepartmentId(1));
    }
}
