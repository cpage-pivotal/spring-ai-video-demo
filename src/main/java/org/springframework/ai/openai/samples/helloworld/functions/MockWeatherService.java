package org.springframework.ai.openai.samples.helloworld.functions;

import java.util.function.Function;

import static org.springframework.ai.openai.samples.helloworld.functions.MockWeatherService.*;

public class MockWeatherService implements Function<Request, Response> {

    public enum Unit {CELSIUS, FAHRENHEIT}
    public record Request(String location, Unit unit) {}
    public record Response(double temp, Unit unit) {}

    public Response apply(Request request) {
        System.out.println("CALLED FUNCTION: " + request);
        Response result;
        if (request.location.contains("Houston"))
            result = new Response(75, Unit.FAHRENHEIT);
        else if (request.location.contains("Austin"))
            result = new Response(80, Unit.FAHRENHEIT);
        else if (request.location.contains("Dallas"))
            result = new Response(70, Unit.FAHRENHEIT);
        else result = new Response(60, Unit.FAHRENHEIT);
        return result;
    }
}