package com.tera.ChatAI.bo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OpenAiPersonalisedContentResponse {
    private Message message;

    @Getter
    @Setter
    @Builder
    public static class Message {
        String heading;
        String subtext;
        String cta;
        String offerReasoning;
        String reasoning;
    }
    @Getter
    @Setter
    @Builder
    public static class EducationalWhatsappMessage {
        String heading;
        String subtext;
        String cta;
        String offerReasoning;
        String reasoning;
    }
    @Getter
    @Setter
    @Builder
    public static class EducationalNotificationMessage {
        String heading;
        String subtext;
        String cta;
        String offerReasoning;
        String reasoning;
    }

    @Getter
    @Setter
    @Builder
    public static class EducationalEmailMessage {
        String heading;
        String subtext;
        String cta;
        String offerReasoning;
        String reasoning;
    }
}
