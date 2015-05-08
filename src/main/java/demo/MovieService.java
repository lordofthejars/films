package demo;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonValue;

@ApplicationScoped
public class MovieService {

    private List<JsonValue> movies = Arrays.asList(
                                                    Json.createObjectBuilder().add("title", "Snow White and the Seven Dwarfs ")
                                                                              .add("creator", "The brothers Grimm")
                                                                              .add("front", "")
                                                                              .build());

    public List<JsonValue> findMovies() {
        return movies;
    }
}
