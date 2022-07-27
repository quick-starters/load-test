package sse;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class SSESimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080");

    List<Map<String, Object>> feederList = IntStream.rangeClosed(1, 10).mapToObj(i -> {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", i);
        map.put("msg", i);
        return map;
    }).collect(Collectors.toList());
    ChainBuilder sendMessage = exec();

    ChainBuilder connectStream = exec();

    ScenarioBuilder sendUsers = scenario("Send Users").exec(sendMessage);
    ScenarioBuilder receive = scenario("Send Users").exec(sendMessage);


    // 1분간 송신 사용자 10명, 수신 사용자 100명 목표로 테스트
    // 송신 사용자는 1초당 한번씩 메시지 송신
    {
        setUp(
                sendUsers.injectOpen(
                        rampUsers(10).during(60)
                ),
                receive.injectOpen(
                        rampUsers(100).during(60)
                )
        ).protocols(httpProtocol);
    }
}
