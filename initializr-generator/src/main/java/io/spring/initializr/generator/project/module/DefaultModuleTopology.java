package io.spring.initializr.generator.project.module;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * default project module dependency
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class DefaultModuleTopology implements ModuleTopology {


    private Module root;


    public DefaultModuleTopology() {
        root = new Module("root");
        moduleDependency();
    }

    private void moduleDependency() {
        Module common = new Module("common");
        Module rpc = new Module("rpc");
        Module service = new Module("service");
        Module dao = new Module("dao");
        Module web = new Module("web");
        Module api = new Module("api");
        Module mq = new Module("mq");

        dao.addChildModule(common);

        service.addChildModule(dao);
        service.addChildModule(rpc);
        service.addChildModule(mq);

        web.addChildModule(service);
        api.addChildModule(service);

        root.addChildModule(web);
        root.addChildModule(api);

        Module sdk = new Module("sdk");

        root.addReferModule(sdk);
    }


    @Override
    public List<String> getAllModuleNames() {
        Objects.requireNonNull(root, "root module doesn't exist");
        List<String> moduleNames = new ArrayList<>();

        appendChildNames(moduleNames);
        appendReferNames(moduleNames);

        return moduleNames;
    }

    private void appendRootName(List<String> moduleNames) {
        moduleNames.add(this.root.getName());
    }

    private void appendChildNames(List<String> moduleNames) {
        Module root = this.root;

        List<Module> childModules = root.getChildModulesRecursive();

        if (CollectionUtils.isEmpty(childModules)) {
            return;
        }

        moduleNames.addAll(childModules.stream().map(Module::getName)
                .collect(Collectors.toList()));
    }

    private void appendReferNames(List<String> moduleNames) {
        List<String> referNames = root.getReferModules().stream()
                .map(Module::getName)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(referNames)) {
            return;
        }

        moduleNames.addAll(referNames);
    }


    public Module getRoot() {
        return root;
    }


}
