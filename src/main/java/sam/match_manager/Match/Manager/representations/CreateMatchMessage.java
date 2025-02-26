package sam.match_manager.Match.Manager.representations;

import sam.match_manager.Match.Manager.enums.MatchType;

public record CreateMatchMessage(
  String sender,
  Integer boardId,
  MatchType matchType,
  Integer numberOfPlayers,
  Integer numberOfChecks,
  Integer numberOfSubmits // Set it to max if match isn't retricted
) {

}
