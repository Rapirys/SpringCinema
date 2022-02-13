package com.example.cinema.model;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

@Component
public class HallTopology {
    private final static Logger logger = Logger.getLogger(HallTopology.class);
    int size;
    ArrayList<ArrayList<Place>> topology;
    @PostConstruct
    public void init(){
        try(FileReader fr=new FileReader("src/Hall_topology.txt")) {
            Scanner sc = new Scanner(fr);
            int m,n;
            n=sc.nextInt();
            m=sc.nextInt();
            topology=new ArrayList<>(n);
            size=0;
            for (int i=0; i<n; i++){
                topology.add(new ArrayList<>(m));
                int scip=0;
                char[] c=sc.next().toCharArray();
                for(int j=0; j<m; j++) {
                    size+=(c[j]=='#')?1:0;
                    scip+=(c[j]=='0')?1:0;
                    topology.get(i).add(new Place(i,j-scip,c[j]));
                }
            }
        } catch (Exception e) {
            logger.warn("Problem with initializing topology of hall");
            System.exit(0);
        }
    }

    public int size() {
        return size;
    }

    public ArrayList<ArrayList<Place>> get() {
        return topology;
    }
}
class Place {
    public int row, place;
    public Character type;

    public Place(int row, int place, Character type) {
        this.type = type;
        if (type == '#') {
            this.row = row;
            this.place = place;
        }
    }

}