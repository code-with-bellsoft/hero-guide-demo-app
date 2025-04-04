package com.github.asm0dey.chatapi.config;

import gg.jte.models.runtime.JteModel;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Configuration for Spring MVC.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configure resource handlers for static resources.
     *
     * @param registry the ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Configure webjars resource handler
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .setCachePeriod(3600) // Cache for 1 hour
                .resourceChain(true);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new JteModelHttpMessageConverter());
    }

    private static class JteModelHttpMessageConverter implements HttpMessageConverter<JteModel> {
        @Override
        public boolean canRead(@Nullable Class<?> clazz, MediaType mediaType) {
            return false;
        }

        @Override
        public boolean canWrite(@NonNull Class<?> clazz, MediaType mediaType) {
            return JteModel.class.isAssignableFrom(clazz);
        }

        @Override
        @NonNull
        public List<MediaType> getSupportedMediaTypes() {
            return List.of(MediaType.TEXT_HTML);
        }

        @Override
        @NonNull
        public JteModel read(@Nullable Class<? extends JteModel> clazz, @Nullable HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
            throw new UnsupportedOperationException("Reading not supported");
        }

        @Override
        public void write(JteModel jteModel, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
            outputMessage.getHeaders().setContentType(MediaType.TEXT_HTML);
            try (OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), StandardCharsets.UTF_8)) {
                writer.write(jteModel.render());
            }
        }
    }
}
