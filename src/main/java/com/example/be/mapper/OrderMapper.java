package com.example.be.mapper;

import com.example.be.dto.CatSummaryDTO;
import com.example.be.dto.OrderDetailDTO;
import com.example.be.dto.OrderResponseDTO;
import com.example.be.dto.UserDTO;
import com.example.be.entity.Cat;
import com.example.be.entity.Order;
import com.example.be.entity.OrderDetail;
import com.example.be.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponseDTO toDTO(Order order) {

        if(order == null) return null;

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setOrderCode(order.getOrderCode());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingMethod(order.getShippingMethod());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setStatus(order.getStatus().name());
        dto.setUser(toUserSummaryDTO(order.getUser()));

        if (order.getOrderDetails() != null) {
            List<OrderDetailDTO> orderDetails = order.getOrderDetails()
                    .stream()
                    .map(OrderMapper::toOrderDetailDTO)
                    .collect(Collectors.toList());
            dto.setOrderDetails(orderDetails);
        }

        return dto;
    }

    public static UserDTO toUserSummaryDTO(User user) {

        if (user == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setAddress(user.getAddress());
        return dto;
    }

    public static OrderDetailDTO toOrderDetailDTO(OrderDetail detail) {

        if (detail == null) return null;

        OrderDetailDTO dto = new OrderDetailDTO();
        dto.setId(detail.getId());
        dto.setPrice(detail.getPrice());
        dto.setCat(toCatDTO(detail.getCat()));
        return dto;
    }

    private static CatSummaryDTO toCatDTO(Cat cat) {
        if (cat == null) return null;

        CatSummaryDTO dto = new CatSummaryDTO();
        dto.setId(cat.getId());
        dto.setCatName(cat.getCatName());
        dto.setBreed(cat.getBreed());
        dto.setDateOfBirth(cat.getDateOfBirth());
        dto.setGender(cat.getGender().name());
        dto.setImageUrl(cat.getImageUrl());
        dto.setPrice(cat.getPrice());
        return dto;
    }
}
