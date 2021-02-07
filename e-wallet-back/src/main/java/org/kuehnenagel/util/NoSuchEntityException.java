package org.kuehnenagel.util;

public class NoSuchEntityException extends RuntimeException {
    private final int entityId;
    
    public NoSuchEntityException(final int entityId) {
        super("Entity not found!");
        this.entityId = entityId;
    }
    
    public int getEntityId()
    {
        return entityId;
    }
}
