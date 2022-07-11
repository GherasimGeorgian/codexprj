package com.project.repository.orm;

import com.project.domain.Client;
import com.project.repository.interfaces.IClientRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;


public class ClientORMRepository implements IClientRepository {

    SessionFactoryInit sessionFactory;

    public ClientORMRepository(SessionFactoryInit sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    public Client findByEmail(String email){
        sessionFactory.initialize();
        Client client =null;
        try(Session session = sessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                try {
                    client =
                            (Client)session.createQuery("from Client where email=:fn")
                                    .setParameter("fn", email)
                                    .uniqueResult();
                }catch(Exception ex){
                    ex.printStackTrace();
                }


                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }catch (Exception ex){

            ex.printStackTrace();
        }finally
        {
            sessionFactory.close();
        }
        return client;
    }
    public Client findByUserName(String username){
        sessionFactory.initialize();
        Client client =null;
        try(Session session = sessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                try {
                    client =
                            (Client)session.createQuery("from Client where username=:fn")
                                    .setParameter("fn", username)
                                    .uniqueResult();
                }catch(Exception ex){
                    ex.printStackTrace();
                }


                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }catch (Exception ex){

            ex.printStackTrace();
        }finally
        {
            sessionFactory.close();
        }
        return client;
    }

    public Client loginClient(String username, String password) {
        sessionFactory.initialize();
        Client client =null;
        try(Session session = sessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                try {
                    client =
                            (Client)session.createQuery("from Client where username=:fn and password=:ln")
                                    .setParameter("fn", username)
                                    .setParameter("ln", password)
                                    .uniqueResult();
                }catch(Exception ex){
                    ex.printStackTrace();
                }


                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }catch (Exception ex){

            ex.printStackTrace();
        }finally
        {
            sessionFactory.close();
        }
        return client;

    }
    @Override
    public Client save(Client entity) {
        sessionFactory.initialize();


        try(Session session = sessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                try {
                    session.save(entity);

                }catch(Exception ex){
                    ex.printStackTrace();
                }


                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }catch (Exception ex){

            ex.printStackTrace();
        } finally
        {
            sessionFactory.close();
        }
        return entity;
    }
    @Override
    public Client findOne(Long id_param) {
        return null;
    }
    @Override
    public List<Client> findAll() {
        sessionFactory.initialize();

        List<Client> clientii = new ArrayList<>();
        try(Session session = sessionFactory.getSessionFactory().openSession()) {
            Transaction tx = null;

            try {
                tx = session.beginTransaction();

                try {
                    clientii =
                            session.createQuery("from Client").list();
                }catch(Exception ex){
                    ex.printStackTrace();
                }


                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        }catch (Exception ex){

            ex.printStackTrace();
        } finally
        {
            sessionFactory.close();
        }
        return clientii;

    }
    @Override
    public Client update(Client entity,Client newEntity) {
        return null;
    }
    @Override
    public Client delete(Long id){
        return null;
    }



}