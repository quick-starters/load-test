package kakao;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class SSESimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http.baseUrl("http://localhost:8080");

    ChainBuilder sendMessage = exec();

    ChainBuilder connectStream = exec();

    ScenarioBuilder sendUsers = scenario("Send Users").exec(sendMessage);
    ScenarioBuilder reciveUsers = scenario("Send Users").exec(sendMessage);

    {
        setUp(
                sendUsers.injectOpen(
                        rampUsers(10).during(10)
                ),
                reciveUsers.injectOpen(
                        rampUsers(2).during(10)
                )
        ).protocols(httpProtocol);
    }
}
