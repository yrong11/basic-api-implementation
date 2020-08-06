package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {
    public static List<RsEvent> rsList ;
    public static RsEventService rsControllerService = new RsEventService();


  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEvent(@PathVariable int index){
    return ResponseEntity.ok(rsControllerService.getRsEvent(index));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end){
    if (start != null && end != null)
      return ResponseEntity.ok(rsControllerService.getRsEventBetween(start, end));
    return ResponseEntity.ok(rsList);
  }

  @PostMapping("/rs/add/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
    rsControllerService.addRsEvent(rsEvent);

    return ResponseEntity.created(null).build();

  }

  @GetMapping("/rs/delete/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index){
    rsList.remove(index - 1);
    return ResponseEntity.ok().header("index",index+"").build();
  }

  @PostMapping("/rs/modify/{index}")
  public ResponseEntity modifyRsEvent(@PathVariable int index, @RequestBody RsEvent rsEvent) throws JsonProcessingException {
    rsControllerService.modifyRsEvent(index,rsEvent);
    return ResponseEntity.ok().header("index",index+"").build();
  }

}
