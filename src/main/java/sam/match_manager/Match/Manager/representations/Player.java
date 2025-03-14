package sam.match_manager.Match.Manager.representations;

import java.util.UUID;
import java.util.Comparator;

import sam.match_manager.Match.Manager.enums.PlayerStatus;

public class Player {
  private final String id;
  private PlayerStatus playerStatus;
  private int numberOfGuesses;
  private int numberOfCorrects;
  private int checksUsed;
  private int submitsUsed;
  private double score;

  public static final Comparator<Player> compare = (playerA, playerB) -> Double.compare(playerA.getScore(),
      playerB.getScore());

  public Player(PlayerStatus playerStatus, int numberOfGuesses, int numberOfCorrects,
      int checksUsed, int submitsUsed) {
    this.id = UUID.randomUUID().toString().substring(0, 6);
    this.playerStatus = playerStatus;
    this.numberOfGuesses = numberOfGuesses;
    this.numberOfCorrects = numberOfCorrects;
    this.checksUsed = checksUsed;
    this.submitsUsed = submitsUsed;
  }

  public Player(String id, PlayerStatus playerStatus, int numberOfGuesses, int numberOfCorrects,
      int checksUsed, int submitsUsed) {
    this.id = id;
    this.playerStatus = playerStatus;
    this.numberOfGuesses = numberOfGuesses;
    this.numberOfCorrects = numberOfCorrects;
    this.submitsUsed = submitsUsed;
    this.checksUsed = checksUsed;
  }

  public String getId() {
    return this.id;
  }

  public PlayerStatus getPlayerStatus() {
    return this.playerStatus;
  }

  public void setPlayerStatus(PlayerStatus playerStatus) {
    this.playerStatus = playerStatus;
  }

  public int getNumberOfGuesses() {
    return this.numberOfGuesses;
  }

  public void setNumberOfGuesses(int numberOfGuesses) {
    this.numberOfGuesses = numberOfGuesses;
  }

  public int getNumberOfCorrects() {
    return this.numberOfCorrects;
  }

  public void setNumberOfCorrects(int numberOfCorrects) {
    this.numberOfCorrects = numberOfCorrects;
  }

  public int getChecksUsed() {
    return this.checksUsed;
  }

  public void setChecksUsed(int checksUsed) {
    this.checksUsed = checksUsed;
  }

  public int getSubmitsUsed() {
    return this.submitsUsed;
  }

  public void setSubmitsUsed(int submitsUsed) {
    this.submitsUsed = submitsUsed;
  }

  public double getScore() {
    return score;
  }

  public void setScore(double score) {
    this.score = score;
  }
}
