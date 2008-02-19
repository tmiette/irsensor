package fr.umlv.irsensor.supervisor;

import javax.swing.SwingUtilities;

import fr.umlv.irsensor.gui.MainFrame;

/**
 * Main class for the supervisor server application -- supervisor.jar
 * 
 * @author Miette Tom (tmiette@etudiant.univ-mlv.fr)
 * @author Moreau Alan (amorea04@etudiant.univ-mlv.fr)
 * @author Mouret Sebastien (smouret@etudiant.univ-mlv.fr)
 * @author Pons Julien (jpons@etudiant.univ-mlv.fr)
 */
public class Main {
  public static void main(String[] args) {

    // schedule a job for the event dispatch thread:
    // creating and showing this application's GUI.
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new MainFrame().launch();
      }
    });

  }

}
