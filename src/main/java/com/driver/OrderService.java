package com.driver;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private OrderRepository orderRepositoryObj = new OrderRepository();

    public void addOrder(Order order) throws Exception {
        orderRepositoryObj.addOrder(order);
    }

    public void addPartner(String partnerId) throws Exception {
        orderRepositoryObj.addPartner(new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) throws Exception {
        orderRepositoryObj.addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId) throws Exception {
        Order order = orderRepositoryObj.getOrderById(orderId);
        if (order == null)
            throw new Exception("Order not found in the database");

        return order;
    }

    public List<String> getAllOrders() throws Exception {
        List<String> list = orderRepositoryObj.getAllOrders();
        if (list.isEmpty())
            throw new Exception("There are no orders currently");

        return list;
    }

    public DeliveryPartner getPartnerById(String partnerId) throws Exception {
        DeliveryPartner partner = orderRepositoryObj.getPartnerById(partnerId);
        if (partner == null)
            throw new Exception("A delivery partner with this partnerId is not found in the database");

        return partner;
    }

    public Integer getOrderCountByPartnerId(String partnerId) throws Exception {
        DeliveryPartner partner = orderRepositoryObj.getPartnerById(partnerId);
        if (partner == null)
            throw new Exception("A delivery partner with this partnerId is not found in the database");

        return partner.getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) throws Exception {
        List<String> list = orderRepositoryObj.getOrdersByPartnerId(partnerId);

        if (list == null || list.isEmpty())
            throw new Exception("No orders are currently assigned to this delivery partner");

        return list;
    }

    public Integer getCountOfUnassignedOrders() {
        List<String> orders = orderRepositoryObj.getAllOrders();
        List<String> partners = orderRepositoryObj.getAllPartners();

        int count = 0;
        for (String order : orders) {
            boolean flag = true;
            for (String partner : partners) {
                try {
                    if (orderRepositoryObj.getOrdersByPartnerId(partner).contains(order)) {
                        flag = false;
                        break;
                    }
                } catch (Exception ignored) {}
            }

            if (flag) ++count;
        }

        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) throws Exception {
        List<String> list = orderRepositoryObj.getOrdersByPartnerId(partnerId);
        if (list == null || list.isEmpty())
            throw new Exception("No orders are currently assigned to this delivery partner");

        int timeInMin = Integer.parseInt(time); //Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));

        int count = 0;
        for (String orderId : list) {
            Order order = orderRepositoryObj.getOrderById(orderId);

            if (order.getDeliveryTime() > timeInMin)
                ++count;
        }

        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) throws Exception {
        List<String> list = orderRepositoryObj.getOrdersByPartnerId(partnerId);

        if (list == null || list.isEmpty())
            throw new Exception("No orders are currently assigned to this delivery partner");

        int lastTime = 0;
        for (String orderId : list) {
            Order order = orderRepositoryObj.getOrderById(orderId);

            lastTime = Math.max(lastTime, order.getDeliveryTime());
        }

        return lastTime / 60 + ":" + lastTime % 60;
    }

    public void deletePartnerById(String partnerId) throws Exception {
        orderRepositoryObj.deletePartnerById(partnerId);
    }

    public void deleteOrderById(String orderId) throws Exception {
        orderRepositoryObj.deleteOrderById(orderId);
    }
}