package kakao.messagethroughput;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.http.SseMessageCheck;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.sse;

public class Rooms {
    private static final int ROOM_COUNT = Integer.parseInt(System.getProperty("ROOM_COUNT", "1"));

    private static final FeederBuilder<Object> roomIdsFeeder = listFeeder(IntStream.rangeClosed(1, ROOM_COUNT)
            .mapToObj(i -> Map.of("roomId", (Object) i))
            .collect(Collectors.toList())).circular();

    public static final ChainBuilder getId = feed(roomIdsFeeder);

    private static final SseMessageCheck sseCheck = sse.checkMessage("Check SSE")
            .check(substring("msgType").exists());

    public static final ChainBuilder connectStream =
            exec(sse("Connect Stream #{roomId}")
                    .connect("/rooms/#{roomId}/msgs"));

    public static final ChainBuilder disconnectStream =
            exec(sse("Disconnect Stream").close());
}
