package xyz.relentlesscrew.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Application implements Serializable {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Getter @Setter
    @Column(unique = true)
    private String discordUsername;

    @Getter @Setter
    @Column(unique = true)
    private String dauntlessUsername;

    @Getter
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

    public Application() {
    }
}
