/*
 * Decompiled with CFR 0_102.
 */
package org.equestria.minecraft.common.gamemaster;

import java.io.Serializable;
import org.equestria.minecraft.common.gamemaster.WorldMessageParams;

public class ItemMessage
implements Serializable {
    private String id;
    private WorldMessageParams messageParameter;
    private String message;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WorldMessageParams getMessageParameter() {
        return this.messageParameter;
    }

    public void setMessageParameter(WorldMessageParams messageParameter) {
        this.messageParameter = messageParameter;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

