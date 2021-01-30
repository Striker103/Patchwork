package view.aui;

import model.Game;
import model.Tuple;

import java.io.File;
import java.util.List;

public interface LoadGameAUI {

    public void loadGame(List<Tuple<Game, File>> games);
}
