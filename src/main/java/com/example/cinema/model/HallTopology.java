package com.example.cinema.model;

import com.example.cinema.contrloller.Admin.FilmController;
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
    ArrayList<ArrayList<Character>> topology;
    @PostConstruct
    public void init(){
        try(FileReader fr=new FileReader("src/Hall_topology.txt")) {
            Scanner sc = new Scanner(fr);
            int m,n;
            n=sc.nextInt();
            m=sc.nextInt();
            topology=new ArrayList<>(n);
            for (int i=0; i<n; i++){
                topology.add(new ArrayList<>(m));
                char[] c=sc.next().toCharArray();
                for(int j=0; j<m; j++) {
                    size+=(c[j]=='#')?1:0;
                    topology.get(i).add(c[j]);
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

    public ArrayList<ArrayList<Character>> get() {
        return topology;
    }
}
