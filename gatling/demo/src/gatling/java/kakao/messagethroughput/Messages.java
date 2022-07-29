package kakao.messagethroughput;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Messages {

    private static FeederBuilder.Batchable<String> messagesFeeder = csv("data/messages.csv").circular();

    public static ChainBuilder send = feed(messagesFeeder)
            .exec(http("Send Message #{roomId}")
                    .post("/rooms/#{roomId}/msgs")
                    .body(ElFileBody("kakao/recordedsimulation/send-message.json")));

    public static ChainBuilder sendLargeMessage = feed(messagesFeeder)
            .exec(http("Send Message #{messageBody}")
                    .post("/rooms/#{roomId}/msgs")
                    .body(ElFileBody("kakao/recordedsimulation/large-send-message.json")));
}
