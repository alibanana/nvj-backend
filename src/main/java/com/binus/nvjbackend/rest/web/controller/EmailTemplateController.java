package com.binus.nvjbackend.rest.web.controller;

import com.binus.nvjbackend.model.entity.EmailTemplate;
import com.binus.nvjbackend.rest.web.model.ApiPath;
import com.binus.nvjbackend.rest.web.model.request.emailtemplate.EmailTemplateRequest;
import com.binus.nvjbackend.rest.web.model.request.emailtemplate.EmailTemplateSendRequest;
import com.binus.nvjbackend.rest.web.model.response.EmailTemplateResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestBaseResponse;
import com.binus.nvjbackend.rest.web.model.response.rest.RestSingleResponse;
import com.binus.nvjbackend.rest.web.service.EmailTemplateService;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;

@Api(value = "Email Templates", description = "Email Templates Service API")
@RestController
@RequestMapping(value = ApiPath.BASE_PATH_EMAIL_TEMPLATE)
public class EmailTemplateController extends BaseController {

  @Autowired
  private EmailTemplateService emailTemplateService;

  @PostMapping(value = ApiPath.EMAIL_TEMPLATE_CREATE)
  public RestSingleResponse<EmailTemplateResponse> createEmailTemplate(
      @Valid @RequestBody EmailTemplateRequest request) {
    EmailTemplate emailTemplate = emailTemplateService.createNewTemplate(request);
    return toSingleResponse(toEmailTemplateResponse(emailTemplate));
  }

  @GetMapping(value = ApiPath.EMAIL_TEMPLATE_FIND_BY_TEMPLATE_NAME)
  public RestSingleResponse<EmailTemplateResponse> findByTemplateName(
      @PathVariable("templateName") String templateName) {
    EmailTemplate emailTemplate = emailTemplateService.findByTemplateName(templateName);
    return toSingleResponse(toEmailTemplateResponse(emailTemplate));
  }

  @GetMapping(value = ApiPath.EMAIL_TEMPLATE_CHECK_BY_TEMPLATE_NAME)
  public ResponseEntity<String> checkByTemplateName(
      @PathVariable("templateName") String templateName) {
    String htmlContent = emailTemplateService.findByTemplateName(templateName).getContent();
    return ResponseEntity.ok()
        .contentType(MediaType.TEXT_HTML)
        .body(htmlContent);
  }

  @PutMapping(value = ApiPath.EMAIL_TEMPLATE_UPDATE_BY_TEMPLATE_NAME)
  public RestSingleResponse<EmailTemplateResponse> updateByTemplateName(
      @Valid @RequestBody EmailTemplateRequest request) {
    EmailTemplate emailTemplate = emailTemplateService.updateByTemplateName(request);
    return toSingleResponse(toEmailTemplateResponse(emailTemplate));
  }

  @PostMapping(value = ApiPath.EMAIL_TEMPLATE_SEND)
  public RestBaseResponse sendEmailTemplate(@Valid @RequestBody EmailTemplateSendRequest request)
      throws TemplateException, MessagingException, IOException {
    emailTemplateService.sendEmailTemplate(request);
    return toBaseResponse();
  }

  @DeleteMapping(value = ApiPath.EMAIL_TEMPLATE_DELETE_BY_TEMPLATE_NAME)
  public RestBaseResponse deleteByTemplateName(@PathVariable("templateName") String templateName) {
    emailTemplateService.deleteByTemplateName(templateName);
    return toBaseResponse();
  }

  private EmailTemplateResponse toEmailTemplateResponse(EmailTemplate emailTemplate) {
    EmailTemplateResponse response = new EmailTemplateResponse();
    BeanUtils.copyProperties(emailTemplate, response);
    return response;
  }
}
