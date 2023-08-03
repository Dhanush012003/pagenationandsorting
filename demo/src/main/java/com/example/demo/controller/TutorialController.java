package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Tutorial;
import com.example.demo.repository.Tutorialrepository;

@RestController
@RequestMapping("/api")
public class TutorialController {
    
    @Autowired
    Tutorialrepository tutorialRepository;


    ///pagination
    private Sort.Direction getSortDirection(String _sort){
        if(_sort.equals("asc")){
            return Sort.Direction.ASC;
        }else if(_sort.equals("desc")){
            return Sort.Direction.DESC;
        }else{
            return Sort.Direction.ASC;
        }
    }
    @GetMapping("/paginationandsorting")
    public ResponseEntity<Map<String, Object>> getAllTutorials(
        @RequestParam(defaultValue = "0")int page,
        @RequestParam(defaultValue = "5")int size,
        @RequestParam(defaultValue = "id,asc") String[] sort){
            List<Order> order=new ArrayList<Order>();

            if(sort[0].contains(",")){
                for(String sortOrdet : sort){
                    String[] _sort=sortOrdet.split(",");
                    order.add(new Order(getSortDirection(_sort[1]),_sort[0]));
                }
            }
            else{
                order.add(new Order(getSortDirection(sort[1]),sort[0]) );
            }

            List<Tutorial> tutorials = new ArrayList<Tutorial>();
            Pageable pagingSort = PageRequest.of(page, size, Sort.by(order));

            Page<Tutorial>pageTuts;
            pageTuts = tutorialRepository.findAll(pagingSort);

            tutorials=pageTuts.getContent();

            Map<String, Object>response=new HashMap<>();
            response.put("tutorials",tutorials);
            response.put("currentPage", pageTuts.getNumber());
            response.put("pagesize",pageTuts.getTotalElements());
            response.put("totalpage",pageTuts.getTotalPages());

            return new ResponseEntity<>(response,HttpStatus.OK);
        }
}

