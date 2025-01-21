package io.potato.journalApp.cache;

import io.potato.journalApp.entity.ConfigJournalAppEntity;
import io.potato.journalApp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys {
        WEATHER_API
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String, String> appCache;

    @PostConstruct
    public void init() {
        appCache = new HashMap<>();
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        for (ConfigJournalAppEntity configJournalAppEntity : all) {
            appCache.put(configJournalAppEntity.getKey(), configJournalAppEntity.getValue());
        }
    }
}

/*
    ----------- @PostConstruct annotation -----------

    :: applies only to "methods".
    :: after creating a Bean of a class, it will be "invoked" IMMEDIATELY.
    :: immediately after crating the Bean of 'AppCache' class, "init()" method will be invoked.
*/
/*
    -------------------- APPLICATION CACHING --------------------

    :: A technique of 'loading' the "FREQUENTLY USED" & "FREQUENTLY CHANGING" data inside the Spring Boot Application
*/

