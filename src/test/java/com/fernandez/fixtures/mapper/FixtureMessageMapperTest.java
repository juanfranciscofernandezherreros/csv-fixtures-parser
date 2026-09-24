package com.fernandez.fixtures.mapper;
import com.fernandez.fixtures.dto.FixtureDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class FixtureMessageMapperTest {
 @Test void mapsFixture(){
  var d=new FixtureDTO("m1","usa","nba","2026-09-24T20:00","A","B");
  var mapper=new FixtureMessageMapper();
  var key=mapper.key(d); var value=mapper.value(d);
  assertEquals("m1",key.getMatchId());
  assertEquals("usa",value.getCountry());
  assertEquals("nba",value.getCompetition());
  assertEquals("A",value.getHomeTeam());
 }
}
