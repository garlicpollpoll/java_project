package com.study_site.java_project.web.repository;

import com.study_site.java_project.web.entity.PersistentLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
@RequiredArgsConstructor
public class JpaPersistentTokenRepository implements PersistentTokenRepository {

    private final PersistentLoginRepository repository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        repository.save(PersistentLogin.from(token));
    }

    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        repository.findBySeries(series)
                .ifPresent(persistentLogin -> {
                    persistentLogin.updateToken(tokenValue, lastUsed);
                    repository.save(persistentLogin);
                });
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        PersistentLogin findLogin = repository.findBySeries(seriesId).orElse(null);

        if (findLogin != null) {
            return repository.findBySeries(seriesId)
                    .map(persistentLogin ->
                        new PersistentRememberMeToken(
                                persistentLogin.getUsername(),
                                persistentLogin.getSeries(),
                                persistentLogin.getToken(),
                                persistentLogin.getLastUsed()
                        ))
                    .orElseThrow(IllegalArgumentException::new);
        }
        return null;
    }

    @Override
    public void removeUserTokens(String username) {
        repository.deleteAllInBatch(repository.findByUsername(username));
    }
}
