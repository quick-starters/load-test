package kakao.realuser;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Simulation extends io.gatling.javaapi.core.Simulation {
    private static final int USER_COUNT = Integer.parseInt(System.getProperty("USER_COUNT", "1"));
    private static final int USER_LIFETIME = Integer.parseInt(System.getProperty("USER_LIFETIME", "1"));

    private HttpProtocolBuilder httpProtocol = http
            .enableHttp2()
            .baseUrl("http://localhost:8080")
            .header("Cache-Control", "no-cache")
            .contentTypeHeader("application/json")
            .acceptHeader("application/json");

    @Override
    public void before() {
        // TODO 각종 초기화 작업 (ex: 데이터베이스 테스트 데이터 설정)
        System.out.printf("부하 테스트를 시작합니다.%n");
        System.out.printf("- 유저 수: %d명%n", USER_COUNT);
        System.out.printf("- 유저 생명 주기: %d초%n", USER_LIFETIME);
    }

    private static class UserJourneys {
        private static Duration minPause = Duration.ofMillis(200);
        private static Duration maxPause = Duration.ofSeconds(1);

        public static ChainBuilder hostUsers =
            exec(Users.create)
            .pause(minPause, maxPause)
            .exec(Rooms.create)
            .pause(minPause, maxPause)
            .exec(Rooms.getList)
            .pause(minPause, maxPause)
            .exec(Rooms.enter)
            .pause(minPause, maxPause)
            .exec(Rooms.connectStream)
            .pause(minPause, maxPause)
            .during(USER_LIFETIME).on(pace(1).exec(Messages.send))
            .exec(Rooms.disconnectStream);

        public static ChainBuilder normalUsers =
            exec(Users.create)
            .pause(minPause, maxPause)
            .exec(Rooms.getList)
            .pause(minPause, maxPause)
            .exec(Rooms.enter)
            .pause(minPause, maxPause)
            .exec(Rooms.connectStream)
            .pause(minPause, maxPause)
            .during(USER_LIFETIME).on(pace(1).exec(Messages.send))
            .exec(Rooms.disconnectStream);
    }

    private final ScenarioBuilder randomUsers = scenario("Inject Random Users Scenario")
            .randomSwitch().on(
                    Choice.withWeight(10d, exec(UserJourneys.hostUsers)),
                    Choice.withWeight(90d, exec(UserJourneys.normalUsers))
            );

    // 랜덤한 유저 (호스트, 참가자) 주입
    {
        setUp(
                randomUsers.injectOpen(
                    rampUsers(USER_COUNT).during(30)
                )
        ).maxDuration(USER_LIFETIME)
        .protocols(httpProtocol);
    }

    @Override
    public void after() {
        // TODO 각종 초기화 작업 (ex: 데이터베이스 초기화)
        System.out.println("부하 테스트가 종료되었습니다.");
    }
}
