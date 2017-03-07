package preferences.ExtensionUI.XmlEditor;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;
import com.intellij.ui.treeStructure.Tree;
import org.w3c.dom.*;
import preferences.SimpleFilter;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.util.EventObject;

/**
 * Created by Nils-Hendrik Berger on 04.01.17.
 */
public class XmlViewer extends JPanel{

    public static final String APP_NAME = "XML Viewer";

    protected static Icon NEW_ICON = AllIcons.Actions.NewFolder;
    protected static Icon OPEN_ICON = AllIcons.Actions.Menu_open;
    protected static Icon SAVE_ICON = AllIcons.Actions.Menu_saveall;
    protected static Icon SAVE_REPLACE_ICON = AllIcons.RunConfigurations.SaveTempConfig;

    protected static Icon NEW_NODE_ICON = AllIcons.ToolbarDecorator.Add;
    protected static Icon EDIT_NODE_ICON = AllIcons.Actions.Edit;
    protected static Icon DELETE_NODE_ICON = AllIcons.Actions.Delete;

    protected static Icon NEW_ATTR_ICON = AllIcons.ToolbarDecorator.Add;
    protected static Icon EDIT_ATTR_ICON = AllIcons.Actions.Edit;
    protected static Icon DELETE_ATTR_ICON = AllIcons.Actions.Delete;

    protected Document doc;

    protected Tree tree;
    protected DefaultTreeModel model;
    protected DefaultTreeCellEditor treeEditor;
    protected Node editingNode = null;

    protected JBTable table;
    protected AttrTableModel tableModel;

    protected JFileChooser chooser;
    protected File currentFile;
    protected boolean xmlChanged;

    protected JButton addNodeBtn;
    protected JButton editNodeBtn;
    protected JButton delNodeBtn;
    protected JButton addAttrBtn;
    protected JButton editAttrBtn;
    protected JButton delAttrBtn;


    public XmlViewer()
    {
       //super(APP_NAME);
        initXMmlViewer();
    }

    /**
     * Constructs XmlViewer with a given XMl-File
     * @param currentFile
     */
    public XmlViewer(final File currentFile)
    {
        //super(APP_NAME);
        initXMmlViewer();
        this.currentFile = currentFile;
        openDocumentFromFile();
    }

    private void initXMmlViewer()
    {
        setSize(1200,800);
        //getContentPane().setLayout(new BorderLayout());
        setLayout(new BorderLayout());

        JToolBar tb = createToolbar();
        //getContentPane().add(tb, BorderLayout.NORTH);
        add(tb, BorderLayout.NORTH);


        DefaultMutableTreeNode top = new DefaultMutableTreeNode("No XML loaded");
        model = new DefaultTreeModel(top);
        tree = new Tree(model);

        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.setEditable(false);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer()
        {
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
            {
                Component res = super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);
                if(value instanceof XmlViewerNode)
                {
                    Node node = ((XmlViewerNode)value).getXmlNode();
                    if(node instanceof Element)
                    {
                        setIcon(expanded ? openIcon : closedIcon);
                    }
                    else
                        setIcon(leafIcon);
                }
                return res;
            }
        };
        tree.setCellRenderer(renderer);
        treeEditor = new DefaultTreeCellEditor(tree, renderer)
        {

            public boolean isCellEditable(EventObject event) {
                Node node = getSelectedNode();
                if(node != null && node.getNodeType() == Node.TEXT_NODE)
                    return super.isCellEditable(event);
                else
                    return false;
            }


            public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
                if(value instanceof XmlViewerNode)
                    editingNode = ((XmlViewerNode)value).getXmlNode();
                return super.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row);
            }
        };
        treeEditor.addCellEditorListener(new XmlEditorListener());
        tree.setCellEditor(treeEditor);
        tree.setEditable(true);
        tree.setInvokesStopCellEditing(true);

        tableModel = new AttrTableModel();
        table = new JBTable(tableModel);

        JScrollPane scrollPaneTree = new JBScrollPane(tree);
        JScrollPane scrollPaneTable = new JBScrollPane(table);

        scrollPaneTable.getViewport().setBackground(table.getBackground());
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPaneTree, scrollPaneTable);
        sp.setDividerLocation(800);
        sp.setDividerSize(5);
        //getContentPane().add(sp,BorderLayout.CENTER);
        add(sp,BorderLayout.CENTER);

        TreeSelectionListener lstSel = new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Node node = getSelectedNode();
                setNodeToTable(node);
                enableNodeButtons();
                enableAttrButtons();
            }
        };
        tree.addTreeSelectionListener(lstSel);

        ListSelectionListener lTBl = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                enableAttrButtons();
            }
        };
        table.getSelectionModel().addListSelectionListener(lTBl);
        enableNodeButtons();
        enableAttrButtons();

        //JScrollPane scrollPane = new JBScrollPane(tree);
        //getContentPane().add(scrollPane, BorderLayout.CENTER);

        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setFileFilter(new SimpleFilter("xml", "XML FILES"));
        File dir = (new File(System.getProperty("user.home")));
        chooser.setCurrentDirectory(dir);
    }

    /**
     *
     * @return
     */
    protected JToolBar createToolbar()
    {
        JToolBar tb = new JToolBar();
        tb.setFloatable(false);

        JButton bt = new JButton(NEW_ICON);
        bt.setToolTipText("New XML document");
        ActionListener lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!promptToSave())
                    return;
                newDocument();
            }
        };
        bt.addActionListener(lst);
        tb.add(bt);

        bt = new JButton(OPEN_ICON);
        bt.setToolTipText("Open Xml file");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!promptToSave())
                    return;
                openDocument();
            }
        };
        bt.addActionListener(lst);
        tb.add(bt);

        bt = new JButton(SAVE_ICON);
        bt.setToolTipText("Save changes to current file");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(false);
            }
        };
        bt.addActionListener(lst);
        tb.add(bt);

        bt = new JButton(SAVE_REPLACE_ICON);
        bt.setToolTipText("Save changes to another file");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile(true);
            }
        };
        bt.addActionListener(lst);
        tb.add(bt);

        //Node buttons
        tb.addSeparator();
        tb.add(new JBLabel("Node: "));
        addNodeBtn = new JButton(NEW_NODE_ICON);
        addNodeBtn.setToolTipText("Add new XML element");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewNode();
            }
        };
        addNodeBtn.addActionListener(lst);
        tb.add(addNodeBtn);

        editNodeBtn = new JButton(EDIT_NODE_ICON);
        editNodeBtn.setToolTipText("Edit XML node");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editNode();
            }
        };
        editNodeBtn.addActionListener(lst);
        tb.add(editNodeBtn);

        delNodeBtn = new JButton(DELETE_NODE_ICON);
        delNodeBtn.setToolTipText("Edit XML node");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteNode();
            }
        };
        delNodeBtn.addActionListener(lst);
        tb.add(delNodeBtn);

        //Buttons attr
        tb.addSeparator();
        tb.add(new JBLabel("Attr: "));

        addAttrBtn = new JButton(NEW_ATTR_ICON);
        addAttrBtn.setToolTipText("Add new attribute");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewAttribute();
            }
        };
        addAttrBtn.addActionListener(lst);
        tb.add(addAttrBtn);

        editAttrBtn = new JButton(EDIT_ATTR_ICON);
        editAttrBtn.setToolTipText("Edit attribute");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editAttribute();
            }
        };
        editAttrBtn.addActionListener(lst);
        tb.add(editAttrBtn);

        delAttrBtn = new JButton(DELETE_ATTR_ICON);
        delAttrBtn.setToolTipText("Delete attribute");
        lst = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteAttribute();
            }
        };
        delAttrBtn.addActionListener(lst);
        tb.add(delAttrBtn);


        return tb;
    }

    public String getDocumentName()
    {
        return currentFile == null ? "Untitled" : currentFile.getName();
    }

    /**
     * Opens a file without a JFileChooser
     */
    protected void openDocumentFromFile()
    {
        Thread thread = new Thread()
        {
            public void run(){

                File f = currentFile;
                if (f == null|| !f.isFile()) {
                    return;
                }
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try
                {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                    doc = documentBuilder.parse(f);

                    Element root = doc.getDocumentElement();
                    root.normalize();

                    DefaultMutableTreeNode top = createTreeNode(root);

                    model.setRoot(top);
                    tree.treeDidChange();
                    expandTree(tree);
                    setNodeToTable(null);
                    currentFile = f;
                    //setTitle(APP_NAME+"[" + getDocumentName()+ "]");

                }catch(Exception ex)
                {
                    showError(ex, "Error reading or parsing XML file");
                }
                finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        thread.start();
    }

    /**
     * Shows out JFileChooser in a seperate thread to allow selection of an XML file to open for viewing.
     */
    protected void openDocument()
    {
        Thread thread = new Thread()
        {
            public void run(){
                if(chooser.showOpenDialog(XmlViewer.this) != JFileChooser.APPROVE_OPTION)
                {
                    return;
                }
                File f = chooser.getSelectedFile();
                if (f == null|| !f.isFile()) {
                    return;
                }
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try
                {
                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                    doc = documentBuilder.parse(f);

                    Element root = doc.getDocumentElement();
                    root.normalize();

                    DefaultMutableTreeNode top = createTreeNode(root);

                    model.setRoot(top);
                    tree.treeDidChange();
                    expandTree(tree);
                    setNodeToTable(null);
                    currentFile = f;
                    //setTitle(APP_NAME+"[" + getDocumentName()+ "]");

                }catch(Exception ex)
                {
                    showError(ex, "Error reading or parsing XML file");
                }
                finally {
                    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        };
        thread.start();
    }

    /**
     * Uses JOptionPane to prompt the user for a root node name, creating a Document instance with a root element of that name
     * creating a TreeNode structure using that element, and finally assigning that tree node structure as the tree model
     */
    public void newDocument()
    {
        String input = (String) JOptionPane.showInputDialog(this, "Please enter root node name of the new XML document", APP_NAME,JOptionPane.PLAIN_MESSAGE,null,null,"");
        if(!isLegalXmlName(input))
            return;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try{
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            doc = documentBuilder.newDocument();

            Element root = doc.createElement(input);
            root.normalize();
            doc.appendChild(root);

            DefaultMutableTreeNode top = createTreeNode(root);

            model.setRoot(top);
            tree.treeDidChange();
            expandTree(tree);
            setNodeToTable(null);
            currentFile = null;
            //setTitle(APP_NAME +" ["+getDocumentName()+"]");
            xmlChanged = true;
        }catch(Exception ex)
        {
            showError(ex, "Error creating new Extension-XML document");
        }
        finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    /**
     * Enables the add attribute Button only if the selected node is an instance of Element. It enables the edit node and
     * delete node buttons onyl if the selected node is not null
     */
    protected void enableNodeButtons()
    {
        boolean b1 = (getSelectedNode() instanceof Element);
        boolean b2 = (getSelectedNode() != null);
        addNodeBtn.setEnabled(b1);
        editNodeBtn.setEnabled(b2);
        delNodeBtn.setEnabled(b2);
    }

    /**
     * Enables the add attribute button only of the slected node is an instance of Element. It enables the edit
     * attribute and delete buttons only if ther is a table row selected
     */
    protected void enableAttrButtons()
    {
        boolean b1 = (tableModel.getNode() instanceof Element);
        boolean b2 = (table.getSelectedRowCount() > 0);
        addAttrBtn.setEnabled(b1);
        editAttrBtn.setEnabled(b2);
        delAttrBtn.setEnabled(b2);
    }

    /**
     * Takes a root node as a parameter. Our canDisplayNode()-Method is used to find out whether the root node is either an element or a text node.
     * @param root
     * @return
     */
    protected DefaultMutableTreeNode createTreeNode(Node root)
    {
        if(!canDisplayNode(root))
        {
            return null;
        }
        XmlViewerNode treeNode = new XmlViewerNode(root);
        NodeList list = root.getChildNodes();
        for(int k = 0; k<list.getLength(); k++)
        {
            Node nd = list.item(k);
            DefaultMutableTreeNode child = createTreeNode(nd);
            if(child != null)
            {
                treeNode.add(child);
            }
        }
        return treeNode;
    }

    protected boolean canDisplayNode(Node node)
    {
        switch(node.getNodeType()) {
            case Node.ELEMENT_NODE:
                return true;
            case Node.TEXT_NODE:
                String text = node.getTextContent().trim();
                return !(text.equals("")) || text.equals("\r\n");
        }
        return false;
    }
    public static void expandTree(JTree tree)
    {
        TreeNode root = (TreeNode) tree.getModel().getRoot();
        TreePath path = new TreePath(root);
        for(int k = 0; k < root.getChildCount(); k++)
        {
            TreeNode child = root.getChildAt(k);
            expandTree(tree, path, child);
        }
    }

    public static void expandTree(JTree tree, TreePath path, TreeNode node)
    {
        if(path == null || node == null)
                return;
        tree.expandPath(path);
        TreePath newPath = path.pathByAddingChild(node);
        for(int k = 0; k<node.getChildCount(); k++)
        {
            TreeNode child = node.getChildAt(k);
            if(child!= null)
            {
                expandTree(tree, newPath,child);
            }
        }
    }

    /**
     * Used to display exceptions in a JOptionsPane dialog.
     * @param ex
     * @param message
     */
    public void showError(Exception ex, String message)
    {
     ex.printStackTrace();
        JOptionPane.showMessageDialog(this,message,APP_NAME,JOptionPane.WARNING_MESSAGE);
    }


    public void bringToFront()
    {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //toFront();
                setEnabled(true);
                //setFocusableWindowState(true);
                
            }
        });
    }

    /**
     * Retrives te currently selected node by calling node and returns it as an instance of XMLViewerNode
     * @return
     */
    public XmlViewerNode getSelectedTreeNode()
    {
        TreePath path = tree.getSelectionPath();
        if(path == null)
        {
            return null;
        }
        Object obj = path.getLastPathComponent();
        if(!(obj instanceof  XmlViewerNode))
        {
            return null;
        }
        return (XmlViewerNode) obj;
    }
    /**
     * Retrives te currently selected node by calling getSelectedTreeNode()
     * @return
     */
    public Node getSelectedNode()
    {
        XmlViewerNode treeNode = getSelectedTreeNode();
        if(treeNode == null)
        {
            return null;
        }
        return treeNode.getXmlNode();
    }

    /**
     * Assigns a given node to the node-variable and reinitializes the attrs collection using Node's getAttributes() method
     * @param node
     */
    public void setNodeToTable(Node node)
    {
        tableModel.setNode(node);
        table.tableChanged(new TableModelEvent(tableModel));
    }

    /**
     * Used to write to disk the XML file represented by our tree. First this method shows a file chooser of the
     * parameter is true of if the currentFile  variable is null.
     *
     * The file chooser is specify a name and location for the target file. The document is then written
     * to disk using a FileWriter an the static writer() method of our custom XMLRoutines class.
     * @param saveAs
     * @return
     */
    protected boolean saveFile(boolean saveAs)
    {
        if(doc == null)
            return false;
        if(saveAs || currentFile == null)
        {
            if(chooser.showSaveDialog(XmlViewer.this) != JFileChooser.APPROVE_OPTION)
            {
                return false;
            }
            File f = chooser.getSelectedFile();
            if(f == null)
                return false;
            currentFile = f;
            //setTitle(APP_NAME+" ["+getDocumentName()+"]");
        }
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try
        {
            FileWriter out = new FileWriter(currentFile);
            XMLRoutines.write(doc,out);
            out.close();
        }
        catch(Exception ex)
        {
            showError(ex ,"Error saving XML file");
        }
        finally {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        xmlChanged = false;
        return true;
    }

    /**
     * Shows a JOptionPane asking the user whether or not to save any changes made to the
     * document before proceeding with a pending action
     * @return
     */
    protected boolean promptToSave()
    {
        if(!xmlChanged)
            return true;
        int result = JOptionPane.showConfirmDialog(this, "Save changes to " + getDocumentName() +"?", APP_NAME, JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE);
        switch(result){
            case JOptionPane.YES_OPTION:
                return saveFile(false);
            case JOptionPane.NO_OPTION:
                return true;
            case JOptionPane.CANCEL_OPTION:
                return false;
        }
        return true;
    }

    /**
     * Performs basic XML and Swing tree and table operations
     */
    protected void addNewNode()
    {
        if(doc == null)
            return;
        XmlViewerNode treeNode = getSelectedTreeNode();
        if(treeNode == null)
            return;
        Node parent = treeNode.getXmlNode();
        if(parent == null)
            return;
        String input = (String) JOptionPane.showInputDialog(this,"Please enter name of the new XML node", APP_NAME, JOptionPane.PLAIN_MESSAGE,null,null,"");
        if(!isLegalXmlName(input))
            return;
        try
        {
            Element newElement = doc.createElement(input);
            XmlViewerNode nodeElement = new XmlViewerNode(newElement);
            treeNode.addXmlNode(nodeElement);

            model.nodeStructureChanged(treeNode);
            TreePath path = tree.getSelectionPath();
            if(path != null) {
                path = path.pathByAddingChild(nodeElement);
                tree.setSelectionPath(path);
                tree.scrollPathToVisible(path);
            }
            xmlChanged = true;
        }catch(Exception ex)
        {
            showError(ex, "Error adding new node");
        }
    }
    private void addNewAttribute()
    {
        Node node = tableModel.getNode();
        if(!(node instanceof Element))
            return;

        String input = (String)JOptionPane.showInputDialog(this,"Please enter new attribute name",APP_NAME,JOptionPane.PLAIN_MESSAGE,null,null,"");
        if(!isLegalXmlName(input))
            return;

        try
        {
            ((Element)node).setAttribute(input,"");
            setNodeToTable(node);
            for(int k = 0; k < tableModel.getRowCount(); k++)
            {
                if(tableModel.getValueAt(k,AttrTableModel.NAME_COLUMN).equals(input))
                {
                    table.editCellAt(k,AttrTableModel.VALUE_COLUMN);
                    break;
                }
            }
            xmlChanged = true;

        }catch (Exception ex)
        {
            showError(ex, "Error adding attribute");
        }
    }
    /**
     * Performs basic XML and Swing tree and table operations
     */
    protected void editNode()
    {
        TreePath path = tree.getSelectionPath();
        XmlViewerNode treeNode = getSelectedTreeNode();
        if(treeNode == null)
            return;
        Node node = treeNode.getXmlNode();
        if(node == null)
            return;
        try{
            switch (node.getNodeType())
            {
                case Node.ELEMENT_NODE:
                    for(int k = 0; k < treeNode.getChildCount(); k++)
                    {
                        XmlViewerNode childNode = (XmlViewerNode) treeNode.getChildAt(k);
                        Node nd = childNode.getXmlNode();
                        if(nd instanceof Text)
                            path = path.pathByAddingChild(childNode);
                            tree.setSelectionPath(path);
                            tree.scrollPathToVisible(path);
                            tree.startEditingAtPath(path);
                            return;
                    }
                    Text text = doc.createTextNode("");
                    XmlViewerNode nodeText = new XmlViewerNode(text);
                    treeNode.addXmlNode(nodeText);
                    model.nodeStructureChanged(treeNode);
                    path = path.pathByAddingChild(nodeText);
                    tree.setSelectionPath(path);
                    tree.scrollPathToVisible(path);
                    tree.startEditingAtPath(path);
                    return;

                case Node.TEXT_NODE:
                    tree.startEditingAtPath(path);
                    return;
            }
        }catch (Exception ex)
        {
            showError(ex, "Error editing node");
        }
    }

    /**
     * Performs basic XML and Swing tree and table operations
     */
    protected void editAttribute()
    {
        int row = table.getSelectedRow();
        if(row >= 0)
            table.editCellAt(row, AttrTableModel.VALUE_COLUMN);
    }

    /**
     * Performs basic XML and Swing tree and table operations
     */
    protected void deleteNode()
    {
        TreePath path = tree.getSelectionPath();
        XmlViewerNode treeNode = getSelectedTreeNode();
        if(tree == null)
            return;
        Node node = treeNode.getXmlNode();
        if(node == null)
            return;
        int result = JOptionPane.showConfirmDialog(XmlViewer.this, "Delete node" + node.getNodeName() +"?", APP_NAME, JOptionPane.YES_NO_OPTION);
        if(result != JOptionPane.YES_OPTION)
            return;
        try
        {
            TreeNode treeParent = treeNode.getParent();
            treeNode.remove();
            model.nodeStructureChanged(treeParent);
            xmlChanged = true;
        }catch(Exception ex)
        {
            showError(ex, "Error deleting node");
        }
    }

    /**
     * Performs basic XML and Swing tree and table operations
     */
    protected void deleteAttribute()
    {
        int row = table.getSelectedRow();
        if(row < 0)
            return;
        Node node = getSelectedNode();
        if(!(node instanceof Element))
            return;

        String name = (String) tableModel.getValueAt(row,AttrTableModel.NAME_COLUMN);
        int result = JOptionPane.showConfirmDialog(XmlViewer.this, "Delete attribute" + name + "?", APP_NAME,JOptionPane.YES_NO_OPTION);
        if(result != JOptionPane.YES_OPTION)
            return;

        try
        {
            ((Element)node).removeAttribute(name);
            setNodeToTable(node);
            xmlChanged = true;
        }catch(Exception ex)
        {
            showError(ex, "Error deleting attribute");
        }
    }

    /**
     * Takes a String parameter and returns a boolean flag specifying whether or not the String legal according
     * to XMLRoutine's static isLegalXmlName()-method. If it is not legal a warning message is displayed.
     * @param input
     * @return
     */
    public boolean isLegalXmlName(String input)
    {
        if(input==null||input.length() ==0)
            return false;
        if(!(XMLRoutines.isLegalXMLName(input)))
            JOptionPane.showMessageDialog(this,"Invalid XML name",APP_NAME, JOptionPane.WARNING_MESSAGE);
        return true;
    }

    /**
     * The isCellEditable() method is overriden to return true for the VALUE_COLUMN so the user can edit attribute values
     * The setValueAt() method is overriden to perform the actual attribute value modification after the edit occurs in the table
     */
    class AttrTableModel extends AbstractTableModel {
        public static final int NAME_COLUMN = 0;
        public static final int VALUE_COLUMN = 1;

        protected Node node;
        protected NamedNodeMap attrs;

        public void setNode(Node node) {
            this.node = node;
            attrs = node == null ? null : node.getAttributes();
        }

        public Node getNode()
        {
            return node;
        }

        public int getRowCount()
        {
            if(attrs == null)
            {
                return 0;
            }
            return attrs.getLength();
        }

        public int getColumnCount()
        {
            return 2;
        }

        public String getColumnName(int col )
        {
            return col==NAME_COLUMN ? "Attribute" : "Value";
        }

        /**
         * Method is overridden to perform the acutal attribute value modification after the edit occurs in the table
         * @param row
         * @param col
         * @return
         */
        public Object getValueAt(int row, int col)
        {
            if(attrs ==null || row < 0 || row >= getRowCount())
            {
                return "";
            }
            Attr attr = (Attr)attrs.item(row);
            if(attr == null)
            {
                return "";
            }
            switch (col) {
                case NAME_COLUMN:
                    return attr.getName();
                case VALUE_COLUMN:
                    return attr.getValue();
            }
            return "";
        }

        /**
         * Method is overridden to return true for the VALUE_COLUMN so that the user can edit attribute values
         * @param row
         * @param col
         * @return
         */
        public boolean isCellEditable(int row, int col)
        {
            return (col == VALUE_COLUMN);
        }


        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if(rowIndex < 0 || rowIndex >=getRowCount())
            {
                return;
            }
            if(!(node instanceof Element))
            {
                return;
            }
            String name = getValueAt(rowIndex, NAME_COLUMN).toString();
            ((Element) node).setAttribute(name, aValue.toString());
            xmlChanged = true;
        }
    }

    /**
     * This class implements CellEditorListener and is used to set the value of a Node after its corresponding tree node is edited
     */
    class XmlEditorListener implements CellEditorListener {

        public void editingStopped(ChangeEvent e) {
            String value = treeEditor.getCellEditorValue().toString();
            if(editingNode != null)
                editingNode.setNodeValue(value);
            TreePath path = tree.getSelectionPath();
            if(path != null)
            {
                DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                treeNode.setUserObject(editingNode);
                model.nodeStructureChanged(treeNode);
            }
            xmlChanged = true;
            editingNode = null;
        }

        public void editingCanceled(ChangeEvent e) {
            editingNode = null;
        }
    }
}


