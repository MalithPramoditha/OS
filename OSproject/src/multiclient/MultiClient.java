package multiclient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class MultiClient {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Client");
            frame.setSize(400, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));

            JLabel statusLabel = new JLabel("Status: Not connected");
            panel.add(statusLabel, BorderLayout.NORTH);

            JButton sendButton = new JButton("Send File");
            panel.add(sendButton, BorderLayout.CENTER);

            frame.add(panel, BorderLayout.CENTER);
            frame.setVisible(true);

            sendButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    int returnVal = fileChooser.showOpenDialog(frame);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File fileToSend = fileChooser.getSelectedFile();
                        statusLabel.setText("Status: Sending " + fileToSend.getName());
                        sendFileToServer(fileToSend);
                    }
                }
            });
        });
    }

    private static void sendFileToServer(File file) {
        try (Socket socket = new Socket("192.168.36.81", 1234)) {
            System.out.println("Connected to server.");

            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
            outputStream.writeUTF(file.getName()); // Send file name to server

            byte[] fileData = new byte[(int) file.length()];
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileData);
            outputStream.writeInt(fileData.length); // Send file length to server
            outputStream.write(fileData); // Send file data to server

            System.out.println("File sent: " + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
