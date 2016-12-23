
package org.opendaylight.controller.temperature.northbound;

import java.util.LinkedList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.codehaus.enunciate.jaxrs.ResponseCode;
import org.codehaus.enunciate.jaxrs.StatusCodes;
import org.codehaus.enunciate.jaxrs.TypeHint;
import org.opendaylight.controller.northbound.commons.RestMessages;
import org.opendaylight.controller.northbound.commons.exception.InternalServerErrorException;
import org.opendaylight.controller.northbound.commons.exception.ResourceNotFoundException;
import org.opendaylight.controller.northbound.commons.exception.UnauthorizedException;
import org.opendaylight.controller.northbound.commons.utils.NorthboundUtils;
import org.opendaylight.controller.sal.authorization.Privilege;
import org.opendaylight.controller.sal.utils.ServiceHelper;
import org.opendaylight.controller.sal.utils.Status;
import org.opendaylight.controller.sal.utils.StatusCode;
import org.opendaylight.controller.topologymanager.ITopologyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Temperature Northbound REST API
 *
 * <br>
 * <br>
 * Authentication scheme : <b>HTTP Basic</b><br>
 * Authentication realm : <b>opendaylight</b><br>
 * Transport : <b>HTTP and HTTPS</b><br>
 * <br>
 * HTTPS Authentication is disabled by default.
 */

@Path("/")
public class TemperatureNorthboundJAXRS {
    private static final Logger logger = LoggerFactory.getLogger(TemperatureNorthboundJAXRS.class);
    private static LinkedList<TemperatureUserStringConfig> stringStorage = new LinkedList<TemperatureUserStringConfig>();
    private String username;

    @Context
    public void setSecurityContext(SecurityContext context) {
        if (context != null && context.getUserPrincipal() != null) {
            username = context.getUserPrincipal().getName();
        }
    }

    protected String getUserName() {
        return username;
    }

    /**
     * Retrieve the String 'Test'
     *
     * @param containerName
     *            The container for which we want to retrieve the String
     *            (Eg. 'default')
     *
     * @return A String
     *
     *         <pre>
     *
     * Example:
     *
     * Request URL:
     * http://localhost:8080/controller/nb/v2/temperature/default/testString
     *
     * Response:
     * Test
     *
     * </pre>
     */
    @Path("/{containerName}/testString")
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @TypeHint(String.class)
    @StatusCodes({ @ResponseCode(code = 404, condition = "The Container Name was not found") })
    public String getTestString(@PathParam("containerName") String containerName) {
        logger.info("getTestString");

        ITopologyManager topologyManager = (ITopologyManager) ServiceHelper.getInstance(ITopologyManager.class,
                containerName, this);

        if (topologyManager == null) {
            throw new ResourceNotFoundException(RestMessages.NOCONTAINER.toString());
        }

        int size = topologyManager.getEdges().size();
        return "Test" + size;
    }

    /**
     * Add a User String
     *
     * @param containerName
     *            Name of the Container (Eg. 'default')
     * @param name
     *            Name of the user string
     * @param TopologyUserLinkConfig
     *            in JSON or XML format
     * @return Response as dictated by the HTTP Response Status code
     *
     *         <pre>
     *
     * Example:
     *
     * Request URL:
     * http://localhost:8080/controller/nb/v2/temperature/default/AddUserString/string1
     *
     * Request body in XML:
     * &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
     * &lt;temperatureUserStringConfig&gt;
     * &lt;status&gt;Success&lt;/status&gt;
     * &lt;name&gt;string1&lt;/name&gt;
     * &lt;parameterOne&gt;OF|2@OF|00:00:00:00:00:00:00:02&lt;/parameterOne&gt;
     * &lt;parameterTwo&gt;OF|2@OF|00:00:00:00:00:00:00:51&lt;/parameterTwo&gt;
     * &lt;/temperatureUserStringConfig&gt;
     *
     * Request body in JSON:
     * {
     *    "status":"Success",
     *    "name":"string1",
     *    "parameterOne":"someThing",
     *    "parameterTwo":"More"
     * }
     *
     * </pre>
     */
    @Path("/{containerName}/AddUserString/{name}")
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @StatusCodes({
        @ResponseCode(code = 201, condition = "User String added successfully"),
        @ResponseCode(code = 404, condition = "The Container Name was not found"),
        @ResponseCode(code = 409, condition = "Failed to add User String due to Conflicting Name"),
        @ResponseCode(code = 500, condition = "Failed to add User String. Failure Reason included in HTTP Error response"),
        @ResponseCode(code = 503, condition = "One or more of Controller services are unavailable") })
    public Response addUserLink(@PathParam(value = "containerName") String containerName,
            @PathParam(value = "name") String name,
            @TypeHint(TemperatureUserStringConfig.class) TemperatureUserStringConfig userStringConfig) {
        logger.info("addUserLink: " + userStringConfig.getName() + " with parameter " +
                userStringConfig.getParameterOne() + " and " + userStringConfig.getparameterTwo());

        if (!NorthboundUtils.isAuthorized(getUserName(), containerName, Privilege.WRITE, this)) {
            throw new UnauthorizedException("User is not authorized to perform this operation on container "
                    + containerName);
        }

        boolean stat = false;
        if (!stringStorage.contains(userStringConfig)) {
            stat = stringStorage.add(userStringConfig);
        }

        logger.info("addUserLink: stat (" + stat + ")");

        if (stat) {
            NorthboundUtils.auditlog(
                    "User String", username, "added", userStringConfig.getName() + " with parameter " +
                            userStringConfig.getParameterOne() + " and " + userStringConfig.getparameterTwo(),
                            containerName);
            return Response.status(Response.Status.CREATED).build();
        }

        throw new InternalServerErrorException(StatusCode.CONFLICT.toString());
    }

    /**
     * Delete a String
     *
     * @param containerName
     *            Name of the Container (Eg. 'default')
     * @param name
     *            Name of the String (Eg. 'string1')
     * @return Response as dictated by the HTTP Response Status code
     *
     *         <pre>
     *
     * Example:
     *
     * Request URL:
     * http://localhost:8080/controller/nb/v2/temperature/default/DelUserString/string1
     *
     * </pre>
     */
    @Path("/{containerName}/DelUserString/{name}")
    @DELETE
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @StatusCodes({ @ResponseCode(code = 204, condition = "User string removed successfully"),
        @ResponseCode(code = 404, condition = "The Container Name or String Configuration Name was not found"),
        @ResponseCode(code = 503, condition = "One or more of Controller services are unavailable") })
    public Response deleteUserLink(@PathParam("containerName") String containerName, @PathParam("name") String name) {
        logger.info("deleteUserLink: " + name);

        boolean stat = false;
        for (TemperatureUserStringConfig conf : stringStorage) {
            if (conf.getName().equals(name)) {
                stat = stringStorage.remove(conf);
                break;
            }
        }

        logger.info("deleteUserLink: stat (" + stat + ")");

        if (stat) {
            NorthboundUtils.auditlog("User String", username, "removed", name, containerName);
            return Response.noContent().build();
        }

        return NorthboundUtils.getResponse(new Status(StatusCode.NOTFOUND));
    }
}
