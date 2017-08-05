/*
 * Copyright 2017-present Open Networking Laboratory
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
package org.unl.cse.netgroup.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.onosproject.rest.AbstractWebResource;
import org.unl.cse.netgroup.GetOvsDeviceInfo;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;

/**
 * Sample web resource.
 */
@Path("")
public class QossibleWebResource extends AbstractWebResource {

    private static GetOvsDeviceInfo getOvsDeviceInfo;
    private final ObjectNode root = mapper().createObjectNode();

    /**
     * Get hello world greeting.
     *
     * @return 200 OK
     */
    @GET
    @Path("info")
    public Response getGreeting() {
        ObjectNode node = mapper().createObjectNode().put("Info", "QoSsible App");
        return ok(node).build();
    }

    @POST
    @Path("device-id")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getOpenVswitchDeviceInfo(InputStream stream) {
        getOvsDeviceInfo = JsonToDeviceInfo(stream);

        String msg = "SUCCESS";
        return Response.ok(root).build();
    }

    private GetOvsDeviceInfo JsonToDeviceInfo(InputStream stream) {
        JsonNode node;

        try {
            node = mapper().readTree(stream);
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse the QoSsible device-id POST");
        }

        String ovsDeviceId = node.path("device-id").asText();

        if (ovsDeviceId != null) {
            return new GetOvsDeviceInfo(ovsDeviceId);
        } else {
            throw new IllegalArgumentException("Arguments cannot be null.");
        }
    }

}
