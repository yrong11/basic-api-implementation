package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
    public static List<RsEvent> rsList ;


  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEvent(@PathVariable int index){
    return ResponseEntity.ok(rsList.get(index - 1));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end){
    if (start != null && end != null)
      return ResponseEntity.ok(rsList.subList(start-1, end));
    return ResponseEntity.ok(rsList);
  }

  @PostMapping("/rs/add/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
    if (!UserController.userList.contains(rsEvent))
      UserController.userList.add(rsEvent.getUser());
    rsList.add(rsEvent);

    return ResponseEntity.created(null).build();

  }

  @GetMapping("/rs/delete/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index){
    rsList.remove(index - 1);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/rs/modify/{index}")
  public ResponseEntity modifyRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) throws JsonProcessingException {
//    ObjectMapper objectMapper = new ObjectMapper();
//    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
    if (!rsEvent.getEventName().equals(""))
      rsList.get(index - 1).setEventName(rsEvent.getEventName());
    if (!rsEvent.getKeyword().equals(""))
      rsList.get(index - 1).setKeyword(rsEvent.getKeyword());
    return ResponseEntity.ok().build();
  }

}
