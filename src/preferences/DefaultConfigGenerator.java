package preferences;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gerrit Greiert on 30.10.16.
 */
public class DefaultConfigGenerator {

    /**
     * Generates an instance of the J2Swift config files needed to run the converter in the given directory
     *
     * @param path
     * @return path to the generated config file to be put into the config file field
     */
    public String generateDefaultConfigFiles(String path) throws IOException {

        File configDirectory = new File(path);
        configDirectory.mkdirs();

        InputStream mappingsFileStream = this.getClass().getClassLoader().getResourceAsStream("/defaultconfig/mappings.txt");
        InputStream extensionFileStream = this.getClass().getClassLoader().getResourceAsStream("/defaultconfig/extension.xml");

        File mappingFileDestination = new File(configDirectory + File.separator + "mappings.txt");
        File extensionFileDestination = new File(configDirectory + File.separator + "extension.xml");

        Files.copy(mappingsFileStream, mappingFileDestination.toPath());
        Files.copy(extensionFileStream, extensionFileDestination.toPath());

        writeMainConfigFile(path);


        //return string to the main config file
        return configDirectory + File.separator + "j2swift_config.txt";
    }

    private void writeMainConfigFile(String path) throws IOException {

        File mainConfigFile = new File(path + File.separator + "j2swift_config.txt");
        List<String> lines = new ArrayList<>();

        lines.add("——Config file for J2Swift——");
        lines.add("——Insert file paths to all additional mapping and extension files as well as required libraries——");
        lines.add("");
        lines.add("——Highest occurrence of a mapping in mapping files has priority——");
        lines.add("#mapping " + path + File.separator + "mappings.txt");
        lines.add("");
        lines.add("#extension " + path + File.separator + "extension.xml" + " (hashCode/equals-extension)");
        lines.add("");
        lines.add("——List required library jars using #requiredLibrary——");

        Files.write(mainConfigFile.toPath(), lines);
    }

}
