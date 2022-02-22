package com.spring.cinema.model.service;


import com.spring.cinema.entities.Ticket;
import com.spring.cinema.model.service.Hall.HallTopology;
import com.spring.cinema.model.service.Hall.Place;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class HallTopologyTest {
    @Autowired
    HallTopology hallTopology;

    @Test
    void getCopyTopology() {
        ArrayList<ArrayList<Place>>  topology= hallTopology.getCopyTopology();
        for (int i=0; i<topology.size(); i++)
            for (int j=0; j<topology.get(i).size(); j++)
                assertThat(topology.get(i).get(j)).isEqualTo(hallTopology.topology.get(i).get(j));
    }

    @Test
    void GetCopyTopologyByTickets() {
        List<Ticket> tickets =new ArrayList<>();

        for (int i=0; i<hallTopology.topology.size(); i+=2){
            for (int j=0; j<hallTopology.topology.get(i).size(); j+=2) {
                if (hallTopology.topology.get(i).get(j).type.equals('#')) {
                    Ticket ticket = new Ticket();
                    Place p=hallTopology.topology.get(i).get(j);
                    ticket.setRow(p.row);
                    ticket.setPlace(p.place);
                    tickets.add(ticket);
                }
            }
        }
        ArrayList<ArrayList<Place>>  topology= hallTopology.getCopyTopology(tickets);
        for (int i=0; i<hallTopology.topology.size(); i++){
            for (int j=0; j<hallTopology.topology.get(i).size(); j++) {
                if (i%2==0 && j%2==0 && hallTopology.topology.get(i).get(j).type.equals('#')) {
                    assertThat(topology.get(i).get(j).type).isEqualTo('X');
                }
                else  assertThat(topology.get(i).get(j)).isEqualTo(hallTopology.topology.get(i).get(j));
            }
        }
    }
}