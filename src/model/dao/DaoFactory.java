package model.dao;

import java.sql.Connection;

import model.dao.impl.DepartmentDaoJDBC;
import model.dao.impl.SellerDaoJDBC;
import model.entities.Department;
import model.entities.Seller;

public class DaoFactory {

    public static DaoBase<Seller> createSellerDao(Connection conn) {
        return new SellerDaoJDBC(conn);
    }

    public static DaoBase<Department> createDepartmentDao(Connection conn) {
        return new DepartmentDaoJDBC(conn);
    }
}
