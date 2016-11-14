package de.effectivetrainings.tracer.spring;

import de.effectivetrainings.tracer.influx.InfluxConnection;
import de.effectivetrainings.tracer.repository.TracerRepository;
import okhttp3.OkHttpClient;
import org.influxdb.InfluxDB;
import org.influxdb.impl.InfluxDBImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracerWebConfig {


    @Value("${tracer.influx.connection.url}")
    private String influxUrl;
    @Value("${tracer.influx.connection.user}")
    private String user;
    @Value("${tracer.influx.connection.password}")
    private String password;
    @Value("${tracer.influx.connection.database:tracer}")
    private String database;

    @Bean
    public InfluxDB influxDB() {
        final InfluxConnection influxConnection = tracerInfluxConnection();
        return new InfluxDBImpl(influxConnection.getUrl(), influxConnection.getUser(), influxConnection.getPassword(), tracerClientBuilder());

    }

    @ConditionalOnMissingBean
    @Bean
    public OkHttpClient.Builder tracerClientBuilder() {
        return new OkHttpClient.Builder();
    }


    @Bean
    public InfluxConnection tracerInfluxConnection() {
        return new InfluxConnection(influxUrl, user, password, database);
    }

    @Bean
    public TracerRepository tracerRepository() {
        return new TracerRepository(influxDB(), database);
    }

}
