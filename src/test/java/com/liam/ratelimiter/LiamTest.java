package com.liam.ratelimiter;

import com.eudemon.ratelimiter.exception.InternalErrorException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Stopwatch;
import com.liam.ratelimiter.rule.ApiLimit;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Test
public class LiamTest {

    public void testM1(){
        System.out.println("xxx");
        System.out.println(TimeUnit.SECONDS.toMillis(1));
        Stopwatch started = Stopwatch.createStarted();

             do {
                 if(started.elapsed(TimeUnit.MILLISECONDS) > TimeUnit.SECONDS.toMillis(2)){
                     System.out.println(" ....2");
                     started.reset();
                     started.start();
                 }
             }while (true);

    }

    public void testLiamRateLimiter(){
        LiamRateLimiterV1 liamRateLimiter = new LiamRateLimiterV1();
        try {
            boolean isLimit = liamRateLimiter.limit("app1", "/xxx");
        } catch (InternalErrorException e) {
            e.printStackTrace();
        }
    }

    public void testJsonNode(){
        String carJson =
                "{ \"brand\" : \"Mercedes\", \"doors\" : 5," +
                        "  \"owners\" : [\"John\", \"Jack\", \"Jill\"]," +
                        "  \"nestedObject\" : { \"field\" : \"value\" } }";

        ApiLimit apiLimit = new ApiLimit();
        apiLimit.setApi("api1");
        apiLimit.setLimit(10);
        apiLimit.setUnit(1);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ObjectNode objectNode = objectMapper.createObjectNode();
            ObjectNode put = objectNode.put("x", "y");
            System.out.println(put);//{"x":"y"}
            System.out.println(put.get("x"));//"y"
            System.out.println(put.get("x").asText());//y



            JsonNode node1 = objectMapper.valueToTree(apiLimit);
            System.out.println(node1);

            JsonNode node = objectMapper.readTree(carJson);
            System.out.println(node);

            JsonNode jsonNode = objectMapper.readValue(carJson, JsonNode.class);
            System.out.println(jsonNode);

            JsonNode brandNode = jsonNode.get("brand");
            String brand = brandNode.asText();
            System.out.println("brand = " + brand);

            JsonNode doorsNode = jsonNode.get("doors");
            int doors = doorsNode.asInt();
            System.out.println("doors = " + doors);

            JsonNode array = jsonNode.get("owners");
            JsonNode personName = array.get(0);
            String john = personName.asText();
            System.out.println("john  = " + john);

            JsonNode child = jsonNode.get("nestedObject");
            JsonNode childField = child.get("field");
            String field = childField.asText();
            System.out.println("field = " + field);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
