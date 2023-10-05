package com.driver;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class OrderController {
    private OrderService orderServiceObj = new OrderService();

    @PostMapping("/add-order")
    public ResponseEntity<String> addOrder(@RequestBody Order order) {
        try {
            orderServiceObj.addOrder(order);
            return new ResponseEntity<>("New order added successfully", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error adding the order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add-partner/{partnerId}")
    public ResponseEntity<String> addPartner(@PathVariable String partnerId) {
        try {
            orderServiceObj.addPartner(partnerId);
            return new ResponseEntity<>("New delivery partner added successfully", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error adding the partner: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/add-order-partner-pair")
    public ResponseEntity<String> addOrderPartnerPair(@RequestParam("orderId") String orderId, @RequestParam("partnerId") String partnerId) {
        //This is basically assigning that order to that partnerId
        try {
            orderServiceObj.addOrderPartnerPair(orderId, partnerId);
            return new ResponseEntity<>("New order-partner pair added successfully", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error adding the order-partner pair: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-order-by-id/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        //order should be returned with an orderId.
        try {
            Order order = orderServiceObj.getOrderById(orderId);
            return new ResponseEntity<>(order, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-partner-by-id/{partnerId}")
    public ResponseEntity<DeliveryPartner> getPartnerById(@PathVariable String partnerId) {
        //deliveryPartner should contain the value given by partnerId
        try {
            DeliveryPartner deliveryPartner = orderServiceObj.getPartnerById(partnerId);
            return new ResponseEntity<>(deliveryPartner, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-order-count-by-partner-id/{partnerId}")
    public ResponseEntity<Integer> getOrderCountByPartnerId(@PathVariable String partnerId) {
        //orderCount should denote the orders given by a partner-id
        try {
            Integer orderCount = orderServiceObj.getOrderCountByPartnerId(partnerId);
            return new ResponseEntity<>(orderCount, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-orders-by-partner-id/{partnerId}")
    public ResponseEntity<List<String>> getOrdersByPartnerId(@PathVariable String partnerId) {
        //orders should contain a list of orders by PartnerId
        try {
            List<String> orders = orderServiceObj.getOrdersByPartnerId(partnerId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<String>> getAllOrders() {
        //Get all orders
        try {
            List<String> orders = orderServiceObj.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-count-of-unassigned-orders")
    public ResponseEntity<Integer> getCountOfUnassignedOrders() {
        //Count of orders that have not been assigned to any DeliveryPartner
        try {
            Integer countOfOrders = orderServiceObj.getCountOfUnassignedOrders();
            return new ResponseEntity<>(countOfOrders, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-count-of-orders-left-after-given-time/{time}/{partnerId}")
    public ResponseEntity<Integer> getOrdersLeftAfterGivenTimeByPartnerId(@PathVariable("time") String time, @PathVariable("partnerId") String partnerId) {
        //countOfOrders that are left after a particular time of a DeliveryPartner
        try {
            Integer countOfOrders = orderServiceObj.getOrdersLeftAfterGivenTimeByPartnerId(time, partnerId);
            return new ResponseEntity<>(countOfOrders, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-last-delivery-time/{partnerId}")
    public ResponseEntity<String> getLastDeliveryTimeByPartnerId(@PathVariable String partnerId) {
        //Return the time when that partnerId will deliver his last delivery order.
        try {
            String time = orderServiceObj.getLastDeliveryTimeByPartnerId(partnerId);
            return new ResponseEntity<>(time, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-partner-by-id/{partnerId}")
    public ResponseEntity<String> deletePartnerById(@PathVariable String partnerId) {
        //Delete the partnerId
        //And push all his assigned orders to unassigned orders.
        try {
            orderServiceObj.deletePartnerById(partnerId);
            return new ResponseEntity<>(partnerId + " removed successfully", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error removing " + partnerId + " : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete-order-by-id/{orderId}")
    public ResponseEntity<String> deleteOrderById(@PathVariable String orderId) {
        //Delete an order and also
        // remove it from the assigned order of that partnerId
        try {
            orderServiceObj.deleteOrderById(orderId);
            return new ResponseEntity<>(orderId + " removed successfully", HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error removing " + orderId + " : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}