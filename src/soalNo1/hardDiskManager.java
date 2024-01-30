package soalNo1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class hardDiskManager extends JFrame {

    private JTextField filenameTextField, extensionTextField, sizeTextField;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel hardDiskLabel, memorySizeLeftLabel;

    private int memorySizeLeft = 10000;

    public hardDiskManager() {
        setTitle("Hard Disk Manager");
        setLayout(new BorderLayout());

        // Create components
        createTitlePanel();
        createMainPanel();
        createMemoryPanel();

        // set sizeTextField number only input
        sizeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char enter = evt.getKeyChar();
                if (!(Character.isDigit(enter))) {
                    evt.consume();
                }
            }
        });

        // Set default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Pack and set visible
        pack();
        setSize(600, 200);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createTitlePanel() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        hardDiskLabel = new JLabel("Hard Disk Manager");
        titlePanel.add(hardDiskLabel);

        add(titlePanel, BorderLayout.NORTH);
    }

    private void createMainPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 4));

        // Filename
        JLabel filenameLabel = new JLabel("Filename");
        filenameTextField = new JTextField();
        inputPanel.add(filenameLabel);
        inputPanel.add(filenameTextField);

        // Extension
        JLabel extensionLabel = new JLabel("Extension");
        extensionTextField = new JTextField();
        inputPanel.add(extensionLabel);
        inputPanel.add(extensionTextField);

        // Size in MB
        JLabel sizeLabel = new JLabel("Size in MB");
        sizeTextField = new JTextField();
        inputPanel.add(sizeLabel);
        inputPanel.add(sizeTextField);

        // Save and Delete buttons
        JButton saveButton = new JButton("Save");
        JButton deleteButton = new JButton("Delete");
        inputPanel.add(saveButton);
        inputPanel.add(deleteButton);

        // Add action listeners
        saveButton.addActionListener(new SaveButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());

        add(inputPanel, BorderLayout.WEST);

        String[] columnNames = {"Filename", "Extension", "Size in MB"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createMemoryPanel() {
        JPanel memoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        memorySizeLeftLabel = new JLabel("Memory Size Left: " + memorySizeLeft + " MB");
        memoryPanel.add(memorySizeLeftLabel);

        add(memoryPanel, BorderLayout.SOUTH);
    }

    private void updateMemorySizeLabel() {
        memorySizeLeftLabel.setText("Memory Size Left: " + memorySizeLeft + " MB");
    }

    private class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String filename = filenameTextField.getText().trim();
            String extension = extensionTextField.getText().trim();
            String sizeText = sizeTextField.getText().trim();

            // Validasi semua field harus diisi
            if (filename.isEmpty() || extension.isEmpty() || sizeText.isEmpty()) {
                JOptionPane.showMessageDialog(hardDiskManager.this, "Semua field harus diisi", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi Size in MB harus berupa angka positif
            try {
                int size = Integer.parseInt(sizeText);
                if (size <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(hardDiskManager.this, "Size in MB harus berupa angka positif", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validasi tidak boleh ada nama file dan extension yang sama di dalam table
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                if (filename.equals(tableModel.getValueAt(row, 0)) && extension.equals(tableModel.getValueAt(row, 1))) {
                    JOptionPane.showMessageDialog(hardDiskManager.this, "File dengan nama dan extension yang sama sudah ada di dalam tabel", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Validasi Memory Size Left harus cukup menampung file baru tersebut
            int newSize = Integer.parseInt(sizeText);
            if (newSize > memorySizeLeft) {
                JOptionPane.showMessageDialog(hardDiskManager.this, "Memory Size Left tidak cukup untuk menampung file ini", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Jika semua data sudah benar, masukkan data file ke dalam table
            String[] rowData = {filename, extension, sizeText};
            tableModel.addRow(rowData);

            // Kurangi Memory Size Left dengan file yang baru saja ditambahkan
            memorySizeLeft -= newSize;

            // Update Memory Size Left label
            updateMemorySizeLabel();
        }
    }

    private class DeleteButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = table.getSelectedRow();

            // Validasi jika user belum pilih data di table
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(hardDiskManager.this, "Pilih data yang ingin dihapus dari tabel", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Mendapatkan data yang akan dihapus
            int size = Integer.parseInt((String) tableModel.getValueAt(selectedRow, 2));

            // Hapus data dari tabel
            tableModel.removeRow(selectedRow);

            // Tambahkan memory size left dengan file yang baru saja dihapus
            memorySizeLeft += size;

            // Update Memory Size Left label
            updateMemorySizeLabel();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new hardDiskManager());
    }
}
