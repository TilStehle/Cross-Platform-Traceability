package mappings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerrit Greiert on 31.10.16.
 * This connector connects a MappingFileNode to the corresponding file. It manages read and write operations on the file.
 */
public class MappingFileConnector {

    private final File file;

    private List<String> lines;

    public MappingFileConnector(File file) throws IOException {
        this.file = file;

        lines = new ArrayList<>(Files.readAllLines(file.toPath()));
    }

    /**
     * Get all valid mappings in the mapping file
     * @return
     * @throws FileNotFoundException
     */
    public ArrayList<MappingNode> getAllMappingsList() throws FileNotFoundException {

        ArrayList<MappingNode> mappingNodes = new ArrayList<MappingNode>();

        for (String line : lines) {

            String[] parts = line.split("\\s+");

            boolean noGen = false;

            if (parts.length > 3 && parts[3].equals("-noGen"))
                noGen = true;

            switch (parts[0]) {
                case "#type":
                    mappingNodes.add(new MappingNode(MappingType.TYPE, parts[1], parts[2], noGen));
                    break;
                case "#modifier":
                    mappingNodes.add(new MappingNode(MappingType.MODIFIER, parts[1], parts[2], noGen));
                    break;
                case "#method":
                    mappingNodes.add(new MappingNode(MappingType.METHOD, parts[1], parts[2], noGen));
                    break;
                default:
                    break;
            }
        }

        return mappingNodes;
    }

    /**
     * Adds the given to the mapping file
     * @param mapping
     * @throws IOException
     */
    public void addMappingToFile(MappingNode mapping) throws IOException {

        lines.add(createMappingStringFromMapping(mapping));
        writeFile();

    }

    /**
     * Replaces the old mapping with the new (updates a mapping) in the mapping file
     * @param oldMapping
     * @param newMapping
     * @throws IOException
     */
    public void editMappingInFile(MappingNode oldMapping, MappingNode newMapping) throws IOException {

        lines.set(getMappingIndex(oldMapping), createMappingStringFromMapping(newMapping));
        writeFile();
    }

    /**
     * Deletes the given mapping from the mapping file
     * @param mapping
     * @throws IOException
     */
    public void deleteMappingFromFile(MappingNode mapping) throws IOException {

        lines.remove(getMappingIndex(mapping));
        writeFile();
    }

    private String createMappingStringFromMapping(MappingNode mapping){

        String typeString = null;

        switch (mapping.getMappingType()){
            case TYPE: typeString = "#type";
                break;
            case METHOD: typeString = "#method";
                break;
            case MODIFIER: typeString = "#modifier";
                break;
        }

        String mappingLine = typeString + " " + mapping.getJavaExpression() + " " + mapping.getSwiftExpression();

        if (mapping.getNoGen() == true)
            mappingLine = mappingLine + " -noGen";

        return mappingLine;
    }

    private int getMappingIndex (MappingNode mapping){
        int index = -1;

        for (String line: lines) {
            if (line.contains(mapping.getJavaExpression()) && line.contains(mapping.getSwiftExpression())){
                index = lines.indexOf(line);
            }
        }

        return index;
    }

    private void writeFile() throws IOException {
        Files.write(file.toPath(), lines);
    }
}
