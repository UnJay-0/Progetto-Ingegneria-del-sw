package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.controller.vote.CategoricalVote;
import com.ycv.youcanvote.controller.vote.RankedVote;
import com.ycv.youcanvote.controller.vote.Referendum;
import com.ycv.youcanvote.controller.vote.Vote;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.model.Party;
import com.ycv.youcanvote.model.Session;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

/**
 * OVERVIEW:
 * VotingSession represent a session of voting with name, description
 * candidates to choose. A VotingSession can be open or closed.
 * <br/><br/>
 * REP INV:
 * <br/>
 * The name can't be null or empty.
 * <br/>
 * The description can't be null or empty.
 * <br/>
 * There must be at least one candidate.
 * <br/><br/>
 * ABS FUN:
 * <br/>
 * ABS(VotingSession) -> {name, description}
 */
@Entity
@Table(name = "voting_session", schema = "public", catalog = "youcanvote")
@NamedQuery(name = "VotingSession.byId", query = "SELECT e FROM VotingSession e WHERE e.vsId=?1")
@NamedQuery(name = "VotingSession.all", query = "SELECT e FROM VotingSession e")
@NamedQuery(name = "VotingSession.ifOpen", query = "SELECT e FROM VotingSession e WHERE e.isOpen=true")
@NamedQuery(name = "VotingSession.ifClose", query = "SELECT e FROM VotingSession e WHERE e.isOpen=false")
public class VotingSession {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vs_generator")
    @SequenceGenerator(name="vs_generator", sequenceName = "vs_sequence", allocationSize = 1)
    @Id
    @Column(name = "vs_id")
    private long vsId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "type")
    private String type;
    @Basic
    @Column(name = "creation_date")
    private Date creationDate;
    @Basic
    @Column(name = "candidates")
    private String candidates;

    @Basic
    @Column(name = "is_open")
    private boolean isOpen;



    public VotingSession() {

    }

    /**
     *
     * @param name of the voting session.
     * @param description of the voting session.
     * @param choices of candidates to select.
     * @param type of the voting session.
     * @param modVictory mode of determine the winner.
     */
    public VotingSession(@NotNull String name, String description,
                         @NotNull List<Candidate> choices,
                         @NotNull String type,
                         @NotNull String modVictory) {
        if (name.equals("") ) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }
        if (description.equals("") ) {
            throw new IllegalArgumentException("La descrizione non può essere vuota");
        }
        this.name = name;
        this.description = description;
        this.creationDate = Date.valueOf(LocalDate.now());
        StringBuilder candidates = new StringBuilder();
        for(Candidate el : choices) {
            candidates.append(el.toString()).append(";");
        }
        this.candidates = candidates.toString();
        this.type = type + ";" + modVictory;
        this.isOpen = true;
        this.vsId = 0;
    }

    public Vote getVote() {
        String typeOfVote = type.split(";")[0];
        return TypeOfVote.valueOf(typeOfVote).getController(this);
    }

    public List<Candidate> getCandidatesList() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        List<Candidate> candidatesList = new ArrayList<>();
        for(String candidateToStr : candidates.split(";")) {
            String name = candidateToStr.split("\n")[0];
            Long id = Long.parseLong(candidateToStr.split("\n")[1].split(" ")[1]);

            try {
                entityManager.getTransaction().begin();

                TypedQuery<Individual> getIndividual = entityManager.createNamedQuery(
                        "Individual.byIdAndName", Individual.class);
                getIndividual.setParameter(1, id);
                getIndividual.setParameter(2, name);

                if(getIndividual.getResultList().size() != 0) {
                    candidatesList.add(getIndividual.getSingleResult());
                } else {
                    TypedQuery<Party> getParty = entityManager.createNamedQuery(
                            "Party.byIdAndName", Party.class);
                    getParty.setParameter(1, id);
                    getParty.setParameter(2, name);
                    candidatesList.add(getIndividual.getSingleResult());
                }
            } finally {
                entityManager.getTransaction().commit();
            }
        }
        return candidatesList;
    }

    /**
     * This method close this voting session impeding any other votes to be
     * accepted.
     *
     */
    public void close() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.createQuery("UPDATE VotingSession set isOpen=false WHERE vsId=?1")
                .setParameter(1, this.vsId).executeUpdate();
        this.isOpen = false;
    }

    /**
     *
     * @return the number of votes already submitted for this voting session
     */
    public int nVote(){
        EntityManager entityManager = Session.getInstance().getEntityManager();
        TypedQuery<com.ycv.youcanvote.entity.Vote> getVotes = entityManager.createNamedQuery(
                "Vote.byVotingSession",
                com.ycv.youcanvote.entity.Vote.class);
        getVotes.setParameter(1, this.vsId);
        return getVotes.getResultList().size();
    }

    /**
     *
     * @return the id this.
     */
    public long getVsId() {
        return vsId;
    }

    private void setVsId(long vsId) {
        this.vsId = vsId;
    }

    /**
     *
     * @return the name this.
     */
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the description.
     */
    public String getDescription() {
        return description;
    }

    private void setDescription(String description) {
        this.description = description;
    }


    private String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return the date of creation of the voting session
     */
    public Date getCreationDate() {
        return creationDate;
    }

    private void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    private String getCandidates() {
        return candidates;
    }

    private void setCandidates(String candidates) {
        this.candidates = candidates;
    }

    /**
     * Method used to get the state of the voting session.
     *
     * @return true if this is open, false otherwise.
     */
    public boolean isOpen() {
        return isOpen;
    }

    private void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public String toString() {
        return name + "\n" + description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotingSession that = (VotingSession) o;
        return vsId == that.vsId && isOpen == that.isOpen && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(type, that.type) && Objects.equals(creationDate, that.creationDate) && Objects.equals(candidates, that.candidates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vsId, name, description, type, creationDate, candidates, isOpen);
    }

    public enum TypeOfVote {
        RANKEDVOTE("Ordinale", Arrays.asList(ResultMod.MAJORITY, ResultMod.ABS_MAJORITY)) {
            Vote getController(VotingSession session) {
                return new RankedVote(session);
            }

        },
        CATEGORICALVOTE("Categorico", Arrays.asList(ResultMod.MAJORITY, VotingSession.ResultMod.ABS_MAJORITY)) {

            Vote getController(VotingSession session) {
                return CategoricalVote.createCategoricalVote(session);
            }
        },
        PREFERENTIALVOTE("Categorico Preferenziale", Arrays.asList(VotingSession.ResultMod.MAJORITY, VotingSession.ResultMod.ABS_MAJORITY)) {

            com.ycv.youcanvote.controller.vote.Vote getController(VotingSession session) {
                return CategoricalVote.createPreferentialVote(session);
            }
        },

        REFERENDUM("Referendum", Arrays.asList(ResultMod.W_QUORUM, ResultMod.WO_QUORUM)) {
            com.ycv.youcanvote.controller.vote.Vote getController(VotingSession session) {
                return new Referendum(session);
            }
        };

        private final String name;
        private final List<ResultMod> mods;
        private static final Map<String, TypeOfVote> stringToEnum
                = new HashMap<>();

        static { // Initialize map from constant name to enum constant
            for (TypeOfVote vote : values())
                stringToEnum.put(vote.name, vote);
        }

        TypeOfVote(String name, List<ResultMod> mods) {
            this.name = name;
            this.mods = mods;
        }

        public String toString() {
            return name;
        }

        abstract Vote getController(VotingSession session);

        public List<ResultMod> getMods() {

            return new ArrayList<>(this.mods);
        }

        // Returns Operation for string, or null if string is invalid
        public static TypeOfVote fromString(String name) {
            return stringToEnum.get(name);
        }

    }
    public enum ResultMod {
        MAJORITY("Maggioranza"),
        ABS_MAJORITY("Maggioranza assoluta"),
        WO_QUORUM("Senza quorum"),
        W_QUORUM("Con quorum");

        private final String name;

        ResultMod(String name) {
            this.name = name;
        }

        private static final Map<String, ResultMod> stringToEnum
                = new HashMap<>();

        static { // Initialize map from constant name to enum constant
            for (ResultMod mod : values())
                stringToEnum.put(mod.name, mod);
        }

        public String toString() {
            return name;
        }

        public static ResultMod fromString(String name) {
            return stringToEnum.get(name);
        }
    }

}
