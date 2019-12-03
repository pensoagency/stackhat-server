package penso.stackhat.server.rest;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import penso.stackhat.server.filter.JWTTokenNeededFilter;
import penso.stackhat.server.util.KeyGenerator;
import penso.stackhat.server.util.SimpleKeyGenerator;

public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        packages("penso.stackhat.server.filter", "penso.stackhat.server.rest");

        register(JWTTokenNeededFilter.class);
        register(Authentication.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                //bindAsContract(SimpleKeyGenerator.class);
                bind(SimpleKeyGenerator.class).to(KeyGenerator.class);
            }
        });
    }
}