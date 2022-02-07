package com.redrover.xoyou.network.model;
public class DeleteRelationshipInfo {
    int relationshipId;

    public DeleteRelationshipInfo(int relationshipId) {
        this.relationshipId = relationshipId;
    }

    public int getRelationshipId() {
        return relationshipId;
    }

    public void setRelationshipId(int relationshipId) {
        this.relationshipId = relationshipId;
    }
}
