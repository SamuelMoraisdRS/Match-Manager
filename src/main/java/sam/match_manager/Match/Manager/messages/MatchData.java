package sam.match_manager.Match.Manager.messages;

import java.time.Instant;

public record MatchData(
  Integer boardId,
  Integer numberOfChecks,
  Integer numberOfSubmits
) {

}
