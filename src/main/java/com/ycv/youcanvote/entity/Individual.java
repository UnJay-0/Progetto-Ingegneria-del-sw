package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.model.Candidate;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

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
public class Individual implements Candidate {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Individual_generator")
    @SequenceGenerator(name="Individual_generator", sequenceName = "Individual_sequence", allocationSize = 1)
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
