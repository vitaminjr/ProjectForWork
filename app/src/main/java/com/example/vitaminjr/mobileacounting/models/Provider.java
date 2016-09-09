package com.example.vitaminjr.mobileacounting.models;

/**
 * Created by vitaminjr on 22.08.16.
 */
public class Provider {

    private int providerId;
    private String providerCode;
    private int codeEDRPOU;
    private String name;
    private int typeAgent;

    public Provider(){
        providerId = 0;
        providerCode = "";
        codeEDRPOU = 0;
        name = "";
        typeAgent = 0;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public String getProviderCode() {
        return providerCode;
    }

    public void setProviderCode(String providerCode) {
        this.providerCode = providerCode;
    }

    public int getCodeEDRPOU() {
        return codeEDRPOU;
    }

    public void setCodeEDRPOU(int codeEDRPOU) {
        this.codeEDRPOU = codeEDRPOU;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeAgent() {
        return typeAgent;
    }

    public void setTypeAgent(int typeAgent) {
        this.typeAgent = typeAgent;
    }
}
