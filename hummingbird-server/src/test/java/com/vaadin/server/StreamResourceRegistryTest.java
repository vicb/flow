/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class StreamResourceRegistryTest {

    private VaadinServlet servlet = new VaadinServlet();
    private VaadinServletService service;
    private VaadinSession session;

    @Before
    public void setUp() throws ServletException {
        service = servlet.getService();
        session = new VaadinSession(service) {

            @Override
            public boolean hasLock() {
                return true;
            }
        };
    }

    @Test
    public void registerResource_registrationResultCanBeFound() {
        StreamResourceRegistry registry = session.getResourceRegistry();

        StreamResource resource = new StreamResource("name",
                () -> makeEmptyStream());
        StreamResourceRegistration registration = registry
                .registerResource(resource);
        Assert.assertNotNull(registration);

        URI uri = registration.getResourceUri();

        Optional<StreamResource> stored = registry.getResource(uri);
        Assert.assertSame(
                "Unexpected stored resource is returned for registered URI",
                resource, stored.get());

        Assert.assertSame(
                "Unexpected resource is returned by the registration instance",
                resource, registration.getResource().get());
    }

    @Test
    public void unregisterResource_resourceIsRemoved() {
        StreamResourceRegistry registry = session.getResourceRegistry();

        StreamResource resource = new StreamResource("name",
                () -> makeEmptyStream());
        StreamResourceRegistration registration = registry
                .registerResource(resource);
        Assert.assertNotNull(registration);

        URI uri = registration.getResourceUri();

        registration.unregister();

        Optional<StreamResource> stored = registry.getResource(uri);
        Assert.assertFalse(
                "Unexpected stored resource is found after unregister()",
                stored.isPresent());
        Assert.assertFalse(
                "Unexpected resource is returned by the registration instance",
                registration.getResource().isPresent());
    }

    @Test
    public void registerTwoResourcesWithSameName_resourcesHasDifferentURI() {
        StreamResourceRegistry registry = session.getResourceRegistry();

        StreamResource resource1 = new StreamResource("name",
                () -> makeEmptyStream());
        StreamResourceRegistration registration1 = registry
                .registerResource(resource1);

        StreamResource resource2 = new StreamResource("name",
                () -> makeEmptyStream());
        StreamResourceRegistration registration2 = registry
                .registerResource(resource2);

        Assert.assertNotEquals(
                "Two different resource are registered to the same URI",
                registration1.getResourceUri(), registration2.getResourceUri());

        registration1.unregister();

        Assert.assertTrue(
                "Second resource is not found after first resource has been unregistered",
                registry.getResource(registration2.getResourceUri())
                        .isPresent());
    }

    @Test
    public void getResourceUrlIsEncoded() throws UnsupportedEncodingException {
        StreamResourceRegistry registry = session.getResourceRegistry();

        StreamResource resource = new StreamResource("a?b=c d&e",
                () -> makeEmptyStream());
        StreamResourceRegistration registration = registry
                .registerResource(resource);

        URI url = registration.getResourceUri();
        String suffix = URLEncoder.encode(resource.getFileName(),
                StandardCharsets.UTF_8.name());
        Assert.assertTrue("Resource url is not encoded",
                url.toString().endsWith(suffix));
    }

    private InputStream makeEmptyStream() {
        return new ByteArrayInputStream(new byte[0]);
    }
}
