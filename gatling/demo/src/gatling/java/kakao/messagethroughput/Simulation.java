package kakao.messagethroughput;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class Simulation extends io.gatling.javaapi.core.Simulation {

    private static final int ROOM_COUNT = Integer.parseInt(System.getProperty("ROOM_COUNT", "1"));
    private static final int USER_LIFETIME = Integer.parseInt(System.getProperty("USER_LIFETIME", "1"));
    private static final int SENDER_COUNT = Integer.parseInt(System.getProperty("SENDER_COUNT", "100"));
    private static final int RECEIVER_COUNT = Integer.parseInt(System.getProperty("RECEIVER_COUNT", "10"));
    private static final int SENDER_RAMP_TIME = Integer.parseInt(System.getProperty("SENDER_RAMP_TIME", "10"));
    private static final int RECEIVER_RAMP_TIME = Integer.parseInt(System.getProperty("RECEIVER_RAMP_TIME", "10"));

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
        System.out.printf("- 방 개수: %d개%n", ROOM_COUNT);
        System.out.printf("- 유저 생명 주기: %d초%n", USER_LIFETIME);
        System.out.printf("- 수신 유저 수: %d명%n", RECEIVER_COUNT);
        System.out.printf("- 수신 유저 램프 업 시간: %d초%n", RECEIVER_RAMP_TIME);
        System.out.printf("- 송신 유저 수: %d명%n", SENDER_COUNT);
        System.out.printf("- 송신 유저 램프 업 시간: %d초%n", SENDER_RAMP_TIME);
    }

    private static class UserJourneys {
        private static Duration minPace = Duration.ofMillis(900);
        private static Duration maxPace = Duration.ofSeconds(1);

        public static ChainBuilder receiver =
                exec(Rooms.getId)
                .exec(Rooms.connectStream);
//                .exec(Rooms.disconnectStream);

        public static ChainBuilder sender =
                exec(Users.getId)
                .exec(Rooms.getId)
                .during(USER_LIFETIME).on(pace(minPace, maxPace).exec(Messages.send));
    }

    private final ScenarioBuilder sender = scenario("Inject Sender Scenario")
            .exec(UserJourneys.sender);

    private final ScenarioBuilder receiver = scenario("Inject Receiver Scenario")
            .exec(UserJourneys.receiver);

    {
        Integer TEST_TIME = RECEIVER_RAMP_TIME + SENDER_RAMP_TIME + USER_LIFETIME;

        setUp(
                receiver.injectOpen(rampUsers(RECEIVER_COUNT).during(RECEIVER_RAMP_TIME)),
                sender.injectOpen(
                        nothingFor(RECEIVER_RAMP_TIME),
                        rampUsers(SENDER_COUNT).during(SENDER_RAMP_TIME))
        )
                .maxDuration(TEST_TIME)
                .protocols(httpProtocol);
    }

    @Override
    public void after() {
        // TODO 각종 초기화 작업 (ex: 데이터베이스 초기화)
        System.out.println("부하 테스트가 종료되었습니다.");
    }
}
