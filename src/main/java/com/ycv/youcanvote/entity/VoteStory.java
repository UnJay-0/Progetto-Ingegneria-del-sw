package com.ycv.youcanvote.entity;

import com.ycv.youcanvote.model.Session;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity
@Table(name = "vote_story", schema = "public", catalog = "youcanvote")
@NamedQuery(name = "VoteStory.byUserAndVs", query = "SELECT e FROM VoteStory e WHERE e.userByCf.cf=?1 AND e.votingSessionByVsId.vsId=?2")
@NamedQuery(name = "VoteStory.byUser", query = "SELECT e FROM VoteStory e WHERE e.userByCf.cf=?1")
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

    public static void saveVoteStory(VoteStory vote) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(vote);
        entityManager.getTransaction().commit();
    }
    public static List<VoteStory> getVoteStoryByUser(String cf) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        List<VoteStory> voteList = entityManager.createNamedQuery("VoteStory.byUser", VoteStory.class)
                .setParameter(1, cf)
                .getResultList();
        entityManager.getTransaction().commit();
        return voteList;
    }

    public static  VoteStory getVoteStory(long userId, long votingSessionId) {
        EntityManager entityManager = Session.getInstance().getEntityManager();
        entityManager.getTransaction().begin();
        VoteStory vote = entityManager.createNamedQuery("VoteStory.byUserAndVs", VoteStory.class)
                .setParameter(1, userId)
                .setParameter(2, votingSessionId)
                .getSingleResult();
        entityManager.getTransaction().commit();
        return vote;
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
