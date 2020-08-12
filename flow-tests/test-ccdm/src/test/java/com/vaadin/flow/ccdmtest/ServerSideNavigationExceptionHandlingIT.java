/*
 * Copyright 2000-2020 Vaadin Ltd.
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
 *
 */
package com.vaadin.flow.ccdmtest;

import java.util.List;

import com.vaadin.flow.testutil.ChromeBrowserTest;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ServerSideNavigationExceptionHandlingIT extends ChromeBrowserTest {

    private void openTestUrl(String url) {
        getDriver().get(getRootURL() + "/foo" + url);
        waitForDevServer();
    }
    
    @Test
    public void should_showErrorView_when_targetViewThrowsException() {
        openVaadinRouter("/");

        findAnchor("view-with-server-view-button").click();

        // Navigate to a server-side view that throws exception.
        findElement(By.id("serverViewThrowsExcpetionButton")).click();
        
        assertView("errorView", "Tried to navigate to a view without being authenticated", "view-throws-exception");
    }

    protected final void openVaadinRouter(String url) {
        openTestUrl(url);

        waitForElementPresent(By.id("loadVaadinRouter"));
        findElement(By.id("loadVaadinRouter")).click();
        waitForElementPresent(By.id("outlet"));
    }

    protected final WebElement findAnchor(String href) {
        final List<WebElement> anchors = findElements(By.tagName("a"));
        for (WebElement element : anchors) {
            if (element.getAttribute("href").endsWith(href)) {
                return element;
            }
        }

        return null;
    }

    protected final void assertView(String viewId, String assertViewText, String assertViewRoute) {
        waitForElementPresent(By.id(viewId));
        final WebElement serverViewDiv = findElement(By.id(viewId));

        Assert.assertEquals(assertViewText, serverViewDiv.getText());
        assertCurrentRoute(assertViewRoute);
    }

    protected final void assertCurrentRoute(String route) {
        final String currentUrl = getDriver().getCurrentUrl();
        Assert.assertTrue(String.format("Expecting route '%s', but url is '%s'",
                route, currentUrl), currentUrl.endsWith(route));
    }


}