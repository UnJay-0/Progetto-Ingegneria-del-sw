package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.controller.vote.*;
import com.ycv.youcanvote.controller.vote.CategoricalVoting;
import com.ycv.youcanvote.controller.vote.Voting;
import com.ycv.youcanvote.model.Answer;
import com.ycv.youcanvote.model.Candidate;
import com.ycv.youcanvote.entity.Party;
import com.ycv.youcanvote.model.Results;
import com.ycv.youcanvote.model.Session;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    public Voting getVote() {
        String typeOfVote = type.split(";")[0];
        return TypeOfVote.fromString(typeOfVote).getController(this);
    }

    public TypeOfVote getTypeOfVote() {
        return TypeOfVote.fromString(type.split(";")[0]);
    }

    public  ResultMod getResultMod() {
        return ResultMod.fromString(type.split(";")[1]);
    }

    public List<Candidate> getCandidatesList() {

        if(this.getTypeOfVote() == TypeOfVote.REFERENDUM) {
            return Arrays.asList(new Answer("Favorevole"), new Answer("Contrario"));
        }
        EntityManager entityManager = Session.getInstance().getEntityManager();
        List<Candidate> candidatesList = new ArrayList<>();
        for(String candidateToStr : candidates.split(";")) {

            Long id = Long.parseLong(candidateToStr.split("\n")[1].split(" ")[1]);


            try {
                entityManager.getTransaction().begin();

                TypedQuery<Individual> getIndividual = entityManager.createNamedQuery(
                        "Individual.byId", Individual.class);
                getIndividual.setParameter(1, id);

                if(getIndividual.getResultList().size() != 0) {
                    candidatesList.add(getIndividual.getSingleResult());
                } else {
                    TypedQuery<Party> getParty = entityManager.createNamedQuery(
                            "Party.byId", Party.class);
                    getParty.setParameter(1, id);
                    candidatesList.add(getParty.getSingleResult());
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
        entityManager.getTransaction().begin();
        entityManager.createQuery("UPDATE VotingSession set isOpen=false WHERE vsId=?1")
                .setParameter(1, this.vsId).executeUpdate();
        entityManager.getTransaction().commit();
        this.isOpen = false;
    }

    /**
     *
     * @return the number of votes already submitted for this voting session
     */
    public int nVote() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        TypedQuery<Vote> getVotes = entityManager.createNamedQuery(
                "Vote.byVotingSession",
                Vote.class);
        getVotes.setParameter(1, this.vsId);
        return getVotes.getResultList().size();
    }

    /**
     *
     * @return the number of blank votes already submitted for this voting session
     */
    public int nBlankVotes() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        TypedQuery<Vote> getBlankVotes = entityManager.createNamedQuery("Vote.getBlankVotes", Vote.class);
        getBlankVotes.setParameter(1, this.vsId);
        getBlankVotes.setParameter(2, "Bianca");
        int n = getBlankVotes.getResultList().size();
        entityManager.getTransaction().commit();
        return n;
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
     * @return the date of creation of the voting session.
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

    public static List<VotingSession> getVotingSession() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        List<VotingSession> votingSessionList = entityManager.createNamedQuery("VotingSession.all", VotingSession.class).getResultList();
        entityManager.getTransaction().commit();
        return votingSessionList;
    }

    public static VotingSession getVotingSessionById(long id) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();

        VotingSession votingSession = entityManager.createNamedQuery("VotingSession.byId", VotingSession.class)
                .setParameter(1, id)
                .getSingleResult();


        entityManager.getTransaction().commit();

        return  votingSession;
    }

    public static List<VotingSession> getOpenVotingSession() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        List<VotingSession> votingSessionList = entityManager.createNamedQuery("VotingSession.ifOpen", VotingSession.class).getResultList();
        entityManager.getTransaction().commit();
        return votingSessionList;
    }

    public static List<VotingSession> getCloseVotingSession() {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        List<VotingSession> votingSessionList = entityManager.createNamedQuery("VotingSession.ifClose", VotingSession.class).getResultList();
        entityManager.getTransaction().commit();
        return votingSessionList;
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

            Voting getController(VotingSession session) {
                return new RankedVoting(session);
            }


            @Override
            public List<Results> getResults(VotingSession session, ResultMod mod) {

                EntityManager entityManager = Session.getInstance().getEntityManager();
                List<Integer> weights = Arrays.asList(1, 4, 7, 10, 0);
                Map<Candidate, Integer> ranking = new HashMap<>();
                List<Candidate> candidates = session.getCandidatesList();
                Candidate blank = new Answer("Bianca");
                List<Vote> votes = Vote.getVotesByVsId(session.vsId);
                candidates.forEach(candidate -> {
                    for (Vote v : votes) {
                        String selection = v.getSelection();
                        if (selection.equals("Bianca")) {
                            break;
                        }
                        int i = 0;
                        for (String votedCandidate : selection.split(";")) {
                            Long id = Long.parseLong(votedCandidate.split("\n")[1].split(" ")[1]);

                            try {
                                entityManager.getTransaction().begin();
                                Candidate temp;
                                TypedQuery<Individual> getIndividual = entityManager.createNamedQuery(
                                        "Individual.byId", Individual.class);
                                getIndividual.setParameter(1, id);

                                if(getIndividual.getResultList().size() != 0) {
                                    temp = getIndividual.getSingleResult();
                                } else {
                                    TypedQuery<Party> getParty = entityManager.createNamedQuery(
                                            "Party.byId", Party.class);
                                    getParty.setParameter(1, id);
                                    temp = getParty.getSingleResult();
                                }

                                if (temp.equals(candidate)) {
                                    if (ranking.containsKey(candidate)) {
                                        ranking.replace(candidate, ranking.get(candidate) + weights.get(i));
                                    } else
                                        ranking.put(temp, weights.get(i));
                                }
                            } finally {
                                entityManager.getTransaction().commit();
                                if (i < 4) {
                                    i++;
                                }
                            }
                        }
                    }
                });

                Map<Candidate, Integer> result = ranking.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                Map.Entry<Candidate, Integer> entry = result.entrySet().iterator().next();
                Candidate winner = null;
                if (mod.isAbsolute(entry.getValue(), session.nVote())) {
                    winner = entry.getKey();
                }
                return List.of(new Results(session, ranking, winner));
            }

        },
        CATEGORICALVOTE("Categorico", Arrays.asList(ResultMod.MAJORITY, VotingSession.ResultMod.ABS_MAJORITY)) {

            Voting getController(VotingSession session) {
                return CategoricalVoting.createCategoricalVote(session);
            }

            @Override
            public List<Results> getResults(VotingSession session, ResultMod mod) {

                EntityManager entityManager = Session.getInstance().getEntityManager();
                Map<Candidate, Integer> ranking = new HashMap<>();
                List<Candidate> candidates = session.getCandidatesList();
                Candidate blank = new Answer("Bianca");
                List<Vote> votes = Vote.getVotesByVsId(session.vsId);
                candidates.forEach(candidate -> {
                    for (Vote v : votes) {
                        String selection = v.getSelection();
                        if (selection.equals("Bianca")) {
                            break;
                        }
                        Long id = Long.parseLong(selection.split("\n")[1].split(" ")[1]);
                        try {
                            entityManager.getTransaction().begin();
                            Candidate temp;
                            TypedQuery<Individual> getIndividual = entityManager.createNamedQuery(
                                    "Individual.byId", Individual.class);
                            getIndividual.setParameter(1, id);

                            if(getIndividual.getResultList().size() != 0) {
                                temp = getIndividual.getSingleResult();
                            } else {
                                TypedQuery<Party> getParty = entityManager.createNamedQuery(
                                        "Party.byId", Party.class);
                                getParty.setParameter(1, id);
                                temp = getParty.getSingleResult();
                            }

                            if (temp.equals(candidate)) {
                                if (ranking.containsKey(candidate)) {
                                    ranking.replace(candidate, ranking.get(candidate) + 1);
                                } else
                                    ranking.put(temp, 1);
                            }
                        } finally {
                            entityManager.getTransaction().commit();
                        }
                    }
                });

                Map<Candidate, Integer> result = ranking.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                Map.Entry<Candidate, Integer> entry = result.entrySet().iterator().next();
                Candidate winner = null;
                if (mod.isAbsolute(entry.getValue(), session.nVote())) {
                    winner = entry.getKey();
                }
                return List.of(new Results(session, result, winner));
            }
        },
        PREFERENTIALVOTE("Categorico Preferenziale", Arrays.asList(VotingSession.ResultMod.MAJORITY, VotingSession.ResultMod.ABS_MAJORITY)) {

            Voting getController(VotingSession session) {
                return CategoricalVoting.createPreferentialVote(session);
            }

            @Override
            public List<Results> getResults(VotingSession session, ResultMod mod) {
                Map<Candidate, Integer> ranking = new HashMap<>();
                Map<Candidate, Integer> ranking2 = new HashMap<>();
                List<Candidate> candidates = session.getCandidatesList();
                List<Vote> votes = Vote.getVotesByVsId(session.vsId);
                candidates.forEach(candidate -> {
                    for (Vote v : votes) {
                        String selection = v.getSelection();
                        if (selection.equals("Bianca")) {
                            break;
                        }
                        long id = Long.parseLong(selection.split("\n")[1].split(" ")[1].split(";")[0]);

                        Candidate temp = Party.getPartyById(id);
                        if (temp.equals(candidate)) {
                            if (ranking.containsKey(candidate)) {
                                ranking.replace(candidate, ranking.get(candidate) + 1);
                            } else
                                ranking.put(temp, 1);
                        }
                    }
                });

                Map<Candidate, Integer> result = ranking.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                Map.Entry<Candidate, Integer> entry = result.entrySet().iterator().next();
                Candidate winner = null;
                if (mod.isAbsolute(entry.getValue(), session.nVote())) {
                    winner = entry.getKey();
                }
                if (winner == null) {
                    return List.of(new Results(session, ranking, null));
                } else {
                    List<Individual> members = Individual.getIndividualByPartyId(winner.getId());
                    for(Vote v : votes) {
                        String selection = v.getSelection();
                        if (!selection.equals("Bianca")) {
                            for(Individual member: members) {
                                for (String votedCandidate :
                                        List.of(selection.split(";"))
                                                .subList(1, selection.split(";").length))
                                {
                                    long id = Long.parseLong(votedCandidate.split("\n")[1].split(" ")[1]);
                                    Individual temp = Individual.getIndividualById(id);
                                    if (temp.equals(member)) {
                                        if (ranking2.containsKey(member)) {
                                            ranking2.replace(member, ranking2.get(member) + 1);
                                        } else
                                            ranking2.put(temp, 1);
                                    }

                                }
                            }

                        }
                    }

                    Map<Candidate, Integer> resultMembers = ranking2.entrySet()
                            .stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue,
                                    (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                    Map.Entry<Candidate, Integer> entryMembers = resultMembers.entrySet().iterator().next();
                    System.out.println(entryMembers.getKey());
                    return Arrays.asList(new Results(session, ranking, winner), new Results(session, resultMembers, entryMembers.getKey()));
                }
            }
        },

        REFERENDUM("Referendum", Arrays.asList(ResultMod.W_QUORUM, ResultMod.WO_QUORUM)) {
            Voting getController(VotingSession session) {
                return new Referendum(session);
            }

            @Override
            public List<Results> getResults(VotingSession session, ResultMod mod) {
                Map<Candidate, Integer> ranking = new HashMap<>();
                List<Candidate> candidates = session.getCandidatesList();
                List<Vote> votes = Vote.getVotesByVsId(session.vsId);
                System.out.println(votes);
                candidates.forEach(candidate -> {
                    for (Vote v : votes) {
                        String selection = v.getSelection();
                        if (selection.equals(candidate.name())) {
                            if (ranking.containsKey(candidate)) {
                                ranking.replace(candidate, ranking.get(candidate) + 1);
                            } else
                                ranking.put(candidate, 1);
                        }
                    }
                });

                Map<Candidate, Integer> result = ranking.entrySet()
                        .stream()
                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                Map.Entry<Candidate, Integer> entry = result.entrySet().iterator().next();

                return List.of(new Results(session, result, entry.getKey()));
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

        abstract Voting getController(VotingSession session);

        public abstract List<Results> getResults(VotingSession session, ResultMod mod);

        public List<ResultMod> getMods() {
            return new ArrayList<>(this.mods);
        }

        // Returns Operation for string, or null if string is invalid
        public static TypeOfVote fromString(String name) {
            return stringToEnum.get(name);
        }

    }
    public enum ResultMod {
        MAJORITY("Maggioranza") {
            /**
             * @param nVotes number of votes of the candidate with the highest number of votes.
             * @return true.
             */
            @Override
            boolean isAbsolute(int nVotes, int totalVotes) {
                return true;
            }
        },
        ABS_MAJORITY("Maggioranza assoluta"){
            /**
             * @param nVotes number of votes of the candidate with the highest number of votes.
             * @return true if nVotes is higher or equal of 50%+1 of the total votes.
             */
            @Override
            boolean isAbsolute(int nVotes, int totalVotes) {
                return nVotes >= (totalVotes * 50 / 100) + 1;
            }
        },
        WO_QUORUM("Senza quorum"){
            /**
             * @param nVotes number of submitted vote of the referendum.
             * @return true.
             */
            @Override
            boolean isAbsolute(int nVotes, int totalVotes) {
                return true;
            }
        },
        W_QUORUM("Con quorum"){
            /**
             * @param nVotes number of submitted vote of the referendum
             * @return true if the number of submitted vote is higher or equal of 50%+1 of the people able top vote,
             *          false otherwise.
             */
            @Override
            boolean isAbsolute(int nVotes, int totalVotes) {
                return nVotes >= (totalVotes * 50 / 100) + 1;
            }
        };

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

        abstract boolean isAbsolute(int nVotes, int totalVotes);

        public String toString() {
            return name;
        }

        public static ResultMod fromString(String name) {
            return stringToEnum.get(name);
        }
    }

}
