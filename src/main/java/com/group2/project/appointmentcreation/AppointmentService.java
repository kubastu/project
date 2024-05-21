package com.group2.project.appointmentcreation;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/airports")
@Log4j2
public class AppointmentService {
  @Autowired
  private AppointmentRepo repo;

  public List<Appointment> list() {
    log.traceEntry("Enter list");
    var retval = repo.findAll();
    log.traceExit("Exit list", retval);
    return repo.findAll();
  }

  public void save(Appointment appointment) {
    log.traceEntry("enter save", appointment);
    repo.save(appointment);
    log.traceExit("exit save", appointment);
  }

  public void delete(String code) {
    log.traceEntry("Enter delete", code);
    repo.deleteById(code);
    log.traceExit("Exit delete");
  }

}
