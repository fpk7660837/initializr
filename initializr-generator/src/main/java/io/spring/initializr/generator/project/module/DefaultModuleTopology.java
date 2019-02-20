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
        Module common = newModule(Modules.COMMON.getName());
        Module rpc = newModule(Modules.RPC.getName());
        Module service = newModule(Modules.SERVICE.getName());
        Module dao = newModule(Modules.DAO.getName());
        Module web = newModule(Modules.WEB.getName(), Modules.WEB.getPackaging());
        Module api = newModule(Modules.API.getName(), Modules.API.getPackaging());
        Module mq = newModule(Modules.MQ.getName());
        Module pojo = newModule(Modules.POJO.getName());

        dao.addChildModule(common);
        dao.addChildModule(pojo);

        service.addChildModule(dao);
        service.addChildModule(rpc);
        service.addChildModule(mq);

        web.addChildModule(service);
        api.addChildModule(service);

        root.addChildModule(web);
        root.addChildModule(api);

        Module sdk = newModule(Modules.SDK.getName());

        root.addReferModule(sdk);
    }


    private Module newModule(String name, String... packaging) {
        Module module = new Module(name);
        module.setPackaging(null == packaging || packaging.length == 0 ? null : packaging[0]);
        moduleMap.put(name, module);
        return module;
    }


    @Override
    public List<String> getAllChildModuleNames() {
        Objects.requireNonNull(root, "root module doesn't exist");
        List<String> moduleNames = new ArrayList<>();

        appendChildNames(moduleNames);


        return moduleNames;
    }

    @Override
    public List<String> getAllReferModuleNames() {
        Objects.requireNonNull(root, "root module doesn't exist");
        List<String> moduleNames = new ArrayList<>();
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


    public enum Modules {
        WEB("web", "war"),
        API("api", "war"),
        SERVICE("service", null),
        DAO("dao", null),
        COMMON("common", null),
        POJO("pojo", null),
        RPC("rpc", null),
        MQ("mq", null),
        SDK("sdk", null);


        private String name;

        private String packaging;

        Modules(String name, String packaging) {
            this.name = name;
            this.packaging = packaging;
        }


        public String getName() {
            return name;
        }

        public String getPackaging() {
            return packaging;
        }
    }


}
