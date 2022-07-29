package kakao.messagethroughput;


import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.FeederBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.gatling.javaapi.core.CoreDsl.feed;
import static io.gatling.javaapi.core.CoreDsl.listFeeder;

public class Users {
    private static final int SENDER_COUNT = Integer.parseInt(System.getProperty("SENDER_COUNT", "1"));

    private static final FeederBuilder<Object> userIdsFeeder = listFeeder(IntStream.rangeClosed(1, SENDER_COUNT)
            .mapToObj(i -> Map.of("userId", (Object) i))
            .collect(Collectors.toList())).circular();

    public static final ChainBuilder getId = feed(userIdsFeeder);
}
