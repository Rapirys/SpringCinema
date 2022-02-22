package com.spring.cinema.model.service.Hall;

import com.spring.cinema.entities.Ticket;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.util.*;

/**
 * Represents the hall topology.
 * Stores topology information on application startup.
 */
@Component
public class HallTopology {
    private final static Logger logger = Logger.getLogger(HallTopology.class);
    /**
     * Maps the location number and series to coordinates in space.
     */
    HashMap<Point, Point> map;
    int size;
    public int m,n;
    public ArrayList<ArrayList<Place>> topology;
    @PostConstruct
    public void init(){
        try(FileReader fr=new FileReader("src/Hall_topology.txt")) {
            Scanner sc = new Scanner(fr);
            int m,n;
            n=sc.nextInt();
            m=sc.nextInt();
            this.n=n;
            this.m=m;
            topology=new ArrayList<>(n);
            size=0;
            map=new HashMap<>();
            for (int i=0; i<n; i++){
                topology.add(new ArrayList<>(m));
                int scip=0;
                char[] c=sc.next().toCharArray();
                for(int j=0; j<m; j++) {
                    size+=(c[j]=='#')?1:0;
                    scip+=(c[j]=='0')?1:0;
                    if (c[j]=='#')
                        map.put(new Point(i,j-scip), new Point(i,j));
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

    public ArrayList<ArrayList<Place>> getCopyTopology() {
        ArrayList<ArrayList<Place>> copyTopology=new ArrayList<>(n);
        for (int i=0; i<n; i++){
            copyTopology.add(new ArrayList<>(m));
            for(int j=0; j<m; j++)
                copyTopology.get(i).add(new Place(topology.get(i).get(j)));
        }
        return copyTopology;
    }
    public ArrayList<ArrayList<Place>> getCopyTopology(List<Ticket> tickets){
        ArrayList<ArrayList<Place>> copyTopology=getCopyTopology();
        for (Ticket ticket: tickets) {
            Point t=map.get(new Point(ticket.getRow(), ticket.getPlace()));
            int row=t.i;
            int place=t.j;
            copyTopology.get(row).set(place, new Place(ticket.getRow(), ticket.getPlace(), 'X'));
        }
        return copyTopology;
    }
}
class Point{
    int i,j;

    public Point(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return i == point.i && j == point.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}
