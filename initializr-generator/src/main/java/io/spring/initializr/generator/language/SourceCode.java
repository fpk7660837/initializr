/*
 * Copyright 2012-2018 the original author or authors.
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

package io.spring.initializr.generator.language;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Representation of application source code.
 *
 * @param <T> types
 * @param <C> compilation units
 * @author Andy Wilkinson
 */
public abstract class SourceCode<T extends TypeDeclaration, C extends CompilationUnit<T>> {

    private List<C> compilationUnits = new ArrayList<>();


    private final BiFunction<String, String, C> compilationUnitFactory;

    protected SourceCode(BiFunction<String, String, C> compilationUnitFactory) {
        this.compilationUnitFactory = compilationUnitFactory;
    }


    public C createCompilationUnit(String packageName, String name) {
        return createCompilationUnit(packageName, name, null);
    }


    public C createCompilationUnit(String packageName, String name, String module) {
        C compilationUnit = this.compilationUnitFactory.apply(packageName, name);
        compilationUnit.setModule(module);
        this.compilationUnits.add(compilationUnit);
        return compilationUnit;
    }


    /**
     * Returns an unmodifiable view of the {@link CompilationUnit compilation units}.
     *
     * @return the compilation units
     */
    public List<C> getCompilationUnits() {
        return Collections.unmodifiableList(this.compilationUnits);
    }

    public void setCompilationUnits(List<C> compilationUnits) {
        this.compilationUnits = compilationUnits;
    }

    public List<C> getModuleCompilationUnits(String module) {
        return compilationUnits.stream()
                .filter(c -> module.equals(c.getModule()))
                .collect(Collectors.toList());
    }


}
