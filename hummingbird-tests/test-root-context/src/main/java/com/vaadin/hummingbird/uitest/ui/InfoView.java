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
package com.vaadin.hummingbird.uitest.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.hummingbird.html.Button;
import com.vaadin.hummingbird.html.Div;
import com.vaadin.hummingbird.html.Hr;
import com.vaadin.hummingbird.router.View;
import com.vaadin.server.DeploymentConfiguration;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.AttachEvent;
import com.vaadin.ui.Html;
import com.vaadin.ui.UI;

public class InfoView extends Div implements View {

    public InfoView() {
        setClassName("infoContainer");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if (attachEvent.isInitialAttach()) {
            update(attachEvent.getUI());
        }
    }

    private void update(UI ui) {
        VaadinSession session = ui.getSession();
        WebBrowser webBrowser = session.getBrowser();
        DeploymentConfiguration deploymentConfiguration = session
                .getConfiguration();
        List<String> device = new ArrayList<>();
        List<String> os = new ArrayList<>();
        List<String> browser = new ArrayList<>();

        removeAll();
        add(new Button("Refresh", e -> {
            update(ui);
        }));

        header("Browser");
        info("Address", webBrowser.getAddress());

        add(device, "Android", webBrowser.isAndroid());
        add(device, "iOS", webBrowser.isIOS());
        add(device, "iPad", webBrowser.isIPad());
        add(device, "iPhone", webBrowser.isIPhone());
        add(device, "Windows Phone", webBrowser.isWindowsPhone());

        info("Device", device.stream().collect(Collectors.joining(", ")));

        add(os, "Linux", webBrowser.isLinux());
        add(os, "Mac", webBrowser.isMacOSX());
        add(os, "Windows", webBrowser.isWindows());

        info("Os", os.stream().collect(Collectors.joining(", ")));

        add(browser, "Touch device", webBrowser.isTouchDevice());
        add(browser, "Chrome", webBrowser.isChrome());
        add(browser, "Edge", webBrowser.isEdge());
        add(browser, "Firefox", webBrowser.isFirefox());
        add(browser, "IE", webBrowser.isIE());
        add(browser, "PhantomJS", webBrowser.isPhantomJS());
        add(browser, "Safari", webBrowser.isSafari());

        info("Browser", browser.stream().collect(Collectors.joining(", ")));

        if (webBrowser.isTooOldToFunctionProperly()) {
            header("Browser is too old to function properly");
        }
        info("User-agent", webBrowser.getBrowserApplication());
        info("Browser major", webBrowser.getBrowserMajorVersion());
        info("Browser minor", webBrowser.getBrowserMinorVersion());
        info("Screen height", webBrowser.getScreenHeight());
        info("Screen width", webBrowser.getScreenWidth());
        info("Locale", webBrowser.getLocale());

        info("Secure connection (https)", webBrowser.isSecureConnection());

        separator();

        header("Push configuration");
        info("Push mode", ui.getPushConfiguration().getPushMode());
        info("Push transport", ui.getPushConfiguration().getTransport());

        separator();

        header("Deployment configuration");
        info("Heartbeat interval",
                deploymentConfiguration.getHeartbeatInterval());
        info("Router configurator class",
                deploymentConfiguration.getRouterConfiguratorClassName());
        info("UI class", deploymentConfiguration.getUIClassName());
        info("Close idle sessions",
                deploymentConfiguration.isCloseIdleSessions());
        info("Send URLs as parameters",
                deploymentConfiguration.isSendUrlsAsParameters());
        info("Sync id enabled", deploymentConfiguration.isSyncIdCheckEnabled());
        info("XSRF protection enabled",
                deploymentConfiguration.isXsrfProtectionEnabled());
        info("Production mode", deploymentConfiguration.isProductionMode());

    }

    private void add(List<String> collection, String value, boolean add) {
        if (add) {
            collection.add(value);
        }
    }

    private void separator() {
        add(new Hr());
    }

    private void header(String header) {
        new Html("<div><b>" + header + "</b></div>");
    }

    private void info(String header, Object value) {
        add(new Html("<div>" + header + ": " + value + "</div>"));
    }
}
