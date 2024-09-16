package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtils {

    Connection db = DBConnection.getConnection();

    public DBUtils() throws SQLException {
    }

    public void insertOrderInDB (Order order){
        try {
           insertCustomer(order.getCustomer());
           insertOrder(order);
           order.getItems().forEach(orderItem -> {
               insertSKU(orderItem.getSku());
               insertOrderItem(orderItem, order.getUuid());
           });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void insertSKU(Sku sku){
        String insertSkuSQL = "INSERT INTO TB_SKU (id, value) VALUES (?, ?) ON CONFLICT (id) DO NOTHING";
        try (PreparedStatement ps = db.prepareStatement(insertSkuSQL)) {
            ps.setString(1, sku.getId());
            ps.setDouble(2, sku.getValue());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro no SKU" +e);
        }
    }

    private void insertCustomer(Customer customer){
        String insertCustomerSQL = "INSERT INTO TB_CUSTOMER (id, name) VALUES (?, ?) ON CONFLICT (id) DO NOTHING";
        try (PreparedStatement ps = db.prepareStatement(insertCustomerSQL)) {
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro no Customer" + e);
        }
    }

    private void insertOrder(Order order){
        String insertOrderSQL = "INSERT INTO TB_ORDER (id, order_type, created_at, customer_id) VALUES (?, ?, ?, ?) ON CONFLICT (id) DO NOTHING";
        try (PreparedStatement ps = db.prepareStatement(insertOrderSQL)) {
            ps.setString(1, order.getUuid());
            ps.setString(2, order.getType());
            ps.setString(3, order.getCreatedAt());
            ps.setInt(4, order.getCustomer().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro no Order" +e);
        }
    }

    private void insertOrderItem(OrderItem item, String orderId){
        String insertOrderItemSQL = "INSERT INTO TB_ORDER_ITEM (id, order_id, sku_id, quantity, category_id, sub_category_id) VALUES (?, ?, ?, ?, ?, ?) ON CONFLICT (id) DO NOTHING";
        try (PreparedStatement ps = db.prepareStatement(insertOrderItemSQL)) {
            ps.setInt(1, item.getId());
            ps.setString(2, orderId);
            ps.setString(3, item.getSku().getId());
            ps.setDouble(4, item.getQuantity());
            ps.setString(5, item.getCategory().getId());
            ps.setString(6, item.getCategory().getSubCategory().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro no Order Item" +e);
        }
    }
}
