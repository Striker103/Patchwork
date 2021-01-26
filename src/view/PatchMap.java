package view;

import controller.IOController;
import model.Patch;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PatchMap {

    private final HashMap<Patch, String> map = new HashMap<>();

    private static PatchMap patchMap;

    private PatchMap(){

        IOController ioController = new IOController(null);

        List<Patch> patches = ioController.importCSVNotShuffled();

        for (int i = 0; i < patches.size(); i++){
            map.put(patches.get(i), "/view/images/Patches/Patch" + i + ".png");
        }

    }

    public static PatchMap getInstance(){
        if (patchMap == null)
            patchMap = new PatchMap();

        return patchMap;
    }

    public String getImagePath(Patch patch){
        String path = map.get(patch);

        if (path == null)
            throw new IllegalArgumentException("Patch is not supported.");

        return path;
    }


}
