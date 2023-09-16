package org.radiomuseum.cohiradia.server.api;

import lombok.SneakyThrows;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.radiomuseum.cohiradia.server.ApplicationConfiguration;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class GitRepository {

    @Inject
    ApplicationConfiguration config;

    @SneakyThrows
    public boolean pull() {
        try (var git = Git.open(config.basePathMetadata().getParentFile())) {
            var credentialsProvider = new UsernamePasswordCredentialsProvider(config.gitUserName(), config.gitPassword());
            var result = git.pull().setCredentialsProvider(credentialsProvider).call();
            return result.isSuccessful();
        }
    }
}
