package com.example.demo.socialMedia;

import com.example.demo.team.Team;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="SocialMedia")
public class SocialMedia {
    @Id
    @SequenceGenerator(
            name = "social_media_sequence",
            sequenceName = "social_media_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "social_media_sequence"
    )
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    private String facebookUrl;
    private String twitterUrl;
    private String instagramUrl;

    public SocialMedia(Team team, String facebookUrl, String twitterUrl, String instagramUrl){
        this.team = team;
        this.facebookUrl = facebookUrl;
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
    }
}
