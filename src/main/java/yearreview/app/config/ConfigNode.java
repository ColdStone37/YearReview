package yearreview.app.config;

import org.w3c.dom.*;

import java.util.*;

/**
 * A Wrapper around a {@link org.w3c.dom.Node node} that allows for easier access of child nodes.
 *
 * @author ColdStone37
 */
public class ConfigNode implements Iterable<ConfigNode> {
	/**
	 * The node that is wrapped by this object.
	 */
	private final Node node;
	/**
	 * A Map used to convert the name of a child to it's node directly.
	 */
	private final HashMap<String, Node> nameToNodeMap;

	/**
	 * Constructs a ConfigNode from a {@link org.w3c.dom.Node Node}.
	 *
	 * @param node the node to create the ConfigNode from
	 */
	public ConfigNode(Node node) {
		this.node = node;
		NodeList subNodes = node.getChildNodes();

		// Inserts every child into the HashMap
		nameToNodeMap = new HashMap<String, Node>();
		Node c;
		for (int i = 0; i < subNodes.getLength(); i++) {
			c = subNodes.item(i);
			if (c.getNodeType() == Node.ELEMENT_NODE)
				nameToNodeMap.put(c.getNodeName(), c);
		}
	}

	/**
	 * Gets the Name of the Node. (Wrapper for {@link org.w3c.dom.Node#getNodeName})
	 *
	 * @return name of the node
	 */
	public String getName() {
		return node.getNodeName();
	}

	/**
	 * Gets a child of this node by name.
	 *
	 * @param name name of the node to get
	 * @return child with given name or null if such a node doesn't exists
	 */
	public ConfigNode getChildByName(String name) {
		if (!nameToNodeMap.containsKey(name))
			return null;
		return new ConfigNode(nameToNodeMap.get(name));
	}

	/**
	 * Gets text content of a child node directly.
	 *
	 * @param name name of the node to get the text content from
	 * @return text content of the child node with the given name
	 */
	public String getChildContent(String name) {
		if (!nameToNodeMap.containsKey(name))
			return null;
		return nameToNodeMap.get(name).getTextContent();
	}

	/**
	 * Tests whether a child node exists.
	 *
	 * @param name name of the child node to test for
	 * @return true if the node exists, false otherwise
	 */
	public boolean hasChild(String name) {
		return nameToNodeMap.containsKey(name);
	}

	/**
	 * Gets the text content of a node. (Wrapper for {@link Node#getTextContent})
	 *
	 * @return text content of this node
	 */
	public String getTextContent() {
		return node.getTextContent();
	}

	/**
	 * Gets an attribute by its name.
	 *
	 * @param name name of the attribute to get
	 * @return Value of the attribute or null if the attribute doesn't exist
	 */
	public String getAttributeByName(String name) {
		Node item = node.getAttributes().getNamedItem(name);
		if (item == null)
			return null;
		return item.getNodeValue();
	}

	/**
	 * Makes sure that this node has certain children. If one or more of them don't exist an error is thrown.
	 *
	 * @param children the children that must exist
	 */
	public void assertChildNodesExist(String... children) {
		boolean[] contained = new boolean[children.length];
		int found = 0;
		for (Map.Entry<String, Node> entry : nameToNodeMap.entrySet())
			for (int i = 0; i < children.length; i++)
				if (children[i].equals(entry.getKey())) {
					contained[i] = true;
					found++;
					break;
				}

		// Everything found -> no error
		if (found == children.length)
			return;

		// Otherwise find the names of the missing nodes and throw an error
		String missing = "";
		for (int i = 0; i < children.length; i++)
			if (!contained[i])
				missing += children[i] + ", ";

		missing = missing.substring(0, missing.length() - 2);
		throw new Error("Configuration part \"" + node.getNodeName() + "\" lacks children: " + missing);
	}

	/**
	 * Makes sure that this node has certain attributes. If one or more of them don't exist an error is thrown.
	 *
	 * @param attributes the attributes that must exist
	 */
	public void assertAttributesExist(String... attributes) {
		NamedNodeMap nnm = node.getAttributes();
		String missing = "";
		for (String s : attributes)
			if (nnm.getNamedItem(s) == null)
				missing += s + ", ";

		// Everything found -> no error
		if (missing.isEmpty())
			return;

		// Otherwise output the names of the missing nodes in an error
		missing = missing.substring(0, missing.length() - 2);
		throw new Error("Configuration part \"" + node.getNodeName() + "\" lacks attributes: " + missing);
	}

	/**
	 * Gets an Iterator for the children contained in the node. Internally a {@link java.util.ArrayList} of ConfigNodes is created and it's iterator gets returned.
	 *
	 * @return iterator over all children
	 */
	@Override
	public Iterator<ConfigNode> iterator() {
		NodeList subNodes = node.getChildNodes();
		ArrayList<ConfigNode> list = new ArrayList<ConfigNode>();
		Node c;
		for (int i = 0; i < subNodes.getLength(); i++) {
			c = subNodes.item(i);
			if (c.getNodeType() == Node.ELEMENT_NODE)
				list.add(new ConfigNode(c));
		}
		return list.iterator();
	}
}