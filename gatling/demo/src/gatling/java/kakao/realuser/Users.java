package kakao.realuser;


import io.gatling.javaapi.core.ChainBuilder;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Users {
    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USER_COUNT", "100"));

    public static final ChainBuilder create =
            exec(http("Create User")
                    .post("/users")
                    .body(StringBody("{\"name\": \"gatling test\", \"avatar\": \"\"}"))
                    .check(jmesPath("id").exists().saveAs("userId")));
}
