package io.spring.initializr.generator.project.module;

import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * default project module dependency
 * Date : 2019-02-14
 * Author : pengkai.fu
 */
public class DefaultModuleTopology implements ModuleTopology {


    private final Module root;

    private final Map<String, Module> moduleMap = new HashMap<>();


    public DefaultModuleTopology() {
        root = new Module("root");
        moduleDependency();
    }

    private void moduleDependency() {
        Module common = newModule("common");
        Module rpc = newModule("rpc");
        Module service = newModule("service");
        Module dao = newModule("dao");
        Module web = newModule("web", "war");
        Module api = newModule("api", "war");
        Module mq = newModule("mq");
        Module pojo = newModule("pojo");

        dao.addChildModule(common);
        dao.addChildModule(pojo);

        service.addChildModule(dao);
        service.addChildModule(rpc);
        service.addChildModule(mq);

        web.addChildModule(service);
        api.addChildModule(service);

        root.addChildModule(web);
        root.addChildModule(api);

        Module sdk = newModule("sdk");

        root.addReferModule(sdk);
    }


    private Module newModule(String name, String... packaging) {
        Module module = new Module(name);
        module.setPackaging(null == packaging || packaging.length == 0 ? null : packaging[0]);
        moduleMap.put(name, module);
        return module;
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


    @Override
    public Module getRoot() {
        return root;
    }


    @Override
    public Module getModule(String name) {
        return moduleMap.get(name);
    }
}
