package io.spring.initializr.generator.project.module;

import java.util.List;

/**
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public interface ModuleTopology {


    List<String> getAllModuleNames();

    Module getRoot();

    Module getModule(String name);

}
