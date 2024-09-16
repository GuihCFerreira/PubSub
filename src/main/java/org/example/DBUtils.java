package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtils {

    Connection db = DBConnection.getConnection();

    public DBUtils() throws SQLException {
    }

    public void insertOnDB (Order order){
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
        String insertSkuSQL = "CALL sku_iu(?, ?)";
        try (PreparedStatement ps = db.prepareStatement(insertSkuSQL)) {
            ps.setString(1, sku.getId());
            ps.setDouble(2, sku.getValue());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Erro no SKU" +e);
        }
    }

    private void insertCustomer(Customer customer){
        String insertCustomerSQL = "CALL customer_iu(?, ?)";
        try (PreparedStatement ps = db.prepareStatement(insertCustomerSQL)) {
            ps.setInt(1, customer.getId());
            ps.setString(2, customer.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro no Customer" + e);
        }
    }

    private void insertOrder(Order order){
        String insertOrderSQL = "CALL order_iu(?, ?, ?, ?)";
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
        String insertOrderItemSQL = "CALL order_item_iu(?, ?, ?, ?, ?, ?)";
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
