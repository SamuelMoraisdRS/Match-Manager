package sam.match_manager.Match.Manager.controller;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import sam.match_manager.Match.Manager.representations.MatchData;

import org.springframework.messaging.Message;


@Controller
public class MatchController {

  // TODO: Use a NoSQL DB
  // private final Map<UUID, MatchManager> matches = new HashMap<>();

  private final SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  public MatchController(SimpMessagingTemplate simpMessagingTemplate) {
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  @MessageMapping("/create")
  @SendTo("/topics/created")
  public String createMatch(Message<String> message) {
   String matchCode = UUID.randomUUID().toString().substring(0, 6);
   return matchCode;
  }

  @MessageMapping("/join")
  public void joinMatch(Message<String> joinMatchMessage) {
    MatchData stubMatch = new MatchData(1,4, 12);
    String matchCode = joinMatchMessage.getPayload();
    simpMessagingTemplate.convertAndSend("/topics/created/" + matchCode, stubMatch);
  }
}
