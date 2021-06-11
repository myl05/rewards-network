package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.javafaker.Faker;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author mlee
 **/

public class DataGenerator {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String getRandomUsername(){
        Faker faker = new Faker();
        String username = faker.name().username();
        return username;
    }

    public static ObjectNode getRandomNameQuery() {
        Faker faker = new Faker();
        ObjectNode queryInfo = objectMapper.createObjectNode();

        queryInfo.putPOJO("name", faker.name().fullName());
        return queryInfo;
    }

}
