package com.conduit.data;

import com.conduit.api.models.Article;
import com.conduit.api.models.Comment;
import com.conduit.api.models.User;
import com.conduit.config.Config;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;
import net.datafaker.Faker;

public final class TestDataFactory {

    private static final Faker FAKER = new Faker(Locale.ENGLISH);

    private TestDataFactory() {
    }

    public static User randomUser() {
        String uniqueSuffix = uniqueSuffix();
        return User.builder()
                .username(FAKER.internet().username().replaceAll("[^a-zA-Z0-9]", "") + uniqueSuffix)
                .email("qa." + uniqueSuffix + "@" + FAKER.internet().domainName())
                .password(Config.defaultPassword())
                .build();
    }

    public static Article randomArticle() {
        return Article.builder()
                .title(FAKER.book().title() + " " + uniqueSuffix())
                .description(FAKER.lorem().sentence())
                .body(FAKER.lorem().paragraph())
                .tagList(List.of(FAKER.book().genre().toLowerCase(Locale.ROOT).replace(' ', '-')))
                .build();
    }

    public static Comment randomComment() {
        return Comment.builder()
                .body(FAKER.lorem().sentence())
                .build();
    }

    private static String uniqueSuffix() {
        return System.currentTimeMillis() + String.valueOf(ThreadLocalRandom.current().nextInt(1000, 9999));
    }
}
