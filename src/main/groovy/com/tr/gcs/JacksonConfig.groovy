package com.tr.gcs

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

import java.text.DateFormat
import java.text.SimpleDateFormat

class JacksonConfig
{
    /**
     * Sets up and returns an ObjectMapper.
     *
     * @return The new ObjectMapper
     */
    static ObjectMapper objectMapper()
    {
        final ObjectMapper MAPPER = new ObjectMapper()
        MAPPER.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true)
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        DateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'", Locale.US)
        utcDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        MAPPER.dateFormat = utcDateFormat

        MAPPER.registerModule(new JavaTimeModule())
        MAPPER
    }
}

