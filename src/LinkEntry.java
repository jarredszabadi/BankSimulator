// The "LinkEntry" class.
// This is an entry (or node) for a linked list containing an
// object of type E as the entry's data.
public class LinkEntry<E> {
	protected E element; // The entry's data.

	protected LinkEntry<E> link; // The link to the next entry.

	// Create a new link entry.
	public LinkEntry(E element, LinkEntry<E> link) {
		this.element = element;
		this.link = link;
	} // LinkEntry constructor

	// Return the element of the link entry.
	public E getElement() {
		return element;
	} // getElement method

	// Return the link to the next link entry.
	public LinkEntry<E> getLink() {
		return link;
	} // getLink method

	// Set the link to the next link entry.
	public void setLink(LinkEntry<E> newLink) {
		link = newLink;
	} // setLink method
} /* LinkEntry class */