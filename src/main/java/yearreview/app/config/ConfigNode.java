package yearreview.app.config;

import org.w3c.dom.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

public class ConfigNode implements Iterable<ConfigNode> {
	private final Node node;
	private final HashMap<String, Node> nameToNodeMap;

	public ConfigNode(Node node) {
		this.node = node;
		nameToNodeMap = new HashMap<String, Node>();
		NodeList subNodes = node.getChildNodes();
		Node c;
		for (int i = 0; i < subNodes.getLength(); i++) {
			c = subNodes.item(i);
			if (c.getNodeType() == Node.ELEMENT_NODE)
				nameToNodeMap.put(c.getNodeName(), c);
		}
	}

	public String getName() {
		return node.getNodeName();
	}

	public ConfigNode getChildByName(String name) {
		if (!nameToNodeMap.containsKey(name))
			return null;
		return new ConfigNode(nameToNodeMap.get(name));
	}

	public String getChildContent(String name) {
		if (!nameToNodeMap.containsKey(name))
			return null;
		return nameToNodeMap.get(name).getTextContent();
	}

	public boolean hasChild(String name) {
		return nameToNodeMap.containsKey(name);
	}

	public String getTextContent() {
		return node.getTextContent();
	}

	public String getAttributeByName(String name) {
		return node.getAttributes().getNamedItem(name).getNodeValue();
	}

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