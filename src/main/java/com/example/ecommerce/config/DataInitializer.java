package com.example.ecommerce.config;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.mapper.OrderMapper;
import com.example.ecommerce.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initUser();
        initOrders();
    }

    private void initUser() {
        User existingUser = userMapper.selectByUsername("admin");
        if (existingUser == null) {
            logger.info("Creating default admin user");
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@example.com");
            userMapper.insert(admin);
            logger.info("Default admin user created successfully");
        } else {
            logger.info("Admin user already exists");
        }
    }

    private void initOrders() {
        if (orderMapper.selectCount(null) == 0) {
            logger.info("Creating sample orders");
            
            Order order1 = new Order();
            order1.setUserId(1L);
            order1.setOrderNo("ORD202401010001");
            order1.setTotalAmount(BigDecimal.valueOf(99.99));
            order1.setStatus("COMPLETED");
            orderMapper.insert(order1);

            Order order2 = new Order();
            order2.setUserId(1L);
            order2.setOrderNo("ORD202401020002");
            order2.setTotalAmount(BigDecimal.valueOf(199.99));
            order2.setStatus("PENDING");
            orderMapper.insert(order2);

            Order order3 = new Order();
            order3.setUserId(1L);
            order3.setOrderNo("ORD202401030003");
            order3.setTotalAmount(BigDecimal.valueOf(299.99));
            order3.setStatus("SHIPPED");
            orderMapper.insert(order3);

            Order order4 = new Order();
            order4.setUserId(1L);
            order4.setOrderNo("ORD202401040004");
            order4.setTotalAmount(BigDecimal.valueOf(399.99));
            order4.setStatus("COMPLETED");
            orderMapper.insert(order4);

            Order order5 = new Order();
            order5.setUserId(1L);
            order5.setOrderNo("ORD202401050005");
            order5.setTotalAmount(BigDecimal.valueOf(499.99));
            order5.setStatus("CANCELLED");
            orderMapper.insert(order5);

            logger.info("Sample orders created successfully");
        } else {
            logger.info("Orders already exist");
        }
    }
}
