package model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.Function;

public class MinMaxTree<E> {
    private final boolean nodeType;
    private E nodeContent;
    private final HashSet<MinMaxTree<E>> children;

    public MinMaxTree(E content, boolean type){
        nodeContent = content;
        nodeType = type;
        children = new HashSet<>();
    }

    /**
     * Creates a new MinMaxTree
     * @param type true, if max node, false if min node
     */
    public MinMaxTree(boolean type){
        nodeType = type;
        children = new HashSet<>();
    }

    @SafeVarargs
    public final void add(MinMaxTree<E>... childs){
        children.addAll(Arrays.asList(childs));
    }

    public HashSet<MinMaxTree<E>> getChildren(){
        return children;
    }

    public boolean remove(MinMaxTree<E> toRemove){
        return children.remove(toRemove);
    }

    public E getNodeContent(){
        return nodeContent;
    }

    public int calculateMinMaxWeight(final Function<E, Integer> mapper){
        if(children.isEmpty()) return mapper.apply(nodeContent);
        if(nodeType) return children.stream().map(node -> node.calculateMinMaxWeight(mapper)).max(Comparator.naturalOrder()).orElse(0);
        return children.stream().map(node -> node.calculateMinMaxWeight(mapper)).min(Comparator.naturalOrder()).orElse(0);
    }

    public void createOnLevel(final Function<E, HashSet<MinMaxTree<E>>> funct, int level){
        if(level==0){
            children.addAll(funct.apply(nodeContent));
        }
        else{
            children.forEach(tree -> tree.createOnLevel(funct, level-1));
        }
    }

}
