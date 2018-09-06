package cs544.hpa1;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class AppConflict {

    private static SessionFactory sessionFactory;

    /* Reads hibernate.cfg.xml and prepares Hibernate for use     */
    protected static void setUp() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("conflict.cfg.xml")
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    protected static void tearDown() throws Exception {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    public static void main(String[] args) throws Exception {
        setUp();


        try (Session session  = sessionFactory.openSession()) {
            session.beginTransaction();

            // retieve all cars
            @SuppressWarnings("unchecked")
            Car carQ = (Car) session.createQuery("select  C from Car as C where  C.id =1 ").getSingleResult();

            carQ.setPrice(carQ.getPrice()+50);


            session.save(carQ);

            System.out.println("brand= " + carQ.getBrand() + ", year= "+ carQ.getYear() + ", price= " + carQ.getPrice());

            session.getTransaction().commit();
        }

        // Close the SessionFactory (best practice)
        tearDown();
    }
}

