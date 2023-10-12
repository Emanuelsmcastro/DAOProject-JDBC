package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "INSERT INTO seller " +
                            "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                            "VALUES " +
                            "(?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());

            int rowsAffected = st.executeUpdate();
            conn.commit();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Error: No rows affected!");
            }

        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Apply Rollback: " + e.getMessage());
            } catch (SQLException e1) {
                System.out.println("Rollback Error: " + e1.getMessage());
            }
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "UPDATE seller " +
                            "SET Name = ?, " + "Email = ?, " + "BirthDate = ?, " + "BaseSalary = ?, "
                            + "DepartmentId = ? " +
                            "WHERE Id = ?");

            st.setString(1, obj.getName());
            st.setString(2, obj.getEmail());
            st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            st.setDouble(4, obj.getBaseSalary());
            st.setInt(5, obj.getDepartment().getId());
            st.setInt(6, obj.getId());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new DbException("Update seller error!");
            }
            conn.commit();

        } catch (SQLException e) {
            try {
                conn.rollback();
                System.out.println("Apply Rollback: " + e.getMessage());
            } catch (SQLException e1) {
                System.out.println("Rollback Error: " + e1.getMessage());
            }
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
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
