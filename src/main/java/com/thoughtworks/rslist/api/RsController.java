package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.dto.AppConf;
import com.thoughtworks.rslist.service.RsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@ComponentScan("com.thoughtworks.rslist")
public class RsController {

  @Autowired
  public RsEventService rsControllerService;


  @GetMapping("/rs/{rsId}")
  public ResponseEntity getRsEvent(@PathVariable int rsId){
    return ResponseEntity.ok(rsControllerService.getRsEvent(rsId));
  }

  @GetMapping("/rs/list")
  public ResponseEntity getRsEventBetween(@RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer size){
    return ResponseEntity.ok(rsControllerService.getRsEventBetween(page, size));
  }

  @PostMapping("/rs/add/event")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent rsEvent) {

    boolean flag = rsControllerService.addRsEvent(rsEvent);
    return flag ? ResponseEntity.created(null).build() : ResponseEntity.badRequest().build();

  }

  @DeleteMapping("/rs/delete/{rsId}")
  public ResponseEntity deleteRsEvent(@PathVariable int rsId){
    rsControllerService.deleteRsEvent(rsId);
    return ResponseEntity.ok().header("rsId",rsId+"").build();
  }

  @PatchMapping("/rs/modify/{rsId}")
  public ResponseEntity updateRsEvent(@PathVariable int rsId, @RequestBody RsEvent rsEvent) {
    boolean flag = rsControllerService.updateRsEvent(rsId,rsEvent);
    if (flag)
      return ResponseEntity.ok().header("index",rsId+"").build();
    else
      return ResponseEntity.badRequest().build();
  }

}
