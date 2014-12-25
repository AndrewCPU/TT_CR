package monkeyboystein.utils;

import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrewcpu on 12/24/14.
 */
public class BlockMemory {
    List<BlockState> originalMap = new ArrayList<BlockState>();

    public List<BlockState> getOriginalMap() {
        return originalMap;
    }

    public void setOriginalMap(List<BlockState> originalMap) {
        this.originalMap = originalMap;
    }

    public void addState(BlockState state)
    {
        originalMap.add(state);
    }

}
