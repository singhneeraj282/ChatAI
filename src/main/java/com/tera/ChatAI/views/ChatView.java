package com.tera.ChatAI.views;

import com.tera.ChatAI.repository.CustomerRepository;
import com.tera.ChatAI.repository.PersonalisedContentRepository;
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
    private final PersonalisedContentRepository personalisedContentRepository;

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

    @RequestMapping("/downloadSegmentDataLogic")
    @ResponseBody
    public HttpEntity<ByteArrayResource>  downloadSegmentDataCsv(Model model) throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/files/SegmentationLogicImage.png");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SegmentationLogic.png");
        return new HttpEntity<>(new ByteArrayResource(IOUtils.toByteArray(in)), header);

    }

    @RequestMapping("/downloadSegmentDefinition")
    @ResponseBody
    public HttpEntity<ByteArrayResource>  downloadSegmentDataLogic(Model model) throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/files/persona_definition.xlsx");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=persona_definition.xlsx");
        return new HttpEntity<>(new ByteArrayResource(IOUtils.toByteArray(in)), header);
    }

    @RequestMapping("/downloadCampaignData")
    @ResponseBody
    public HttpEntity<ByteArrayResource>  downloadCampaignData(Model model) throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/files/CampaignPersonalised.xlsx");
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=CampaignPersonalised.xlsx");
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
        ModelAndView modelAndView = new ModelAndView();
        var customers = personalisedContentRepository.findAll();
        modelAndView.setViewName("content");
        modelAndView.addObject("personalisedData", customers);

        return modelAndView;

    }
}
