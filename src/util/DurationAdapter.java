package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration value) throws IOException {
        if (value != null) {
            jsonWriter.value(value.toSeconds());
        } else {
            jsonWriter.value("<не задано>");
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == null) {
            return null;
        }
        String value = jsonReader.nextString();
        if (value.equals("<не задано>")) {
            return null;
        }
        return Duration.ofSeconds(Long.parseLong(value));
    }
}