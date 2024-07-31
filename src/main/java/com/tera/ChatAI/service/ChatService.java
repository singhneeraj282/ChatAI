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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final String systemText = """
            You are a helpful AI assistant that helps people find information.
            Your name is {name}
            You should reply to the user's request with your name and also in the style of a {segment}
            give me offer related to this segment
            """;

    private static final String USER_TEXT_WHATSAPP = """
            All content generation and responses must strictly reference and adhere to the document provided.\s
            Responses should be clear, concise, and tailored to the needs of those seeking educational content related to Bank of Baroda. \s
            create a whatsapp messages for <segment>. \s
            Segment definition <definition>.\s
            Write the WhatsApp copy with emoji + heading(max 10 words) and subtext(max 32 words) and CTA + reasoning behind the copy you created(100 words). \s
            Take context from knowledge base, segmentation and user persona to create the copy. \s
            Output Guideline: Give a single jSON format "heading": heading,"subtext": subtext, "cta":cta, "reasoning": reasoning.\s
            """;

    private static final String USER_TEXT_NOTIFICATION = """
            All content generation and responses must strictly reference and adhere to the document provided. \s
            Responses should be clear, concise, and tailored to the needs of those seeking educational content related to Bank of Baroda.\s
            create a whatsapp messages for <segment>. \s
            Segment definition is <definition>. \s
            Write a notification copy with heading(max 4 words) and subtext(max 12 words) + reasoning behind the copy you created(100 words). Take context from knowledge base, segmentation and user persona to create the copy. \s 
            Tone of the copy should be stictly based on persona definition. Give in json format.\s
            Output Guideline: Give a single jSON format "heading": heading,"subtext": subtext, "cta":cta, "reasoning": reasoning.\s
            """;

    private static final String USER_TEXT_EMAIL = """
            All content generation and responses must strictly reference and adhere to the document provided.\s
            Responses should be clear, concise, and tailored to the needs of those seeking educational content related to Bank of Baroda.\s
            create a whatsapp messages for <segment>.\s
            Segment definition is <definition>. \s
            Write a Email copy with subject(max 10 words) and title(max 12 words) & introduction (max 80 words)+ reasoning behind the copy you created(100 words).\s
            Take context from knowledge base, segmentation and user persona to create the copy.\s
            Email copy guideline: heading(max 10 words) + subtext (max 80 words)+ reasoning behind the copy you created(100 words).\s
            Tone of the copy should be strictly based on persona definition.\s
            Output Guideline: Give a single jSON format "heading": heading,"subtext": subtext, "cta":cta, "offerReasoning: offerReasoning,"reasoning": reasoning.\s
            """;

    private static final String USER_MARKETING_TEXT_WHATSAPP = """
            Select the relevant offer from the attached document and create a marketing WhatsApp copy for <segment>.\s 
            Segment definition <definition>. \s
            Also give reasoning for copy generation and offer selection.\s
            Guideline for Offer Selection: Use persona details, defintion and characterstics.\s
            Whatsapp copy guideline: Emoji + Heading (6-8 words) + Subtext (24-32 words) + CTA. \s
            Tone of the copy should be strictly based on persona definition.\s
            Output Guideline: Give a single jSON format "heading": heading,"subtext": subtext, "cta":cta, "offerReasoning: offerReasoning,"reasoning": reasoning.\s
            """;

    private static final String USER_MARKETING_TEXT_NOTIFICATION = """
            Select the relevant offer from the attached document and create a marketing notification copy for <segment>. 
            Segment definition- <definition>.\s
            Also give reasoning for copy generation and offer selection.Guideline for Offer Selection: Use persona details, defintion and characterstics.\s
            Notification copy guideline: Write a notification copy with heading(max 4 words) and subtext(max 12 words). \s
            Output Guideline: Give a single jSON format "heading": heading,"subtext": subtext, "cta":cta, "offerReasoning: offerReasoning,"reasoning": reasoning.\s
            """;

    private static final String USER_MARKETING_TEXT_EMAIL = """
            Select the relevant offer from the attached document and create a marketing email copy for <segment>.\s
            Segment definition- <definition>.\s
            Also give reasoning for copy generation and offer selection.\s
            Guideline for Offer Selection: Use persona details, defintion and characterstics.\s
            Email copy guideline: heading(max 10 words) + subtext (max 80 words)+ reasoning behind the copy you created(100 words).\s
            Tone of the copy should be strictly based on persona definition.\s
            Output Guideline: Give a single jSON format "heading": heading,"subtext": subtext, "cta":cta, "offerReasoning: offerReasoning,"reasoning": reasoning.\s
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


//generate message for Educational
        byte[] rbGuideLineInputFile = getClass()
                .getResourceAsStream("/files/rbi.pdf")
                .readAllBytes();
        //young adult
        var educationalYoungAdultPC = personalisedContents(rbGuideLineInputFile, List.of("Young Adult - Salaried"),
                List.of("Young Adult - Salaried - Low Debit"),
                List.of("educational"), List.of("whatsapp", "email", "notification"), "educational");
        //freelancer
        var educationalFreelancerPC = personalisedContents(rbGuideLineInputFile, List.of("Freelancer"),
                List.of("Freelancer - high Credit"),
                List.of("educational"), List.of("whatsapp", "email", "notification"), "educational");
        //retired
        var educationalRetiredPC = personalisedContents(rbGuideLineInputFile, List.of("Retired"),
                List.of("Retired - Saver(ATM + Netbanking)"),
                List.of("educational"), List.of("whatsapp", "email", "notification"), "educational");

//generate message for Marketing
        byte[] offersInputFile = getClass()
                .getResourceAsStream("/files/offers.pdf")
                .readAllBytes();
        //young adult
        var marketingYoungAdultPC = personalisedContents(offersInputFile, List.of("Young Adult - Salaried"),
                List.of("Young Adult - Salaried - Restaurant Lover", "Young Adult - Salaried - Restaurant Lover - Language Hindi, English", "Young Adult - Salaried - Restaurant Lover - Language Hindi, English"),
                List.of("marketing"), List.of("whatsapp", "email", "notification"), "marketing");
        //freelancer
        var marketingFreelancerPC = personalisedContents(offersInputFile, List.of("Freelancer"),
                List.of("Freelancer - High Credit - Online Lover", "Freelancer - High Credit - Online Lover - Language English, Tamil", "Freelancer - High Credit - Online Lover - Language English, Tamil"),
                List.of("marketing"), List.of("whatsapp", "email", "notification"), "marketing");
        //retired
        var marketingRetiredPC = personalisedContents(offersInputFile, List.of("Retired"),
                List.of("Retired - Saver(Saving & FD) Globetrotter", "Retired - Saver(Saving & FD) Globetrotter Language Hindi, Marathi", "Retired - Saver(Saving & FD) Globetrotter Language Hindi, Marathi"),
                List.of("marketing"), List.of("whatsapp", "email", "notification"), "marketing");
        return null;
    }

    private List<PersonalisedContent> personalisedContents(byte[] in, List<String> segments, List<String> personas, List<String> types, List<String> messageTypes, String promptMessageType) {
        return segments.stream()
                .map(segment -> personas.stream()
                        .map(persona -> types.stream()
                                .map(type -> messageTypes.stream()
                                        .map(messageType -> {
                                            try {
                                                var personalisedContent = personalisedContentRepository
                                                        .findBySegmentAndPersonaAndTypeAndMessageType(segment, persona, type, messageType);
                                                if (nonNull(personalisedContent))
                                                    return personalisedContent;
                                                return populateMessagesForEducationalSegment(in, segment, persona, type, messageType, promptMessageType);
                                            } catch (JsonProcessingException ignored) {
                                                return null;
                                            }

                                        }).collect(Collectors.toList())
                                )
                                .flatMap(java.util.Collection::stream)
                                .collect(Collectors.toList())
                        )
                        .flatMap(java.util.Collection::stream)
                        .collect(Collectors.toList())
                ).flatMap(java.util.Collection::stream)
                .toList();
    }

    private PersonalisedContent populateMessagesForEducationalSegment(byte[] in, String segment, String persona, String type, String messageType, String promprtMessageType) throws JsonProcessingException {
        String messageTypeTemplate;
        if ("educational".equals(promprtMessageType)) {
            switch (messageType) {
                case "whatsapp":
                    messageTypeTemplate = USER_TEXT_WHATSAPP;
                    break;
                case "email":
                    messageTypeTemplate = USER_TEXT_EMAIL;
                    break;
                case "notification":
                    messageTypeTemplate = USER_TEXT_NOTIFICATION;
                    break;
                default:
                    messageTypeTemplate = USER_TEXT_NOTIFICATION;
            }
        } else {
            switch (messageType) {
                case "whatsapp":
                    messageTypeTemplate = USER_MARKETING_TEXT_WHATSAPP;
                    break;
                case "email":
                    messageTypeTemplate = USER_MARKETING_TEXT_EMAIL;
                    break;
                case "notification":
                    messageTypeTemplate = USER_MARKETING_TEXT_NOTIFICATION;
                    break;
                default:
                    messageTypeTemplate = USER_TEXT_NOTIFICATION;
            }
        }

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(messageTypeTemplate);
        var segmentDefinition = segmentDefinitionRepository.findSegmentDefinitionBySegment(segment);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(new ByteArrayResource(in)),
                systemPromptTemplate.createMessage(Map.of("definition", segmentDefinition.getDefinition(), "segment", persona)));
        var openAiResponse = objectMapper.readValue(jsonResponse, OpenAiPersonalisedContentResponse.Message.class);
        return Optional.ofNullable(openAiResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .persona(persona)
                        .type(type)
                        .messageType(messageType)
                        .heading(response.getHeading())
                        .subtext(response.getSubtext())
                        .cta(response.getCta())
                        .offerReasoning(response.getOfferReasoning())
                        .reasoning(response.getReasoning())
                        .build()))
                .orElseThrow(() -> new RuntimeException("rponse from openAi is not valid"));
    }

}
