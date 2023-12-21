package com.example.email.controller;

import com.amazonaws.auth.*;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.example.email.dto.Email;
import com.example.email.utils.EmailDecryptor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.example.email.utils.AWSCredentialsHelper.getAwsCredentials;

@Slf4j
@RestController
@Tag(name = "Email", description = "Email base controller")
@RequestMapping("/")
public class EmailController {

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Access via web browser", description = "Allows anyone get the service introduction via root path.")
    @GetMapping("/")
    public String get() {
        return "<h2>This is the email service in UCD Parcel Delivery System.</h2>" +
                "<h2>Swagger API Document: <a href='/swagger-ui/index.html'>/swagger-ui/index.html</a>.</h2>" +
                "<h2>For more information, please refer: <a href='https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Email'>GitHub page</a>.</h2>";
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Send an email", description = "Allowed other systems invoke to send an email.")
    @PostMapping("/send")
    public void sendEmail(@RequestBody byte[] encryptedData) {
        try {
            Email email = EmailDecryptor.decrypt(encryptedData);
            email.setHtmlBody(
                    email.getHtmlBody() +
                    "<hr>" +
                    "<p>UCD Parcel Delivery System</p>"
            );
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(getAwsCredentials()))
                            .withRegion(Regions.EU_WEST_1).build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(
                            new Destination().withToAddresses(email.getTo()))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(email.getHtmlBody())))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(email.getSubject())))
                    .withSource("no_reply@mail.ucdparcel.ie");
            client.sendEmail(request);
            log.info("Send email to: " + email.getTo() + ", with subject: " + email.getSubject());
        } catch (Exception e) {
            log.error("Data decrypt fail: " + e.getMessage());
        }
    }
}
