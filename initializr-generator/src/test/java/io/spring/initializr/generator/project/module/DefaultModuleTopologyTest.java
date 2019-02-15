package io.spring.initializr.generator.project.module;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
class DefaultModuleTopologyTest {

    @Test
    void getAllModuleNames() {
        DefaultModuleTopology defaultModuleTopology = new DefaultModuleTopology(null);

        List<String> modules = new ArrayList<>();
        modules.add("web");
        modules.add("api");
        modules.add("rpc");
        modules.add("dao");
        modules.add("service");
        modules.add("common");
        modules.add("sdk");
        modules.add("mq");

        Assertions.assertThat(defaultModuleTopology.getAllModuleNames())
                .containsAll(modules);
    }
}