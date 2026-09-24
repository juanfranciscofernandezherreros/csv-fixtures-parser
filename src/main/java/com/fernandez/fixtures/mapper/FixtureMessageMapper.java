package com.fernandez.fixtures.mapper;

import com.fernandez.fixtures.avro.FixtureKey;
import com.fernandez.fixtures.avro.FixtureValue;
import com.fernandez.fixtures.dto.FixtureDTO;
import org.springframework.stereotype.Component;

@Component
public class FixtureMessageMapper {
    public FixtureKey key(FixtureDTO d) {
        return FixtureKey.newBuilder()
                .setMatchId(d.matchId())
                .setCountry(d.country())
                .setCompetition(d.competition())
                .build();
    }

    public FixtureValue value(FixtureDTO d) {
        return FixtureValue.newBuilder()
                .setMatchId(d.matchId())
                .setCountry(d.country())
                .setCompetition(d.competition())
                .setEventTime(d.eventTime())
                .setHomeTeam(d.homeTeam())
                .setAwayTeam(d.awayTeam())
                .build();
    }
}
