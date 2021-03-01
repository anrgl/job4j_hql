package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hql.model.Candidate;

import java.util.List;

public class HqlRun {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        try (Session session = sf.openSession()) {
            session.beginTransaction();

            // 2. Save candidates
            session.save(Candidate.of("Petr", 10, 300));
            session.save(Candidate.of("Ivan", 7, 290));
            session.save(Candidate.of("Oleg", 1, 100));

            // todo 3.1. Find all candidates
            List<Candidate> candidates = session.createQuery("from Candidate").list();

            // todo 3.1. Find candidate by ID
            Candidate candidateById = session.find(Candidate.class, 2);

            // todo 3.1. Find candidate by NAME
            Candidate candidateByName = session
                    .createQuery("from Candidate where name = :name", Candidate.class)
                    .setParameter("name", "Petr")
                    .uniqueResult();

            // todo 4. Update candidate by ID
            session.createQuery("update Candidate set salary = :salary where id = :id")
                    .setParameter("salary", 150.0)
                    .setParameter("id", 3)
                    .executeUpdate();

            // todo 5. DELETE candidate by ID
            session.createQuery("delete Candidate where id = :id")
                    .setParameter("id", 2)
                    .executeUpdate();

            session.getTransaction().commit();
        }
    }
}
