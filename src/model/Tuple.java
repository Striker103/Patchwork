package model;


import java.io.Serializable;
import java.util.Objects;

/**
 * A Tuple containing two elements which can be from different types.
 *
 * @param <T1> Type of first element
 * @param <T2> Type of second element
 *
 * @author sopr090 (Lukas Kidin) (JavaDoc)
 */
public class Tuple<T1, T2> implements Serializable {


	/**
	 * first Element
	 */
	private T1 first;

	/**
	 * second Element
	 */
	private T2 second;


	/**
	 * Creates a new Tuple with given Elements
	 *
	 * @param first  first Element
	 * @param second second Element
	 */
	public Tuple(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}



	/**
	 * Getter for first element
	 *
	 * @return first Element in Tuple
	 */
	public T1 getFirst() {
		return first;
	}

	/**
	 * Sets first Element in Tuple to specified element
	 *
	 * @param first new second element
	 */
	public void setFirst(T1 first) {
		this.first = first;
	}


	/**
	 * Getter for second element
	 *
	 * @return second element
	 */
	public T2 getSecond() {
		return second;
	}

	/**
	 * Sets second element to sepcified element
	 *
	 * @param second new second element
	 */
	public void setSecond(T2 second) {
		this.second = second;
	}

	/**
	 * Rewrites equals-method for T1 and T2
	 *
	 * @param toCompare Object which should be compared
	 * @return true, iff given Object is equal to this tuple
	 */
	@Override
	public boolean equals(Object toCompare) {
		if (this == toCompare) return true;
		if (toCompare == null || getClass() != toCompare.getClass()) return false;
		Tuple<?, ?> tuple = (Tuple<?, ?>) toCompare;
		return Objects.equals(getFirst(), tuple.getFirst()) &&
				Objects.equals(getSecond(), tuple.getSecond());
	}

	/**
	 * Generates hash for this tuple
	 *
	 * @return hash for this tuple
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getFirst(), getSecond());
	}
}

