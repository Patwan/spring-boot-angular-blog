package com.pwebk.SpringBootBlogApplication.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service@AllArgsConstructor
public class MailContentBuilder {

    //We bring TemplateEngine dependency on board
    private final TemplateEngine templateEngine;

    //In this method we pass in the body of the email we want to send, we instantiate Context object from
    //thymeleaf templating engine. We set the message to the message that was passed
    //We then return the templatingengine usig the process method
    //So at runtime tymeleaf will add our email message to our HTML template.
    String build(String message){
        Context context = new Context();
        context.setVariable("message", message);
        return templateEngine.process("mailTemplate", context);
    }
}
