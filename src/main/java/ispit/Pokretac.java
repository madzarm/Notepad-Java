package ispit;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;

import javax.swing.*;

public class Pokretac {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JNotepadPP().setVisible(true);
        });
    }
}
