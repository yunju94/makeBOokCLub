package com.make.bookclub.service;

import com.make.bookclub.dto.OrderDto;
import com.make.bookclub.dto.OrderHistDto;
import com.make.bookclub.dto.OrderItemDto;
import com.make.bookclub.entity.*;
import com.make.bookclub.repository.ItemImgRepository;
import com.make.bookclub.repository.ItemRepository;
import com.make.bookclub.repository.MemberRepository;
import com.make.bookclub.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private  final ItemRepository itemRepository;
    private  final MemberRepository memberRepository;
    private  final OrderRepository orderRepository;
    private  final ItemImgRepository itemImgRepository;

    public  long order(OrderDto orderDto, String email){
        //entity 빼오는 과정
        Item item = itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);
        List<OrderItem> orderItemList = new ArrayList<>();
        //주문 아이템 추가
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);
        //주문서 추가
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){
        List<Order> orders = orderRepository.findOrders(email, pageable);//주문리스트
        Long totalCount = orderRepository.countOrder(email);//주문 총 개수
        List<OrderHistDto> orderHistDtos = new ArrayList<>();
        //Order -> OderHistDto
        //OrderItem -> OrderItemDto
        for (Order order : orders){
            //주문 히스토리 객체 생성
            OrderHistDto orderHistDto = new OrderHistDto(order);
            //주문에 있는 주문 아이템 리스트 추출
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems){
                //주문 아이템의 아이템 추출 후, id를 매개 변수로 img 대표 객체 추출
                ItemImg itemImg = itemImgRepository.findByItemIdAndRepImgYn(orderItem.getItem().getId(),
                        "Y");
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                orderHistDto.addOrderItemDto(orderItemDto);
            }
            orderHistDtos.add(orderHistDto);
        }
        return  new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
    }

    @Transactional(readOnly = true)
    public  boolean validateOrder(Long orderId, String email){
        Member curMember = memberRepository.findByEmail(email);
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if (!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public  void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }
}
