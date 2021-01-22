package controller;

import org.junit.Test;

import static org.junit.Assert.*;

public class IOControllerTest {

    @Test
    public void saveGame() {
    }

    @Test
    public void loadGame() {
    }

    @Test
    public void importCSV() {
    }

    @Test
    public void testImportCSV() {

        IOController io = new IOController(null);
        System.out.println(io.importCSV().toString());

    }

    @Test
    public void exportGameResult() {
    }
}