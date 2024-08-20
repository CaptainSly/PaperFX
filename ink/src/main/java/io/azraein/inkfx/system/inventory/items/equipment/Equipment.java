package io.azraein.inkfx.system.inventory.items.equipment;

import java.io.Serializable;

import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import io.azraein.inkfx.system.Paper;
import io.azraein.inkfx.system.actors.Actor;

public class Equipment implements Serializable {

    private static final long serialVersionUID = 6870777223465173466L;

    private String equipmentId;
    private String equipmentName;
    private String equipmentDescription;

    private String equipmentScript;

    private EquipmentSlot equipmentSlotType;

    public Equipment(String equipmentId, String equipmentName, String equipmentDescription,
            EquipmentSlot equipmentSlotType) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.equipmentDescription = equipmentDescription;
        this.equipmentSlotType = equipmentSlotType;

        equipmentScript = "";
    }

    public void onEquip(Actor actor) {
        if (!equipmentScript.isEmpty()) {
            Paper.SE.runFunction(equipmentScript, "onEquip", CoerceJavaToLua.coerce(actor.getActorState()));
        }

        actor.getActorState().setActorEquipment(this, equipmentSlotType);
    }

    public void onUnequip(Actor actor) {
        if (!equipmentScript.isEmpty()) {
            Paper.SE.runFunction(equipmentScript, "onUnequip", CoerceJavaToLua.coerce(actor.getActorState()));
        }

        actor.getActorState().setActorEquipment(null, equipmentSlotType);
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public String getEquipmentDescription() {
        return equipmentDescription;
    }

}
