package pl.umcs.informatyka;

import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import pl.umcs.informatyka.bron.kerbosch.BronKerbosch;

import java.util.Collection;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {

        UndirectedGraph<Integer, Pair<Integer>> graph = new UndirectedSparseGraph<>();

        Scanner scanner = new Scanner(System.in);
        int edgeCount = scanner.nextInt();

        IntStream.range(0, edgeCount).forEach(i -> {
            int vertex1 = scanner.nextInt();
            int vertex2 = scanner.nextInt();

            graph.addEdge(new Pair<>(vertex1, vertex2), vertex1, vertex2);
        });

        BronKerbosch<Integer, Pair<Integer>> algorythm = new BronKerbosch<>(graph);

        Runnable task = () -> {

            long start = System.nanoTime();

            Collection<Set<Integer>> allMaximalCliques = algorythm.getAllMaximalCliques();

            allMaximalCliques.stream()
                    .collect(Collectors.groupingBy(Set::size))
                    .entrySet()
                    .stream()
                    .sorted((o1, o2) -> Integer.compare(o2.getKey(), o1.getKey()))
                    .limit(1)
                    .forEach(integerListEntry -> {

                        int maxCliqueSize = integerListEntry.getValue().size();

                        System.out.println("Znaleziono klik największych: " + maxCliqueSize);

                        integerListEntry.getValue().forEach(vertexList -> {
                            System.out.println("Klika największa: " + vertexList.stream().map(String::valueOf)
                                    .collect(Collectors.joining(" ")));
                            System.out.println("Liczba wierzchowków w klicę największej: " + vertexList.size());
                        });
                    });

            System.out.println("Czas wykonania w milisekundach: " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - start, TimeUnit.NANOSECONDS));
        };

        task.run();
    }
}
