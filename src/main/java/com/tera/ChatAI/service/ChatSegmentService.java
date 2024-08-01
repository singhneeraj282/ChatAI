package com.tera.ChatAI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tera.ChatAI.bo.OpenAiPersonalisedContentResponse;
import com.tera.ChatAI.dto.ChatDTO;
import com.tera.ChatAI.entity.PersonalisedContent;
import com.tera.ChatAI.repository.PersonalisedContentRepository;
import com.tera.ChatAI.repository.SegmentDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.azure.openai.AzureOpenAiChatModel;
import org.springframework.ai.chat.messages.Media;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static org.codehaus.groovy.runtime.DefaultGroovyMethods.collect;

@Service
@RequiredArgsConstructor
public class ChatSegmentService {


    private static final String systemText = """
            You are a helpful AI assistant that helps people find information.
            Your name is {name}
            You should reply to the user's request with your name and also in the style of a {segment}
            give me offer related to this segment
            """;

    private final String EDUCATIONAL_WHATSAPP = """
            All content generation and responses must strictly reference and adhere to the document provided. Responses should be clear, concise, and tailored to the needs of those seeking educational content related to Bank of Baroda.
            Create a whatsapp messages for {segment}.
            Segment definition is {definition}.
            Write the WhatsApp copy with emoji + heading(max 10 words) and subtext(max 32 words) and CTA + reasoning behind the copy you created(100 words). Tone of the copy should be stictly based on persona definition.
            Take context from knowledge base, segmentation and user persona to create the copy.
            """;

    private final String EDUCATIONAL_NOTIFICATION = """
            All content generation and responses must strictly reference and adhere to the document provided. Responses should be clear, concise, and tailored to the needs of those seeking educational content related to Bank of Baroda.
             Create a notification copy for {segment}. Segment definition is {definition}.
             Write a notification copy with heading(max 4 words) and subtext(max 12 words) + reasoning behind the copy you created(100 words).
             Take context from knowledge base, segmentation and user persona to create the copy. Tone of the copy should be stictly based on persona definition.
            """;

    private final String EDUCATIONAL_EMAIL = """
            All content generation and responses must strictly reference and adhere to the document provided. Responses should be clear, concise, and tailored to the needs of those seeking educational content related to Bank of Baroda.
             Create a Email messages for {segment}.Segment definition is {definition}.
             Write a Email copy with subject(max 10 words) and title(max 12 words) & introduction (max 80 words)+ reasoning behind the copy you created(100 words). Tone of the copy should be stictly based on persona definition.
             Take context from knowledge base, segmentation and user persona to create the copy.
            """;

    private final String MARKETING1_WHATSAPP = """
            Select the relevant offer from the attached document and create a marketing WhatsApp copy for {segment}. Segment definition - {definition}. Also give reasoning for copy generation and offer selection.
             Guideline for Offer Selection: Use persona details, defintion and characterstics.
             Whatsapp copy guideline: Emoji + Heading (6-8 words) + Subtext (24-32 words) + CTA. Tone of the copy should be stictly based on persona definition.
             Output Guideline: Give a message + Offer Selection Reasoning + Copy generation reasoning.
            """;

    private final String MARKETING1_EMAIL = """
            Select the relevant offer from the attached document and create a marketing email copy for {segment}. Segment definition- {definition}. Also give reasoning for copy generation and offer selection.
            Guideline for Offer Selection: Use persona details, defintion and characterstics.
            Email copy guideline: subject(max 10 words) + title(max 12 words) & introduction (max 80 words)+ reasoning behind the copy you created(100 words). Tone of the copy should be stictly based on persona definition.
            Output Guideline: Give a message + Offer Selection Reasoning + Copy generation reasoning.
            """;

    private final String MARKETING1_NOTIFICATION = """
            Select the relevant offer from the attached document and create a marketing notifcation copy for {segment}. Segment definition- {definition}. Also give reasoning for copy generation and offer selection.
            Guideline for Offer Selection: Use persona details, defintion and characterstics.
            Notification copy guideline: Write a notification copy with heading(max 4 words) and subtext(max 12 words). Tone of the copy should be stictly based on persona definition.
            Output Guideline: Give a message + Offer Selection Reasoning + Copy generation reasoning.
            """;

    private final String MARKETING2_WHATSAPP = """
            Select the relevant offer from the attached document and create a marketing email copy for {segment}. Segment definition- {definition}. Also give reasoning for copy generation and offer selection.
            Guideline for Offer Selection: Use persona details, defintion and characterstics.
            Email copy guideline: subject(max 10 words) + title(max 12 words) & introduction (max 80 words)+ reasoning behind the copy you created(100 words). Tone of the copy should be stictly based on persona definition.
            Output Guideline: Give a single jSON with message + Offer Selection Reasoning + Copy generation reasoning.
                        """;

    private final String MARKETING2_EMAIL = """
            Select the relevant offer from the attached document and create a marketing email copy for {segment}. Segment definition- {definition}. Also give reasoning for copy generation and offer selection.
            Guideline for Offer Selection: Use persona details, defintion and characterstics.
            Email copy guideline: subject(max 10 words) + title(max 12 words) & introduction (max 80 words)+ reasoning behind the copy you created(100 words). Tone of the copy should be stictly based on persona definition.
            Output Guideline: Give a message + Offer Selection Reasoning + Copy generation reasoning.
            """;

    private final String MARKETING2_NOTIFICATION = """
            "Select the relevant offer from the attached document and create a marketing notifcation copy for {segment}. Segment definition- {definition}. Also give reasoning for copy generation and offer selection.
             Guideline for Offer Selection: Use persona details, defintion and characterstics.
             Notification copy guideline: Write a notification copy with heading(max 4 words) and subtext(max 12 words). Tone of the copy should be stictly based on persona definition.
             Output Guideline: Give a single jSON with message + Offer Selection Reasoning + Copy generation reasoning."
            """;


    private final OpenAiChatModel chatModel;
    private final AzureOpenAiChatModel azureOpenAiChatModel;
    private final ObjectMapper objectMapper;
    private final PersonalisedContentRepository personalisedContentRepository;
    private final SegmentDefinitionRepository segmentDefinitionRepository;

    public String chat(String message) {
        return chatModel.call(message);
    }

    public String azureChat(String message) {
        return azureOpenAiChatModel.call(message);
    }

    public String chatWithDocument(ChatDTO dto, MultipartFile file) {
        return chatModel.call(new UserMessage(dto.getMessage()), new SystemMessage(file.getResource()));
    }

    public String chatWithSegments(ChatDTO dto) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemText);
        //all the  Segment will come from DB
        //for now giving the static genz segment, and this dto should be replaced with segmentBO which will come from BO
        String segment = "genz";
        return chatModel.call(new UserMessage(dto.getMessage(), new Media(MimeTypeUtils.APPLICATION_JSON, objectMapper.valueToTree(dto))),
                systemPromptTemplate.createMessage(Map.of("name", "Bank of Baroda", "voice", "Bank reception")));
    }

    public List<PersonalisedContent> azureChatWithSegments() throws IOException {


        byte[] rbGuideLineInputFile = getClass()
                .getResourceAsStream("/files/rbi.pdf")
                .readAllBytes();

        //MARKETING1

        //whatsapp
        var marketing1WhatsappPC = Stream.of("Freelancer - High Credit - Online Lover", "Retired - Saver(Saving & FD) Globetrotter", "Young Adult - Salaried - Restaurant Lover - Language Hindi")
                .map(segment -> {
                    var personalisedContent = personalisedContentRepository
                            .findBySegmentAndTypeAndMessageType(segment, "marketing1", "whatsapp");
                    if (nonNull(personalisedContent))
                        return personalisedContent;
                    return populateMessagesForMarketing1SegmentWhatsapp(rbGuideLineInputFile, segment);
                }).toList();
        //email
        var marketing1EmailPC = Stream.of("Freelancer - High Credit - Online Lover", "Retired - Saver(Saving & FD) Globetrotter", "Young Adult - Salaried - Restaurant Lover - Language Hindi")
                .map(segment -> {
                    var personalisedContent = personalisedContentRepository
                            .findBySegmentAndTypeAndMessageType(segment, "marketing1", "email");
                    if (nonNull(personalisedContent))
                        return personalisedContent;
                    return populateMessagesForMarketing1SegmentEmail(rbGuideLineInputFile, segment);
                }).toList();
        //notification
        var marketing1NotificationPc = Stream.of("Freelancer - High Credit - Online Lover", "Retired - Saver(Saving & FD) Globetrotter", "Young Adult - Salaried - Restaurant Lover - Language Hindi")
                .map(segment -> {
                    var personalisedContent = personalisedContentRepository
                            .findBySegmentAndTypeAndMessageType(segment, "marketing1", "notification");
                    if (nonNull(personalisedContent))
                        return personalisedContent;
                    return populateMessagesForMarketing1SegmentNotification(rbGuideLineInputFile, segment);
                }).toList();

        //MARKETING2

        //whatsapp
        var marketing2WhatsappPC = Stream.of("Young Adult - Salaried - Restaurant Lover - Language Hindi", "Freelancer - High Credit - Online Lover - Language English", "Retired - Saver(Saving & FD) Globetrotter Language Hinglish")
                .map(segment -> {
                    var personalisedContent = personalisedContentRepository
                            .findBySegmentAndTypeAndMessageType(segment, "marketing2", "whatsapp");
                    if (nonNull(personalisedContent))
                        return personalisedContent;
                    return populateMessagesForMarketing2SegmentWhatsapp(rbGuideLineInputFile, segment);
                }).toList();
        //email
        var marketing2EmailPC = Stream.of("Young Adult - Salaried - Restaurant Lover - Language Hindi", "Freelancer - High Credit - Online Lover - Language English", "Retired - Saver(Saving & FD) Globetrotter Language Hinglish")
                .map(segment -> {
                    var personalisedContent = personalisedContentRepository
                            .findBySegmentAndTypeAndMessageType(segment, "marketing2", "email");
                    if (nonNull(personalisedContent))
                        return personalisedContent;
                    return populateMessagesForMarketing2SegmentEmail(rbGuideLineInputFile, segment);
                }).toList();
        //notification
        var marketing2NotificationPc = Stream.of("Young Adult - Salaried - Restaurant Lover - Language Hindi", "Freelancer - High Credit - Online Lover - Language English", "Retired - Saver(Saving & FD) Globetrotter Language Hinglish")
                .map(segment -> {
                    var personalisedContent = personalisedContentRepository
                            .findBySegmentAndTypeAndMessageType(segment, "marketing2", "notification");
                    if (nonNull(personalisedContent))
                        return personalisedContent;
                    return populateMessagesForMarketing2SegmentNotification(rbGuideLineInputFile, segment);
                }).toList();
        return null;
    }


    //populateMessagesForEducationalSegmentWhatsapp
    private PersonalisedContent populateMessagesForEducationalSegmentWhatsapp(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(EDUCATIONAL_WHATSAPP);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(new ByteArrayResource(in)),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("educational")
                        .messageType("whatsapp")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    //populateMessagesForEducationalSegmentEmail
    private PersonalisedContent populateMessagesForEducationalSegmentEmail(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(EDUCATIONAL_EMAIL);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(offer),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("educational")
                        .messageType("email")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    //populateMessagesForEducationalSegmentNotification
    private PersonalisedContent populateMessagesForEducationalSegmentNotification(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(EDUCATIONAL_NOTIFICATION);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(new ByteArrayResource(in)),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("educational")
                        .messageType("notification")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    //populateMessagesForMarketing1SegmentWhatsapp
    private PersonalisedContent populateMessagesForMarketing1SegmentWhatsapp(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(MARKETING1_WHATSAPP);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(offer),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("marketing1")
                        .messageType("whatsapp")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    //populateMessagesForMarketing1SegmentEmail
    private PersonalisedContent populateMessagesForMarketing1SegmentEmail(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(MARKETING1_EMAIL);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(offer),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("marketing1")
                        .messageType("email")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    //populateMessagesForMarketing1SegmentNotification
    private PersonalisedContent populateMessagesForMarketing1SegmentNotification(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(MARKETING1_NOTIFICATION);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(offer),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("marketing1")
                        .messageType("notification")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    //populateMessagesForMarketing2SegmentWhatsapp
    private PersonalisedContent populateMessagesForMarketing2SegmentWhatsapp(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(MARKETING2_WHATSAPP);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(offer),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("marketing2")
                        .messageType("whatsapp")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    //populateMessagesForMarketing2SegmentEmail
    private PersonalisedContent populateMessagesForMarketing2SegmentEmail(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(MARKETING2_EMAIL);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(offer),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("marketing2")
                        .messageType("email")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    //populateMessagesForMarketing2SegmentNotification
    private PersonalisedContent populateMessagesForMarketing2SegmentNotification(byte[] in, String segment) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(MARKETING2_NOTIFICATION);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(offer),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", segment)));
        return Optional.ofNullable(jsonResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type("marketing2")
                        .messageType("notification")
                        .content(jsonResponse)
                        .build()))
                .orElseThrow(() -> new RuntimeException("response from openAi is not valid"));
    }

    private String offer = """
            Travel
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Discount 10% Off on
            MakeMyTrip
            Get 10% off on flight
            bookings on
            MakeMyTrip.
            Travel Credit Pan
            India Digital
            Cashback
            15%
            Cashback on
            Yatra
            Avail 15% cashback
            on hotel bookings via
            Yatra using BoB
            cards.
            Travel Debit,
            Credit
            Pan
            India Digital
            Bonus
            Points
            Double Points
            on Goibibo
            Earn double reward
            points on
            international
            bookings with
            Goibibo.
            Travel Credit Pan
            India Digital
            Discount 20% Off on
            IRCTC
            Get 20% off on train
            tickets booked via
            IRCTC with BoB
            cards.
            Travel Debit Pan
            India Digital
            Cashback 5% Cashback
            on Cleartrip
            Get 5% cashback on
            domestic flight
            bookings on
            Cleartrip.
            Travel Credit Pan
            India Digital
            Discount
            Flat 25% Off
            on OYO
            Rooms
            Enjoy flat 25% off on
            OYO Rooms
            bookings.
            Travel Debit,
            Credit
            Pan
            India Digital
            Discount
            10% Off on
            Ola
            Outstation
            Get 10% off on Ola
            Outstation rides
            using BoB cards.
            Travel Debit Pan
            India Digital
            Cashback
            20%
            Cashback on
            Uber
            Avail 20% cashback
            on Uber rides with
            BoB credit cards.
            Travel Credit Pan
            India Digital
            Discount 30% Off on
            RedBus
            Get 30% off on bus
            bookings with
            Travel Debit Pan
            India Digital
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            RedBus using BoB
            debit cards.
            Discount 10% Off on
            Vistara
            Enjoy 10% off on
            Vistara flight
            bookings using BoB
            net banking.
            Travel Netbanking Pan
            India Digital
            Shopping
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Discount 20% Off on
            Amazon
            Get 20% off on
            purchases above
            ₹2000 on Amazon
            India.
            Shopping Credit Pan
            India Digital
            Cashback
            15%
            Cashback on
            Flipkart
            Avail 15% cashback
            on electronics
            purchases on
            Flipkart.
            Shopping Debit,
            Credit
            Pan
            India Digital
            Bonus
            Points
            Triple Points
            on Myntra
            Earn triple reward
            points on fashion
            purchases on
            Myntra.
            Shopping Credit Pan
            India Digital
            Discount 25% Off on
            Ajio
            Get 25% off on
            clothing and
            accessories on Ajio.
            Shopping Debit Pan
            India Digital
            Cashback
            10%
            Cashback on
            BigBasket
            Avail 10% cashback
            on grocery shopping
            on BigBasket.
            Shopping Credit Pan
            India Digital
            Discount 20% Off on
            Tata Cliq
            Enjoy 20% off on
            electronics and
            apparel on Tata Cliq.
            Shopping Debit,
            Credit
            Pan
            India Digital
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Discount 15% Off on
            Pepperfry
            Get 15% off on
            furniture and decor
            on Pepperfry.
            Shopping Debit Pan
            India Digital
            Cashback
            20%
            Cashback on
            Paytm Mall
            Avail 20% cashback
            on purchases via
            Paytm Mall.
            Shopping Credit Pan
            India Digital
            Discount 30% Off on
            Nykaa
            Get 30% off on
            beauty products on
            Nykaa.
            Shopping Debit Pan
            India Digital
            Discount 10% Off on
            Lenskart
            Enjoy 10% off on
            eyewear on Lenskart
            using BoB net
            banking.
            Shopping Netbanking Pan
            India Digital
            Grocery
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Discount 10% Off on
            Grofers
            Get 10% off on
            grocery purchases
            on Grofers.
            Grocery Credit Pan India Digital
            Cashback 5% Cashback
            on DMart
            Avail 5% cashback
            on DMart grocery
            shopping.
            Grocery Debit,
            Credit
            Select
            Cities Offline
            Bonus
            Points
            Double Points
            on Nature's
            Basket
            Earn double reward
            points on organic
            products at Nature's
            Basket.
            Grocery Credit Pan India Digital
            Discount 15% Off on
            Spencers
            Get 15% off on
            grocery shopping at
            Spencers.
            Grocery Debit Select
            Cities Offline
            Cashback
            10%
            Cashback on
            More
            Avail 10% cashback
            on More grocery
            Grocery Credit Select
            Cities Offline
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            stores with BoB
            cards.
            Discount
            20% Off on
            Reliance
            Fresh
            Enjoy 20% off on
            fresh produce at
            Reliance Fresh.
            Grocery Debit,
            Credit Pan India Offline
            Discount 5% Off on
            Star Bazaar
            Get 5% off on
            grocery shopping at
            Star Bazaar.
            Grocery Debit Select
            Cities Offline
            Cashback
            15%
            Cashback on
            HyperCITY
            Avail 15% cashback
            on purchases at
            HyperCITY.
            Grocery Credit Select
            Cities Offline
            Discount 10% Off on
            Spar
            Enjoy 10% off on
            grocery shopping at
            Spar.
            Grocery Debit Select
            Cities Offline
            Discount 25% Off on
            FoodHall
            Get 25% off on
            gourmet products at
            FoodHall using BoB
            net banking.
            Grocery Netbanking Select
            Cities Offline
            Restaurant
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Discount 15% Off on
            Zomato
            Get 15% off on food
            orders on Zomato
            using BoB cards.
            Restaurant Credit Pan
            India Digital
            Cashback
            20%
            Cashback on
            Swiggy
            Avail 20% cashback
            on Swiggy orders. Restaurant Debit,
            Credit
            Pan
            India Digital
            Bonus
            Points
            Triple Points
            on Dineout
            Earn triple reward
            points on
            reservations via
            Dineout.
            Restaurant Credit Pan
            India Digital
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Discount 25% Off on
            Domino's
            Get 25% off on
            pizza orders from
            Domino's.
            Restaurant Debit Pan
            India Digital
            Cashback
            10%
            Cashback on
            Pizza Hut
            Avail 10% cashback
            on orders from
            Pizza Hut.
            Restaurant Credit Pan
            India Digital
            Discount 20% Off on
            McDonald's
            Enjoy 20% off on
            meals at
            McDonald's outlets.
            Restaurant Debit,
            Credit
            Pan
            India Offline
            Discount 15% Off on
            KFC
            Get 15% off on
            orders from KFC. Restaurant Debit Pan
            India Digital
            Cashback 5% Cashback
            on Burger King
            Avail 5% cashback
            on meals at Burger
            King.
            Restaurant Credit Pan
            India Offline
            Discount 10% Off on
            Starbucks
            Enjoy 10% off on
            beverages at
            Starbucks.
            Restaurant Debit Pan
            India Offline
            Discount
            20% Off on
            Barbeque
            Nation
            Get 20% off on
            dining at Barbeque
            Nation using BoB
            net banking.
            Restaurant Netbanking Pan
            India Offline
            Entertainment
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Discount 10% Off on
            BookMyShow
            Get 10% off on
            movie tickets
            booked via
            BookMyShow.
            Entertainment Credit Pan
            India Digital
            Cashback
            15%
            Cashback on
            PVR Cinemas
            Avail 15%
            cashback on PVR
            Cinemas ticket
            purchases.
            Entertainment Debit,
            Credit
            Pan
            India Offline
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Bonus
            Points
            Double Points
            on Netflix
            Earn double
            reward points on
            Netflix
            subscriptions.
            Entertainment Credit Pan
            India Digital
            Discount 20% Off on
            Amazon Prime
            Get 20% off on
            Amazon Prime
            memberships.
            Entertainment Debit Pan
            India Digital
            Cashback
            10%
            Cashback on
            Zee5
            Avail 10%
            cashback on
            Zee5 annual
            subscriptions.
            Entertainment Credit Pan
            India Digital
            Discount 25% Off on
            Hotstar
            Enjoy 25% off on
            Disney+ Hotstar
            subscriptions.
            Entertainment Debit,
            Credit
            Pan
            India Digital
            Discount 10% Off on
            Gaana Plus
            Get 10% off on
            Gaana Plus
            subscriptions.
            Entertainment Debit Pan
            India Digital
            Cashback
            20%
            Cashback on
            SonyLIV
            Avail 20%
            cashback on
            SonyLIV
            subscriptions.
            Entertainment Credit Pan
            India Digital
            Discount
            30% Off on
            Spotify
            Premium
            Get 30% off on
            Spotify Premium
            subscriptions
            using BoB debit
            cards.
            Entertainment Debit Pan
            India Digital
            Discount 10% Off on
            Tata Sky Binge
            Enjoy 10% off on
            Tata Sky Binge
            subscriptions
            using BoB net
            banking.
            Entertainment Netbanking Pan
            India Digital
            Healthcare
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Discount 10% Off on
            Practo
            Get 10% off on
            online consultations
            via Practo.
            Healthcare Credit Pan
            India Digital
            Cashback
            15%
            Cashback on
            PharmEasy
            Avail 15% cashback
            on medicine orders
            on PharmEasy.
            Healthcare Debit,
            Credit
            Pan
            India Digital
            Bonus
            Points
            Double
            Points on
            1mg
            Earn double reward
            points on orders
            from 1mg.
            Healthcare Credit Pan
            India Digital
            Discount 20% Off on
            Medlife
            Get 20% off on
            healthcare products
            on Medlife.
            Healthcare Debit Pan
            India Digital
            Cashback
            10%
            Cashback on
            Apollo
            Avail 10% cashback
            on health check-ups
            at Apollo Hospitals.
            Healthcare Credit Pan
            India Offline
            Discount 25% Off on
            Dr. Batra's
            Enjoy 25% off on
            homeopathy
            treatments at Dr.
            Batra's.
            Healthcare Debit,
            Credit
            Pan
            India Offline
            Discount 15% Off on
            HealthKart
            Get 15% off on
            health supplements
            at HealthKart.
            Healthcare Debit Pan
            India Digital
            Cashback 5% Cashback
            on NetMeds
            Avail 5% cashback
            on medicine orders
            from NetMeds.
            Healthcare Credit Pan
            India Digital
            Discount 10% Off on
            Fortis
            Enjoy 10% off on
            consultations at
            Fortis Hospitals.
            Healthcare Debit Pan
            India Offline
            Discount
            20% Off on
            SRL
            Diagnostics
            Get 20% off on
            diagnostic tests at
            SRL Diagnostics
            using BoB net
            banking.
            Healthcare Netbanking Pan
            India Offline
            Bill
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Cashback
            5% Cashback
            on Electricity
            Bills
            Avail 5% cashback
            on electricity bill
            payments via BoB
            net banking.
            Bill Netbanking Pan
            India Digital
            Discount
            10% Off on
            Mobile
            Recharge
            Get 10% off on
            mobile recharges
            done via BoB debit
            cards.
            Bill Debit Pan
            India Digital
            Bonus
            Points
            Double Points
            on DTH
            Recharge
            Earn double reward
            points on DTH
            recharges using BoB
            credit cards.
            Bill Credit Pan
            India Digital
            Cashback
            10%
            Cashback on
            Gas Bill
            Avail 10% cashback
            on gas bill payments
            using BoB cards.
            Bill Debit,
            Credit
            Pan
            India Digital
            Discount 15% Off on
            Broadband Bill
            Get 15% off on
            broadband bill
            payments via BoB
            net banking.
            Bill Netbanking Pan
            India Digital
            Cashback 5% Cashback
            on Water Bill
            Avail 5% cashback
            on water bill
            payments using BoB
            debit cards.
            Bill Debit Pan
            India Digital
            Bonus
            Points
            Triple Points
            on Cable TV
            Bill
            Earn triple reward
            points on cable TV
            bill payments with
            BoB credit cards.
            Bill Credit Pan
            India Digital
            Discount 20% Off on
            Landline Bill
            Get 20% off on
            landline bill
            payments via BoB
            net banking.
            Bill Netbanking Pan
            India Digital
            Offer
            Type Offer Title Offer Short
            Description
            Offer
            Category Product Location Digital/Offline
            Cashback
            10%
            Cashback on
            Insurance
            Premium
            Avail 10% cashback
            on insurance
            premium payments
            with BoB cards.
            Bill Debit,
            Credit
            Pan
            India Digital
            Discount
            25% Off on
            Credit Card
            Bill
            Enjoy 25% off on
            BoB credit card bill
            payments using BoB
            net banking.
            Bill Netbanking Pan
            India Digital
            """;

}
