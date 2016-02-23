package com.cphandheld.unisonscanner;

import java.io.Serializable;

/**
 * Created by Supernova on 2/16/2016.
 */
public class CurrentContext implements Serializable
{
    int organizationId;
    int locationId;
    String locationName;
    String vin;
    int year;
    String make;
    String model;
    int binId;
    int pathId;
    String notes;

    CurrentContext() {
    }


}
