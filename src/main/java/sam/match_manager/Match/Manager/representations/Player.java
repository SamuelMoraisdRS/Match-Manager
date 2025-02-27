package sam.match_manager.Match.Manager.representations;

import sam.match_manager.Match.Manager.enums.PlayerStatus;

public record Player(
  String id,
  PlayerStatus playerStatus,
  int numberOfGuesses,
  int numberOfCorrects,
  int checksUsed,
  int submitsUsed
) {
}
