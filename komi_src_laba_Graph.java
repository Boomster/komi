package laba;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<List<Double>> dist;
    Graph(List<List<Double>> d) {
        this.dist = d;
    }
    Graph(int n){
        this.dist = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> tmp = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                tmp.add(0.);
            }
            this.dist.add(tmp);
        }
    }

    public void Print(){
        for (int i = 0; i < dist.size(); i++) {
            for (int j = 0; j < dist.size(); j++) {
                String outS = String.format("|%2.3f|", this.dist.get(i).get(j));
                System.out.print(outS);
            }
            System.out.println();
        }
    }
}
