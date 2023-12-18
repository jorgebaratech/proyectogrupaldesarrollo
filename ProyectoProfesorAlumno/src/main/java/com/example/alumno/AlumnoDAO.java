package com.example.alumno;

import com.example.DAO;
import com.example.HibernateUtil;
import com.example.actividad.Actividad;
import lombok.extern.java.Log;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
@Log
public class AlumnoDAO implements DAO<Alumno> {
    @Override
    public List<Alumno> getAll( ) {
        List<Alumno> result = null;
        try( Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Alumno> q = s.createQuery( "from Alumno", Alumno.class);
            result = q.getResultList();
        }
        catch ( Exception ignore ){
        }
        return result;
    }

    @Override
    public Alumno get( Long id ) {
        return null;
    }

    @Override
    public Alumno save(Alumno data) {
        return null;
    }

    @Override
    public void update(Alumno data) {

    }

    @Override
    public void delete( Alumno data ) {

    }

    public boolean esAlumnoCorrecto(String user, String pass) {
        return loadLogin(user,pass) != null;
    }

    public Alumno loadLogin(String user, String pass) {
        Alumno result = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Alumno> q = session.createQuery("from Alumno where nombre=:n and contraseña=:p", Alumno.class);
            q.setParameter("n",user);
            q.setParameter("p",pass);

            try {
                result = q.getSingleResult();
            }catch (Exception e){

            }
        }
        return result;
    }
}
