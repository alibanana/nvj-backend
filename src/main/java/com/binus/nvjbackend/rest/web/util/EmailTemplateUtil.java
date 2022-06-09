package com.binus.nvjbackend.rest.web.util;

import com.binus.nvjbackend.config.properties.SysparamProperties;
import com.binus.nvjbackend.model.entity.Order;
import com.binus.nvjbackend.model.entity.OrderItem;
import com.binus.nvjbackend.model.entity.TicketArchive;
import com.binus.nvjbackend.model.enums.EmailTemplateNames;
import com.binus.nvjbackend.rest.web.model.request.emailtemplate.EmailTemplateSendRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EmailTemplateUtil {

  @Autowired
  private SysparamProperties sysparamProperties;

  @Autowired
  private DateUtil dateUtil;

  public EmailTemplateSendRequest buildWaitingForPaymentEmail(Order order,
      Map<String, TicketArchive> ticketIdAndTicketArchiveMap) {
    return EmailTemplateSendRequest.builder()
        .to(order.getEmail())
        .templateName(EmailTemplateNames.WAITING_FOR_PAYMENT)
        .templateKeyAndValues(
            buildWaitingForPaymentEmailTemplateKeyAndValues(order, ticketIdAndTicketArchiveMap))
        .build();
  }

  private Map<String, Object> buildWaitingForPaymentEmailTemplateKeyAndValues(Order order,
      Map<String, TicketArchive> ticketIdAndTicketArchiveMap) {
    Map<String, Object> content =
        buildEmailTemplateKeyAndValues(order, ticketIdAndTicketArchiveMap);
    String paymentStatusUrl = sysparamProperties.getClientPaymentStatusLink() + "?order_id=" +
        order.getMidtrans().getOrderId();
    content.put("payment_status_url", paymentStatusUrl);
    content.put("link_to_snap_payment", order.getMidtrans().getRedirectUrl());
    return content;
  }

  public EmailTemplateSendRequest buildPaymentSuccessEmail(Order order,
      Map<String, TicketArchive> ticketIdAndTicketArchiveMap) {
    return EmailTemplateSendRequest.builder()
        .to(order.getEmail())
        .templateName(EmailTemplateNames.PAYMENT_SUCCESS)
        .templateKeyAndValues(buildPaymentSuccessAndExpiredEmailTemplateKeyAndValues(
            order, ticketIdAndTicketArchiveMap))
        .build();
  }

  public EmailTemplateSendRequest buildPaymentExpiredEmail(Order order,
      Map<String, TicketArchive> ticketIdAndTicketArchiveMap) {
    return EmailTemplateSendRequest.builder()
        .to(order.getEmail())
        .templateName(EmailTemplateNames.PAYMENT_EXPIRED)
        .templateKeyAndValues(buildPaymentSuccessAndExpiredEmailTemplateKeyAndValues(
            order, ticketIdAndTicketArchiveMap))
        .build();
  }

  public Map<String, Object> buildPaymentSuccessAndExpiredEmailTemplateKeyAndValues(Order order,
      Map<String, TicketArchive> ticketIdAndTicketArchiveMap) {
    Map<String, Object> content =
        buildEmailTemplateKeyAndValues(order, ticketIdAndTicketArchiveMap);
    content.put("full_name", order.getFirstname() + " " + order.getLastname());
    content.put("phone_number", order.getPhoneNumber());
    return content;
  }

  private Map<String, Object> buildEmailTemplateKeyAndValues(Order order,
      Map<String, TicketArchive> ticketIdAndTicketArchiveMap) {
    Map<String, Object> content = new HashMap<>();
    List<Object> orderItemList = new ArrayList<>();
    for (OrderItem orderItem : order.getOrderItems()) {
      TicketArchive ticketArchive = ticketIdAndTicketArchiveMap.get(orderItem.getTicket().getId());
      Map<String, Object> orderItemMap = new HashMap<>();
      orderItemMap.put("title", ticketArchive.getTitle());
      orderItemMap.put("price", ticketArchive.getPrice());
      orderItemMap.put("quantity", orderItem.getQuantity());
      orderItemList.add(orderItemMap);
    }
    content.put("orderItems", orderItemList);
    content.put("order_id", order.getMidtrans().getOrderId());
    content.put("visit_date", dateUtil.toReversedDateOnlyFormat(order.getVisitDate()));
    content.put("grand_total", order.getTotalPrice());
    return content;
  }
}
