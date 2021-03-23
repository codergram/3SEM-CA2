/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.AddressDTO;
import dtos.CityInfoDTO;
import dtos.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Person;
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
public class AddressFacade {

    private static AddressFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    public AddressFacade() {
    }

    public static AddressFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AddressFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public synchronized AddressDTO createAddress(AddressDTO adto) throws WebApplicationException {
        if (adto.getStreet() == null) {
            throw new WebApplicationException("Street is missing", 400);
        }
        Address a = new Address(adto.getStreet(), adto.getAdditionalInfo());
        CityInfoDTO cidto = new CityInfoDTO(adto.getCityInfo().getZipCode(), adto.getCityInfo().getCity());
        cidto = new CityInfoFacade().createCityInfo(cidto);
        a = checkIfAddressExistsElseCreateIt(a);
        EntityManager em = emf.createEntityManager();
        try {
            CityInfo ci = em.find(CityInfo.class, cidto.getId());
            if (ci == null) {
                throw new WebApplicationException(String.format("No cityinfo with provided id: (%d) found", cidto.getId()), 404);
            }
            em.getTransaction().begin();
            ci.addAddress(a);
            em.merge(ci);
            em.getTransaction().commit();
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }

        return new AddressDTO(a);
    }

    public AddressDTO getAddressById(long id) {
        EntityManager em = emf.createEntityManager();
        AddressDTO addressDTODTO = new AddressDTO(em.find(Address.class, id));
        if (addressDTODTO != null) {
            return addressDTODTO;
        } else {
            throw new WebApplicationException(String.format("No address with provided id: (%d) found", id), 404);
        }
    }

    public Address getAddressByPersonId(long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT a FROM Address a JOIN a.persons p WHERE p.id = :id", Address.class);
            query.setParameter("id", id);
            return (Address) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    private synchronized Address checkIfAddressExistsElseCreateIt(Address address) {
        EntityManager em = emf.createEntityManager();
        try {
            Query query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street", Address.class);
            query.setParameter("street", address.getStreet());
            address = (Address) query.getSingleResult();
            return address;
        } catch (NoResultException ex) {
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
            return address;
        } catch (RuntimeException ex) {
            throw new WebApplicationException("Internal Server Problem. We are sorry for the inconvenience", 500);
        } finally {
            em.close();
        }
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        AddressFacade fe = getFacade(emf);
    }
}
