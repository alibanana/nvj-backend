package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.EmailTemplate;
import com.binus.nvjbackend.rest.web.model.request.emailtemplate.EmailTemplateRequest;
import com.binus.nvjbackend.rest.web.model.request.emailtemplate.EmailTemplateSendRequest;
import freemarker.template.TemplateException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailTemplateService {

  EmailTemplate createNewTemplate(EmailTemplateRequest request);

  EmailTemplate findByTemplateName(String templateName);

  EmailTemplate updateByTemplateName(EmailTemplateRequest request);

  void sendEmailTemplate(EmailTemplateSendRequest request) throws IOException, TemplateException,
      MessagingException;

  void deleteByTemplateName(String templateName);
}
