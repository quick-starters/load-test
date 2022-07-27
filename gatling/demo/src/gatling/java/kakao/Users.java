package kakao;


import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.listFeeder;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Users {
    public static final ChainBuilder create =
            exec(http("Create User")
                    .post("/users")
                    .body(StringBody("{\"name\": \"gatling test\", \"avatar\": \"\"}"))
                    .check(jmesPath("id").exists().saveAs("userId")));

    private static final FeederBuilder<Object> usersFeeder = listFeeder(IntStream.range(1, 10)
            .mapToObj(i -> Map.of("userId", (Object) i))
            .collect(Collectors.toList())).queue();

    private static final ChainBuilder get = feed(usersFeeder)
            .exec(session -> session.set("userId", "#userId"));
}
