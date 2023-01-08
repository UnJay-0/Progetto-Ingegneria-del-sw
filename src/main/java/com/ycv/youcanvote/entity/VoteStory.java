package com.ycv.youcanvote.entity;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "vote_story", schema = "public", catalog = "youcanvote")
@NamedQuery(name = "VoteStory.ByUserAndVs", query = "SELECT e FROM VoteStory e WHERE e.userByCf.cf=?1 AND e.votingSessionByVsId.vsId=?2")
@NamedQuery(name = "VoteStory.ByUser", query = "SELECT e FROM VoteStory e WHERE e.userByCf.cf=?1")
public class VoteStory {
    @ManyToOne
    @Id
    @JoinColumn(name = "cf", referencedColumnName = "cf", nullable = false)
    private User userByCf;
    @ManyToOne
    @Id
    @JoinColumn(name = "vs_id", referencedColumnName = "vs_id", nullable = false)
    private VotingSession votingSessionByVsId;

    public VoteStory() {

    }

    public VoteStory(@NotNull User user, @NotNull VotingSession session) {
        this.userByCf = user;
        this.votingSessionByVsId = session;
    }

    public User getUserByCf() {
        return userByCf;
    }

    private void setUserByCf(User userByCf) {
        this.userByCf = userByCf;
    }

    public VotingSession getVotingSessionByVsId() {
        return votingSessionByVsId;
    }

    private void setVotingSessionByVsId(VotingSession votingSessionByVsId) {
        this.votingSessionByVsId = votingSessionByVsId;
    }
}
