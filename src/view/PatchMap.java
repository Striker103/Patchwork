package view;

import controller.IOController;
import model.Patch;

import java.util.HashMap;
import java.util.List;

/**
 * Maps patches to the correct image path. Singleton.
 *
 * @author Alexandra Latys
 */
public class PatchMap {

    private final HashMap<Patch, String> map = new HashMap<>();

    private static PatchMap patchMap;

    /**
     * Images are numbered in the same order as the patches in the csv file.
     */
    private PatchMap(){

        IOController ioController = new IOController(null);

        List<Patch> patches = ioController.importCSVNotShuffled();

        for (int i = 0; i < patches.size(); i++){
            map.put(patches.get(i), "/view/images/Patches/Patch" + i + ".png");
        }

    }

    /**
     * Returns an instance of PatchMap. Ensures only one instance of PatchMap is created to avoid unnecessary IO Operations.
     *
     * @return instance of PatchMap
     */
    public static PatchMap getInstance(){
        if (patchMap == null)
            patchMap = new PatchMap();

        return patchMap;
    }

    /**
     * Returns image path for the patch
     * @param patch patch
     * @return image path
     * @throws IllegalArgumentException if no image matching the patch exists
     */
    public String getImagePath(Patch patch){
        String path = map.get(patch);

        if (path == null)
            throw new IllegalArgumentException("Patch is not supported.");

        return path;
    }

}
