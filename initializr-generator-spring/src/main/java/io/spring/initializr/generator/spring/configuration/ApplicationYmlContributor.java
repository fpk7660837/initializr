package io.spring.initializr.generator.spring.configuration;

import io.spring.initializr.generator.project.contributor.MultipleResourcesProjectContributor;

import java.util.function.Predicate;

/**
 * Date : 2019-02-15
 * Author : pengkai.fu
 */
public class ApplicationYmlContributor extends MultipleResourcesProjectContributor {


    public ApplicationYmlContributor(String rootResource) {
        super(rootResource);
    }

    public ApplicationYmlContributor(String rootResource, Predicate<String> executable) {
        super(rootResource, executable);
    }
}
