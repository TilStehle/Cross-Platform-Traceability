package actions;

import actions.psiutils.SwiftParserUtils;
import actions.psiutils.TokenPosition;
import com.intellij.codeInsight.codeFragment.Position;
import com.intellij.openapi.editor.markup.LineMarkerRendererEx;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.ui.Messages;
import de.unihamburg.masterprojekt2016.traceability.AttributePointer;
import de.unihamburg.masterprojekt2016.traceability.ConstructorPointer;
import de.unihamburg.masterprojekt2016.traceability.MethodPointer;
import de.unihamburg.masterprojekt2016.traceability.Parameter;
import preferences.MissingPropertyException;
import preferences.Settings;
import preferences.SettingsPropertyProvider;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Tilmann Stehle on 24.11.2016.
 */
public class XCodeController {



    public static void openXCodeAtLine(String path, int lineNumber) {

        Runtime runtime = Runtime.getRuntime();
        String appleScript = "tell application \"Xcode\"\n" +
                "\topen \"" + path + "\"\n" +
                "\tactivate\n" +
                "\ttell application \"System Events\"\n" +
                "\t\ttell process \"Xcode\"\n" +
                "\t\t\tkeystroke \"l\" using command down\n" +
                "\t\t\trepeat until window \"Open Quickly\" exists\n" +
                "\t\t\tend repeat\n" +
                "\t\t\tset value of text field 1 of window \"Open Quickly\" to \"" + lineNumber + "\"\n" +
                "\t\t\t--set winstuff to entire contents of front window\n" +
                "\t\t\t--return winstuff -- comment this out too to get just menustuff\n" +
                "\t\t\tkeystroke return\n" +
                "\t\tend tell\n" +
                "\tend tell\n" +
                "end tell";
        String[] args = {"osascript", "-e", appleScript};
        try {
            runtime.exec(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static void openXCodeAtMethod(MethodPointer pointer) {


        TokenPosition methodPosition = SwiftParserUtils.getMethodPosition(pointer);
        String projectFilePath = SettingsPropertyProvider.getTargetProjectFilePath();
        if(projectFilePath==null ||"".equals(projectFilePath)) {
           openXCodeAtLine(pointer.getSourceFilePath(), methodPosition.getLine());
        }
        else{
            String typeName = pointer.getTypePointer().getFullyQualifiedName();
            typeName=typeName.substring(typeName.lastIndexOf('.') + 1);
            openXCodeProjectAtTypeAndLine(projectFilePath,typeName, methodPosition.getLine());
        }
    }

    public static void openXCodeProjectAtTypeAndLine(String projectFilePath, String typeName, int lineNumber) {
        Runtime runtime = Runtime.getRuntime();
        String appleScript = "tell application \"Xcode\"\n" +
                "\topen \""+projectFilePath+"\"\n" +
                "\tactivate\n" +
                "\ttell application \"System Events\"\n" +
                "\t\ttell process \"Xcode\"\n" +
                "\t\t\t\n" +
                "\t\t\t--open file containing class declaration\n" +
                "\t\t\tkeystroke \"o\" using {shift down, command down}\n" +
                "\t\t\trepeat until window \"Open Quickly\" exists\n" +
                "\t\t\tend repeat\n" +
                "\t\t\tset value of text field 1 of window \"Open Quickly\" to \""+typeName+"\"\n" +
                "\t\t\tdelay 0.5\n" +
                "\t\t\tkeystroke return\n" +
                "\t\t\t\n" +
                "\t\t\t--navigate to method line\n" +
                "\t\t\tkeystroke \"l\" using command down\n" +
                "\t\t\trepeat until window \"Open Quickly\" exists\n" +
                "\t\t\tend repeat\n" +
                "\t\t\tset value of text field 1 of window \"Open Quickly\" to \""+lineNumber+"\"\n" +
                "\t\t\tkeystroke return\t\n" +
                "\t\tend tell\n" +
                "\tend tell\n" +
                "end tell";
        String[] args = {"osascript", "-e", appleScript};
        try {
            runtime.exec(args);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void renameMethod(String typeName, String methodName, TokenPosition methodPosition, String newMethodName) {
        String projectFilePath = SettingsPropertyProvider.getTargetProjectFilePath();
        if(projectFilePath==null ||"".equals(projectFilePath)) {
            int answer = Messages.showOkCancelDialog("Please configure an Xcode project path. Would you like to open the " +
                    "preferences?", "Missing Xcode project path", Messages.getQuestionIcon());
            if (answer == 0)
            {

                ShowSettingsUtil.getInstance().showSettingsDialog(null, Settings.class);
            }
        }

        Runtime runtime = Runtime.getRuntime();
        StringBuilder builder = new StringBuilder("");
        InputStream renameScriptStream = XCodeController.class.getClassLoader().getResourceAsStream("applescripts/rename.script");
        try (Scanner scanner = new Scanner(renameScriptStream)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                builder.append(line).append("\n");
            }

            scanner.close();

        }
        String script=builder.toString();
        script=script.replace("$methodName", methodName);
        script=script.replace("$newMethodName", newMethodName);
        script=script.replace("$typeName", typeName);
        script=script.replace("$methodLine", ""+methodPosition.getLine());
        script=script.replace("$methodColumn", ""+methodPosition.getColumn());
        script=script.replace("$xcodePath", projectFilePath);
        String[] args = {"osascript", "-e", script};
        try {
            runtime.exec(args).waitFor();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void openXCodeAtConstructor(ConstructorPointer pointer) {

        TokenPosition methodPosition = SwiftParserUtils.getConstructorPosition(pointer);
        String projectFilePath = SettingsPropertyProvider.getTargetProjectFilePath();
        if(projectFilePath==null ||"".equals(projectFilePath)) {
            openXCodeAtLine(pointer.getSourceFilePath(), methodPosition.getLine());
        }
        else{
            String typeName = pointer.getTypePointer().getFullyQualifiedName();
            typeName=typeName.substring(typeName.lastIndexOf('.') + 1);
            openXCodeProjectAtTypeAndLine(projectFilePath,typeName, methodPosition.getLine());
        }
    }

    public static void openXCodeAtAttribute(AttributePointer target) {
        TokenPosition methodPosition = SwiftParserUtils.getAtributePosition(target);
        String projectFilePath = SettingsPropertyProvider.getTargetProjectFilePath();
        if(projectFilePath==null ||"".equals(projectFilePath)) {
            openXCodeAtLine(target.getSourceFilePath(), methodPosition.getLine());
        }
        else{
            String typeName = target.getTypePointer().getFullyQualifiedName();
            typeName=typeName.substring(typeName.lastIndexOf('.') + 1);
            openXCodeProjectAtTypeAndLine(projectFilePath,typeName, methodPosition.getLine());
        }
    }
}
