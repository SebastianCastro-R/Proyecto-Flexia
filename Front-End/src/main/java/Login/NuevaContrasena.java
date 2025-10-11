package Login;

import com.mycompany.flexia.database.UsuariosDAO;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class NuevaContrasena extends javax.swing.JFrame {
    private String correoUsuario;
    private UsuariosDAO usuariosDAO;

    public NuevaContrasena(String correoUsuario) {
        this.correoUsuario = correoUsuario;
        this.usuariosDAO = new UsuariosDAO();
        initComponents();
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        tituloLabel = new javax.swing.JLabel();
        nuevaContrasenaField = new javax.swing.JPasswordField();
        confirmarContrasenaField = new javax.swing.JPasswordField();
        nuevaLabel = new javax.swing.JLabel();
        confirmarLabel = new javax.swing.JLabel();
        guardarButton = new javax.swing.JButton();
        cancelarButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nueva Contraseña");
        setResizable(false);
        setPreferredSize(new Dimension(400, 300));

        jPanel1.setBackground(new java.awt.Color(250, 250, 250));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tituloLabel.setFont(new java.awt.Font("Segoe UI", 1, 20));
        tituloLabel.setText("Establecer Nueva Contraseña");
        jPanel1.add(tituloLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, -1, -1));

        // Etiqueta para nueva contraseña
        nuevaLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
        nuevaLabel.setText("Nueva Contraseña:");
        jPanel1.add(nuevaLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, -1, -1));

        // Campo para nueva contraseña (JPasswordField)
        nuevaContrasenaField.setFont(new java.awt.Font("Segoe UI", 0, 14));
        nuevaContrasenaField.setToolTipText("Ingresa tu nueva contraseña");
        jPanel1.add(nuevaContrasenaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 105, 300, 35));

        // Etiqueta para confirmar contraseña
        confirmarLabel.setFont(new java.awt.Font("Segoe UI", 0, 14));
        confirmarLabel.setText("Confirmar Contraseña:");
        jPanel1.add(confirmarLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));

        // Campo para confirmar contraseña (JPasswordField)
        confirmarContrasenaField.setFont(new java.awt.Font("Segoe UI", 0, 14));
        confirmarContrasenaField.setToolTipText("Confirma tu nueva contraseña");
        jPanel1.add(confirmarContrasenaField, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 175, 300, 35));

        // Botón Guardar
        guardarButton.setBackground(new java.awt.Color(30, 56, 136));
        guardarButton.setFont(new java.awt.Font("Segoe UI", 1, 14));
        guardarButton.setForeground(Color.WHITE);
        guardarButton.setText("Guardar");
        guardarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarNuevaContrasena();
            }
        });
        jPanel1.add(guardarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 100, 35));

        // Botón Cancelar
        cancelarButton.setBackground(new java.awt.Color(200, 200, 200));
        cancelarButton.setFont(new java.awt.Font("Segoe UI", 1, 14));
        cancelarButton.setText("Cancelar");
        cancelarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelar();
            }
        });
        jPanel1.add(cancelarButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 230, 100, 35));

        jPanel1.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 320, 10));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    private void guardarNuevaContrasena() {
        String nuevaContrasena = new String(nuevaContrasenaField.getPassword());
        String confirmarContrasena = new String(confirmarContrasenaField.getPassword());

        // Validaciones
        if (nuevaContrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor ingresa la nueva contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            nuevaContrasenaField.requestFocus();
            return;
        }

        if (confirmarContrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor confirma la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
            confirmarContrasenaField.requestFocus();
            return;
        }

        if (!nuevaContrasena.equals(confirmarContrasena)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            nuevaContrasenaField.setText("");
            confirmarContrasenaField.setText("");
            nuevaContrasenaField.requestFocus();
            return;
        }

        if (nuevaContrasena.length() < 6) {
            JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            nuevaContrasenaField.requestFocus();
            return;
        }

        // Actualizar contraseña en la base de datos
        if (actualizarContrasenaEnBD(correoUsuario, nuevaContrasena)) {
            JOptionPane.showMessageDialog(this, 
                "✅ Contraseña actualizada correctamente.\n\nAhora puedes iniciar sesión con tu nueva contraseña.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            // Aquí puedes redirigir al login si lo deseas
            // new LoginForm().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "❌ Error al actualizar la contraseña.\nPor favor intenta nuevamente.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean actualizarContrasenaEnBD(String correo, String nuevaContrasena) {
        // Usar el método del DAO si existe, sino hacer la actualización directa
        try {
            // Intentar usar el DAO primero
            if (usuariosDAO != null) {
                // Si tienes el método en el DAO, úsalo:
                // return usuariosDAO.actualizarContrasena(correo, nuevaContrasena);
            }
            
            // Si no, hacer la actualización directa
            String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(nuevaContrasena, org.mindrot.jbcrypt.BCrypt.gensalt(12));
            
            String sql = "UPDATE usuarios SET contrasena = ? WHERE correo_electronico = ?";
            
            try (java.sql.Connection conn = com.mycompany.flexia.database.Conexion.getConnection();
                 java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                
                ps.setString(1, hashedPassword);
                ps.setString(2, correo);
                
                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0;
                
            } catch (java.sql.SQLException e) {
                System.err.println("❌ Error al actualizar contraseña: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error general al actualizar contraseña: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void cancelar() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Estás seguro de que quieres cancelar?\nLos cambios no se guardarán.",
            "Confirmar cancelación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (respuesta == JOptionPane.YES_OPTION) {
            this.dispose();
        }
    }

    // Variables declaration
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel tituloLabel;
    private javax.swing.JPasswordField nuevaContrasenaField;
    private javax.swing.JPasswordField confirmarContrasenaField;
    private javax.swing.JLabel nuevaLabel;
    private javax.swing.JLabel confirmarLabel;
    private javax.swing.JButton guardarButton;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JSeparator jSeparator1;
}
