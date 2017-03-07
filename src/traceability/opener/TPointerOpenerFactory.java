package traceability.opener;

/**
 * Created by Gerrit Greiert on 04.01.17.
 */
public class TPointerOpenerFactory {

    public static TPointerOpener createOpener(){

        String osType = System.getProperty("os.name").toLowerCase();

        if (osType.contains("mac")){
            return new MacTPointerOpener(); //TODO: Differentiate between Mac OS with or without XCode
        }
        else if (osType.contains("win")){
            //TODO: Implement an Traceability Pointer Opener for Windows with or without Visual Studio
        }
        else if (osType.contains("nux")){
            //TODO: Implement an Traceability Pointer Opener for Linux
        }

        System.out.println("No valid TPointerOpener found or OS unknown");
        return new DefaultTPointerOpener();

    }
}
