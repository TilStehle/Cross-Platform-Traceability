package services;

import com.intellij.ide.util.PropertiesComponent;
import de.unihamburg.masterprojekt2016.traceability.TraceabilityModel;
import preferences.MissingPropertyException;
import preferences.SettingsPropertyProvider;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Observable;

/**
 * Created by Tilmann Stehle on 11.11.2016.
 */
public class TraceabilityService extends Observable implements ITraceabilityService {

    private static final String xmlFileName = "TraceabilityModel.xml";
    private TraceabilityModel traceabilityModel;

    public TraceabilityService()
    {
        try {
            traceabilityModel = importModelFromXML();
        } catch (JAXBException e) {
            traceabilityModel = new TraceabilityModel();
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }

    }

    @Override
    public TraceabilityModel getTraceabilityModel() {

        if (traceabilityModel == null){
            try {
                traceabilityModel = importModelFromXML();
            } catch (JAXBException e) {
                e.printStackTrace();
            } catch (MissingPropertyException e) {
                e.printStackTrace();
            }
        }
        return traceabilityModel;
    }

    @Override
    public void setTraceabilityModel(TraceabilityModel traceabilityModel) {

        this.traceabilityModel = traceabilityModel;

        try {
            exportModelToXML(traceabilityModel);
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }

        setChanged();
        notifyObservers();
    }

    public void setTraceabilityModelFromConfiguredFile(){

        try {
            traceabilityModel = importModelFromXML();
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }

        setChanged();
        notifyObservers();
    }

    private TraceabilityModel importModelFromXML() throws JAXBException, MissingPropertyException {

        String targetPath = SettingsPropertyProvider.getTraceabilityFilePath();

        File file = new File(targetPath + File.separator + xmlFileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(TraceabilityModel.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (TraceabilityModel) jaxbUnmarshaller.unmarshal(file);
    }

    private void exportModelToXML(TraceabilityModel traceabilityModel) throws JAXBException, MissingPropertyException {

        String targetPath = SettingsPropertyProvider.getTraceabilityFilePath();

        File file = new File(targetPath + File.separator + xmlFileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(TraceabilityModel.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(traceabilityModel, file);
    }

}
