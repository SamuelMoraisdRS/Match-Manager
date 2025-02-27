package sam.match_manager.Match.Manager.messages;

import sam.match_manager.Match.Manager.representations.Player;

public record JoinMatchMessage(
  String matchCode,
  Player playerData
) {

}
