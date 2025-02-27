package sam.match_manager.Match.Manager.messages;

import sam.match_manager.Match.Manager.enums.MatchType;


// TODO: Aggregate a MatchData record here. This enhances the complexity at a front end level, but reduces it on backend
public record CreateMatchMessage(
  String sender,
  Integer boardId,
  MatchType matchType,
  Integer numberOfPlayers,
  Integer numberOfChecks,
  Integer numberOfSubmits // Set it to max if match isn't retricted
) {

}
