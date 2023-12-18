package com.example.actividad;

import com.example.DAO;
import com.example.HibernateUtil;
import com.example.alumno.Alumno;
import org.hibernate.Session;
import lombok.extern.java.Log;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@Log
public class ActividadDAO implements DAO<Actividad> {
    @Override
    public List<Actividad> getAll() {
        List<Actividad> result = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Actividad> query = session.createQuery("from Actividad", Actividad.class);
            result = query.getResultList();
        } catch (Exception ignore) {
        }
        return result;
    }

    @Override
    public Actividad get(Long id) {
        var salida = new Actividad();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            salida = session.get(Actividad.class, id);
        }
        return salida;
    }

    @Override
    public Actividad save(Actividad data) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(data);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return data;
        }
    }

    @Override
    public void update(Actividad data) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(data);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void delete(Actividad data) {
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            Alumno alumno = session.get(Alumno.class, data.getId());
            session.remove(alumno);
        });
    }

    public List<Actividad> actividadesdelalumno(Actividad student) {
        List<Actividad> salida;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Actividad> q = s.createQuery("from Actividad where alumno =: id", Actividad.class);
            q.setParameter("id", student);
            salida = q.getResultList();
        }
        return salida;
    }
}
