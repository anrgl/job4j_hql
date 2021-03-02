package ru.job4j.integration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class OrderStoreTest {
    private final BasicDataSource pool = new BasicDataSource();

    @Before
    public void setUp() throws SQLException {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);
        StringBuilder builder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream("./db/schema.sql")))) {
            br.lines().forEach(line -> builder.append(line).append(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pool.getConnection().prepareStatement(builder.toString()).executeUpdate();
    }

    @Test
    public void whenSaveOrderAndFindAllOneRowWithDescription() {
        OrderStore store = new OrderStore(pool);
        store.save(Order.of("name1", "description1"));
        List<Order> all = (List<Order>) store.findAll();

        assertThat(all.size(), is(1));
        assertThat(all.get(0).getDescription(), is("description1"));
        assertThat(all.get(0).getId(), is(1));
    }

    @Test
    public void whenSaveOrderAndFindOrderById() {
        OrderStore store = new OrderStore(pool);
        store.save(Order.of("name1", "description1"));
        Order order = store.findById(1);

        assertThat(order.getId(), is(1));
        assertThat(order.getName(), is("name1"));
        assertThat(order.getDescription(), is("description1"));
    }

    @Test
    public void whenSaveOrderAndFindOrdersByName() {
        OrderStore store = new OrderStore(pool);
        store.save(Order.of("name", "description1"));
        store.save(Order.of("name", "description2"));
        List<Order> orders = (List<Order>) store.findByName("name");

        assertThat(orders.size(), is(2));
        assertThat(orders.get(0).getDescription(), is("description1"));
        assertThat(orders.get(1).getDescription(), is("description2"));
    }

    @Test
    public void whenUpdateOrderAndFindOrderById() {
        OrderStore store = new OrderStore(pool);
        store.save(Order.of("name1", "description1"));
        store.update(1, Order.of("new name", "new description"));
        Order order = store.findById(1);

        assertThat(order.getId(), is(1));
        assertThat(order.getName(), is("new name"));
        assertThat(order.getDescription(), is("new description"));
    }
}