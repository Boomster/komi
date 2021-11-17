package laba;

import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static List <Integer> C_MADNESS(int n, Graph myG){
        List<List<Double>> graph = myG.dist;

        List<List<Double>> PrimGraph = Prim(graph);

        List<List<Integer>> multyGraph = ParosochetIzO(PrimGraph, graph);

        List<Integer> EulerovCircle = EulerCircle(multyGraph.size(), multyGraph);

        List<Integer> circle = GamilCircle(EulerovCircle, graph.size());

        System.out.println(CircleLength(circle, myG, n));

        return circle;
    }

    static List<Integer> GamilCircle(List<Integer> circle, int n){
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            visited[i] = false;
        }
        List<Integer> result = new ArrayList<>();
        for (Integer ver : circle) {
            if (!visited[ver]) {
                visited[ver] = true;
                result.add(ver);
            }

        }
        return result;
    }

    static void DepthSearchParosochet(List<List<Double>> podGraph, boolean[] visited, List<Integer> buffer, List<Integer> result) {

        for (int i = 1; i < podGraph.size(); i++) {

            if(!visited[i]) {
                buffer.add(i);
                visited[i] = true;
                if(buffer.size() == podGraph.size()) {
                    if (result.size() == 0) {
                        result.addAll(buffer);
                    } else {
                        double cost = 0.0;
                        double resultCost = 0.0;
                        for (int j = 0; j < buffer.size(); j += 2) {
                            cost += podGraph.get(buffer.get(j)).get(buffer.get(j+1));
                            resultCost += podGraph.get(result.get(j)).get(result.get(j+1));
                        }

                        if (resultCost > cost) {
                            result.clear();
                            result.addAll(buffer);
                        }
                    }
                }else {
                    DepthSearchParosochet(podGraph, visited, buffer, result);
                }
                visited[i] = false;
                buffer.remove(buffer.size()-1);
            }
        }


    }
    static List<List<Integer>> ParosochetIzO(List<List<Double>> minBaseTree, List<List<Double>> graph){
        boolean[] graphO = new boolean[graph.size()];
        for (int i = 0; i < graph.size(); i++) {
            graphO[i] = false;
        }

        for (int i = 0; i < minBaseTree.size(); i++) {
            int count = 0;
            for (int j = 0; j < minBaseTree.size(); j++) {
                if(minBaseTree.get(i).get(j)> 0.0)
                    count++;
            }

            if(count % 2 == 1)
                graphO[i] = true;
        }

        List<List<Double>> podGraph = new ArrayList<>();
        List<Integer> podGraphVer = new ArrayList<>();

        for (int i = 0; i < graph.size(); i++) {
            if(graphO[i]) {
                podGraph.add(new ArrayList<>());
                podGraphVer.add(i);
                for (int j = 0; j < graph.size(); j++) {
                    if(graphO[j]){
                        podGraph.get(podGraphVer.size()-1).add(graph.get(i).get(j));
                    }
                }
            }
        }


        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[podGraph.size()];
        for (int i = 0; i < visited.length; i++) {
            visited[i] = false;
        }
        visited[0] = true;
        List<Integer> buffer = new ArrayList<>();
        buffer.add(0);

        DepthSearchParosochet(podGraph, visited, buffer,result);

        List<List<Integer>> multyGraph = new ArrayList<>();
        for (int i = 0; i < graph.size(); i++) {
            multyGraph.add(new ArrayList<>());
            for (int j = 0; j < graph.size(); j++) {
                multyGraph.get(i).add(0);
            }
        }
        for (int i = 0; i < multyGraph.size(); i++) {
            for (int j = 0; j < multyGraph.size(); j++) {
                if(minBaseTree.get(i).get(j) != 0.0)
                    multyGraph.get(i).set(j,multyGraph.get(i).get(j)+1);
            }
        }
        for (int i = 0; i < result.size(); i+=2) {
            multyGraph.get(podGraphVer.get(result.get(i))).set(podGraphVer.get(result.get(i+1)), multyGraph.get(podGraphVer.get(result.get(i))).get(podGraphVer.get(result.get(i+1)))+1);
            multyGraph.get(podGraphVer.get(result.get(i+1))).set(podGraphVer.get(result.get(i)), multyGraph.get(podGraphVer.get(result.get(i+1))).get(podGraphVer.get(result.get(i)))+1);
        }

        return multyGraph;
    }

    static List<List<Double>> Prim(List<List<Double>> graph)
    {
        List<List<Double>> minBaseTree = new ArrayList<>();
        for (int i = 0; i < graph.size(); i++) {
            minBaseTree.add(new ArrayList<>());
            for (int j = 0; j < graph.size(); j++) {
                minBaseTree.get(i).add(0.0);
            }
        }
        boolean[] passed = new boolean[graph.size()];
        for (int i = 0; i < passed.length; i++) {
            passed[i] = false;
        }

        passed[0] = true;
        boolean lookingForNextVertex = true;
        while(lookingForNextVertex){
            Double min = Double.MAX_VALUE;
            Point minp = null;
            for (int i = 0; i < graph.size(); i++) {
                if(passed[i]){
                    for (int j = 0; j < graph.size(); j++) {
                        if(!passed[j]){
                            if(min > graph.get(i).get(j)) {
                                min = graph.get(i).get(j);
                                minp = new Point(i,j);
                            }
                        }
                    }
                }
            }
            if(minp != null){
                passed[minp.y] = true;
                minBaseTree.get(minp.x).set(minp.y,min);
                minBaseTree.get(minp.y).set(minp.x,min);
            }
            else
                lookingForNextVertex = false;

        }
        return minBaseTree;
    }

    static Graph GenerateGraph(int n) {
        List<List<Double>> g = new ArrayList<>();

        List<Point2D> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Point2D v = new Point2D.Double(Math.random()*100, Math.random()*100);
            vertices.add(v);
        }
        for (int i = 0; i < n; i++) {
            List<Double> line = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                line.add(vertices.get(i).distance(vertices.get(j)));
            }
            g.add(line);
        }

        return new Graph(g);
    }

    static void WriteOutput(List<Integer> circle){
        for (Integer ver : circle) {
            System.out.print(ver + " ");
        }
        System.out.print(circle.get(0)+" ");
        System.out.println();
    }

    static int CircleLength(List<Integer> circle, Graph myG, int n){
        int sum = 0;
        //int m = circle.size();
        for (int i = 0; i < n - 1; i++) {
            sum += myG.dist.get(circle.get(i)).get(circle.get(i+1));
        }
        sum += myG.dist.get(circle.get(n-1)).get(circle.get(0));
        return sum;
    }

    static void GetMinCircle(List<Integer> tryCircle, List<Boolean> freeVert, List<Integer> circle, int n, Graph myG){

        if(tryCircle.size() == n){
            if(circle.isEmpty()) {
                circle.addAll(tryCircle);
            } else {
                int tmpLength = CircleLength(tryCircle, myG, n);
                int circleLength = CircleLength(circle, myG, n);
                if(tmpLength < circleLength){
                    circle.clear();
                    circle.addAll(tryCircle);
                }
            }

        } else {
            for (int i = 0; i < n; i++) {
                if(freeVert.get(i)){
                    freeVert.set(i, false);
                    tryCircle.add(i);
                    GetMinCircle(tryCircle, freeVert, circle, n, myG);
                }
            }
        }
        if(tryCircle.size()>0) {
            freeVert.set(tryCircle.get(tryCircle.size()-1), true);
            tryCircle.remove(tryCircle.size() - 1);
        }
    }

    public static List<Integer> A_TRY_HARD(int n, Graph myG){
        List<Integer> circle = new ArrayList<>();
        List<Integer> tryCircle = new ArrayList<>();
        List<Boolean> freeVert = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            freeVert.add(true);
        }
        GetMinCircle(tryCircle, freeVert, circle, n, myG);



        System.out.println(CircleLength(circle, myG, n));
        return circle;
    }

    public static List<Integer> B_GREED(int n, Graph myG){
        List<Integer> circle = new ArrayList<>();
        List<Boolean> freeVert = new ArrayList<>();
        int vertLeft = n - 1;
        for (int i = 0; i < n; i++) {
            freeVert.add(true);
        }
        freeVert.set(0, false);
        circle.add(0);
        int last = 0;
        while(vertLeft > 0){
            double minDist = 10000;
            int minVert = -1;
            for (int i = 0; i < n; i++) {
                if(freeVert.get(i)){
                    if(minVert == -1) {
                        minVert = i;
                        minDist = myG.dist.get(last).get(i);
                    } else if(minDist > myG.dist.get(last).get(i)){
                        minDist = myG.dist.get(last).get(i);
                        minVert = i;
                    }
                }
            }
            circle.add(minVert);
            last = minVert;
            freeVert.set(minVert, false);
            vertLeft--;
        }

        System.out.println(CircleLength(circle, myG, n));
        return circle;
    }

    static void GetEulerCircle(List<List<Integer>> hGr, List<Integer> circle, int n){
        boolean hasWay = false;
        int cur = circle.get(circle.size()-1);
        for (int i = 0; i < n && !hasWay; i++) {
            if(hGr.get(cur).get(i) > 0) hasWay = true;
        }
        if(hasWay){
            boolean way = false;
            int k = 0;
            while(!way && k < n){
                int tmp = hGr.get(cur).get(k);
                if(tmp > 0){
                    way = true;
                    hGr.get(cur).set(k, tmp - 1);
                    hGr.get(k).set(cur, tmp - 1);
                    circle.add(k);
                    GetEulerCircle(hGr, circle, n);
                } else {
                    k++;
                }
            }
            if (!way) System.out.println("Something is wrong");
        }
    }

    static List<Integer> EulerCircle(int n, List<List<Integer>> hGr){
        List<Integer> circle = new ArrayList<>();
        circle.add(0);
        GetEulerCircle(hGr, circle, n);
        boolean hasMinorCircles;
        do {
            hasMinorCircles = false;
            int startCircle = 0;
            for (int i = 0; i < circle.size() && !hasMinorCircles; i++) {
                for (int j = 0; j < n && !hasMinorCircles; j++) {
                    if(hGr.get(circle.get(i)).get(j) > 0) {
                        startCircle = circle.get(i);
                        hasMinorCircles = true;
                    }
                }
            }
            List<Integer> minorCircle = new ArrayList<>();
            minorCircle.add(startCircle);
            if (hasMinorCircles) GetEulerCircle(hGr,minorCircle, n);
            minorCircle.remove(0);
            int pos = circle.indexOf(startCircle);
            circle.addAll(pos+1, minorCircle);
        } while (hasMinorCircles);
        return circle;
    }

    public static void main(String[] args) {

        long mainTID = Thread.currentThread().getId();

        Scanner myScan = new Scanner(System.in);
        System.out.println("Input n:");
        int n = Integer.parseInt(myScan.nextLine());
        Graph myGraph = GenerateGraph(n);
        List<Integer> gamilCircle;

        long startT;
        long startPT;
        long endT;
        long endPT;
/*
        System.out.println("A:");
        startT = System.nanoTime();
        startPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
        gamilCircle = A_TRY_HARD(n, myGraph);
        WriteOutput(gamilCircle);
        endT = System.nanoTime();
        endPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
        System.out.println("System thread time");
        System.out.println(endPT-startPT);
        System.out.println("System time");
        System.out.println(endT-startT);

        System.out.println("B:");
        startT = System.nanoTime();
        startPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
        gamilCircle = B_GREED(n, myGraph);
        WriteOutput(gamilCircle);
        endT = System.nanoTime();
        endPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
        System.out.println("System thread time");
        System.out.println(endPT-startPT);
        System.out.println("System time");
        System.out.println(endT-startT);*/

        System.out.println("C:");
        startT = System.nanoTime();
        startPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
        gamilCircle = C_MADNESS(n, myGraph);
        WriteOutput(gamilCircle);
        endT = System.nanoTime();
        endPT = ManagementFactory.getThreadMXBean().getThreadCpuTime(mainTID);
        System.out.println("System thread time");
        System.out.println(endPT-startPT);
        System.out.println("System time");
        System.out.println(endT-startT);
    }
}
