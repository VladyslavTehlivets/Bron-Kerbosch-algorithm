package pl.umcs.informatyka.bron.kerbosch;

import edu.uci.ics.jung.graph.UndirectedGraph;

import java.util.*;
import java.util.stream.Collectors;

public class BronKerbosch<V, E> {

    private UndirectedGraph<V, E> graph;

    private Collection<Set<V>> cliques;

    public BronKerbosch(UndirectedGraph<V, E> graph) {
        this.graph = graph;
    }

    public Collection<Set<V>> getAllMaximalCliques() {
        cliques = new ArrayList<>();

        List<V> potential_clique = new ArrayList<>();
        List<V> candidates = new ArrayList<>();
        List<V> already_found = new ArrayList<>();

        candidates.addAll(graph.getVertices());
        findCliques(potential_clique, candidates, already_found);

        return cliques;
    }

    private void findCliques(List<V> potential_clique, List<V> candidates, List<V> already_found) {
        List<V> candidates_array = new ArrayList<>(candidates);
        if (!end(candidates, already_found)) {

            candidates_array.forEach((V candidate) -> {

                potential_clique.add(candidate);

                candidates.remove(candidate);
                List<V> new_candidates = candidates.stream()
                        .filter(new_candidate -> graph.isNeighbor(candidate, new_candidate))
                        .collect(Collectors.toList());

                List<V> new_already_found = already_found.stream()
                        .filter(new_found -> graph.isNeighbor(candidate, new_found))
                        .collect(Collectors.toList());

                if (new_candidates.isEmpty() && new_already_found.isEmpty()) {
                    cliques.add(new HashSet<>(potential_clique));
                } else {
                    findCliques(potential_clique, new_candidates, new_already_found);
                }

                potential_clique.remove(candidate);
                already_found.add(candidate);
            });
        }
    }

    private boolean end(List<V> candidates, List<V> already_found) {

        return already_found.stream()
                .anyMatch(found -> candidates.stream()
                        .filter(candidate -> graph.isNeighbor(found, candidate)).count() > candidates.size());
    }
}
