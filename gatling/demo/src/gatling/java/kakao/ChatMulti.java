package kakao;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.sse;

public class ChatMulti extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080");

    /////////////////////////////////
    // 1. CONFIG
    /////////////////////////////////

    final int roomId = 1;

    //////////////////
    // sse - config
    //////////////////
    final int sseOnceUsers = 1;

    final long sseWaitingSeconds = 70;

    //////////////////
    // send - config
    //////////////////
    final int sendOnceUsers = 1;

    final int sendSeconds = 60;
    final int maxUserId = 10;

    List<Map<String, Object>> feederList = IntStream.rangeClosed(1, maxUserId).mapToObj(i -> {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", i);
        map.put("msg", i);
        return map;
    }).collect(Collectors.toList());

    /////////////////////////////////
    // 2. SSE
    /////////////////////////////////
    ChainBuilder sseChain = exec(sse("msgs").connect("/rooms/" + roomId + "/msgs"))
            .pause(sseWaitingSeconds)
            .exec(sse("msgs-close").close());

    ScenarioBuilder sseScn = scenario("sse msgs").exec(sseChain);

    /////////////////////////////////
    // 3. SEND
    /////////////////////////////////
    ChainBuilder sendChain = feed(listFeeder(feederList).circular())
            .pause(1)
            .exec(
                    http("request_1")
                            .post("/rooms/" + roomId + "/msgs")
                            .header("content-type", "application/json")
                            .body(StringBody("{\"userId\":#{userId},\"msg\":\"#{msg}\"}"))
            );

    ScenarioBuilder sendScn = scenario("send msg").during(Duration.ofSeconds(sendSeconds)).on(sendChain);

    /////////////////////////////////
    // 4. RUN
    /////////////////////////////////
    {
        setUp(
                sseScn.injectOpen(atOnceUsers(sseOnceUsers)),
                sendScn.injectOpen(atOnceUsers(sendOnceUsers))
        ).protocols(httpProtocol);
    }
}
