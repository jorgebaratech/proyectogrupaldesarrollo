package com.example.empresas;

import com.example.DAO;
import com.example.HibernateUtil;
import org.hibernate.Session;
import lombok.extern.java.Log;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

@Log
public class EmpresasDAO implements DAO<Empresas> {
    @Override
    public List<Empresas> getAll( ) {
        List<Empresas> result = null;
        try( Session s = HibernateUtil.getSessionFactory().openSession()) {
            Query<Empresas> q = s.createQuery( "from Empresas ", Empresas.class);
            result = q.getResultList();
        }
        catch ( Exception ignore ){
        }
        return result;
    }

    @Override
    public Empresas get(Long id ) {
        return null;
    }

    @Override
    public Empresas save (Empresas data) {
        return null;
    }

    @Override
    public void update(Empresas data) {

    }

    @Override
    public void delete(Empresas data) {

    }
    public void insertarEmpresa(Empresas empresas){
        try(Session s = HibernateUtil.getSessionFactory( ).openSession( ) ) {
            Transaction t = s.beginTransaction( );
            Empresas nuevaEmpresa = new Empresas( );

            nuevaEmpresa.setNombre( empresas.getNombre() );
            nuevaEmpresa.setTelefono( empresas.getTelefono() );
            nuevaEmpresa.setEmail( empresas.getEmail() );
            nuevaEmpresa.setResponsable( empresas.getResponsable() );
            nuevaEmpresa.setObservaciones( empresas.getObservaciones() );
            s.persist( nuevaEmpresa );
            t.commit( );
        } catch ( Exception e ) {
            log.severe( "Error" );
        }
    }

    public void actualizarEmpresa( Empresas empresas ){
        try(Session s = HibernateUtil.getSessionFactory().openSession()){
            Transaction t = s.beginTransaction();
            Empresas nuevaEmpresa = s.get(Empresas.class, empresas.getId());
            nuevaEmpresa.setNombre( empresas.getNombre() );
            nuevaEmpresa.setResponsable(empresas.getResponsable());
            nuevaEmpresa.setEmail(empresas.getEmail());
            nuevaEmpresa.setTelefono(empresas.getTelefono());
            nuevaEmpresa.setObservaciones(empresas.getObservaciones());
            t.commit();
        }
    }

    public void borrarEmpresa( Empresas EmpresaSelected ) {
        HibernateUtil.getSessionFactory().inTransaction(session -> {
            Empresas c = session.get(Empresas.class, EmpresaSelected.getId());
            session.remove(c);
        });
    }
}
