package CapturaDatosGUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CapturarDatosGUI {

    public static void main(String[] args) {
        // Crear el marco de la ventana
        JFrame frame = new JFrame("Captura de Datos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 500);
        frame.setLayout(new BorderLayout());

        // Configurar fuente y colores
        Font labelFont = new Font("Arial", Font.PLAIN, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        Color backgroundColor = new Color(230, 240, 250);
        Color buttonColor = new Color(100, 150, 200);
        Color borderColor = new Color(150, 200, 250);

        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(mainPanel, BorderLayout.CENTER);

        // Crear campos de entrada y etiquetas
        JLabel enteroLabel = new JLabel("Número entero:");
        JTextField enteroField = new JTextField();
        estilizarComponente(enteroLabel, enteroField, labelFont, fieldFont, borderColor);
        agregarComponente(mainPanel, enteroLabel, enteroField, gbc, 0);

        JLabel decimalLabel = new JLabel("Número decimal:");
        JTextField decimalField = new JTextField();
        estilizarComponente(decimalLabel, decimalField, labelFont, fieldFont, borderColor);
        agregarComponente(mainPanel, decimalLabel, decimalField, gbc, 1);

        JLabel caracterLabel = new JLabel("Carácter:");
        JTextField caracterField = new JTextField();
        estilizarComponente(caracterLabel, caracterField, labelFont, fieldFont, borderColor);
        agregarComponente(mainPanel, caracterLabel, caracterField, gbc, 2);

        JLabel cadenaTextoLabel = new JLabel("Cadena de texto:");
        JTextField cadenaTextoField = new JTextField();
        estilizarComponente(cadenaTextoLabel, cadenaTextoField, labelFont, fieldFont, borderColor);
        agregarComponente(mainPanel, cadenaTextoLabel, cadenaTextoField, gbc, 3);

        JLabel booleanoLabel = new JLabel("Valor booleano (true/false):");
        JTextField booleanoField = new JTextField();
        estilizarComponente(booleanoLabel, booleanoField, labelFont, fieldFont, borderColor);
        agregarComponente(mainPanel, booleanoLabel, booleanoField, gbc, 4);

        // Botón para capturar datos
        JButton submitButton = new JButton("Capturar Datos");
        submitButton.setFont(buttonFont);
        submitButton.setBackground(buttonColor);
        submitButton.setForeground(Color.WHITE);
        submitButton.setBorder(new LineBorder(borderColor, 2));
        gbc.gridx = 1;
        gbc.gridy = 5;
        mainPanel.add(submitButton, gbc);

        // Área de texto para mostrar los datos capturados
        JTextArea resultArea = new JTextArea(10, 30);
        resultArea.setFont(fieldFont);
        resultArea.setEditable(false);
        resultArea.setBorder(new LineBorder(borderColor, 2));
        frame.add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        // Manejar la acción del botón
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int Entero = Integer.parseInt(enteroField.getText());
                    double Decimal = Double.parseDouble(decimalField.getText());
                    String entrada = caracterField.getText();
                    if (entrada.length() != 1) {
                        throw new Exception("Debes ingresar un solo carácter.");
                    }
                    char caracter = entrada.charAt(0);
                    String cadenaTexto = cadenaTextoField.getText();
                    boolean Booleano = Boolean.parseBoolean(booleanoField.getText());

                    // Mostrar los datos capturados en el área de texto
                    resultArea.setText("Datos capturados:\n");
                    resultArea.append("Número entero: " + Entero + "\n");
                    resultArea.append("Número decimal: " + Decimal + "\n");
                    resultArea.append("Carácter: " + caracter + "\n");
                    resultArea.append("Cadena de texto: " + cadenaTexto + "\n");
                    resultArea.append("Valor booleano: " + Booleano + "\n");

                    // Guardar los datos en la base de datos
                    String url = "jdbc:mysql://localhost:3306/CapturaDatosDB";
                    String username = "root";  
                    String password = "";  

                    try (Connection conn = DriverManager.getConnection(url, username, password)) {
                        String query = "INSERT INTO DatosCapturados (numero_entero, numero_decimal, caracter, cadena_texto, valor_booleano) VALUES (?, ?, ?, ?, ?)";
                        try (PreparedStatement stmt = conn.prepareStatement(query)) {
                            stmt.setInt(1, Entero);
                            stmt.setDouble(2, Decimal);
                            stmt.setString(3, String.valueOf(caracter));
                            stmt.setString(4, cadenaTexto);
                            stmt.setBoolean(5, Booleano);
                            stmt.executeUpdate();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Error: Por favor ingresa valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Hacer visible el marco
        frame.setVisible(true);
    }

    private static void agregarComponente(JPanel panel, JLabel label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(label, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private static void estilizarComponente(JLabel label, JTextField field, Font labelFont, Font fieldFont, Color borderColor) {
        label.setFont(labelFont);
        field.setFont(fieldFont);
        field.setBorder(new LineBorder(borderColor, 1));
    }
}
