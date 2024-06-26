package org.radiomuseum.cohiradia.server.api;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.radiomuseum.cohiradia.server.ApplicationConfiguration;

@Dependent
public class GitRepository {

    @Inject
    ApplicationConfiguration config;

    @SneakyThrows
    public boolean pull() {
        try (var git = Git.open(config.basePathMetadata().getParentFile())) {
            //var credentialsProvider = new UsernamePasswordCredentialsProvider(config.gitUserName(), config.gitPassword());
            //var result = git.pull().setCredentialsProvider(credentialsProvider).call();
            var result = git.pull().setCredentialsProvider(null).call();
            return result.isSuccessful();
        }
    }
}
