package model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.function.Function;

/**
 * @author Lukas Kidin
 * @param <E>
 */
public class MinMaxTree<E> {
    private final boolean nodeType;
    private E nodeContent;
    private final HashSet<MinMaxTree<E>> children;

    /**
     * Constructor for MinMaxTree
     * @param content content
     * @param type true, if max node, false if min node
     */
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

    /**
     * Add some children to the tree
     * @param childs the trees which should be added to the tree
     */
    @SafeVarargs
    public final void add(MinMaxTree<E>... childs){
        children.addAll(Arrays.asList(childs));
    }

    /**
     * get the children of the tree
     * @return the hashList of the tree. This is no copy
     */
    public HashSet<MinMaxTree<E>> getChildren(){
        return children;
    }

    /**
     * gets the node content
     * @return the content of the node. may be null.
     */
    public E getNodeContent(){
        return nodeContent;
    }

    /**
     * calculates the best Child of the Children based on the value function which will be applied on leafs on this tree. Take a look on MiniMaxAlgorithms.
     * @param mapper the function to evaluate the leafnodes
     * @return the best option for the actual Content with this function. null if there is none
     */
    public E calculateMinMaxNode(final Function<E, Double> mapper){
        return children.stream()
                .map(child -> new Tuple<>(child.getNodeContent(), child.calculateMinMaxWeight(mapper)))
                .max(Comparator.comparingDouble(Tuple::getSecond))
                .orElse(new Tuple<>(null, 1.0)).getFirst();
    }

    private double calculateMinMaxWeight(final Function<E, Double> mapper){
        synchronized (this) {
            if (children.isEmpty()) return mapper.apply(nodeContent);
            if (nodeType)
                return children.stream().map(node -> node.calculateMinMaxWeight(mapper)).max(Comparator.naturalOrder()).orElse(0.0);
            return children.stream().map(node -> node.calculateMinMaxWeight(mapper)).min(Comparator.naturalOrder()).orElse(0.0);
        }
    }

    /**
     * Builds tree.
     * @param funct function
     * @param level level
     * @throws InterruptedException if thread is interrupted
     */
    public void createOnLevel(final Function<E, HashSet<MinMaxTree<E>>> funct, int level) throws InterruptedException {
        if(Thread.interrupted()) throw new InterruptedException();
        if(level==0){
            synchronized (this) {
                children.addAll(funct.apply(nodeContent));
            }
        }
        else{
            for (MinMaxTree<E> tree : children) {
                tree.createOnLevel(funct, level - 1);
            }
        }
    }

    /**
     * Builds tree and deletes nodes.
     * @param funct function
     * @param level level
     * @throws InterruptedException if thread is interrupted
     */
    public void createOnLevelAndDelete(final Function<E, HashSet<MinMaxTree<E>>> funct, int level) throws InterruptedException {
        if(Thread.interrupted()) throw new InterruptedException();
        if(level==0){
            synchronized(this) {
                children.addAll(funct.apply(nodeContent));
                nodeContent = null;
            }
        }
        else{
            for (MinMaxTree<E> tree : children) {
                tree.createOnLevelAndDelete(funct, level - 1);
            }
        }
    }

}
