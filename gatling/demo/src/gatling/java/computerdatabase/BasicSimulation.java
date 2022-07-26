package computerdatabase;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.time.Duration;

public class BasicSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://computer-database.gatling.io")
            .doNotTrackHeader("1")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate");

    ScenarioBuilder scn =
            scenario("Scenario Name")
                    .exec(http("request_1").get("/"))
                    .pause(7)
                    .exec(http("request_2").get("/computers?f=macbook"))
                    .pause(2)
                    .exec(http("request_3").get("/computers/6"))
                    .pause(3)
                    .exec(http("request_4").get("/"))
                    .pause(2)
                    .exec(http("request_5").get("/computers?p=1"))
                    .pause(Duration.ofMillis(670))
                    .exec(http("request_6").get("/computers?p=2"))
                    .pause(Duration.ofMillis(629))
                    .exec(http("request_7").get("/computers?p=3"))
                    .pause(Duration.ofMillis(734))
                    .exec(http("request_8").get("/computers?p=4"))
                    .pause(5)
                    .exec(http("request_9").get("/computers/new"))
                    .pause(1)
                    .exec(
                            http("request_10")
                                    .post("/computers")
                                    .formParam("name", "Beautiful Computer")
                                    .formParam("introduced", "2012-05-30")
                                    .formParam("discontinued", "")
                                    .formParam("company", "37"));
    {
        setUp(scn.injectOpen(atOnceUsers(1))
                .protocols(httpProtocol));
    }
}
