package com.cphandheld.unisonscanner;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Supernova on 2/16/2016.
 */
public class User implements Serializable
{

    String email;
    int userId;
    String name;
    int organizationId;
    List<String> abilities;
    int currentOrg;
    int currentLoc;

    User() {
    }


}
