/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PhoneDTO;
import entities.Person;
import entities.Phone;
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
public class PhoneFacade {

    private static PhoneFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    public PhoneFacade() {
    }

    public static PhoneFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PhoneFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public synchronized PhoneDTO createPhone(PhoneDTO pdto) throws WebApplicationException {
        if (pdto.getNumber() == 0 || pdto.getNumber() < 10000000) {
            throw new WebApplicationException("Number is missing or is smaller den 8 digits", 400);
        }
        Phone p = new Phone(pdto.getNumber(), pdto.getDescription());
        p = checkIfNumberExistsElseCreateIt(p);
        return new PhoneDTO(p);
    }

    public PhoneDTO getPhoneById(long id) {
        EntityManager em = emf.createEntityManager();
        PhoneDTO phoneDTO = new PhoneDTO(em.find(Phone.class, id));
        if (phoneDTO != null) {
            return phoneDTO;
        } else {
            throw new WebApplicationException(String.format("No phone with provided id: (%d) found", id), 404);
        }
    }

    public List<Phone> getPhoneByPersonId(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Phone> query = em.createQuery("SELECT p FROM Phone p JOIN p.person pe WHERE pe.id = :id", Phone.class);
            query.setParameter("id", id);
            List<Phone> phones = query.getResultList();
            return phones;
        } catch (NoResultException ex) {
            return new ArrayList<>();
        }
    }

    public PhoneDTO getPhoneByNumber(PhoneDTO phoneDTO) {
        if (phoneDTO.getNumber() == 0 || phoneDTO.getNumber() < 10000000) {
            throw new WebApplicationException("Number is missing or is smaller den 8 digits", 400);
        }
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT p FROM Phone p WHERE p.number = :number", Phone.class);
            query.setParameter("name", phoneDTO.getNumber());
            Phone phone = (Phone) query.getSingleResult();
            return new PhoneDTO(phone);
        } catch (NoResultException ex) {
            return null;
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    public synchronized PhoneDTO deletePhoneFromPerson(long phoneId, long personId) throws WebApplicationException {
        if (phoneId <= 0 || personId <= 0) {
            throw new WebApplicationException("The provided is at not valid", 400);
        }
        EntityManager em = emf.createEntityManager();

        Phone phone = em.find(Phone.class, phoneId);
        if (phone == null) {
            throw new WebApplicationException(String.format("No phone with provided id: (%d) found", phoneId), 404);
        }
        Person person = em.find(Person.class, personId);
        if (person == null) {
            throw new WebApplicationException(String.format("No person with provided id: (%d) found", personId), 404);
        }
        if (phone.getPerson().getId() == person.getId()) {
            phone.setPerson(null);
            try {
                em.getTransaction().begin();
                em.merge(phone);
                em.getTransaction().commit();
                return new PhoneDTO(phone);
            } catch (RuntimeException ex) {
                throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
            } finally {
                em.close();
            }
        } else {
            throw new WebApplicationException("Person does not own this number", 404);
        }

    }


    private synchronized Phone checkIfNumberExistsElseCreateIt(Phone phone) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT p FROM Phone p WHERE p.number = :number", Phone.class);
            query.setParameter("number", phone.getNumber());
            phone = (Phone) query.getSingleResult();
            return phone;
        } catch (NoResultException ex) {
            em.getTransaction().begin();
            em.persist(phone);
            em.getTransaction().commit();
            return phone;
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    private Boolean isPhoneNumberTaken(PhoneDTO phoneDTO) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT p FROM Phone p WHERE p.number = :number", Phone.class);
            query.setParameter("number", phoneDTO.getNumber());
            Phone phone = (Phone) query.getSingleResult();
            if (phone.getPerson() != null) {
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
        PhoneFacade fe = getFacade(emf);
    }
}
