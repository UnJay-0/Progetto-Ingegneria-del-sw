package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Session;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

/**
 * The instance of Individual represents individual candidates.
 * They are defined by a name and a membership to a specific Party.
 * <br/><br/>
 *
 * REP INV:
 * <br/>
 * party != null
 * <br/>
 * name != null && name != ""
 * <br/>
 *
 * ABS FUN:
 * ABS(Individual) -> {name, ABS(party)}
 */
@Entity
@NamedQuery(name="Individual.byId", query="SELECT e FROM Individual e WHERE e.individualId=?1")
@NamedQuery(name="Individual.byIdAndName", query="SELECT e FROM Individual e WHERE e.individualId=?1 AND e.name=?2")
@NamedQuery(name="Individual.byPartyId", query="SELECT e FROM Individual e WHERE e.partyByPartyId.partyId=?1")
@NamedQuery(name="Individual.all", query="SELECT e FROM Individual e")
public class Individual implements Candidate {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Candidate_generator")
    @SequenceGenerator(name="Candidate_generator", sequenceName = "candidate_sequence", allocationSize = 1)
    @Id
    @Column(name = "individual_id")
    private long individualId;
    @Basic
    @Column(name = "name")
    private String name;
    @ManyToOne
    @JoinColumn(name = "party_id", referencedColumnName = "party_id")
    private Party partyByPartyId;

    public Individual() {

    }

    /**
     * Creates an instance of Individual with passed name and party.
     *
     * @param name of the individual candidate
     * @param party of the individual candidate
     */
    public Individual(@NotNull String name, @NotNull Party party) {
        if(name.equals("")) {
            throw new IllegalArgumentException("name can't be null");
        }
        partyByPartyId = party;
        this.name = name;
    }

    public void alterName(String name) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("UPDATE Individual set name=?1 WHERE individualId=?2")
                .setParameter(1, name)
                .setParameter(2, this.getIndividualId())
                .executeUpdate();
        entityManager.getTransaction().commit();
        this.name = name;
    }

    @Override
    public long getId() {
        return this.individualId;
    }

    public static Individual getIndividualById(long id) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();

        Individual individual = entityManager.createNamedQuery("Individual.byId", Individual.class)
                .setParameter(1, id)
                .getSingleResult();


        entityManager.getTransaction().commit();
        return  individual;
    }

    public static Individual getIndividualByIdAndName(long id, String name) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();

        Individual individual = entityManager.createNamedQuery("Individual.byIdAndName", Individual.class)
                .setParameter(1, id)
                .setParameter(2, name)
                .getSingleResult();


        entityManager.getTransaction().commit();
        return  individual;
    }

    public static List<Individual> getIndividual() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        List<Individual> individualList = entityManager.createNamedQuery("Individual.all", Individual.class).getResultList();
        entityManager.getTransaction().commit();
        return individualList;
    }

    public static List<Individual> getIndividualByPartyId(long id) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        List<Individual> individualList = entityManager.createNamedQuery("Individual.byPartyId", Individual.class)
                .setParameter(1, id)
                .getResultList();
        entityManager.getTransaction().commit();
        return individualList;
    }

    /**
     *
     * @return the id of the individual candidate.
     */
    public long getIndividualId() {
        return individualId;
    }


    private void setIndividualId(long individualId) {
        this.individualId = individualId;
    }

    /**
     *
     * @return name of the individual candidate.
     */
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Individual that = (Individual) o;
        return individualId == that.individualId && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(individualId, name);
    }

    /**
     *
     * @return the belonging party of the individual candidate.
     */
    public Party getPartyByPartyId() {
        return partyByPartyId;
    }

    private void setPartyByPartyId(Party partyByPartyId) {
        this.partyByPartyId = partyByPartyId;
    }

    @Override
    public String toString() {
        return   name + "\nid: " + individualId + "\n"+  partyByPartyId.toString();
    }

    @Override
    public String name() {
        return name;
    }
}
