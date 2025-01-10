package todo.list.application;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class ToDoListApplication extends JFrame implements ActionListener {
    JTextField taskField;
    JDateChooser dateChooser, deadlineChooser;
    JButton addButton, viewButton, editButton, deleteButton;

    public ToDoListApplication() {
        super("TODO LIST APPLICATION");

        // Header Section
        JLabel header = new JLabel("TODO LIST APPLICATION");
        header.setFont(new Font("Raleway", Font.BOLD, 28));
        header.setBounds(120, 20, 400, 40);
        add(header);

        // Task Label and Input
        JLabel taskLabel = new JLabel("Task:");
        taskLabel.setFont(new Font("Arial", Font.BOLD, 18));
        taskLabel.setBounds(50, 100, 100, 30);
        add(taskLabel);

        taskField = new JTextField();
        taskField.setFont(new Font("Arial", Font.PLAIN, 16));
        taskField.setBounds(150, 100, 350, 30);
        add(taskField);

        // Task Date Label and Input
        JLabel dateLabel = new JLabel("Task Date:");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 18));
        dateLabel.setBounds(50, 150, 100, 30);
        add(dateLabel);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(150, 150, 350, 30);
        add(dateChooser);

        // Deadline Date Label and Input
        JLabel deadlineLabel = new JLabel("Deadline Date:");
        deadlineLabel.setFont(new Font("Arial", Font.BOLD, 18));
        deadlineLabel.setBounds(50, 200, 150, 30);
        add(deadlineLabel);

        deadlineChooser = new JDateChooser();
        deadlineChooser.setBounds(200, 200, 300, 30);
        add(deadlineChooser);

        // Buttons
        addButton = new JButton("Add Task");
        addButton.setBounds(50, 260, 120, 30);
        addButton.addActionListener(this);
        add(addButton);

        viewButton = new JButton("View Tasks");
        viewButton.setBounds(180, 260, 120, 30);
        viewButton.addActionListener(this);
        add(viewButton);

        editButton = new JButton("Edit Task");
        editButton.setBounds(310, 260, 120, 30);
        editButton.addActionListener(this);
        add(editButton);

        deleteButton = new JButton("Delete Task");
        deleteButton.setBounds(440, 260, 120, 30);
        deleteButton.addActionListener(this);
        add(deleteButton);

        // Frame Settings
        getContentPane().setBackground(new Color(222, 255, 228));
        setLayout(null);
        setSize(600, 400);
        setLocation(350, 100);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            ConnectionClass connectionClass = new ConnectionClass();

            if (e.getSource() == addButton) {
                // Add Task
                String task = taskField.getText();

                // Ensure we format the dates properly
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String taskDate = (dateChooser.getDate() != null) ? sdf.format(dateChooser.getDate()) : null;
                String deadlineDate = (deadlineChooser.getDate() != null) ? sdf.format(deadlineChooser.getDate()) : null;

                if (task.isEmpty() || taskDate == null || deadlineDate == null) {
                    JOptionPane.showMessageDialog(this, "Please fill all the fields");
                    return;
                }

                String query = "INSERT INTO tasks (task_name, task_date, deadline_date) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connectionClass.connection.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, task);
                    preparedStatement.setString(2, taskDate);
                    preparedStatement.setString(3, deadlineDate);
                    preparedStatement.executeUpdate();

                    // Retrieve and show generated Task ID
                    var resultSet = preparedStatement.getGeneratedKeys();
                    if (resultSet.next()) {
                        int taskId = resultSet.getInt(1);
                        JOptionPane.showMessageDialog(this, "Task Added Successfully! Task ID: " + taskId);
                    }

                    clearFields();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error while inserting task into the database.");
                }
            } else if (e.getSource() == viewButton) {
                // View Task
                String taskIdInput = JOptionPane.showInputDialog(this, "Enter Task ID to View:");
                if (taskIdInput != null && !taskIdInput.isEmpty()) {
                    int taskId = Integer.parseInt(taskIdInput);
                    String query = "SELECT * FROM tasks WHERE task_id = ?";
                    try (PreparedStatement preparedStatement = connectionClass.connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, taskId);
                        ResultSet rs = preparedStatement.executeQuery();
                        if (rs.next()) {
                            String taskName = rs.getString("task_name");
                            String taskDate = rs.getString("task_date");
                            String deadlineDate = rs.getString("deadline_date");
                            JOptionPane.showMessageDialog(this, "Task ID: " + taskId + "\nTask: " + taskName + "\nTask Date: " + taskDate + "\nDeadline Date: " + deadlineDate);
                        } else {
                            JOptionPane.showMessageDialog(this, "No task found with that ID.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error while fetching task from the database.");
                    }
                }
            } else if (e.getSource() == editButton) {
                // Edit Task
                String taskIdInput = JOptionPane.showInputDialog(this, "Enter Task ID to Edit:");
                if (taskIdInput != null && !taskIdInput.isEmpty()) {
                    int taskId = Integer.parseInt(taskIdInput);
                    String query = "SELECT * FROM tasks WHERE task_id = ?";
                    try (PreparedStatement preparedStatement = connectionClass.connection.prepareStatement(query)) {
                        preparedStatement.setInt(1, taskId);
                        ResultSet rs = preparedStatement.executeQuery();
                        if (rs.next()) {
                            String taskName = rs.getString("task_name");
                            String taskDate = rs.getString("task_date");
                            String deadlineDate = rs.getString("deadline_date");

                            taskField.setText(taskName);
                            dateChooser.setDate(Date.valueOf(taskDate));
                            deadlineChooser.setDate(Date.valueOf(deadlineDate));

                            int confirm = JOptionPane.showConfirmDialog(this, "Do you want to edit this task?", "Edit Task", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                // Prompt user for new task description
                                String newTaskDescription = JOptionPane.showInputDialog(this, "Enter New Task Description:", taskName);

                                if (newTaskDescription != null && !newTaskDescription.isEmpty()) {
                                    String newTaskDate = new SimpleDateFormat("yyyy-MM-dd").format(dateChooser.getDate());
                                    String newDeadlineDate = new SimpleDateFormat("yyyy-MM-dd").format(deadlineChooser.getDate());

                                    String updateQuery = "UPDATE tasks SET task_name = ?, task_date = ?, deadline_date = ? WHERE task_id = ?";
                                    try (PreparedStatement updateStatement = connectionClass.connection.prepareStatement(updateQuery)) {
                                        updateStatement.setString(1, newTaskDescription);
                                        updateStatement.setString(2, newTaskDate);
                                        updateStatement.setString(3, newDeadlineDate);
                                        updateStatement.setInt(4, taskId);
                                        updateStatement.executeUpdate();
                                        JOptionPane.showMessageDialog(this, "Task updated successfully!");
                                        clearFields();
                                    } catch (SQLException ex) {
                                        ex.printStackTrace();
                                        JOptionPane.showMessageDialog(this, "Error while updating task.");
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(this, "Invalid Task Description.");
                                }
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "No task found with that ID.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error while fetching task for editing.");
                    }
                }
            } else if (e.getSource() == deleteButton) {
                // Delete Task
                String taskIdInput = JOptionPane.showInputDialog(this, "Enter Task ID to Delete:");
                if (taskIdInput != null && !taskIdInput.isEmpty()) {
                    int taskId = Integer.parseInt(taskIdInput);
                    String deleteQuery = "DELETE FROM tasks WHERE task_id = ?";
                    try (PreparedStatement deleteStatement = connectionClass.connection.prepareStatement(deleteQuery)) {
                        deleteStatement.setInt(1, taskId);
                        int rowsAffected = deleteStatement.executeUpdate();
                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(this, "Task deleted successfully!");
                        } else {
                            JOptionPane.showMessageDialog(this, "No task found with that ID.");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error while deleting task.");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while interacting with the database.");
        }
    }

    private void clearFields() {
        taskField.setText("");
        dateChooser.setDate(null);
        deadlineChooser.setDate(null);
    }

    public static void main(String[] args) {
        new ToDoListApplication();
    }
}
