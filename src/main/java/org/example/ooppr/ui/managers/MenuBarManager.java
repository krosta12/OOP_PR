package org.example.ooppr.ui.managers;

import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class MenuBarManager {

    private final MenuBar menuBar;
    private final MenuItem saveAsItem;
    private final MenuItem exitItem;

    public MenuBarManager(MenuBar menuBar, MenuItem saveAsItem, MenuItem exitItem ) {
        this.menuBar = menuBar;
        this.saveAsItem = saveAsItem;
        this.exitItem = exitItem;

        setupMenuActions();
    }

    private void setupMenuActions() {
        saveAsItem.setOnAction( e -> saveAs() );
        exitItem.setOnAction( e -> exit() );
    }

    private void saveAs() {
        System.out.println( "Save as..." );
    }

    private void exit() {
        System.out.println( "exit" );
    }



}
