/*
 * Copyright 2025 BellSoft (info@bell-sw.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.asm0dey.botassistant.config;

/*-
 * #%L
 * hero-guide-demo-app
 * %%
 * Copyright (C) 2025 BellSoft
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.TEXT_HTML;

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
            return List.of(TEXT_HTML);
        }

        @Override
        @NonNull
        public JteModel read(@Nullable Class<? extends JteModel> clazz, @Nullable HttpInputMessage inputMessage) throws HttpMessageNotReadableException {
            throw new UnsupportedOperationException("Reading not supported");
        }

        @Override
        public void write(JteModel jteModel, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
            outputMessage.getHeaders().setContentType(TEXT_HTML);

            try (OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), UTF_8)) {
                writer.write(jteModel.render());
            }
        }
    }
}
