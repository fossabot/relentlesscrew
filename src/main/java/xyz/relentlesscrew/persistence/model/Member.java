package xyz.relentlesscrew.persistence.model;

import lombok.Getter;
import lombok.Setter;
import xyz.relentlesscrew.persistence.DAO.RankDAO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class Member implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter @Setter
    @Column(unique = true)
    private Long discordId;

    @Getter @Setter
    @Column(unique = true)
    private String dauntlessUsername;

    @Getter @Setter
    @ManyToOne
    private Rank rank;

    public Member(Long discordId, String dauntlessUsername, Long discordRole) {
        this.discordId = discordId;
        this.dauntlessUsername = dauntlessUsername;
        this.rank = new RankDAO().findRankByDiscordRole(discordRole);
    }

    public Member() {
    }
}
