package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class RsController {
    public static List<RsEvent> rsList ;
    @Autowired
    public RsEventService rsControllerService;


  @GetMapping("/rs/{index}")
  public ResponseEntity getRsEvent(@PathVariable int index){
    return ResponseEntity.ok(rsControllerService.getRsEvent(index));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size){
    return ResponseEntity.ok(rsControllerService.getRsEventBetween(page, size));
  }

  @PostMapping("/rs/add/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {

    boolean flag = rsControllerService.addRsEvent(rsEvent);
    return flag ? ResponseEntity.created(null).build() : ResponseEntity.badRequest().build();

  }

  @GetMapping("/rs/delete/{index}")
  public ResponseEntity deleteRsEvent(@PathVariable int index){
    rsList.remove(index - 1);
    return ResponseEntity.ok().header("index",index+"").build();
  }

  @PatchMapping("/rs/modify/{rsId}")
  public ResponseEntity updateRsEvent(@PathVariable int rsId, @RequestBody RsEvent rsEvent) throws JsonProcessingException {
    boolean flag = rsControllerService.updateRsEvent(rsId,rsEvent);
    if (flag)
      return ResponseEntity.ok().header("index",rsId+"").build();
    else
      return ResponseEntity.badRequest().build();
  }

}
