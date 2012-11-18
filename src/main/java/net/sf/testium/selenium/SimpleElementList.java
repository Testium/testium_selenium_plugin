package net.sf.testium.selenium;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SimpleElementList extends ArrayList<WebElement> implements SmartWebElementList {

	private static final long serialVersionUID = 1L;
	private final By myBy;
	private final WebDriverInterface myInterface;
	private WebElement myBaseElement;

	public SimpleElementList(By by, WebDriverInterface iface) {
		this(by, iface, new ArrayList<WebElement>(), null);
	}

	/**
	 * @param by		The by to find this element. If the element is a SmartWebElement, this by
	 *                  overrides the by inside the SmartWebElement
	 * @param iface		The interface on which this element will appear
	 * @param elements  the collection whose elements are to be placed into this list
.
	 */
	public SimpleElementList(By by, WebDriverInterface iface, Collection<? extends WebElement> elements)
	{
		this(by, iface, elements, null);
	}

	/**
	 * @param by		The by to find this element. If the element is a SmartWebElement, this by
	 *                  overrides the by inside the SmartWebElement
	 * @param iface		The interface on which this element will appear
	 * @param elements  the collection whose elements are to be placed into this list.
	 * @param baseElement   If set, this element is used as base element for commands like findElement.
	 */
	public SimpleElementList( By by,
			WebDriverInterface iface,
			Collection<? extends WebElement> elements,
			WebElement baseElement )
	{
		super();

		if ( elements != null )
		{
			super.addAll(elements);
		}
		myBy = by;
		myInterface = iface;
		myBaseElement = baseElement;
	}

	/**
	 * @return the original {@link By} used to find the element
	 */
	public By getBy()
	{
		return myBy;
	}
	
	public WebDriverInterface getInterface() {
		return myInterface;
	}

	public List<WebElement> getElements()
	{
		if (super.size() == 0) {
			refresh();
		}
		return this;
	}
	
	/**
	 * Refreshes the list of elements
	 * @throws an Error when the driver is not yet instantiated.
	 */
	public void refresh() {
		super.clear();
		if ( myBaseElement != null ) {
			super.addAll( myBaseElement.findElements(myBy) );
		} else {
			WebDriver driver = this.getInterface().getDriver();
			if ( driver == null ) { // should not happen. The interface must make sure it's not null
				throw new Error( "Element requested, but driver is not yet created: '" + myBy
								 + "'. Make sure this interface (" + this.getInterface().toString() + ") opens a browser first.");
			}
	
			super.addAll( driver.findElements(myBy) );
		}
	}

//	// List<WebElement> methods
//	// Overriden to add a refresh.
//	@Override
//	public int size()
//	{
//		this.refresh();
//		return super.size();
//	}
//
//	@Override
//	public boolean isEmpty() {
//		this.refresh();
//		return super.isEmpty();
//	}
//
//	@Override
//	public boolean contains(Object o) {
//		this.refresh();
//		return super.contains( o );
//	}
//	
//	@Override
//	public Iterator<WebElement> iterator() {
////		this.refresh();
//		return super.iterator();
//	}
//
//	@Override
//	public Object[] toArray() {
////		this.refresh(); // not refreshed as toArray() is used internally as well, causing a loop.
//		return super.toArray();
//	}
//
//	@Override
//	public <T> T[] toArray(T[] a){
////		this.refresh();
//		return super.toArray(a);
//	}
//
//
//    // Modification Operations
//
//	@Override
//	public boolean add(WebElement e) {
////		this.refresh();
//		return super.add(e);
//	}
//
//	@Override
//	public boolean remove(Object o) {
////		this.refresh();
//		return super.remove(o);
//	}
//
//    // Bulk Modification Operations
//
//	@Override
//	public boolean containsAll(Collection<?> c){
////		this.refresh();
//		return super.containsAll(c);
//	}
//
//	@Override
//	public boolean addAll(Collection<? extends WebElement> c) {
////		this.refresh();
//		return super.addAll(c);
//	}
//
//	@Override
//	public boolean addAll(int index, Collection<? extends WebElement> c) {
////		this.refresh();
//		return super.addAll(index, c);
//	}
//
//	@Override
//	public boolean removeAll(Collection<?> c) {
////		this.refresh();
//		return super.removeAll(c);
//	}
//
//	@Override
//	public boolean retainAll(Collection<?> c) {
////		this.refresh();
//		return super.retainAll(c);
//	}
//
//	// No need te refresh. List will be cleared.
//	//	@Override
//	//	public void clear();
//
//    // Comparison and hashing
//
//	@Override
//	public boolean equals(Object o) {
////		this.refresh();
//		return super.equals(o);
//	}
//
//	@Override
//	public int hashCode() {
////		this.refresh();
//		return super.hashCode();
//	}
//
//
//    // Positional Access Operations
//
//	@Override
//	public WebElement get(int index) {
////		this.refresh();
//		return super.get(index);
//	}
//
//	@Override
//	public WebElement set(int index, WebElement element) {
////		this.refresh();
//		return super.set(index, element);
//	}
//
//	@Override
//	public void add(int index, WebElement element) {
////		this.refresh();
//		super.add(index, element);
//	}
//
//	@Override
//	public WebElement remove(int index) {
////		this.refresh();
//		return super.remove(index);
//	}
//
//
//    // Search Operations
//
//	@Override
//	public int indexOf(Object o)
//	 {
////		this.refresh();
//		return super.indexOf(o);
//	 }
//
//	@Override
//	public int lastIndexOf(Object o) {
////		this.refresh();
//		return super.lastIndexOf(o);
//	}
//
//
//    // List Iterators
//
//	@Override
//	public ListIterator<WebElement> listIterator() {
////		this.refresh();
//		return super.listIterator();
//	}
//
//	@Override
//	public ListIterator<WebElement> listIterator(int index) {
////		this.refresh();
//		return super.listIterator(index);
//	}
//
//    // View
//
//	@Override
//	public List<WebElement> subList(int fromIndex, int toIndex) {
////		this.refresh();
//		return super.subList(fromIndex, toIndex);
//	}
}
