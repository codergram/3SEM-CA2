/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PersonDTO;
import entities.Address;
import entities.CityInfo;
import entities.Hobby;
import entities.Person;
import entities.Phone;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author Tweny
 */
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static MainFacade facade;
    private Person p1 = new Person("email 1", "First 1", "Last 1");
    private Person p2 = new Person("email 2", "First 2", "Last 2");
    private Phone ph1 = new Phone(111, "");
    private Phone ph2 = new Phone(222, "");
    private Address a1 = new Address("street 1", "");
    private CityInfo c1 = new CityInfo("2000", "Frederiksberg");
    private Hobby h1 = new Hobby("hobby 1", "");
    private Hobby h2 = new Hobby("hobby 2", "");

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactoryForTest();
       facade = MainFacade.getFacade(emf);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Phone.deleteAllRows").executeUpdate();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Hobby.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            em.createNamedQuery("CityInfo.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    void testDBSize() {
        assertEquals(2, facade.getPersonCount(), "Expects two rows in the database");
    }
    
    @Test
    void testAddPhone() {
        p1.addPhone(ph1);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(p1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        assertEquals(111, p1.getPhones().get(0).getNumber());
    }
    
    @Test
    void testRemovePhone() {
        ph1.setPerson(null);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(ph1);
            em.getTransaction().commit();
            p1 = em.find(Person.class, p1.getId());
        } finally {
            em.close();
        }
        assertEquals(0, p1.getPhones().size());
    }
    
    @Test
    void testAddHobbyToPerson() {
        p1.addHobby(h1);
        p1.addHobby(h2);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(p1);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        assertEquals(2, p1.getHobbies().size());
    }
    
    @Test
    void testUpdatePerson() {
        p1.setFirstName("New name");
        PersonDTO p = new PersonDTO(p1);
        EntityManager em = emf.createEntityManager();
        try {
            p = facade.updatePerson(p1.getId(), p);
        } finally {
            em.close();
        }
        assertEquals("New name", p.getFirstName());
    }

}
