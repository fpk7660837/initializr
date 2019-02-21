package io.spring.initializr.generator.project.module;

import io.spring.initializr.generator.project.ResolvedProjectDescription;
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


    private static ResolvedProjectDescription projectDescription;


    public DefaultModuleTopology(ResolvedProjectDescription projectDescription) {
        DefaultModuleTopology.projectDescription = projectDescription;
        root = new Module("root", "root");
        moduleDependency();
    }

    private void moduleDependency() {
        Module common = newModule(Modules.COMMON);
        Module rpc = newModule(Modules.RPC);
        Module service = newModule(Modules.SERVICE);
        Module dao = newModule(Modules.DAO);
        Module web = newModule(Modules.WEB, Modules.WEB.getPackaging());
        Module api = newModule(Modules.API, Modules.API.getPackaging());
        Module mq = newModule(Modules.MQ);
        Module pojo = newModule(Modules.POJO);

        dao.addChildModule(common);
        dao.addChildModule(pojo);

        service.addChildModule(dao);
        service.addChildModule(rpc);
        service.addChildModule(mq);

        web.addChildModule(service);
        api.addChildModule(service);

        root.addChildModule(web);
        root.addChildModule(api);

        Module sdk = newModule(Modules.SDK);

        root.addReferModule(sdk);
    }


    private Module newModule(Modules modules, String... packaging) {
        String name = modules.getName();
        Module module = new Module(name, modules.getSuffix());
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


        private String suffix;

        private String packaging;

        Modules(String suffix, String packaging) {
            this.suffix = suffix;
            this.packaging = packaging;
        }


        public String getPackaging() {
            return packaging;
        }

        public String getName() {
            return projectDescription.getArtifactId() + "-" + suffix;
        }

        public String getSuffix() {
            return suffix;
        }
    }


}
