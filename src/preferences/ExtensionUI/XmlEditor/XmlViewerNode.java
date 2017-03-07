package preferences.ExtensionUI.XmlEditor;

import org.w3c.dom.Node;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * This class extends DefaultMutableTreeNode to represent the XML nodes in our viewer.
 * Created by Nils-Hendrik Berger on 04.01.17.
 */
public class XmlViewerNode extends DefaultMutableTreeNode {
    public XmlViewerNode(Node node)
    {
        super(node);
    }

    public Node getXmlNode()
    {
        Object object = getUserObject();
        if(object instanceof Node)
        {
            return (Node) object;
        }
        return null;
    }

    /**
     * To enable adding a child node to an existing node
     * @param child
     * @throws Exception
     */
    public void addXmlNode(XmlViewerNode child) throws Exception
    {
        Node node = getXmlNode();
        if(node == null)
        {
            throw new Exception("Corrupted XML node");
        }
        node.appendChild(child.getXmlNode());
        add(child);
    }

    /**
     * Added to allow removal of nodes. Nodes without a parent cannot be deleted
     * @throws Exception
     */
    public void remove() throws  Exception
    {
        Node node = getXmlNode();
        if(node == null)
            throw new Exception("Corrupted XML node");
        Node parent = node.getParentNode();
        if(parent == null)
            throw new Exception("Cannot remove root node");
        TreeNode treeParent = getParent();
        if(!(treeParent instanceof  DefaultMutableTreeNode))
            throw new Exception("Cannot remove tree node");
        parent.removeChild(node);
        ((DefaultMutableTreeNode)treeParent).remove(this);

    }

    /**
     * Returns a rextual representation of the node depending whether it is a type ELEMENT_NODE or TEXT_NODE
     * @return
     */
    public String toString()
    {
        Node node = getXmlNode();
        if(node == null)
        {
            return  getUserObject().toString();
        }
        StringBuffer sb = new StringBuffer();
        switch (node.getNodeType())
        {
            case Node.ELEMENT_NODE:
                sb.append("<");
                sb.append(node.getNodeName());
                sb.append(">");
                break;
            case Node.TEXT_NODE:
                sb.append(node.getNodeValue());
                break;
        }
        return sb.toString();
    }
}
