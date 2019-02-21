/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.spring.initializr.generator.language.java;

import java.util.ArrayList;
import java.util.List;

/**
 * An invocation of a method.
 *
 * @author Andy Wilkinson
 */
public class JavaMethodInvocation extends JavaExpression {

    private final String target;

    private final String name;

    private final List<String> arguments = new ArrayList<>();

    private final String returnType;

    private final String returnName;

    public JavaMethodInvocation(String target, String name, String returnType,
                                String returnName) {
        this.target = target;
        this.name = name;
        this.returnType = returnType;
        this.returnName = returnName;
    }

    public JavaMethodInvocation(String target, String name) {
        this(target, name, null, null);
    }

    public JavaMethodInvocation argument(String argument) {
        this.arguments.add(argument);
        return this;
    }


    public String getTarget() {
        return this.target;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getArguments() {
        return this.arguments;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getReturnName() {
        return returnName;
    }
}
