package ru.digitalleague.taxi_company.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.digitalleague.taxi_company.api.OrderService;
import ru.digitalleague.taxi_company.model.Order;

import java.time.OffsetDateTime;

/**
 * Контроллер получающий информацию о поездке.
 */
@RestController
@Slf4j
public class TaxiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * Метод устанавливает время начала поездки
     * @param order
     * */
    @PostMapping("/trip-start")
    @ApiOperation(value = "Контроллер для установки времени начала поездки")
    public ResponseEntity<String> startTrip(@RequestBody Order order){
        OffsetDateTime currentTime = context.getBean("currentTime", OffsetDateTime.class);
        order.setStartTrip(currentTime);
        orderService.saveStartTripTime(order.getStartTrip(),order.getOrderId());
        log.warn("Save order with start time " + order);
        return ResponseEntity.ok("Start trip time: " + order.getStartTrip());
    }

    /**
     * Метод устанавливает время окончания поездки
     * @param order
     * */
    @PostMapping("/trip-complete")
    @ApiOperation(value = "Контроллер для установки времени окончания поездки")
    public ResponseEntity<String> endTrip(@RequestBody Order order){
        OffsetDateTime currentTime = context.getBean("currentTime", OffsetDateTime.class);
        order.setEndTrip(currentTime);
        orderService.saveEndTimeTrip(order.getEndTrip(),order.getOrderId());
        log.warn("Save order with end time " + order);

        amqpTemplate.convertAndSend("trip-result", "Клиент с ID: " + order.getClientNumber() + " завершил поездку!");

        return ResponseEntity.ok("Услуга оказана");
    }
}


//    @Autowired
//    private AmqpTemplate amqpTemplate;
//
//    /**
//     * Метод получает инфо о завершении поездки.
//     * @param message
//     * */
//    @PostMapping("/trip-complete")
//    public ResponseEntity<String> completeTrip(@RequestBody String message) {
//        System.out.println("Trip is finished");
//
//        amqpTemplate.convertAndSend("trip-result", message);
//
//        return ResponseEntity.ok("Услуга оказана");
//    }
