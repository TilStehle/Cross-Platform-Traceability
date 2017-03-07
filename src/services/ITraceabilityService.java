package services;

import de.unihamburg.masterprojekt2016.traceability.TraceabilityModel;

/**
 * Created by Tilmann Stehle on 11.11.2016.
 */
public interface ITraceabilityService {
    TraceabilityModel getTraceabilityModel();

    void setTraceabilityModel(TraceabilityModel traceabilityModel);
}
