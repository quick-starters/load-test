package computerdatabase;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import java.util.concurrent.ThreadLocalRandom;

public class RefactBasicSimulation extends Simulation {

    FeederBuilder<String> feeder = csv("search.csv").random();

    ChainBuilder search =
        exec(http("Home").get("/"))
            .pause(1)
            .feed(feeder)
            .exec(
                http("Search")
                    .get("/computers?f=#{searchCriterion}")
                    .check(
                        css("a:contains('#{searchComputerName}')", "href").saveAs("computerUrl")
                    )
            )
            .pause(1)
            .exec(
                http("Select")
                    .get("#{computerUrl}")
                    .check(status().is(200))
            )
            .pause(1);

    // repeat is a loop resolved at RUNTIME
    ChainBuilder browse =
        // Note how we force the counter name, so we can reuse it
        repeat(4, "i").on(
            exec(
                http("Page #{i}")
                    .get("/computers?p=#{i}")
            ).pause(1)
        );

    // Note we should be using a feeder here
    // let's demonstrate how we can retry: let's make the request fail randomly and retry a given
    // number of times

    ChainBuilder edit =
        // let's try at max 2 times
        tryMax(2)
            .on(
                exec(
                    http("Form")
                        .get("/computers/new")
                )
                    .pause(1)
                    .exec(
                        http("Post")
                            .post("/computers")
                            .formParam("name", "Beautiful Computer")
                            .formParam("introduced", "2012-05-30")
                            .formParam("discontinued", "")
                            .formParam("company", "37")
                            .check(
                                status().is(
                                    // we do a check on a condition that's been customized with
                                    // a lambda. It will be evaluated every time a user executes
                                    // the request
                                    session -> 200 + ThreadLocalRandom.current().nextInt(2)
                                )
                            )
                    )
            )
            // if the chain didn't finally succeed, have the user exit the whole scenario
            .exitHereIfFailed();

    HttpProtocolBuilder httpProtocol =
        http.baseUrl("https://computer-database.gatling.io")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate");

    ScenarioBuilder users = scenario("Users").exec(search, browse);
    ScenarioBuilder admins = scenario("Admins").exec(search, browse, edit);

    {
        setUp(
            users.injectOpen(rampUsers(10).during(10)),
            admins.injectOpen(rampUsers(2).during(10))
        ).protocols(httpProtocol);
    }
}
