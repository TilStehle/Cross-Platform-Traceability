package mappings;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Gerrit Greiert on 06.10.16.
 */
public class MappingNode extends DefaultMutableTreeNode {


    private MappingType mappingType;
    private String javaExpression;
    private String swiftExpression;
    private boolean noGen;

    public MappingNode(MappingType type, String java, String swing, boolean noGen) {

        mappingType = type;
        javaExpression = java;
        swiftExpression = swing;
        this.noGen = noGen;
    }

    public void updateMapping(MappingType type, String java, String swing, boolean noGen) {

        mappingType = type;
        javaExpression = java;
        swiftExpression = swing;
        this.noGen = noGen;
    }

    public String getSwiftExpression() {
        return swiftExpression;
    }

    public MappingType getMappingType() {
        return mappingType;
    }

    public String getJavaExpression() {
        return javaExpression;
    }

    public boolean getNoGen(){ return noGen; }

    @Override
    public String toString() {
        return javaExpression + " -> " + swiftExpression;
    }

    @Override
    public boolean equals(Object object) {

        boolean same = false;

        if (object != null && object instanceof MappingNode) {

            MappingNode node = (MappingNode) object;

            if (this.mappingType == node.mappingType &&
                    this.javaExpression.equals(node.javaExpression) &&
                    this.swiftExpression.equals(node.swiftExpression) &&
                    this.noGen == node.noGen) {
                same = true;
            }
        }

        return same;
    }
}
