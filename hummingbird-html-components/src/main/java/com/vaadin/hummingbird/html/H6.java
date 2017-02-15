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
package com.vaadin.hummingbird.html;

import com.vaadin.annotations.Tag;
import com.vaadin.hummingbird.html.event.ClickNotifier;
import com.vaadin.ui.Component;

/**
 * Component representing a <code>&lt;h6&gt;</code> element.
 *
 * @author Vaadin Ltd
 */
@Tag(Tag.H6)
public class H6 extends HtmlContainer implements ClickNotifier {

    /**
     * Creates a new empty heading.
     */
    public H6() {
        super();
    }

    /**
     * Creates a new heading with the given child components.
     *
     * @param components
     *            the child components
     */
    public H6(Component... components) {
        super(components);
    }

    /**
     * Creates a new heading with the given text.
     *
     * @param text
     *            the text
     */
    public H6(String text) {
        super();
        setText(text);
    }
}
