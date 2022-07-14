package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    private static final String createTable = "CREATE TABLE IF NOT EXISTS UserTable " +
            "(id int PRIMARY KEY AUTO_INCREMENT, name varchar(30) NOT NULL, " +
            "lastName varchar(30) NOT NULL, age int NOT NULL)";
    private static final String dropTable = "DROP TABLE IF EXISTS UserTable";
    private static final Session session = null;
    private static Transaction transaction = null;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(createTable).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new HibernateException(e);
//            if (transaction != null) {
//                transaction.rollback();
//            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createSQLQuery(dropTable).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new HibernateException(e);
//            if (transaction != null) {
//                transaction.rollback();
//            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        } catch (Exception e) {
            throw new HibernateException(e);
//            if (transaction != null) {
//                transaction.rollback();
//            }
        }

    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User where id = id").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new HibernateException(e);
//            if (transaction != null) {
//                transaction.rollback();
//            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            users = session.createQuery("from User")
                    .getResultList();
            transaction.commit();
        } catch (Exception e) {
            throw new HibernateException(e);
//            transaction.rollback();
//            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete User").executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            throw new HibernateException(e);
//            if (transaction != null) {
//                transaction.rollback();
//            }
        }
    }
}