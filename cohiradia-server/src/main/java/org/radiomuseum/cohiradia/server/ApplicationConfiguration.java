package org.radiomuseum.cohiradia.server;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.File;

@Getter
@Accessors(fluent = true)
@Dependent
public class ApplicationConfiguration {

    @Inject
    @ConfigProperty(name = "rmorg.meta.base", defaultValue = "./git/cohiradia-metadata/yaml")
    File basePathMetadata;

    @Inject
    @ConfigProperty(name = "rmorg.descriptor.base", defaultValue = "./git/cohiradia-metadata/yaml")
    File basePathDescriptor;

    @Inject
    @ConfigProperty(name = "rmorg.storage.base", defaultValue = "./cohistore")
    File basePathStorage;


}
