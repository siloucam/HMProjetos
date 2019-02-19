package com.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.repository.UserRepository.USERS_BY_LOGIN_CACHE, jcacheConfiguration);
            cm.createCache(com.repository.UserRepository.USERS_BY_EMAIL_CACHE, jcacheConfiguration);
            cm.createCache(com.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.domain.DescricaoServico.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.DescricaoSituacao.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.TipoSituacao.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.Codigos.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.ExtendUser.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.ExtendUser.class.getName() + ".situacaos", jcacheConfiguration);
            cm.createCache(com.domain.Cliente.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.Cliente.class.getName() + ".telefones", jcacheConfiguration);
            cm.createCache(com.domain.Cliente.class.getName() + ".servicos", jcacheConfiguration);
            cm.createCache(com.domain.Telefone.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.Servico.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.Servico.class.getName() + ".situacaos", jcacheConfiguration);
            cm.createCache(com.domain.Servico.class.getName() + ".transacaos", jcacheConfiguration);
            cm.createCache(com.domain.Transacao.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.Situacao.class.getName(), jcacheConfiguration);
            cm.createCache(com.domain.MeuServico.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
