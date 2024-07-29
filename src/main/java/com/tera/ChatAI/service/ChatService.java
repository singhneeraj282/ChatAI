package com.tera.ChatAI.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tera.ChatAI.bo.OpenAiPersonalisedContentResponse;
import com.tera.ChatAI.dto.ChatDTO;
import com.tera.ChatAI.entity.PersonalisedContent;
import com.tera.ChatAI.repository.PersonalisedContentRepository;
import lombok.RequiredArgsConstructor;
import org.springdoc.webmvc.api.OpenApiWebMvcResource;
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
            give me offer related to this seg
            """;

    private static final String USER_TEXT_WHATSAPP = """
            All content generation and responses must strictly reference and adhere to the document provided. Responses should be clear, concise, and tailored to the needs of those seeking {type} content related to Bank of Baroda.\s
            create a whatsapp messages for a {segment} person(archetype = ruler) who has a saving & FD account with bank of baroda and an atm card he rarely uses (no internet banking or credit usage).\s
            Write the message copy with emoji + heading(max 10 words) and subtext(max 32 words) + reasoning behind the copy you created(100 words). Take context from knowledge base, segmentation and user profile to create the copy. Give in json format "heading": heading,"subtext": subtext,"reasoning": reasoning.\s
            """;

    private static final String USER_TEXT_EMAIL = """
            All content generation and responses must strictly reference and adhere to the document provided. Responses should be clear, concise, and tailored to the needs of those seeking {type} content related to Bank of Baroda.\s
            create a email messages for a {segment} person(archetype = ruler) who has a saving & FD account with bank of baroda and an atm card he rarely uses (no internet banking or credit usage).\s
            Write a message copy with heading(max 10 words) and subtext(max 80 words) + reasoning behind the copy you created(100 words). Take context from knowledge base, segmentation and user  profile to create the copy. Give in json format  "heading": heading,"subtext": subtext,"reasoning": reasoning.\s
            """;

    private static final String USER_TEXT_NOTIFICATION = """
            All content generation and responses must strictly reference and adhere to the document provided. Responses should be clear, concise, and tailored to the needs of those seeking {type} content related to Bank of Baroda.\s
            create a Notification messages for a {segment} person(archetype = ruler) who has a saving & FD account with bank of baroda and an atm card he rarely uses (no internet banking or credit usage).\s
            Write a message copy with heading(max 4 words) and subtext(max 12 words) + reasoning behind the copy you created(100 words). Take context from knowledge base, segmentation and user  profile to create the copy. Give in json format  "heading": heading,"subtext": subtext,"reasoning": reasoning.\s
            """;
    private final OpenApiWebMvcResource openApiResource;

    private final OpenAiChatModel chatModel;
    private final AzureOpenAiChatModel azureOpenAiChatModel;
    private final ObjectMapper objectMapper;
    private final PersonalisedContentRepository personalisedContentRepository;

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
        byte[] in = getClass()
                .getResourceAsStream("/files/rbi.pdf")
                .readAllBytes();

      /*  SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(USER_TEXT_WHATSAPP);
       return azureOpenAiChatModel.call(new UserMessage(new ByteArrayResource(in)),
                systemPromptTemplate.createMessage(Map.of("type", "Educational", "segment", "salaried")));*/


        return Stream.of("Salaried", "Retired", "Freshers")
                .map(segment -> Stream.of("Educational", "Marketing")
                        .map(type -> Stream.of("whatsapp", "email", "notification")
                                .map(messageType -> {
                                    try {
                                        var personalisedContent = personalisedContentRepository
                                                .findBySegmentAndTypeAndMessageType(segment, type, messageType);
                                        if (nonNull(personalisedContent))
                                            return personalisedContent;
                                        return populateMessagesForSegment(in, segment, type, messageType);
                                    } catch (JsonProcessingException ignored) {
                                        return null;
                                    }

                                }).collect(Collectors.toList())
                        )
                        .flatMap(java.util.Collection::stream)
                        .collect(Collectors.toList())
                )
                .flatMap(java.util.Collection::stream)
                .collect(Collectors.toList());

    }

    private PersonalisedContent populateMessagesForSegment(byte[] in, String segment, String type, String messageType) throws JsonProcessingException {
        String messageTypeTemplate;
        switch (messageType) {
            case "whatsapp":
                messageTypeTemplate = USER_TEXT_WHATSAPP;
            case "email":
                messageTypeTemplate = USER_TEXT_EMAIL;
            case "notification":
                messageTypeTemplate = USER_TEXT_NOTIFICATION;
            default:
                messageTypeTemplate = USER_TEXT_NOTIFICATION;
        }

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(messageTypeTemplate);
        var jsonResponse = azureOpenAiChatModel.call(new UserMessage(new ByteArrayResource(in)),
                systemPromptTemplate.createMessage(Map.of("type", type, "segment", segment)));
        var openAiResponse = objectMapper.readValue(jsonResponse, OpenAiPersonalisedContentResponse.Message.class);
        return Optional.ofNullable(openAiResponse)
                .map(response -> personalisedContentRepository.save(PersonalisedContent.builder()
                        .segment(segment)
                        .type(type)
                        .messageType(messageType)
                        .heading(response.getHeading())
                        .subtext(response.getSubtext())
                        .reasoning(response.getReasoning())
                        .build()))
                .orElseThrow(() -> new RuntimeException("rponse from openAi is not valid"));
    }


}
