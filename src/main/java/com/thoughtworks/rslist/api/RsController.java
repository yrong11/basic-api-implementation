package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = initRsEventList();

  private List<RsEvent> initRsEventList(){
    List<RsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new RsEvent("第一条事件", "无标签"));
    rsEventList.add(new RsEvent("第二条事件", "无标签"));
    rsEventList.add(new RsEvent("第三条事件", "无标签"));
    return rsEventList;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEvent(@PathVariable int index){
    return this.rsList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public List<RsEvent> getRsEventBetween(@RequestParam(required = false) Integer start,
                                         @RequestParam(required = false) Integer end){
    if (start != null && end != null)
      return this.rsList.subList(start-1, end);
    return this.rsList;
  }

  @PostMapping("/rs/add/event")
  public void addRsEvent(@RequestBody String rsEvent) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
    this.rsList.add(event);
  }

  @GetMapping("/rs/delete/{index}")
  public void deleteRsEvent(@PathVariable int index){
    this.rsList.remove(index - 1);
  }

  @PostMapping("/rs/modify/{index}")
  public void modifyRsEvent(@PathVariable int index, @RequestBody String rsEvent) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
    if (!event.getEventName().equals(""))
      this.rsList.get(index - 1).setEventName(event.getEventName());
    if (!event.getKeyword().equals(""))
      this.rsList.get(index - 1).setKeyword(event.getKeyword());
  }

}
