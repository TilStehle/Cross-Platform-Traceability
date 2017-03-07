package services;

import com.intellij.openapi.components.ServiceManager;

import de.unihamburg.masterprojekt2016.converter.IConverter;
import de.unihamburg.masterprojekt2016.converter.j2swift.J2Swift;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;

/**
 * Created by Gerrit Greiert on 20.11.16.
 */
public class J2SwiftService implements IConverterService {

    private IConverter j2Swift = null;

    @Override
    public IConverter getConverter() throws MissingPropertyException{

        if (j2Swift == null){

            j2Swift = new J2Swift(SettingsPropertyProvider.getConfigPath());
            j2Swift.setTraceabilityModel(ServiceManager.getService(TraceabilityService.class).getTraceabilityModel());
        }

        return j2Swift;
    }

    @Override
    public void updateConfigFilePath(String path) {

        if (j2Swift == null){
            j2Swift = new J2Swift(path);
        }
        else{
            j2Swift.changeConfigurationFiles(path);
        }

    }
}
