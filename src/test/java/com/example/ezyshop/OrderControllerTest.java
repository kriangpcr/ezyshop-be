package com.example.ezyshop;

import com.example.ezyshop.controller.OrderController;
import com.example.ezyshop.dto.order.OrderItemRequest;
import com.example.ezyshop.dto.order.OrderRequest;
import com.example.ezyshop.dto.order.OrderResponse;
import com.example.ezyshop.security.CustomDetailUserService;
import com.example.ezyshop.security.JwtService;
import com.example.ezyshop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CustomDetailUserService customDetailUserService;

    @Test
    @WithMockUser(username = "test")
    void shouldGetOrdersSuccessfully() throws Exception {

        OrderResponse order = new OrderResponse();
        order.setId("order-1");

        when(orderService.getOrders(any(Authentication.class)))
                .thenReturn(List.of(order));

        mockMvc.perform(
                        get("/order/get")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id")
                        .value("order-1"));

        verify(orderService)
                .getOrders(any(Authentication.class));
    }

    @Test
    @WithMockUser(username = "test")
    void shouldCreateOrderSuccessfully() throws Exception {
        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setQuantity(2);
        orderItemRequest.setProduct_id("product-1");

        OrderRequest request = new OrderRequest();
        request.setAddress_id("address-1");
        request.setItems(List.of(orderItemRequest));

        mockMvc.perform(
                post("/order/create").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "address_id": "address-1",
                            "items": [
                                    {
                                        "product_id": "product-1",
                                        "quantity": 2
                                    }
                            ]
                        }
                    """))
                .andExpect(status().isNoContent());
        verify(orderService)
                .createOrder(
                        any(OrderRequest.class),
                        any(Authentication.class)
                );
    }

    @Test
    @WithMockUser(username = "test")
    void shouldReturn400WhenAddressIdIsBlank() throws Exception {

        mockMvc.perform(
                        post("/order/create")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "address_id": "",
                            "items": [
                                {
                                    "product_id": "product-1",
                                    "quantity": 2
                                }
                            ]
                        }
                    """)
                )
                .andExpect(status().isBadRequest());

        verify(orderService, never())
                .createOrder(
                        any(OrderRequest.class),
                        any(Authentication.class)
                );
    }
    @Test
    @WithMockUser(username = "test")
    void shouldReturn400WhenItemsIsEmpty() throws Exception {

        mockMvc.perform(
                        post("/order/create")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                        {
                            "address_id": "address-1",
                            "items": []
                        }
                    """)
                )
                .andExpect(status().isBadRequest());

        verify(orderService, never())
                .createOrder(
                        any(OrderRequest.class),
                        any(Authentication.class)
                );
    }
}
