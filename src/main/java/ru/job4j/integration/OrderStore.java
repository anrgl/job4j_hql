package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderStore {
    private final BasicDataSource pool;

    public OrderStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Order save(Order order) {
        try (Connection con = pool.getConnection();
             PreparedStatement pr = con.prepareStatement(
                     "insert into orders(name, description, created) values (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            pr.setString(1, order.getName());
            pr.setString(2, order.getDescription());
            pr.setTimestamp(3, order.getCreated());
            pr.execute();
            ResultSet id = pr.getGeneratedKeys();
            if (id.next()) {
                order.setId(id.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public Order update(int id, Order order) {
        try (Connection con = pool.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "update orders set name = ?, description = ?, created = ? where id = ?"
            )) {
                ps.setInt(4, id);
                ps.setString(1, order.getName());
                ps.setString(2, order.getDescription());
                ps.setTimestamp(3, order.getCreated());
                ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    public Collection<Order> findAll() {
        List<Order> rsl = new ArrayList<>();
        try (Connection con = pool.getConnection();
             PreparedStatement ps = con.prepareStatement("select * from orders")) {
            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rsl.add(new Order(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getTimestamp("created")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public Order findById(int id) {
        Order rsl = null;
        try (Connection con = pool.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from orders where id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rsl = new Order(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getTimestamp("created")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public Collection<Order> findByName(String name) {
        List<Order> rsl = new ArrayList<>();
        try (Connection con = pool.getConnection();
            PreparedStatement ps = con.prepareStatement("select * from orders where name = ?")) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rsl.add(new Order(
                       rs.getInt("id"),
                       rs.getString("name"),
                       rs.getString("description"),
                       rs.getTimestamp("created")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rsl;
    }
}
