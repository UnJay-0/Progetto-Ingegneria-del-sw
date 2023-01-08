package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.model.Candidate;
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



    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "party_generator")
    @SequenceGenerator(name="party_generator", sequenceName = "party_sequence", allocationSize = 1)
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
            throw new IllegalArgumentException("name can't be null");
        }
        this.name = name;
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
        return null;
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
