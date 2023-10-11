package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.DaoBase;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements DaoBase<Seller> {

    private Connection conn;

    public SellerDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Seller obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Seller obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void deleteById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE seller.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateSeller(rs);
            }

        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        throw new DbException("No seller founded!");
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Seller> sellers = new ArrayList<>();
        Map<Integer, Department> map = new HashMap<>();

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name");

            rs = st.executeQuery();

            while (rs.next()) {
                sellers.add(instantiateSeller(rs, map));
            }
            return sellers;
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public List<Seller> findByDepartmentId(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Seller> sellers = new ArrayList<>();
        Map<Integer, Department> map = new HashMap<>();

        try {
            st = conn.prepareStatement(
                    "SELECT seller.*, department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name");
            st.setInt(1, id);
            rs = st.executeQuery();

            while (rs.next()) {
                sellers.add(instantiateSeller(rs, map));
            }
            return sellers;
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs) throws SQLException {
        Department dep = new Department(rs.getInt("DepartmentId"), rs.getString("DepName"));
        return new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getDate("BirthDate"),
                rs.getDouble("BaseSalary"), dep);
    }

    private Seller instantiateSeller(ResultSet rs, Map<Integer, Department> map) throws SQLException {
        Department dep = null;

        int departmentId = rs.getInt("DepartmentId");
        if (map.get(rs.getInt("DepartmentId")) == null) {
            dep = new Department(departmentId, rs.getString("DepName"));
            map.put(departmentId, dep);
        } else {
            dep = map.get(departmentId);
        }
        return new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getDate("BirthDate"),
                rs.getDouble("BaseSalary"), dep);
    }
}
