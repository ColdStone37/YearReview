package yearreview.app.config;

import org.w3c.dom.*;

import java.util.HashMap;
import java.util.Map;

public class ConfigNode {
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
}