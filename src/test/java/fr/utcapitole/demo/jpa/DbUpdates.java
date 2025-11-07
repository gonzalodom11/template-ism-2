package fr.utcapitole.demo.jpa;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class DbUpdates {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void reset() {
        if (tableExists("hibernate_sequence")) {
            entityManager.createNativeQuery("update hibernate_sequence set next_val = 1").executeUpdate();
        }
        if (tableExists("hibernate_sequences")) {
            entityManager.createNativeQuery("update hibernate_sequences set next_val = 1 where sequence_name = 'default'").executeUpdate();
        }
    }

    private boolean tableExists(String name) {
        Number count = (Number) entityManager.createNativeQuery("select count(*) from sqlite_master where type='table' and name=:name")
                .setParameter("name", name)
                .getSingleResult();
        return count != null && count.intValue() > 0;
    }
}
