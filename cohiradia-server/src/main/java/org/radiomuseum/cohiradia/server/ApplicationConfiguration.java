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
    @ConfigProperty(name = "rmorg.meta.base")
    File basePathMetadata;

    @Inject
    @ConfigProperty(name = "rmorg.descriptor.base")
    File basePathDescriptor;

    @Inject
    @ConfigProperty(name = "rmorg.storage.base", defaultValue = "./cohistore")
    File basePathStorage;


}
