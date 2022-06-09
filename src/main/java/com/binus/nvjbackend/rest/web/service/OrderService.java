package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.rest.web.model.request.order.OrderClientRequest;
import com.binus.nvjbackend.rest.web.model.request.order.OrderFilterRequest;
import com.binus.nvjbackend.rest.web.model.request.order.OrderRequest;
import com.midtrans.httpclient.error.MidtransError;
import freemarker.template.TemplateException;
import org.springframework.data.domain.Page;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface OrderService {

  Order createOrder(OrderRequest request);

  Order createClientOrder(OrderClientRequest request) throws MidtransError, TemplateException,
      MessagingException, IOException;

  Boolean isMidtransOrderIdExists(String id);

  void handleNotification(Map<String, Object> requestBody) throws ParseException, TemplateException,
      MessagingException, IOException;

  Order findByMidtransOrderId(String midtransOrderId);

  List<Order> findAll();

  Page<Order> findByFilter(Integer page, Integer size, String orderBy, String sortBy,
      OrderFilterRequest request);

  void resendEmailByMidtransOrderId(String midtransOrderId) throws TemplateException,
      MessagingException, IOException;
}
