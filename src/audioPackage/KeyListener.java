package audioPackage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.io.IOException;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class KeyListener extends JFrame implements NativeKeyListener, ActionListener {
    JTextField log;
    JTextField guideLog;
    String lastClicked;

    String filePath;
    String filePath1;
    String filePath2;
    String filePath3;

    public KeyListener() {
        setTitle("Audible Key Clicks");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        log = new JTextField();
        log.setEditable(false);
        log.setBounds(0, 0, 200, 30);

        guideLog = new JTextField();
        guideLog.setEditable(false);
        guideLog.setBounds(30, 0, 200, 30);

        JPanel mainPanel = new JPanel();
        JPanel panel2 = new JPanel();
        panel2.setBackground(Color.BLACK);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLUE);

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        mainPanel.add(panel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(panel2, constraints);

        add(mainPanel);
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints constraints1 = new GridBagConstraints();
        constraints1.gridx = 0;
        constraints1.gridy = 0;
        panel2.add(log, constraints1);

        constraints1.gridx = 0;
        constraints1.gridy = 1;
        panel2.add(guideLog, constraints1);
        mainPanel.add(panel2, constraints);

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        JButton BaseKeys = new JButton("set audio for keys");
        JButton SpaceKey = new JButton("set audio for space key");
        JButton BackspaceKey = new JButton("set audio for BackspaceKey");
        JButton EnterKey = new JButton("set audio for EnterKey");
        BaseKeys.addActionListener(this);
        SpaceKey.addActionListener(this);
        BackspaceKey.addActionListener(this);
        EnterKey.addActionListener(this);
        BaseKeys.setActionCommand("Key");
        SpaceKey.setActionCommand("Space");
        BackspaceKey.setActionCommand("Backspace");
        EnterKey.setActionCommand("Enter");
        // BaseKeys.setBounds(0, 10, 200, 300);
        // SpaceKey.setBounds(200, 10, 200, 300);
        // BackspaceKey.setBounds(10, 10, 200, 200);
        // EnterKey.setBounds(600, 10, 200, 300);
        panel.add(BaseKeys, c);
        c.gridx = 1;
        c.gridy = 0;
        panel.add(SpaceKey, c);
        c.gridx = 2;
        c.gridy = 0;
        panel.add(BackspaceKey, c);
        c.gridx = 3;
        c.gridy = 0;
        panel.add(EnterKey, c);
        c.gridx = 4;
        c.gridy = 0;
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println(ex);
        }
        GlobalScreen.addNativeKeyListener(this);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        if (action.equals("Space")) {
            filePath1 = fileDialog(filePath1);
        }
        if (action.equals("Enter")) {
            filePath2 = fileDialog(filePath2);
        }
        if (action.equals("Backspace")) {
            filePath3 = fileDialog(filePath3);
        }
        if (action.equals("Key")) {
            filePath = fileDialog(filePath);
        }
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (lastClicked != NativeKeyEvent.getKeyText(e.getKeyCode())) {
            if (NativeKeyEvent.getKeyText(e.getKeyCode()) == "Space") {
                try {
                    playAudio2();
                } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e1) {
                    e1.printStackTrace();
                }
            }
            if (NativeKeyEvent.getKeyText(e.getKeyCode()) == "Backspace") {
                try {
                    playAudio3();
                } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e2) {
                    e2.printStackTrace();
                }
            }
            if (NativeKeyEvent.getKeyText(e.getKeyCode()) == "Enter") {
                try {
                    playAudio4();
                } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e2) {
                    e2.printStackTrace();
                }
            }
            if (NativeKeyEvent.getKeyText(e.getKeyCode()) != "Space"
                    || NativeKeyEvent.getKeyText(e.getKeyCode()) != "Backspace"
                    || NativeKeyEvent.getKeyText(e.getKeyCode()) != "Enter") {
                try {
                    playAudio();
                } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e3) {
                    e3.printStackTrace();
                }
            }

            String string = "Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode());
            appendToLog(string, log);
            lastClicked = NativeKeyEvent.getKeyText(e.getKeyCode());
        }
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        if (lastClicked == NativeKeyEvent.getKeyText(e.getKeyCode())) {
            lastClicked = "";
        }
    }

    public void appendToLog(String input, JTextField log) {
        log.setText(input);
    }

    private String fileDialog(String filePath) {
        FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
        fd.setDirectory("audioFiles");
        fd.setFile("*.wav");
        fd.setVisible(true);
        filePath = fd.getDirectory();
        filePath += fd.getFile();
        filePath = filePath.replace("\\", "/");
        if (filePath == null) {
            appendToLog("error in assigning .wav file", guideLog);
        } else {
            appendToLog("sucessfully assigned file", guideLog);
        }
        return filePath;
    }

    void playAudio() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        if (filePath == null) {
            appendToLog("no file assigned to Keys", guideLog);
        } else {
            new audio(filePath);

        }

    }

    void playAudio2() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        // SpaceBar
        if (filePath1 == null) {
            appendToLog("no file assigned to Spacebar", guideLog);
        } else {
            new audio(filePath1);

        }
    }

    void playAudio3() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        // Ent
        if (filePath2 == null) {
            appendToLog("no file assigned to Enter", guideLog);
        } else {
            new audio(filePath2);

        }
    }

    void playAudio4() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        // BackSpace
        if (filePath3 == null) {
            appendToLog("no file assigned to Backspace", guideLog);
        } else {
            new audio(filePath3);

        }
    }

    public static void main(String[] args) {
        new KeyListener();
    }
}