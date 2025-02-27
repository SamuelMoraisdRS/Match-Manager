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

  public void addPlayer(String playerId, Player player) {
    players.put(player.id(), player);
  }

  private Double calculateScore(Player player) {
    return ((player.numberOfCorrects() / player.numberOfGuesses()) - player.checksUsed()
        - (1.5 * player.submitsUsed()));
  }

  public String getMatchCode() {
    return matchCode;
  }

  public MatchData getMatchData() {
    return this.matchData;
  }

  private void generateMatchCode() {
    this.matchCode = UUID.randomUUID().toString().substring(0, 6);
  }
}
