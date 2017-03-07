package services;


import de.unihamburg.masterprojekt2016.converter.IConverter;
import preferences.MissingPropertyException;

/**
 * Created by Gerrit Greiert on 20.11.16.
 */
public interface IConverterService {

    IConverter getConverter() throws MissingPropertyException;

    void updateConfigFilePath(String path);
}
