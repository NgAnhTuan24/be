    package com.example.be.controller;

    import com.example.be.dto.OrderRequestDTO;
    import com.example.be.dto.OrderResponseDTO;
    import com.example.be.entity.Order;
    import com.example.be.service.OrderService;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/order")
    public class OrderController {

        private final OrderService orderService;

        public OrderController(OrderService orderService) {
            this.orderService = orderService;
        }


        // 🧾 Lấy tất cả đơn hàng
        @GetMapping
        public List<OrderResponseDTO> getAllOrders() {
            return orderService.getAllOrders();
        }


        // 🔍 Lấy chi tiết đơn
        @GetMapping("/{id}")
        public OrderResponseDTO getOrderById(@PathVariable Long id) {
            return orderService.getOrderById(id);
        }


        // 🆕 Tạo đơn hàng nhiều mèo
        @PostMapping
        public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO request) {
            return orderService.createOrder(request);
        }


        // 🔄 Cập nhật trạng thái đơn
        @PutMapping("/{id}/status")
        public OrderResponseDTO updateStatus(@PathVariable Long id,
                                  @RequestParam Order.Status status) {
            return orderService.updateStatus(id, status);
        }


        @PutMapping("/by-code/{orderCode}/status")
        public OrderResponseDTO updateStatusByCode(@PathVariable String orderCode, @RequestParam Order.Status status) {
            return orderService.updateStatusByCode(orderCode, status);
        }


        @GetMapping("/by-code/{orderCode}")
        public OrderResponseDTO getOrderByCode(@PathVariable String orderCode) {
            return orderService.getOrderByCode(orderCode);
        }
    }
