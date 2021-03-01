package ru.job4j.hql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.hql.model.Candidate;

public class HqlRun {
    public static void main(String[] args) {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        Candidate rsl = null;
        try (Session session = sf.openSession()) {
            session.beginTransaction();

            rsl = session.createQuery("select distinct c from Candidate c "
                    + "join fetch c.base b "
                    + "join fetch b.jobs j "
                    + "where c.id = :id", Candidate.class)
                    .setParameter("id", 4)
                    .uniqueResult();

            session.getTransaction().commit();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }

        System.out.println(rsl);
    }
}
