package com.app.voting.model;

public class PartiesModel {

    private String logo, partyName, id;

    public PartiesModel(){}

    public PartiesModel(String logo, String partyName, String id) {
        this.logo = logo;
        this.partyName = partyName;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partName) {
        this.partyName = partName;
    }
}
