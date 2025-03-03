package sam.match_manager.Match.Manager.representations;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Function;

import javax.swing.event.InternalFrameAdapter;

import sam.match_manager.Match.Manager.enums.MatchStatus;
import sam.match_manager.Match.Manager.enums.PlayerStatus;
import sam.match_manager.Match.Manager.messages.MatchData;

/*
 * Manages a match. Keeps tabs on the players and responds when one of them wins.
 */
public class MatchManager {

  final Integer RANDOM_NUM_FACTOR = 10000;

  private String matchCode;
  private Integer numOfPlayers;
  private final Map<String, Player> players = new HashMap<>();
  // TODO: Add leaderboard data structure
  private final MatchData matchData;
  private Instant matchStartInstant;
  private Instant matchEndInstant;
  private MatchStatus matchStatus = MatchStatus.IDLE;

  public MatchManager(Integer numOfPlayers, MatchData matchData) {
    this.numOfPlayers = numOfPlayers;
    this.matchData = matchData;
    generateMatchCode();
    matchStartInstant = Instant.now();
  }

  public void addPlayers(Map<String, Player> players) {
    this.players.putAll(players);
  }

  public void addPlayer(Player player) {
    players.put(player.getId(), player);
  }

  private Double calculateScore(Player player) {
    return ((player.getNumberOfCorrects() / player.getNumberOfGuesses()) - player.getChecksUsed()
        - (1.5 * player.getSubmitsUsed()));
  }

  public String getMatchCode() {
    return matchCode;
  }

  public Map<String, Player> getPlayers() {
   return players;
  }

  public MatchData getMatchData() {
    return this.matchData;
  }

  public Integer getRANDOM_NUM_FACTOR() {
    return this.RANDOM_NUM_FACTOR;
  }

  public void setMatchCode(String matchCode) {
    this.matchCode = matchCode;
  }

  public Integer getNumOfPlayers() {
    return this.numOfPlayers;
  }

  public void setNumOfPlayers(Integer numOfPlayers) {
    this.numOfPlayers = numOfPlayers;
  }

  public Instant getMatchStartInstant() {
    return this.matchStartInstant;
  }

  public void setMatchStartInstant(Instant matchStartInstant) {
    this.matchStartInstant = matchStartInstant;
  }

  public Instant getMatchEndInstant() {
    return this.matchEndInstant;
  }

  public void setMatchEndInstant(Instant matchEndInstant) {
    this.matchEndInstant = matchEndInstant;
  }

  public MatchStatus getMatchStatus() {
    return this.matchStatus;
  }

  public void setMatchStatus(MatchStatus matchStatus) {
    this.matchStatus = matchStatus;
  }

  private void generateMatchCode() {
    this.matchCode = UUID.randomUUID().toString().substring(0, 6);
  }
}
