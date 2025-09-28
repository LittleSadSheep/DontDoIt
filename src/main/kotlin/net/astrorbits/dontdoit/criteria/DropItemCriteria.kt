package net.astrorbits.dontdoit.criteria

import net.astrorbits.dontdoit.criteria.helper.CriteriaType
import net.astrorbits.dontdoit.criteria.inspect.InventoryInspectContext
import net.astrorbits.dontdoit.criteria.inspect.InventoryItemInspectCandidate
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

class DropItemCriteria : Criteria(), Listener, InventoryItemInspectCandidate {
    override val type: CriteriaType = CriteriaType.DROP_ITEM
    lateinit var itemTypes: Set<Material>
    var isWildcard: Boolean = false

    override fun getOtherInventoryItemWeightMultiplier(context: InventoryInspectContext): Double {
        return 1.0
    }

    override fun getSelfInventoryItemMatchingWeightMultiplier(context: InventoryInspectContext): Double {
        return SELF_INVENTORY_MULTIPLIER
    }

    override fun getCandidateInventoryItemTypes(): Set<Material> {
        return itemTypes
    }

    override fun canMatchAnyInventoryItem(): Boolean {
        return isWildcard
    }

    override fun readData(data: Map<String, String>) {
        super.readData(data)
        data.setItemTypes(ITEM_TYPES_KEY) { itemTypes, isWildcard ->
            this.itemTypes = itemTypes
            this.isWildcard = isWildcard
        }
    }

    @EventHandler
    fun onCraftItem(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        if (isWildcard || item.type in itemTypes) {
            trigger(event.player)
        }
    }

    companion object {
        const val ITEM_TYPES_KEY = "item"

        const val SELF_INVENTORY_MULTIPLIER = 1.15
    }
}