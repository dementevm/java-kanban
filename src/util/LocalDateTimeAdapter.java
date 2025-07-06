package util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(formatter));
        } else {
            jsonWriter.value("<не задано>");
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == null) {
            return null;
        }
        String value = jsonReader.nextString();
        if (value.equals("<не задано>")) {
            return null;
        }
        return LocalDateTime.parse(value, formatter);
    }
}