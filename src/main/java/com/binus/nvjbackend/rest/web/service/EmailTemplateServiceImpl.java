package com.binus.nvjbackend.rest.web.service;

import com.binus.nvjbackend.model.entity.EmailTemplate;
import com.binus.nvjbackend.model.enums.ErrorCode;
import com.binus.nvjbackend.model.exception.BaseException;
import com.binus.nvjbackend.repository.EmailTemplateRepository;
import com.binus.nvjbackend.rest.web.model.request.emailtemplate.EmailTemplateRequest;
import com.binus.nvjbackend.rest.web.model.request.emailtemplate.EmailTemplateSendRequest;
import freemarker.core.InvalidReferenceException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Objects;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  private FreeMarkerConfigurer freeMarkerConfigurer;

  @Autowired
  private EmailTemplateRepository emailTemplateRepository;

  @Override
  public EmailTemplate createNewTemplate(EmailTemplateRequest request) {
    validateEmailTemplateDoesNotExists(request.getTemplateName());
    return emailTemplateRepository.save(buildEmailTemplate(request));
  }

  @Override
  public EmailTemplate findByTemplateName(String templateName) {
    EmailTemplate emailTemplate = emailTemplateRepository.findByTemplateName(templateName);
    validateEmailTemplateNotNull(emailTemplate);
    return emailTemplate;
  }

  @Override
  public EmailTemplate updateByTemplateName(EmailTemplateRequest request) {
    EmailTemplate emailTemplate = emailTemplateRepository.findByTemplateName(
        request.getTemplateName());
    validateEmailTemplateNotNull(emailTemplate);
    emailTemplate.setFrom(request.getFrom());
    emailTemplate.setSubject(request.getSubject());
    emailTemplate.setContent(request.getContent());
    return emailTemplateRepository.save(emailTemplate);
  }

  @Override
  public void sendEmailTemplate(EmailTemplateSendRequest request) throws IOException,
      TemplateException, MessagingException {
    EmailTemplate emailTemplate = emailTemplateRepository
        .findByTemplateName(request.getTemplateName());
    validateEmailTemplateNotNull(emailTemplate);
    Template template = new Template(emailTemplate.getTemplateName(), emailTemplate.getContent(),
        freeMarkerConfigurer.getConfiguration());
    try {
      String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(template,
          request.getTemplateKeyAndValues());
      sendMessage(emailTemplate.getFrom(), request.getTo(), emailTemplate.getSubject(), htmlBody);
    } catch (InvalidReferenceException e) {
      throw new BaseException(ErrorCode.EMAIL_TEMPLATE_KEY_VALUES_MISSING);
    }
  }

  @Override
  public void deleteByTemplateName(String templateName) {
    validateEmailTemplateExists(templateName);
    emailTemplateRepository.deleteByTemplateName(templateName);
  }

  private void validateEmailTemplateDoesNotExists(String templateName) {
    if (emailTemplateRepository.existsByTemplateName(templateName)) {
      throw new BaseException(ErrorCode.EMAIL_TEMPLATE_EXISTS);
    }
  }

  private void validateEmailTemplateNotNull(EmailTemplate template) {
    if (Objects.isNull(template)) {
      throw new BaseException(ErrorCode.EMAIL_TEMPLATE_NAME_NOT_FOUND);
    }
  }

  private void validateEmailTemplateExists(String templateName) {
    if (!emailTemplateRepository.existsByTemplateName(templateName)) {
      throw new BaseException(ErrorCode.EMAIL_TEMPLATE_NAME_NOT_FOUND);
    }
  }

  private EmailTemplate buildEmailTemplate(EmailTemplateRequest request) {
    EmailTemplate emailTemplate = new EmailTemplate();
    BeanUtils.copyProperties(request, emailTemplate);
    return emailTemplate;
  }

  private void sendMessage(String from, String to, String subject, String htmlBody)
      throws MessagingException {
    MimeMessage message = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    helper.setFrom(from);
    helper.setTo(to);
    helper.setSubject(subject);
    helper.setText(htmlBody, true);
    javaMailSender.send(message);
  }
}
