package kakao;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Rooms {

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
                return session.set("roomId", roomIds.get(new Random().nextInt(roomIds.size())));
            })
                    .exec(http("Enter Room")
                            .post("/rooms/#{roomId}/users")
                            .body(StringBody("{ \"userId\": #{userId}}"))
                            .check(jmesPath("room.id").isEL("#{roomId}")));
}
