package xyz.relentlesscrew.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Rank implements Serializable {

    @Getter @Setter
    @Id
    private Long id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Long discordRoleId;
}
