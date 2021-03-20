/**
 * Car Binary Tree Node class for CS1501 Project 3
 * @author	David Roberts
 */
package cs1501_p3;

public class CarNode
{
	/**
	 * Car held by this node
	 */
	private Car car;

	/**
	 * Left child reference
	 */
	private CarNode left;

	/**
	 * Right child reference
	 */
	private CarNode right;

	/**
	 * Constructor that accepts the car to be held by the new node
	 */
	public CarNode(Car c)
	{
		car = c;
	}

	/**
	 * Getter for the car
	 * 
	 * @return	Reference to the car
	 */
	public Car getCar()
	{
		return car;
	}

	/**
	 * Getter for the left child
	 *
	 * @return	Reference to the left child
	 */
	public CarNode getLeft() 
	{
		return left;
	}

	/**
	 * Getter for the right child
	 *
	 * @return	Reference to the right child
	 */
	public CarNode getRight() 
	{
		return right;
	}

	/**
	 * Setter for the left child
	 *
	 * @param	l CarNode to set as the left child
	 */
	public void setLeft(CarNode l) 
	{
		left = l;
	}

	/**
	 * Setter for the right child
	 *
	 * @param	r CarNode to set as the right child
	 */
	public void setRight(CarNode r) 
	{
		right = r;
	}

	public void setCar(Car c)
	{
		car = c;
	}
}
