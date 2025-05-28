package main;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;

import main.Datas;
import utils.DataManager;
//import main.DataTable;

public class Main extends JFrame{
	private JTextField serial_number, name_burmese, stable_address, search;
	private JComboBox<String> township;
	private ButtonGroup genderGroup;
	private JComboBox<String> education_level;
	private JComboBox<String> qualifying_exam;
	private JRadioButton male, female;
	private DatePicker registered_at, dob;
	private JComboBox<String> religion;
	
	public DefaultTableModel tableModel;
	public JTable table;
	
	public Main() {
		setTitle("Admin Panel");	
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        JPanel registeration_form = new JPanel();
        registeration_form.setLayout(new GridLayout(6,2, 0, 10));
        registeration_form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        registeration_form.setPreferredSize(new Dimension(580, 300));
//        registeration_form.setPreferredSize(new Dimension(100, 100));
        registeration_form.setBorder(BorderFactory.createEtchedBorder());
        defineTextFields();
        registeration_form.add(new JLabel("Regiseration Date: "));
        registered_at.setDate(LocalDate.now());
        registeration_form.add(registered_at);
        
        registeration_form.add(new JLabel("Township: "));
        registeration_form.add(township);
        
        
        registeration_form.add(new JLabel("Serial Number: "));
        registeration_form.add(serial_number);
        
        registeration_form.add(new JLabel("Name (Burmese): "));
        registeration_form.add(name_burmese);
        
        registeration_form.add(new JLabel("Date of Birth: "));
        registeration_form.add(dob);
        
        registeration_form.add(new JLabel("Gender"));
        JPanel gender = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        gender.add(male);
        gender.add(female);
        registeration_form.add(gender);  
        
        registeration_form.add(new JLabel("Religion: "));
        registeration_form.add(religion);
        
        registeration_form.add(new JLabel("Stable Address: "));
        registeration_form.add(stable_address);
        
        registeration_form.add(new JLabel("Education Level: "));
        registeration_form.add(education_level);
        
        registeration_form.add(new JLabel("Qualifying Exam: "));
        registeration_form.add(qualifying_exam);
        
        JButton saveButton = new JButton("Save");
        JPanel BtnPanel = new JPanel();
        JButton clearButton = new JButton("Clear");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        BtnPanel.add(saveButton);
        BtnPanel.add(clearButton);
        BtnPanel.add(updateButton);
        BtnPanel.add(deleteButton);
        
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(new JLabel("Search"));
        search.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(search);
        JButton searchButton = new JButton("Search");
        JButton reloadButton = new JButton("Reload");
        searchPanel.add(searchButton);
        searchPanel.add(reloadButton);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(registeration_form, BorderLayout.CENTER);
        mainPanel.add(BtnPanel, BorderLayout.SOUTH);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        
        this.add(mainPanel, BorderLayout.NORTH);
        
        String[] columns = {"No.","Name", "Registered Date", "Township", "Serial Number", "BirthDate", "Gender", "Religion", "Education Level", "Qualifying Exam", "Stable Address",};
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);
		table.setRowHeight(50);
		table.setAutoResizeMode(HEIGHT);
		loadDatas();
		JScrollPane tableScroll = new JScrollPane(table);
//		events for button
		saveButton.addActionListener( e -> {
			addToTable();
			clearFields();
		});
		searchButton.addActionListener( e -> searchDatas());
		updateButton.addActionListener( e -> update() );
		reloadButton.addActionListener( e -> loadDatas());
		deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                saveTableDataToFile(); 
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Select a row to delete.");
            }
        });
		clearButton.addActionListener(e -> clearFields());
		mouseAdptr();
		
        this.add(tableScroll, BorderLayout.CENTER);
        setVisible(true);
	}
	
	public void addToTable() {
		String[] dataStrings = {
			String.valueOf(tableModel.getRowCount() + 1),
			name_burmese.getText(),
			registered_at.getText(),
			township.getSelectedItem().toString(),
			serial_number.getText(),
			dob.getDateStringOrEmptyString(),
			checkGender(),
			religion.getSelectedItem().toString(),
			education_level.getSelectedItem().toString(),
			qualifying_exam.getSelectedItem().toString(),
			stable_address.getText(),
		};
		tableModel.addRow(dataStrings);
		saveTableDataToFile();
	}
	
	private String checkGender() {
		if(male.isSelected()) {
			return "Male";
		}
		if(female.isSelected()) {
			return "Female";
		}
		return null;
	}
	public void defineTextFields() {
		search = new JTextField();
//		format for datepicker
		DatePickerSettings settings = new DatePickerSettings();
		settings.setFormatForDatesCommonEra(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		serial_number = new JTextField();
		name_burmese = new JTextField();
		registered_at = new DatePicker(settings);
		genderGroup = new ButtonGroup();
		
		male = new JRadioButton("Male");
		male.setText("Male");
		female = new JRadioButton("Female");
		female.setText("Female");
		genderGroup.add(male);
		genderGroup.add(female);
		
		religion = new JComboBox<String>(Datas.religions);
		
		stable_address = new JTextField();
		
		education_level = new JComboBox<String>(Datas.educationLevels);
		
		qualifying_exam = new JComboBox<String>(Datas.qualifyingExam);
		DatePickerSettings dbosettings = new DatePickerSettings();
		dbosettings.setFormatForDatesCommonEra(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		dob = new DatePicker(dbosettings);
		township = new JComboBox<>(Datas.myanmarTownships);
	}
	
	private void loadDatas() {
		List<String[]> dataStrings =  DataManager.loadDatas();
		tableModel.setRowCount(0);
		for(String[] data : dataStrings){
			tableModel.addRow(data);
		}
	}
	
	private void searchDatas() {
        String keyword = search.getText().toLowerCase().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a keyword to search.");
            return;
        }

        tableModel.setRowCount(0); // Clear table
        List<String[]> datas = DataManager.loadDatas();
        for (String[] data : datas) {
            String name = data[1].toLowerCase();
            String serialNumber = data[4].toLowerCase();
            if (name.contains(keyword) || serialNumber.contains(keyword)) {
                tableModel.addRow(data);
            }
        }
    }
	
	private void saveTableDataToFile() {
        ArrayList<String[]> datas = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String[] data = new String[11];
            for (int j = 0; j < 11; j++) {
                data[j] = tableModel.getValueAt(i, j).toString();
            }
            datas.add(data);
        }
        DataManager.saveDatas(datas);  
    }
	
	private void update() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			tableModel.setValueAt(name_burmese.getText(), selectedRow, 1);
	        tableModel.setValueAt(registered_at.getDateStringOrEmptyString(), selectedRow, 2);
	        tableModel.setValueAt(township.getSelectedItem().toString(), selectedRow, 3);
	        tableModel.setValueAt(serial_number.getText(), selectedRow, 4);
	        tableModel.setValueAt(dob.getDateStringOrEmptyString(), selectedRow, 5);
	        tableModel.setValueAt(checkGender(), selectedRow, 6);
	        tableModel.setValueAt(religion.getSelectedItem().toString(), selectedRow, 7);
	        tableModel.setValueAt(education_level.getSelectedItem().toString(), selectedRow, 8);
	        tableModel.setValueAt(qualifying_exam.getSelectedItem().toString(), selectedRow, 9);
	        tableModel.setValueAt(stable_address.getText(), selectedRow, 10);
	        saveTableDataToFile();
	        clearFields();
      } else {
          JOptionPane.showMessageDialog(this, "Select a row to update.");
      }
	}
	
	private void clearFields() {
	    serial_number.setText("");
	    name_burmese.setText("");
	    stable_address.setText("");

	    if (township.getItemCount() > 0) township.setSelectedIndex(0);
	    if (education_level.getItemCount() > 0) education_level.setSelectedIndex(0);
	    if (qualifying_exam.getItemCount() > 0) qualifying_exam.setSelectedIndex(0);
	    if (religion.getItemCount() > 0) religion.setSelectedIndex(0);

	    // Clear radio button selection
	    genderGroup.clearSelection();

	    registered_at.setDate(null); 
	    dob.setDate(null);
	}
	
	public void mouseAdptr() {
		table.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        int selectedRow = table.getSelectedRow();
		        if (selectedRow != -1) {
		            name_burmese.setText(tableModel.getValueAt(selectedRow, 1).toString());
		            registered_at.setDate(LocalDate.parse(tableModel.getValueAt(selectedRow, 2).toString()));
		            township.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
		            serial_number.setText(tableModel.getValueAt(selectedRow, 4).toString());
		            dob.setDate(LocalDate.parse(tableModel.getValueAt(selectedRow, 5).toString()));
		            
		            System.out.println(tableModel.getValueAt(selectedRow, 5).toString());

		            String gender = tableModel.getValueAt(selectedRow, 6).toString();
		            if (gender.equalsIgnoreCase("Male")) {
		                genderGroup.setSelected(male.getModel(), true);
		            } else if (gender.equalsIgnoreCase("Female")) {
		                genderGroup.setSelected(female.getModel(), true);
		            }

		            religion.setSelectedItem(tableModel.getValueAt(selectedRow, 7).toString());
		            education_level.setSelectedItem(tableModel.getValueAt(selectedRow, 8).toString());
		            qualifying_exam.setSelectedItem(tableModel.getValueAt(selectedRow, 9).toString());
		            stable_address.setText(tableModel.getValueAt(selectedRow, 10).toString());
		        }
		    }
		});
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(Main::new);
	}
}
