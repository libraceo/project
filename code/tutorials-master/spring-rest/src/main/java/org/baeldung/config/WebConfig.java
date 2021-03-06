package org.baeldung.config;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan({ "org.baeldung.web" })
public class WebConfig extends WebMvcConfigurerAdapter {

    public WebConfig() {
        super();
    }

    //

    @Override
    public void configureMessageConverters(final List<HttpMessageConverter<?>> messageConverters) {
        messageConverters.add(createXmlHttpMessageConverter());
        // messageConverters.add(new MappingJackson2HttpMessageConverter());

        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true).dateFormat(new SimpleDateFormat("dd-MM-yyyy hh:mm"));
        messageConverters.add(new MappingJackson2HttpMessageConverter(builder.build()));
        // messageConverters.add(new MappingJackson2XmlHttpMessageConverter(builder.createXmlMapper(true).build()));

        super.configureMessageConverters(messageConverters);
    }

    private HttpMessageConverter<Object> createXmlHttpMessageConverter() {
        final MarshallingHttpMessageConverter xmlConverter = new MarshallingHttpMessageConverter();

        final XStreamMarshaller xstreamMarshaller = new XStreamMarshaller();
        xmlConverter.setMarshaller(xstreamMarshaller);
        xmlConverter.setUnmarshaller(xstreamMarshaller);

        return xmlConverter;
    }

}
