package com.example.profesor;

import com.example.DAO;
import com.example.HibernateUtil;
import com.example.empresas.Empresas;
import com.example.alumno.Alumno;
import lombok.extern.java.Log;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

@Log
public class ProfesorDAO implements DAO<Profesor> {
    @Override
    public List<Profesor> getAll( ) {
        List<Profesor> result = null;
        try( Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Profesor> q = s.createQuery( "from Profesor", Profesor.class);
            result = q.getResultList();
        }
        catch ( Exception ignore ){
        }
        return result;
    }


    @Override
    public Profesor get( Long id ) {
        return null;
    }

    @Override
    public Profesor save( Profesor data ) {
        return null;
    }

    @Override
    public void update( Profesor data ) {

    }

    @Override
    public void delete( Profesor data ) {

    }

    public List<Alumno> alumnosdeunprofesor( Profesor profesor ) {
        List<Alumno> salida = new ArrayList<>( );
        try ( Session s = HibernateUtil.getSessionFactory( ).openSession( ) ) {
            Query<Alumno> q = s.createQuery( "from Alumno where tutor.id =: tutor" , Alumno.class );
            q.setParameter( "tutor" , profesor.getId() );
            salida = q.getResultList();
        }
        return salida;
    }

    public Alumno alumnosporDNI( String dni ) {
        Alumno salida = null;
        try ( Session s = HibernateUtil.getSessionFactory( ).openSession( ) ) {
            Query<Alumno> q = s.createQuery( "from Alumno where dni =: d" , Alumno.class );
            q.setParameter( "d" , dni );
            salida = q.getSingleResult();
        }
        return salida;
    }

    public void insertarAlumno(Alumno alumno){
        try ( org.hibernate.Session s = HibernateUtil.getSessionFactory( ).openSession( ) ) {
            Transaction t = s.beginTransaction( );
            //Crear u nuevo item
            Alumno newAlumno = new Alumno( );
            newAlumno.setDni( alumno.getDni() );
            newAlumno.setEmpresas( alumno.getEmpresas() );
            newAlumno.setContraseña( alumno.getContraseña() );
            newAlumno.setEmail( alumno.getEmail() );
            newAlumno.setTelefono( alumno.getTelefono() );
            newAlumno.setActividad( alumno.getActividad() );
            newAlumno.setNacimiento( alumno.getNacimiento() );
            newAlumno.setHorasDual( alumno.getHorasDual() );
            newAlumno.setNombre( alumno.getNombre() );
            newAlumno.setApellidos( alumno.getApellidos() );
            newAlumno.setHorasFct( alumno.getHorasFct() );
            newAlumno.setObservaciones( alumno.getObservaciones() );
            newAlumno.setTutor( alumno.getTutor() );
            s.persist( newAlumno );
            t.commit( );
        } catch ( Exception e ) {
            log.severe( "Error" );
        }
    }
    public Empresas empresasPorNombre( String companyName ) {
        Empresas salida = null;
        try ( Session s = HibernateUtil.getSessionFactory( ).openSession( ) ) {
            Query<Empresas> q = s.createQuery( "from Empresas where nombre =: name" , Empresas.class );
            q.setParameter( "name" , companyName );
            salida = q.getSingleResult();
        }
        return salida;
    }

    public void updateAlumno( Alumno alumno ){
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            Transaction t = s.beginTransaction();
            Alumno newAlumno = s.get(Alumno.class, alumno.getId());

            newAlumno.setNombre( alumno.getNombre() );
            newAlumno.setApellidos( alumno.getApellidos() );
            newAlumno.setDni( alumno.getDni() );
            newAlumno.setNacimiento ( alumno.getNacimiento() );
            newAlumno.setEmail( alumno.getEmail() );
            newAlumno.setEmpresas( alumno.getEmpresas() );
            newAlumno.setHorasDual( alumno.getHorasDual()); ;
            newAlumno.setHorasFct( alumno.getHorasDual() );
            newAlumno.setObservaciones( alumno.getObservaciones() );
            t.commit();
        }
    }

    public void borrarAlumno( Alumno alumnoSelected ) {
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            Alumno i = session.get(Alumno.class, alumnoSelected.getId());
            session.remove(i);
        });
    }

    public boolean esProfesorCorrecto(String user, String pass) {
        return loadLogin(user,pass) != null;
    }

    public Profesor loadLogin(String user, String pass) {
        Profesor result = null;

        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Profesor> q = session.createQuery("from Profesor where nombre=:u and contrasenya=:p", Profesor.class);
            q.setParameter("u",user);
            q.setParameter("p",pass);

            try {
                result = q.getSingleResult();
            }catch (Exception e){

            }
        }
        return result;


    }

}
