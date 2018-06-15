package xyz.relentlesscrew.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
public class Rank implements Serializable {

    @Id
    private Long id;

    private String name;
    private Long discordRoleId;
}
