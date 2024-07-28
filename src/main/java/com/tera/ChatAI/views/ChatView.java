package com.tera.ChatAI.views;

import com.tera.ChatAI.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequiredArgsConstructor
public class ChatView {

    private final CustomerRepository customerRepository;

    @RequestMapping("/")
    public ModelAndView login(Model model) {
        model.addAttribute("name", "neeraj");

        return new ModelAndView("login");
    }

    @RequestMapping("/customerData")
    public ModelAndView customerData(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        var customers = customerRepository.findAll();
        modelAndView.setViewName("customerData");
        modelAndView.addObject("customers", customers);

        return modelAndView;
    }

    @RequestMapping("/downloadCustomerDataCsv")
    @ResponseBody
    public HttpEntity<ByteArrayResource>  downloadCustomerDataCSV(Model model) throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/files/generated_data.xlsx");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customer_data.xlsx");
        return new HttpEntity<>(new ByteArrayResource(IOUtils.toByteArray(in)), header);
    }


    @RequestMapping("/segment")
    public ModelAndView segment(Model model) {

        return new ModelAndView("Segment");
    }

    @RequestMapping("/campaign")
    public ModelAndView campaign(Model model) {

        return new ModelAndView("campaign");
    }

    @RequestMapping("/content")
    public ModelAndView content(Model model) {

        return new ModelAndView("content");
    }
}
