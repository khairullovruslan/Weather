package or.tomato.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import lombok.extern.slf4j.Slf4j;
import or.tomato.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tomato.weather.dto.UserRegistrationDto;
import org.tomato.weather.entity.User;
import org.tomato.weather.exception.authException.LoginDuplicateException;
import org.tomato.weather.service.AuthService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class AuthServiceTest {
    private User user;
    private UserRegistrationDto userDTO;
    private final AuthService authService = new AuthService();
    private final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    @BeforeEach
    void setUp() {
        user = User.builder()
                .login(UUID.randomUUID().toString())
                .password(UUID.randomUUID().toString())
                .build();
        userDTO = UserRegistrationDto.builder().login(user.getLogin()).password(user.getPassword()).build();
    }

    @AfterEach
    void tearDown() {
        try (var hibernateSession = sessionFactory.openSession()) {
            Transaction transaction = hibernateSession.beginTransaction();
            CriteriaBuilder cb = hibernateSession.getCriteriaBuilder();
            CriteriaDelete<User> criteriaDelete = cb.createCriteriaDelete(User.class);
            hibernateSession.createQuery(criteriaDelete).executeUpdate();
            transaction.commit();
        }
    }

    @Test
    void after_success_registration_user_save_to_db(){
        User user1 = authService.registration(userDTO);
        User find;
        try (var session = HibernateUtil.getSessionFactory().openSession()){
            var tr = session.beginTransaction();
             find = (User) session
                    .createQuery("select u from User u where u.id = :i")
                    .setParameter("i", user1.getId())
                    .uniqueResult();
            tr.commit();

            assertNotNull(find);
            assertEquals(find.getId(), user1.getId());
            assertEquals(find.getLogin(), user1.getLogin());
        }
    }
    @Test
    void after_invalid_registration_throws_exception_that_login_exist(){
        authService.registration(userDTO);
        assertThrows(
                LoginDuplicateException.class,
                () -> authService.registration(userDTO));
    }


}

