package com.driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private HashMap<String, Order> orderDb = new HashMap<>();
    private HashMap<String, DeliveryPartner> deliveryPartnerDb = new HashMap<>();
    private HashMap<String, List<String>> orderPartnerDb = new HashMap<>();

    public void addOrder(Order order) throws Exception {
        String orderId = order.getId();
        if (orderDb.containsKey(orderId))
            throw new Exception("Order already exists in the database");

        orderDb.put(orderId, order);
    }

    public Order getOrderById(String orderId) {
        return orderDb.get(orderId);
    }

    public void deleteOrderById(String orderId) throws Exception {
        if (!orderDb.containsKey(orderId))
            throw new Exception("The order was found in the database");

        orderDb.remove(orderId);

        for (String partnerId : orderPartnerDb.keySet()) {
            List<String> list = orderPartnerDb.get(partnerId);

            if (list.contains(orderId)) {
                list.remove(orderId);
                orderPartnerDb.put(partnerId, list);
                break;
            }
        }
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderDb.keySet());
    }

    public void addPartner(DeliveryPartner partner) throws Exception {
        String partnerId = partner.getId();
        if (deliveryPartnerDb.containsKey(partnerId))
            throw new Exception("THe delivery partner already exists");

        deliveryPartnerDb.put(partnerId, partner);
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        return deliveryPartnerDb.get(partnerId);
    }

    public void deletePartnerById(String partnerId) throws Exception {
        if (!deliveryPartnerDb.containsKey(partnerId))
            throw new Exception("A delivery partner with this partnerId was found in the database");

        deliveryPartnerDb.remove(partnerId);

        orderPartnerDb.remove(partnerId);
    }

    public List<String> getAllPartners() {
        return new ArrayList<>(deliveryPartnerDb.keySet());
    }

    public void addOrderPartnerPair(String orderId, String partnerId) throws Exception {
        List<String> list = orderPartnerDb.getOrDefault(partnerId, new ArrayList<>());
        if (list.contains(orderId))
            throw new Exception("The order-partner pair already exists");

        list.add(orderId);
        orderPartnerDb.put(partnerId, list);

        DeliveryPartner partner = deliveryPartnerDb.get(partnerId);
        partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);
    }

    public List<String> getOrdersByPartnerId(String partnerId) throws Exception {
        if (!deliveryPartnerDb.containsKey(partnerId))
            throw new Exception("A delivery partner with this partnerId is not found in the database");

        return orderPartnerDb.get(partnerId);
    }
}