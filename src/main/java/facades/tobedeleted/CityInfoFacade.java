/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.CityInfoDTO;
import entities.CityInfo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class CityInfoFacade {

    private static CityInfoFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    public CityInfoFacade() {
    }

    public static CityInfoFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CityInfoFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public synchronized CityInfoDTO createCityInfo(CityInfoDTO cidto) throws WebApplicationException {
        if (cidto.getZipCode() == null || cidto.getCity() == null) {
            throw new WebApplicationException("City or zipcode is missing", 400);
        }
        CityInfo ci = new CityInfo(cidto.getZipCode(), cidto.getCity());
        ci = checkIfCityInfoExistsElseCreateIt(ci);
        return new CityInfoDTO(ci);
    }
    
    public CityInfoDTO getCityInfoById(long id) {
        EntityManager em = emf.createEntityManager();
        CityInfoDTO cityInfoDTO = new CityInfoDTO(em.find(CityInfo.class, id));
        if(cityInfoDTO != null){
            return cityInfoDTO;
        } else {
            throw new WebApplicationException(String.format("No cityinfo with provided id: (%d) found", id), 404);
        }
    }


    public long getCityInfoCount() {
        EntityManager em = emf.createEntityManager();
        try {
            return (long) em.createQuery("SELECT COUNT(ci) FROM CityInfo ci").getSingleResult();
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    private synchronized CityInfo checkIfCityInfoExistsElseCreateIt(CityInfo cityInfo) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT ci FROM CityInfo ci WHERE ci.zipCode = :zip", CityInfo.class);
            query.setParameter("zip", cityInfo.getZipCode());
            cityInfo = (CityInfo) query.getSingleResult();
            return cityInfo;
        } catch (NoResultException ex) {
            em.getTransaction().begin();
            em.persist(cityInfo);
            em.getTransaction().commit();
            return cityInfo;
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        CityInfoFacade fe = getFacade(emf);
    }
}
