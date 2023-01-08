package com.ycv.youcanvote.entity;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * OVERVIEW: The instances of Vote represent votes expressed on a specific
 * voting session. They are defined by a selection of candidates and the
 * referred voting session.
 * <br/><br/>
 *
 * REP INV:
 * <br/>
 * selection != null && selection != ""
 * <br/>
 * votingSession != null
 * <br/>
 *
 * ABS FUN:
 * ABS(Vote) -> {selection, abs(votingSession)}
 * <br/>
 *
 */
@Entity
@NamedQuery(name = "Vote.byVotingSession", query = "SELECT e FROM Vote e WHERE e.votingSessionByVsId.vsId=?1")
public class Vote {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vote_generator")
    @SequenceGenerator(name="vote_generator", sequenceName = "vote_sequence", allocationSize = 1)
    @Id
    @Column(name = "vote_id")
    private long voteId;
    @Basic
    @Column(name = "selection")
    private String selection;
    @ManyToOne
    @JoinColumn(name = "vs_id", referencedColumnName = "vs_id")
    private VotingSession votingSessionByVsId;

    public Vote() {

    }

    /**
     *
     * @param selection expressed by the voter.
     * @param session of belonging.
     */
    public Vote(@NotNull String selection , @NotNull VotingSession session) {
        if (selection.equals("")) {
            throw new IllegalArgumentException();
        }
        this.selection = selection;
        this.votingSessionByVsId = session;
    }

    /**
     *
     * @return the id of the vote.
     */
    public long getVoteId() {
        return voteId;
    }

    private void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    /**
     *
     * @return selection expressed by the voter.
     */
    public String getSelection() {
        return selection;
    }

    private void setSelection(String selection) {
        this.selection = selection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return voteId == vote.voteId && Objects.equals(selection, vote.selection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(voteId, selection);
    }

    /**
     *
     * @return the voting session of belonging.
     */
    public VotingSession getVotingSessionByVsId() {
        return votingSessionByVsId;
    }

    private void setVotingSessionByVsId(VotingSession votingSessionByVsId) {
        this.votingSessionByVsId = votingSessionByVsId;
    }
}
