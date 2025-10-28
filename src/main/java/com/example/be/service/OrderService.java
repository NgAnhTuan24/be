package com.example.be.service;

import com.example.be.dto.OrderRequestDTO;
import com.example.be.dto.OrderResponseDTO;
import com.example.be.entity.Cat;
import com.example.be.entity.Order;
import com.example.be.entity.OrderDetail;
import com.example.be.entity.User;
import com.example.be.mapper.OrderMapper;
import com.example.be.repository.CatRepository;
import com.example.be.repository.OrderDetailRepository;
import com.example.be.repository.OrderRepository;
import com.example.be.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CatRepository catRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CatRepository catRepository,
                        OrderDetailRepository orderDetailRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.catRepository = catRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    // 🧾 Tạo đơn hàng nhiều mèo
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Order order = new Order();
        order.setOrderCode("ORD-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase());
        order.setUser(user);
        order.setShippingFee(request.getShippingFee());
        order.setShippingMethod(request.getShippingMethod());
        order.setPaymentMethod(request.getPaymentMethod());

        List<OrderDetail> details = new ArrayList<>();
        double total = 0;

        for (Long catId : request.getCatIds()) {
            Cat cat = catRepository.findById(catId)
                    .orElseThrow(() -> new RuntimeException("Mèo ID " + catId + " không tồn tại"));

            if (cat.getStatus() == Cat.Status.Sold) {
                throw new RuntimeException("Mèo " + cat.getCatName() + " đã được bán");
            }

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setCat(cat);
            detail.setPrice(cat.getPrice());
            details.add(detail);

            // Cập nhật mèo thành Đã bán
            cat.setStatus(Cat.Status.Sold);
            catRepository.save(cat);

            total += cat.getPrice();
        }

        total += request.getShippingFee();

        order.setTotalAmount(total);
        order.setOrderDetails(details);

        // Lưu cả đơn + chi tiết
        orderRepository.save(order);
        orderDetailRepository.saveAll(details);

        return OrderMapper.toDTO(order);
    }

    // 📦 Lấy tất cả đơn
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 🔍 Lấy đơn theo ID
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        return OrderMapper.toDTO(order);
    }

    // 🔄 Cập nhật trạng thái đơn
    public OrderResponseDTO updateStatus(Long id, Order.Status status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        orderRepository.save(order);
        return OrderMapper.toDTO(order);
    }

    public OrderResponseDTO updateStatusByCode(String orderCode, Order.Status status) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với mã: " + orderCode));
        order.setStatus(status);
        orderRepository.save(order);
        return OrderMapper.toDTO(order);
    }

    public OrderResponseDTO getOrderByCode(String orderCode) {
        Order order = orderRepository.findByOrderCode(orderCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với mã: " + orderCode));
        return OrderMapper.toDTO(order);
    }
}
