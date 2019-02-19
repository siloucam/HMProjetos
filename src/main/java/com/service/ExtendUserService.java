package com.service;

import com.domain.ExtendUser;
import com.repository.ExtendUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing ExtendUser.
 */
@Service
@Transactional
public class ExtendUserService {

    private final Logger log = LoggerFactory.getLogger(ExtendUserService.class);

    private final ExtendUserRepository extendUserRepository;

    public ExtendUserService(ExtendUserRepository extendUserRepository) {
        this.extendUserRepository = extendUserRepository;
    }

    /**
     * Save a extendUser.
     *
     * @param extendUser the entity to save
     * @return the persisted entity
     */
    public ExtendUser save(ExtendUser extendUser) {
        log.debug("Request to save ExtendUser : {}", extendUser);
        return extendUserRepository.save(extendUser);
    }

    /**
     * Get all the extendUsers.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ExtendUser> findAll() {
        log.debug("Request to get all ExtendUsers");
        return extendUserRepository.findAll();
    }

    /**
     * Get one extendUser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ExtendUser findOne(Long id) {
        log.debug("Request to get ExtendUser : {}", id);
        return extendUserRepository.findOne(id);
    }

    /**
     * Delete the extendUser by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExtendUser : {}", id);
        extendUserRepository.delete(id);
    }
}
