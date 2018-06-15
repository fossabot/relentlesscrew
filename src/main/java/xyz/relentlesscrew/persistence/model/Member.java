package xyz.relentlesscrew.persistence.model;

import com.google.gson.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.relentlesscrew.persistence.DAO.RankDAO;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Type;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "bigint default 3")
    private Rank rank;

    public Member(Long discordId, String dauntlessUsername, Long discordRole) {
        this.discordId = discordId;
        this.dauntlessUsername = dauntlessUsername;
        this.rank = new RankDAO().findRankByDiscordRole(discordRole);
    }


    public static class MemberDeserializer implements JsonDeserializer<Member> {
        public Member deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return new Member(jsonObject.get("discordId").getAsLong(), jsonObject.get("dauntlessUsername").getAsString(), jsonObject.get("rank").getAsLong());
        }
    }
}
