package sam.match_manager.Match.Manager.messages;

import sam.match_manager.Match.Manager.enums.PlayerStatus;
import sam.match_manager.Match.Manager.representations.Player;

public record JoinMatchMessage(
  String matchCode,
  // TODO: Use a player record object
  String clientId,
  PlayerStatus playerStatus,
  int numberOfGuesses,
  int numberOfCorrects,
  int checksUsed,
  int submitsUsed
) {

}
