package com.example.livecoding.fcmsender;

public class MessageFormatter {
    private static String sampleMsgFormat = "{" +
            "  \"to\": \"/topics/%s\"," +
            "  \"notification\": {" +
            "       \"title\":\"%s\"," +
            "       \"body\":\"%s\"" +
            "       \"image\":\"%s\","+
            "   }" +
            "}";

    public static String getSampleMessage(String topic, String title, String body,String img_url){
        return String.format(sampleMsgFormat, topic, title, body,img_url);
    }

}
