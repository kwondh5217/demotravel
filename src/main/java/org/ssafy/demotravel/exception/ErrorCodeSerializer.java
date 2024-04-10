package org.ssafy.demotravel.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ErrorCodeSerializer extends JsonSerializer<ErrorCode> {

    @Override
    public void serialize(ErrorCode errors, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeFieldName("errors");
        gen.writeStartArray();
        try {
            gen.writeStartObject();
            gen.writeStringField("errorCode", errors.getHttpStatus().getReasonPhrase());
            gen.writeStringField("errorMessage", errors.getMessage());

            gen.writeEndObject();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        gen.writeEndArray();
    }
}
