/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.HobbyDTO;
import entities.Hobby;
import java.util.ArrayList;
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
public class HobbyFacade {

    private static HobbyFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    public HobbyFacade() {
    }

    public static HobbyFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new HobbyFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public synchronized HobbyDTO createHobby(HobbyDTO hdto) throws WebApplicationException {
        if (hdto.getName() == null) {
            throw new WebApplicationException("Hobby name is missing", 400);
        }
        Hobby h = new Hobby(hdto.getName(), hdto.getDescription());
        h = checkIfHobbyExistsElseCreateIt(h);
        return new HobbyDTO(h);
    }
    
    public HobbyDTO getHobbyById(long id) {
        EntityManager em = emf.createEntityManager();
        HobbyDTO hobbyDTO = new HobbyDTO(em.find(Hobby.class, id));
        if(hobbyDTO != null){
            return hobbyDTO;
        } else {
            throw new WebApplicationException(String.format("No hobby with provided id: (%d) found", id), 404);
        }
    }
    
    public List<Hobby> getHobbyByPersonId(long id) {
        EntityManager em = emf.createEntityManager();
        try{
            TypedQuery<Hobby> query1 = em.createQuery("SELECT h FROM Hobby h JOIN h.persons p WHERE p.id = :id", Hobby.class);
            query1.setParameter("id", id);
            List<Hobby> hobbies = query1.getResultList();
            return hobbies;
        }catch (NoResultException ex) {
            return new ArrayList<Hobby>();
        }
    }

    public synchronized HobbyDTO editHobby(HobbyDTO hdto, long id) throws WebApplicationException {
        if (hdto.getName() == null) {
            throw new WebApplicationException("Hobby name is missing", 400);
        }
        if (!isHobbyNameTaken(hdto)) {
            EntityManager em = emf.createEntityManager();
            Hobby hobby = em.find(Hobby.class, id);
            if (hobby == null) {
                throw new WebApplicationException(String.format("No bobby with provided id: (%d) found", id), 404);
            }
            hobby.setName(hdto.getName());
            hobby.setDescription(hdto.getDescription());
            try {
                em.getTransaction().begin();
                em.merge(hobby);
                em.getTransaction().commit();
                return new HobbyDTO(hobby);
            } catch (RuntimeException ex) {
                throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
            } finally {
                em.close();
            }

        } else {
            throw new WebApplicationException("Hobby name is aldready taken", 400);
        }
    }


    private synchronized Hobby checkIfHobbyExistsElseCreateIt(Hobby hobby) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :name AND h.description = :des", Hobby.class);
            query.setParameter("name", hobby.getName());
            query.setParameter("des", hobby.getDescription());
            hobby = (Hobby) query.getSingleResult();
            return hobby;
        } catch (NoResultException ex) {
            em.getTransaction().begin();
            em.persist(hobby);
            em.getTransaction().commit();
            return hobby;
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    private Boolean isHobbyNameTaken(HobbyDTO hobbyDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT h FROM Hobby h WHERE h.name = :name", Hobby.class);
            query.setParameter("name", hobbyDTO.getName());
            Hobby hobby = (Hobby) query.getSingleResult();
            if (hobby != null) {
                return true;
            } else {
                return false;
            }
        } catch (NoResultException ex) {
            return false;
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        HobbyFacade fe = getFacade(emf);

    }
}
