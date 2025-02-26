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

/*
 * Manages a match. Keeps tabs on the players and responds when one of them wins.
 */
public class MatchManager {

  final Integer RANDOM_NUM_FACTOR = 10000;

  private String matchCode;
  private Integer numOfPlayers;
  private final Map<String, Player> players = new HashMap<>();
  // private final List leaderboard = new TreeMap<>();
  private Integer boardId;
  private Integer numberOfChecks;
  private Integer numberOfSubmits;
  private Instant matchStartInstant;
  private Instant matchEndInstant;
  private MatchStatus matchStatus = MatchStatus.IDLE;
  private MatchData matchData;

  public MatchManager(Integer numOfPlayers, Integer numberOfChecks, Integer numberOfSubmits,
      Integer boardId) {
    this.numOfPlayers = numOfPlayers;
    this.numberOfChecks = numberOfChecks;
    this.numberOfSubmits = numberOfSubmits;
    this.boardId = boardId;
    generateMatchCode();
    matchStartInstant = Instant.now();
  }

  // public void checkPlayersState() {
  // this.players.values().stream().forEach((player) -> {
  // if (player.playerStatus() == PlayerStatus.WINNER) {
  // calculateScore(player);
  // }
  // });
  // }

  // public void registerWinner(Player winner) {
  //   players.put(winner.id(), winner);
  //   addToLeaderboard(winner, calculateScore(winner));
  // }

  // public void endGame() {
  //   leaderboard.sort
  // }

  public void addPlayers(Map<String, Player> players) {
    this.players.putAll(players);
  }

  public void addPlayer(Player player) {
    players.put(player.id(), player);
  }

  private Double calculateScore(Player player) {
    return ((player.numberOfCorrects() / player.numberOfGuesses()) - player.checksUsed()
        - (1.5 * player.submitsUsed()));
  }

  public void setMatchData(MatchData matchData) {
    this.matchData = matchData;
  }

  public String getMatchCode() {
    return matchCode;
  }

  private void generateMatchCode() {
    this.matchCode = UUID.randomUUID().toString().substring(0, 6);
  }
}
