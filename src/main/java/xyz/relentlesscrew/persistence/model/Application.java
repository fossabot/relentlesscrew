package xyz.relentlesscrew.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class Application implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String discordUsername;

    @Column(unique = true)
    private String dauntlessUsername;

    private Date appliedOn;

    public Application(String discordUsername, String dauntlessUsername, Date appliedOn) {
        this.discordUsername = discordUsername;
        this.dauntlessUsername = dauntlessUsername;
        this.appliedOn = appliedOn;
    }

    public Application(String discordUsername, String dauntlessUsername) {
        this.discordUsername = discordUsername;
        this.dauntlessUsername = dauntlessUsername;
        this.appliedOn = new Date();
    }
}
