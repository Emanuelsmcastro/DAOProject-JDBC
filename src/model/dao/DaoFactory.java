package model.dao;

import java.sql.Connection;

import model.dao.impl.SellerDaoJDBC;
import model.entities.Seller;

public class DaoFactory {

    public static DaoBase<Seller> createSellerDao(Connection conn) {
        return new SellerDaoJDBC(conn);
    }
}
