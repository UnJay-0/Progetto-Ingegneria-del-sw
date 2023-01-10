package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Session;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@NamedQuery(name = "Party.byId", query="SELECT e FROM Party e WHERE e.partyId=?1")
@NamedQuery(name = "Party.byIdAndName", query="SELECT e FROM Party e WHERE e.partyId=?1 AND e.name=?2")
@NamedQuery(name = "Party.all", query="SELECT e FROM Party e")
public class Party implements Candidate {
    /**
     * OVERVIEW:
     * The instance of Party represents a group of candidates.
     * They are defined by a name and an id.
     *
     * REP INV:
     * <br/>
     * name != null && name != ""
     *
     * ABS FUN:
     * <br/>
     * ABS(Party) -> {id, name}
     */



    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Candidate_generator")
    @SequenceGenerator(name="Candidate_generator", sequenceName = "candidate_sequence", allocationSize = 1)
    @Id
    @Column(name = "party_id")
    private long partyId;
    @Basic
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "partyByPartyId")
    private Collection<Individual> individualsByPartyId;

    public Party() {

    }

    public Party(@NotNull String name) {
        if(name.equals("")) {
            throw new IllegalArgumentException("name can't be empty");
        }
        this.name = name;
    }

    @Override
    public void alterName(String name) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("UPDATE Party set name=?1 WHERE partyId=?2")
                .setParameter(1, name)
                .setParameter(2, this.getPartyId())
                .executeUpdate();
        entityManager.getTransaction().commit();
        this.name = name;
    }

    public static Party getPartyById(long id) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();

        Party party = entityManager.createNamedQuery("Party.byId", Party.class)
                .setParameter(1, id)
                .getSingleResult();

        entityManager.getTransaction().commit();
        return  party;
    }

    public static Party getPartyByIdAndName(long id, String name) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();

        Party party = entityManager.createNamedQuery("Party.byIdAndName", Party.class)
                .setParameter(1, id)
                .setParameter(2, name)
                .getSingleResult();


        entityManager.getTransaction().commit();
        return  party;
    }

    public static List<Party> getParty() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        List<Party> partyList = entityManager.createNamedQuery("Party.all", Party.class).getResultList();
        entityManager.getTransaction().commit();
        return partyList;
    }

    public long getPartyId() {
        return partyId;
    }

    private void setPartyId(long partyId) {
        this.partyId = partyId;
    }

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
        Party party = (Party) o;
        return partyId == party.partyId && Objects.equals(name, party.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(partyId, name);
    }

    public Collection<Individual> getIndividualsByPartyId() {
        return individualsByPartyId;
    }

    public List<Candidate> getMembers() {
        return new ArrayList<>(Individual.getIndividualByPartyId(this.getPartyId()));
    }

    private void setIndividualsByPartyId(Collection<Individual> individualsByPartyId) {
        this.individualsByPartyId = individualsByPartyId;
    }

    public String toString() {
        return name + "\nId: " + partyId;
    }

    @Override
    public String name() {
        return this.name;
    }
}
