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

package io.spring.initializr.generator.spring.code;

import io.spring.initializr.generator.language.CompilationUnit;
import io.spring.initializr.generator.language.SourceCode;
import io.spring.initializr.generator.language.SourceCodeWriter;
import io.spring.initializr.generator.language.TypeDeclaration;
import io.spring.initializr.generator.project.ResolvedProjectDescription;
import io.spring.initializr.generator.project.contributor.ProjectContributor;
import io.spring.initializr.generator.project.module.DefaultModuleTopology;
import io.spring.initializr.generator.spring.util.LambdaSafe;
import org.springframework.beans.factory.ObjectProvider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * {@link ProjectContributor} for the application's main source code.
 *
 * @param <T> language-specific type declaration
 * @param <C> language-specific compilation unit
 * @param <S> language-specific source code
 * @author Andy Wilkinson
 * @author Stephane Nicoll
 */
public class MainSourceCodeProjectContributor<T extends TypeDeclaration, C extends CompilationUnit<T>, S extends SourceCode<T, C>>
        implements ProjectContributor {

    private final ResolvedProjectDescription projectDescription;

    private final Supplier<S> sourceFactory;

    private final SourceCodeWriter<S> sourceWriter;

    private final ObjectProvider<MainApplicationTypeCustomizer<? extends TypeDeclaration>> mainTypeCustomizers;

    private final ObjectProvider<MainCompilationUnitCustomizer<?, ?>> mainCompilationUnitCustomizers;

    private final ObjectProvider<MainSourceCodeCustomizer<?, ?, ?>> mainSourceCodeCustomizers;

    public MainSourceCodeProjectContributor(ResolvedProjectDescription projectDescription,
                                            Supplier<S> sourceFactory, SourceCodeWriter<S> sourceWriter,
                                            ObjectProvider<MainApplicationTypeCustomizer<?>> mainTypeCustomizers,
                                            ObjectProvider<MainCompilationUnitCustomizer<?, ?>> mainCompilationUnitCustomizers,
                                            ObjectProvider<MainSourceCodeCustomizer<?, ?, ?>> mainSourceCodeCustomizers) {
        this.projectDescription = projectDescription;
        this.sourceFactory = sourceFactory;
        this.sourceWriter = sourceWriter;
        this.mainTypeCustomizers = mainTypeCustomizers;
        this.mainCompilationUnitCustomizers = mainCompilationUnitCustomizers;
        this.mainSourceCodeCustomizers = mainSourceCodeCustomizers;
    }

    @Override
    public void contribute(Path projectRoot) throws IOException {
        S sourceCode = buildSourceCode();
        for (DefaultModuleTopology.Modules module : DefaultModuleTopology.Modules.values()) {
            String suffix = module.getSuffix();
            String moduleName = projectDescription.getModuleName(suffix);
            List<C> codeModuleCompilationUnits = sourceCode.getModuleCompilationUnits(moduleName);
            S s = buildEmptySourceCode();
            s.setCompilationUnits(codeModuleCompilationUnits);
            writeCode(projectRoot, s, moduleName);
        }
    }

    private S buildSourceCode() {
        S sourceCode = buildEmptySourceCode();
        buildMainApplicationSourceCode(sourceCode, projectDescription.getModuleName(DefaultModuleTopology.Modules.WEB
                .getSuffix()));
        buildMainApplicationSourceCode(sourceCode, projectDescription.getModuleName(DefaultModuleTopology.Modules.API
                .getSuffix()));
        buildMainSourceCode(sourceCode);
        return sourceCode;
    }

    private S buildEmptySourceCode() {
        return this.sourceFactory.get();
    }


    private void buildMainSourceCode(S sourceCode) {
        customizeMainSourceCode(sourceCode);
    }


    private void buildMainApplicationSourceCode(S sourceCode, String module) {
        String applicationName = this.projectDescription.getApplicationName();
        C compilationUnit = sourceCode.createCompilationUnit(
                this.projectDescription.getPackageName(), applicationName, module);
        T mainApplicationType = compilationUnit.createTypeDeclaration(applicationName);
        customizeMainApplicationType(mainApplicationType);
        customizeMainCompilationUnit(compilationUnit);
    }

    private void writeCode(Path projectRoot, S sourceCode, String module) throws IOException {
        this.sourceWriter
                .writeTo(this.projectDescription.getBuildSystem().getDirectory(
                        projectRoot, this.projectDescription.getLanguage(), module),
                        sourceCode);
    }

    @SuppressWarnings("unchecked")
    private void customizeMainApplicationType(T mainApplicationType) {
        List<MainApplicationTypeCustomizer<?>> customizers = this.mainTypeCustomizers
                .orderedStream().collect(Collectors.toList());
        LambdaSafe
                .callbacks(MainApplicationTypeCustomizer.class, customizers,
                        mainApplicationType)
                .invoke((customizer) -> customizer.customize(mainApplicationType));
    }

    @SuppressWarnings("unchecked")
    private void customizeMainCompilationUnit(C compilationUnit) {
        List<MainCompilationUnitCustomizer<?, ?>> customizers = this.mainCompilationUnitCustomizers
                .orderedStream().collect(Collectors.toList());
        LambdaSafe
                .callbacks(MainCompilationUnitCustomizer.class, customizers,
                        compilationUnit)
                .invoke((customizer) -> customizer.customize(compilationUnit));
    }

    @SuppressWarnings("unchecked")
    private void customizeMainSourceCode(S sourceCode) {
        List<MainSourceCodeCustomizer<?, ?, ?>> customizers = this.mainSourceCodeCustomizers
                .orderedStream().collect(Collectors.toList());
        LambdaSafe.callbacks(MainSourceCodeCustomizer.class, customizers, sourceCode)
                .invoke((customizer) -> customizer.customize(sourceCode));
    }

}
