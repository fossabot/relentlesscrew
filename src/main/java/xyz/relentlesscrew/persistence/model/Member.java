package xyz.relentlesscrew.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.relentlesscrew.persistence.DAO.RankDAO;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Data
@Entity
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long discordId;

    @Column(unique = true)
    private String dauntlessUsername;

    @ManyToOne
    @JoinColumn(columnDefinition = "bigint default 3")
    private Rank rank;

    public Member(Long discordId, String dauntlessUsername, Long discordRole) {
        this.discordId = discordId;
        this.dauntlessUsername = dauntlessUsername;
        this.rank = new RankDAO().findRankByDiscordRole(discordRole);
    }
}
