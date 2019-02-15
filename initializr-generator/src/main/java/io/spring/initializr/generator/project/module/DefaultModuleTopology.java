package io.spring.initializr.generator.project.module;

import io.spring.initializr.generator.buildsystem.maven.MavenBuild;
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


    public DefaultModuleTopology(MavenBuild build) {
        root = new Module("root", null, build);

        Module common = new Module("common", null, new MavenBuild());
        Module rpc = new Module("rpc", null, new MavenBuild());
        Module service = new Module("service", null, new MavenBuild());
        Module dao = new Module("dao", null, new MavenBuild());
        Module web = new Module("web", "war", new MavenBuild());
        Module api = new Module("api", "war", new MavenBuild());
        Module mq = new Module("mq", null, new MavenBuild());

        dao.addChildModule(common);

        service.addChildModule(dao);
        service.addChildModule(rpc);
        service.addChildModule(mq);

        web.addChildModule(service);
        api.addChildModule(service);

        root.addChildModule(web);
        root.addChildModule(api);

        Module sdk = new Module("sdk", null, new MavenBuild());

        root.addReferModule(sdk);

        addMavenBuild();
    }

    private void addMavenBuild() {
        List<Module> childModule = root.getChildModulesRecursive();

        for (Module module : childModule) {
            MavenBuild mavenBuild = addChildBuild(module);
            mavenBuild.parent(root.getMavenBuild().getGroup(), root.getMavenBuild().getArtifact(),
                    root.getMavenBuild().getVersion());
        }

        List<Module> referModules = root.getReferModules();

        for (Module referModule : referModules) {
            addChildBuild(referModule);
        }
    }

    private MavenBuild addChildBuild(Module module) {
        String name = module.getName();
        MavenBuild mavenBuild = module.getMavenBuild();
        String packaging = module.getPackaging();

        mavenBuild.setArtifact(name);
        mavenBuild.setPackaging(packaging);
        mavenBuild.setVersion(null);
        return mavenBuild;
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
