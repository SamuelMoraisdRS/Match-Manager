package sam.match_manager.Match.Manager.representations;

import java.time.Duration;
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

  private final String matchCode;
  private final Integer numOfPlayers;
  private final Map<String, Player> players = new HashMap<>();
  List<Player> leaderBoard;
  private final MatchData matchData;
  private Instant matchStartInstant;
  private Instant matchEndInstant;
  private Long matchDuration;
  private MatchStatus matchStatus = MatchStatus.IDLE;
  private int remainingPlayers;
  private Supplier<String> matchCodeGenerator = () -> UUID.randomUUID().toString().substring(0, 6);

  public MatchManager(Integer numOfPlayers, MatchData matchData) {
    this.numOfPlayers = numOfPlayers;
    this.matchData = matchData;
    this.matchCode = matchCodeGenerator.get();
    remainingPlayers = numOfPlayers;
    matchStartInstant = Instant.now();
  }

  public void addPlayer(Player player) {
    players.put(player.getId(), player);
  }

  public void updatePlayer(Player newPlayerData) {
    if (!players.containsKey(newPlayerData.getId()) || isPlayerFinished(newPlayerData.getId())) {
      return;
    }
    addPlayer(newPlayerData);
    if (isPlayerFinished(newPlayerData.getId())) {
      players.get(newPlayerData.getId()).setScore(calculatePlayerScore(newPlayerData));
      remainingPlayers--;
    }
    checkEndGame();
  }

  private void checkEndGame() {
    if(isMatchOver()) {
      endGame();
    }
  }

  private void endGame() {
    matchStatus = MatchStatus.END;
    matchEndInstant = Instant.now();
    matchDuration = Duration.between(matchStartInstant, matchEndInstant).toSeconds();
    this.leaderBoard = players.values().stream().sorted(Player.compare).toList();
  }

  private Double calculatePlayerScore(Player player) {
    return ((player.getNumberOfCorrects() / player.getNumberOfGuesses()) - player.getChecksUsed()
        - (1.5 * player.getSubmitsUsed()));
  }

  public boolean isMatchOver() {
    return remainingPlayers == 0;
  }

  private boolean isPlayerFinished(String playerId) {
    return players.get(playerId).getPlayerStatus() == PlayerStatus.WINNER;
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

}
