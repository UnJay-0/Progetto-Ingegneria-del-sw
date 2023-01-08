package com.ycv.youcanvote.model;

import com.ycv.youcanvote.controller.vote.CategoricalVote;
import com.ycv.youcanvote.controller.vote.RankedVote;
import com.ycv.youcanvote.controller.vote.Referendum;
import com.ycv.youcanvote.controller.vote.Vote;

import java.time.LocalDate;
import java.util.*;

/**
 * OVERVIEW:
 * VotingSession represent a session of voting with name, description
 * and candidates to choose. The instances of VotingSession are immutable.
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
public class VotingSession {
    private final String name;
    private final String description;

///    private final TypeOfVote type;

    private final LocalDate dateOfCreation;

    private final List<Candidate> candidates;

    /**
     * Constructs a Voting session as specified.
     *
     * @param name name of the voting session, can't be null or empty.
     * @param description description of the voting session, can't be null or empty.
     * @param choices listed candidates that voters are able to choose, can't be null or contain null.
     * @throws IllegalArgumentException if name or description are empty
     * @throws NullPointerException if any of the parameters are null or contain null.
     */
    public VotingSession(String name, String description, List<Candidate> choices, String type){
        if (name.equals("") ) {
            throw new IllegalArgumentException("Il nome non può essere vuoto");
        }
        this.name = Objects.requireNonNull(name);
        if (description.equals("") ) {
            throw new IllegalArgumentException("La descrizione non può essere vuota");
        }
        this.dateOfCreation = LocalDate.now();
        this.description = Objects.requireNonNull(description);
        this.candidates = new ArrayList<>(choices);
//        this.type = TypeOfVote.fromString(type);
    }


//    public Vote getVote() {
//        return type.getController(this);
//    }

    public List<Candidate> getCandidates() {
        return new ArrayList<>(candidates);
    }


    /**
     *
     * @return the name of this voting session.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the description of this voting session.
     */
    public String getDescription() {
        return description;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * This method close this voting session impeding any other votes to be
     * accepted.
     *
     */
    public void close() {

    }

    public boolean isOpen() {
        return true;
    }

    public int nVote(){
        return 0;
    }

    public String toString() {
        return String.format("%s\n%s", this.name, this.description);
    }

//    public enum TypeOfVote {
//        RANKEDVOTE("Ordinale", Arrays.asList(ResultMod.MAJORITY, ResultMod.ABS_MAJORITY)) {
//            Vote getController(VotingSession session) {
//                return new RankedVote(session);
//            }
//
//        };
////        CATEGORICALVOTE("Categorico", Arrays.asList(ResultMod.MAJORITY, ResultMod.ABS_MAJORITY)) {
////
////            Vote getController(VotingSession session) {
////                return CategoricalVote.createCategoricalVote(session);
////            }
////        },
////        PREFERENTIALVOTE("Categorico Preferenziale", Arrays.asList(ResultMod.MAJORITY, ResultMod.ABS_MAJORITY)) {
////
//////            Vote getController(VotingSession session) {
//////                return CategoricalVote.createPreferentialVote(session);
//////            }
////        },
//
////        REFERENDUM("Referendum", Arrays.asList(ResultMod.W_QUORUM, ResultMod.WO_QUORUM)) {
////            Vote getController(VotingSession session) {
////                return new Referendum(session);
////            }
////        };
//
//        private final String name;
//        private final List<ResultMod> mods;
//        private static final Map<String, TypeOfVote> stringToEnum
//                = new HashMap<>();
//
//        static { // Initialize map from constant name to enum constant
//            for (TypeOfVote vote : values())
//                stringToEnum.put(vote.name, vote);
//        }
//
//        TypeOfVote(String name, List<ResultMod> mods) {
//            this.name = name;
//            this.mods = mods;
//        }
//
//        public String toString() {
//            return name;
//        }
//
//        abstract Vote getController(VotingSession session);
//
//        public List<ResultMod> getMods() {
//
//            return new ArrayList<>(this.mods);
//        }
//
//        // Returns Operation for string, or null if string is invalid
//        public static TypeOfVote fromString(String name) {
//            return stringToEnum.get(name);
//        }
//
//    }
    public enum ResultMod {
        MAJORITY("Maggioranza"),
        ABS_MAJORITY("Maggioranza assoluta"),
        WO_QUORUM("Senza quorum"),
        W_QUORUM("Con quorum");

        private final String name;

        ResultMod(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }
}
