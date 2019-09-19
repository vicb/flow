/*
 * Copyright 2000-2018 Vaadin Ltd.
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
package com.vaadin.generator.metadata;

/**
 * Enum with all the supported types that are read from the webcomponent and
 * translated to Java types.
 *
 * @see ComponentMetadata
 * @since 1.0
 */
public enum ComponentBasicType implements ComponentType {

    BOOLEAN, DATE, NUMBER, STRING, ARRAY, OBJECT, FUNCTION, UNDEFINED;

}
