package thoughtworks.com;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import thoughtworks.com.domain.Product;
import thoughtworks.com.exception.ProductNotFound;
import thoughtworks.com.repository.ProductRepository;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductResourceTest extends JerseyTest {

    @Mock
    ProductRepository mockProductRepository;
    @Override
    protected Application configure() {
        ResourceConfig resourceConfig = new ResourceConfig();
        resourceConfig.packages("thoughtworks.com");
        resourceConfig.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(mockProductRepository).to(ProductRepository.class);
            }
        });
        return resourceConfig;
    }

    @Test
    public void should_return_200_when_get() {
        when(mockProductRepository.getProductById(eq(1))).thenReturn(new Product("name", "description"));
        Response response = target("/products/1").request().accept(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus(), is(200));

        Map product = response.readEntity(Map.class);

        assertThat(product.get("name"), is("name"));
        assertThat(product.get("description"), is("description"));
        assertThat(product.get("uri").toString(), endsWith("/products/1"));
    }

    @Test
    public void should_return_404_when_not_found_product() {
        when(mockProductRepository.getProductById(eq(999))).thenThrow(ProductNotFound.class);
        Response response = target("/products/999").request().get();
        assertThat(response.getStatus(), is(404));
    }
}
