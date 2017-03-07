package actions.psiutils;

import de.unihamburg.masterprojekt2016.traceability.*;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by tilmannstehle on 08.12.16.
 */
public class SwiftParserUtils {


    private static String BLANKS_PATTERN = "(\\t| |/\\*.*\\*/)*";
    public static TokenPosition getMethodPosition (MethodPointer pointer) {

        String methodName = pointer.getName();

        String parameters = getParametersPattern(pointer);
        Pattern functionPattern=Pattern.compile(".*func" + BLANKS_PATTERN + methodName + BLANKS_PATTERN + "\\(" + parameters + "\\).*");
        String path=pointer.getSourceFilePath();
        Pattern functionStartPattern = Pattern.compile(BLANKS_PATTERN + "(public|private|internal|fileprivate|override)*" + BLANKS_PATTERN + "func.*");
        return getTokenPosition(methodName, functionPattern,functionStartPattern, path);
    }

    @NotNull
    private static String getParametersPattern(TraceabilityPointer pointer) {
        List<Parameter> parameterList=null;
        if(pointer instanceof MethodPointer)
        {
            parameterList= ((MethodPointer)pointer).getParameters();
        }
        else if(pointer instanceof ConstructorPointer)
        {
            parameterList= ((ConstructorPointer)pointer).getParameters();
        }
        if(parameterList==null)
        {
            throw new RuntimeException("The given pointer does not have a parameters attribute");
        }
        String parameters = BLANKS_PATTERN;
        for (Parameter parameter : parameterList) {
            parameters += BLANKS_PATTERN + "_?" + BLANKS_PATTERN + parameter.getName() + BLANKS_PATTERN + ":" + BLANKS_PATTERN + parameter.getType() + BLANKS_PATTERN + "," + BLANKS_PATTERN;
        }
        //cut off the trailing ","+blanksPattern
        if (parameters.endsWith(BLANKS_PATTERN + "," + BLANKS_PATTERN)) {
            parameters = parameters.substring(0, parameters.length() - (BLANKS_PATTERN.length() + 1));
        }
        return parameters;
    }

    @NotNull
    private static TokenPosition getTokenPosition(String token, Pattern functionPattern,Pattern functionStartPattern, String path ) {
        String functionHead="";
        int functionHeadLineNumber=0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            int currentLineNumber = 0;
            for (String line; (line = br.readLine()) != null; ) {
                currentLineNumber++;
                if (functionStartPattern.matcher(line).matches()) {
                    functionHead = new String(line);
                    functionHeadLineNumber=currentLineNumber;

                    if (!functionHead.contains("{")) {
                        for (; (line = br.readLine()) != null; ) {
                            currentLineNumber++;
                            functionHead = functionHead + line;
                            if (functionHead.contains("{")) {
                                break;
                            }
                        }
                    }
                    if (functionPattern.matcher(functionHead).matches())
                        break;
                }
            }
            return new TokenPosition(functionHeadLineNumber,functionHead.indexOf(token));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLineOfFile(TraceabilityPointer pointer, int lineNumber)
    {
        try {
            return Files.readAllLines(Paths.get(pointer.getSourceFilePath())).get(lineNumber);
        } catch (IOException e) {
            return null;
        }

    }


    public static TokenPosition getConstructorPosition(ConstructorPointer pointer) {
        String className = pointer.getName();
        String parameters = getParametersPattern(pointer);
        Pattern functionPattern=Pattern.compile(".*init"+BLANKS_PATTERN + "\\(" + parameters + "\\).*");
        String path=pointer.getSourceFilePath();
        Pattern functionStartPattern = Pattern.compile(BLANKS_PATTERN + "(public|private|internal|fileprivate|override)*" + BLANKS_PATTERN + "init.*");
        return getTokenPosition(className, functionPattern,functionStartPattern, path);

    }

    public static TokenPosition getAtributePosition(AttributePointer target) {
        Pattern attributeDeklarationStartPattern = Pattern.compile(".*(var|let)" + BLANKS_PATTERN + target.getName() +".*");
        Pattern attributeDeklarationPattern = Pattern.compile(".*(var|let)" + BLANKS_PATTERN + target.getName() +".*;.*");
        return getTokenPosition(target.getName(), attributeDeklarationStartPattern , attributeDeklarationPattern, target.getSourceFilePath());
    }
}
