package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DaoBase;
import model.entities.Department;

public class DepartmentDaoJDBC implements DaoBase<Department> {
    private Connection conn;

    public DepartmentDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "INSERT INTO department" +
                            "(Name) " +
                            "VALUE " +
                            "(?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getName());

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
    public void update(Department obj) {
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement(
                    "UPDATE department " +
                            "SET Name = ? " +
                            "WHERE Id = ?");

            st.setString(1, obj.getName());
            st.setInt(2, obj.getId());

            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new DbException("Update department error!");
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
        PreparedStatement st = null;

        try {
            st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
            st.setInt(1, id);
            int rows = st.executeUpdate();
            if (rows == 0) {
                throw new DbException("Delete error: No rows affected!");
            }
            conn.commit();
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM department " +
                            "WHERE department.Id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                return instantiateDepartment(rs);
            }

        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
        throw new DbException("No department founded!");
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Department> departments = new ArrayList<>();

        try {
            st = conn.prepareStatement(
                    "SELECT * FROM department " +
                            "ORDER BY Name");

            rs = st.executeQuery();

            while (rs.next()) {
                departments.add(instantiateDepartment(rs));
            }
            return departments;
        } catch (SQLException e) {
            throw new DbException("Error: " + e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        return new Department(rs.getInt("Id"), rs.getString("Name"));
    }
}
