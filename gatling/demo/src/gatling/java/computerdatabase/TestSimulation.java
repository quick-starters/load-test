package computerdatabase;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class TestSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://computer-database.gatling.io")
            .doNotTrackHeader("1")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate");

    FeederBuilder<String> feeder = csv("search.csv").random();
    ChainBuilder search =
            exec(http("Load Home Page").get("/"))
                    .pause(1)
                    .feed(feeder)
                    .exec(http("Search").get("/computers?f=#{searchCriterion}")
                            .check(css("a:contains('#{searchComputerName}')", "href")
                                    .saveAs("computerUrl"))
                    )
                    .pause(1)
                    .exec(http("Select").get("#{computerUrl}")
                            .check(status().is(200))
                    )
                    .pause(1);

    ChainBuilder browse =
            repeat(4, "i").on(
                    exec(http("Browse Page #{i}").get("/computers?p=#{i}"))
                            .pause(2)
            );

    ChainBuilder create =
            exec(http("Load Create Page").get("/computers/new"))
                    .pause(2)
                    .exec(http("Create").post("/computers")
                            .formParam("name", "Beautiful Computer")
                            .formParam("introduced", "2012-05-30")
                            .formParam("discontinued", "")
                            .formParam("company", "37"));

    ScenarioBuilder users = scenario("Users").exec(search, browse);
    ScenarioBuilder admins = scenario("Admins").exec(search, browse, create);

    {
        setUp(
                users.injectOpen(rampUsers(10).during(10)),
                admins.injectOpen(rampUsers(2).during(10))
        ).protocols(httpProtocol);
    }
}
