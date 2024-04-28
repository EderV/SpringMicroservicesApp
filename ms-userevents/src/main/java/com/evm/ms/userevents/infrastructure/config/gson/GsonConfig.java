package com.evm.ms.userevents.infrastructure.config.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.ZonedDateTime;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new TypeAdapter<ZonedDateTime>() {
            @Override
            public void write(JsonWriter jsonWriter, ZonedDateTime zonedDateTime) throws IOException {
                jsonWriter.value(zonedDateTime.toString());
            }

            @Override
            public ZonedDateTime read(JsonReader jsonReader) throws IOException {
                return ZonedDateTime.parse(jsonReader.nextString());
            }
        }).create();
    }

}
