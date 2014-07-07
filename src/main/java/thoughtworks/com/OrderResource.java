package thoughtworks.com;

import thoughtworks.com.domain.Order;
import thoughtworks.com.domain.User;
import thoughtworks.com.repository.UserRepository;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Map;

public class OrderResource {
    private User user;
    private final UserRepository userRepository;

    public OrderResource(User user, UserRepository userRepository) {
        this.user = user;
        this.userRepository = userRepository;
    }

    @GET
    @Path("{orderId}")
    public String getOrder(@PathParam("orderId") int orderId) {
        Order order = userRepository.getUserOrderById(orderId);
        return "test";
    }

    @POST
    public Response createOrder(@Context UriInfo uriInfo, Map order) {
        String address = order.get("address").toString();
        int orderId = userRepository.createOrderForUser(user, new Order(address));
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(orderId)).build()).build();
    }
}
