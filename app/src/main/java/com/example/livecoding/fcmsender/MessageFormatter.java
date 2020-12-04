package com.example.livecoding.fcmsender;

public class MessageFormatter {
    private static String sampleMsgFormat = "{" +
            "  \"to\": \"/topics/%s\"," +
            "  \"data\": {" +
            "       \"title\":\"%s\"," +
            "       \"body\":\"%s\"" +
            "   }" +
            "}";

    public static String getSampleMessage(String topic, String title, String body){
        return String.format(sampleMsgFormat, topic, title, body);
    }

}
