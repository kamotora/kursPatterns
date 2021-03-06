package util;

import model.*;
import model.categories.Category;
import model.categories.TypeCategory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {
    private static SessionFactory sessionFactory;
    private static Session currentSession;
    private HibernateSessionFactory() {}

    public static Session getSession(){
        if(sessionFactory == null){
            try {
                sessionFactory = getSessionFactory();
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        if(currentSession != null && currentSession.isOpen())
            currentSession.close();
        currentSession = sessionFactory.openSession();
        return currentSession;
    }

    public static SessionFactory getSessionFactory() throws NullPointerException {
        if (sessionFactory == null) {
            try {
                Configuration  configuration = new Configuration().configure( );
                //Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Category.class);
                configuration.addAnnotatedClass(Bill.class);
                configuration.addAnnotatedClass(Limit.class);
                configuration.addAnnotatedClass(Operation.class);
                configuration.addAnnotatedClass(Period.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(TypeCategory.class);
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Исключение!\n" + e);
            }
        }
        if(sessionFactory == null)
            throw new NullPointerException("Не удалось создать фабрику сессий");
        return sessionFactory;
    }
}
