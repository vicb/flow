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

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.vaadin.hummingbird.testutil.PhantomJSTest;
import com.vaadin.testbench.By;

public class SynchronizedPropertyIT extends PhantomJSTest {

    @Test
    public void synchronizeOnChange() {
        open();
        WebElement syncOnChange = findElement(By.id("syncOnChange"));
        WebElement labelSyncOnChange = findElement(By.id("syncOnChangeLabel"));
        syncOnChange.sendKeys("123");
        blur();
        Assert.assertEquals("Server value: 123", labelSyncOnChange.getText());
        syncOnChange.sendKeys("456");
        blur();
        Assert.assertEquals("Server value: 123456",
                labelSyncOnChange.getText());
    }

    private void blur() {
        findElement(By.tagName("body")).click();
    }

    @Test
    public void synchronizeOnKeyUp() {
        open();
        WebElement syncOnKeyUp = findElement(By.id("syncOnKeyUp"));
        WebElement labelSyncOnKeyUp = findElement(By.id("syncOnKeyUpLabel"));
        syncOnKeyUp.sendKeys("123");
        Assert.assertEquals("Server value: 123", labelSyncOnKeyUp.getText());
        syncOnKeyUp.sendKeys("456");
        Assert.assertEquals("Server value: 123456", labelSyncOnKeyUp.getText());
    }

    @Test
    public void synchronizeInitialValueNotSentToServer() {
        open();
        WebElement syncOnChangeInitialValue = findElement(
                By.id("syncOnChangeInitialValue"));
        WebElement labelSyncOnChange = findElement(
                By.id("syncOnChangeInitialValueLabel"));

        // Property was set after label was created and sync set up
        // It is intentionally in the "wrong" state until there is a sync
        // message from the client
        Assert.assertEquals("Server value on create: null",
                labelSyncOnChange.getText());
        syncOnChangeInitialValue.sendKeys("123");
        blur();
        Assert.assertEquals("Server value in change listener: initial123",
                labelSyncOnChange.getText());

    }

    @Test
    public void synchronizeMultipleProperties() {
        open();
        WebElement multiSyncValue = findElement(By.id("multiSyncValue"));
        WebElement multiSyncValueLabel = findElement(
                By.id("multiSyncValueLabel"));
        WebElement multiSyncValueAsNumberLabel = findElement(
                By.id("multiSyncValueAsNumberLabel"));
        multiSyncValue.sendKeys("123");
        Assert.assertEquals("Server value: 123", multiSyncValueLabel.getText());
        Assert.assertEquals("", multiSyncValueAsNumberLabel.getText());
        blur();
        Assert.assertEquals("Server value: 123", multiSyncValueLabel.getText());
        Assert.assertEquals("Server valueAsNumber: 123",
                multiSyncValueAsNumberLabel.getText());

        multiSyncValue.sendKeys("456");
        Assert.assertEquals("Server value: 123456",
                multiSyncValueLabel.getText());
        Assert.assertEquals("Server valueAsNumber: 123",
                multiSyncValueAsNumberLabel.getText());
        blur();
        Assert.assertEquals("Server value: 123456",
                multiSyncValueLabel.getText());
        Assert.assertEquals("Server valueAsNumber: 123456",
                multiSyncValueAsNumberLabel.getText());
    }

}
