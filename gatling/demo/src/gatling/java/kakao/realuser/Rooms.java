package kakao.realuser;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.http.SseMessageCheck;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.sse;

public class Rooms {
    private static final int USER_LIFETIME = Integer.parseInt(System.getProperty("USER_LIFETIME", "1"));
    private static final int ROOM_COUNT = Integer.parseInt(System.getProperty("ROOM_COUNT", "1"));

    private static FeederBuilder.Batchable<String> roomsFeeder = csv("data/rooms.csv").random().circular();

    public static final ChainBuilder create = feed(roomsFeeder)
            .exec(http("Create Room : #{roomTitle}")
                    .post("/rooms")
                    .body(ElFileBody("kakao/recordedsimulation/create-room.json"))
                    .check(jmesPath("id").exists().saveAs("roomId")));

    public static final ChainBuilder getList =
            exec(http("Get Rooms")
                    .get("/rooms?userId=#{userId}")
                    .check(jmesPath("[? id == `-1`]").ofList().is(Collections.emptyList()))
                    .check(jmesPath("[*].id").ofList().saveAs("roomIds")));

    public static final ChainBuilder enter =
            exec(session -> {
                List<Integer> roomIds = session.getList("roomIds");
                if (roomIds.size() > 0)
                    return session.set("roomId", roomIds.get(new Random().nextInt(roomIds.size())));
                else
                    return session;
            })
            .exec(http("Enter Room")
                    .post("/rooms/#{roomId}/users")
                    .body(StringBody("{ \"userId\": #{userId}}"))
                    .check(jmesPath("room.id").isEL("#{roomId}")));

    private static final SseMessageCheck sseCheck = sse.checkMessage("Check SSE")
            .check(jmesPath("data.msgType").exists());

    public static final ChainBuilder connectStream =
            exec(sse("Connect Stream")
                    .connect("/rooms/#{roomId}/msgs")
                    .await(USER_LIFETIME).on(sseCheck));

    public static final ChainBuilder disconnectStream =
            exec(sse("Disconnect Stream").close());
}
