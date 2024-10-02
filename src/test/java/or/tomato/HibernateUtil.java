package or.tomato;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.tomato.weather.entity.Location;
import org.tomato.weather.entity.Session;
import org.tomato.weather.entity.User;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory () {
        if (sessionFactory == null){
            Configuration configuration = new Configuration().configure();
            configuration.addAnnotatedClass(Location.class);
            configuration.addAnnotatedClass(Session.class);
            configuration.addAnnotatedClass(User.class);
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
}