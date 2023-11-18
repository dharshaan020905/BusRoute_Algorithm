import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.geom.RoundRectangle2D;

class AppFrame extends JFrame {
    private JButton enterButton;
    private JComboBox<String> sourceComboBox;
    private JComboBox<String> destinationComboBox;
    private JLabel pathLabel;
    private JLabel timeLabel;
    private JLabel priceLabel;
    private Graph graph;
    private JLabel imageLabel;

    private String[] stationNames = {
            "CHOWRASTA",
            "KOMTAR",
            "UNESCO HERITAGE",
            "CHULIA STREET",
            "PENANG ROAD",
            "STREET OF HARMONY",
            "JETTY",
            "FORT CORNWALLIS",
            "EASTERN & ORIENTAL",
            "GURNEY DRIVE",
            "RECLINING BUDDHA"
    };

    public AppFrame(Graph graph) {
        this.graph = graph;

        try {
            // Use the Nimbus Look and Feel
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set and edit the title of the frame
        setTitle("PenangExpress");
        JLabel titleLabel = new JLabel("Survey Your Travel Route Using PenangExpress Now!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        // Insert image of the bus route
        ImageIcon imageIcon = new ImageIcon("src/main/resources/images/bus_route.png");
        Image image = imageIcon.getImage().getScaledInstance(700, -5, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(image);
        imageLabel = new JLabel(imageIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                // Create a rounded rectangle shape
                Shape shape = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20);

                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setClip(shape);
                super.paintComponent(g2d);
                g2d.dispose();
            }
        };

        // Declare the source label
        JLabel sourceLabel = new JLabel("FROM:");
        sourceLabel.setForeground(Color.WHITE);
        sourceComboBox = new JComboBox<>(stationNames); // Use the station names array directly
        sourceComboBox.setSelectedIndex(-1);

        // Declare the destination label
        JLabel destinationLabel = new JLabel("TO:");
        destinationLabel.setForeground(Color.WHITE);
        destinationComboBox = new JComboBox<>(stationNames); // Use the station names array directly
        destinationComboBox.setSelectedIndex(-1);

        // Declare the enter button
        enterButton = new JButton("ENTER");
        enterButton.setBackground(new Color(255, 255, 255)); // Attractive color
        enterButton.setForeground(Color.BLACK);

        // Declare the path route label
        pathLabel = new JLabel();
        pathLabel.setForeground(Color.WHITE);
        pathLabel.setFont(new Font("Calibri", Font.BOLD, 15));

        // Declare the price label
        priceLabel = new JLabel();
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setFont(new Font("Calibri", Font.BOLD, 15));

        // Declare the time label
        timeLabel = new JLabel();
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Calibri", Font.BOLD, 15));

        // Create a panel and set the layout manager
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(102, 55, 31)); // Attractive color

        // Create GridBagConstraints to control the layout
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5); // Add some spacing

        // Add the title label
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        panel.add(titleLabel, constraints);

        // Add the image label
        constraints.gridy = 1;
        panel.add(imageLabel, constraints);

        // Add the source label and combo box
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        panel.add(sourceLabel, constraints);

        constraints.gridx = 1;
        panel.add(sourceComboBox, constraints);

        // Add the destination label and combo box
        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(destinationLabel, constraints);

        constraints.gridx = 1;
        panel.add(destinationComboBox, constraints);

        // Add the search button
        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 2;
        panel.add(enterButton, constraints);

        // Add the path label
        constraints.gridy = 7;
        panel.add(pathLabel, constraints);

        // Add the time label
        constraints.gridy = 8;
        panel.add(timeLabel, constraints);

        // Add the price label
        constraints.gridy = 9;
        panel.add(priceLabel, constraints);

        // Set the panel as the content pane
        setContentPane(panel);

        // Set the default close operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Add the event listener for the enter button
        enterButton.addActionListener(e -> {
            String sourceStation = (String) sourceComboBox.getSelectedItem();
            String destinationStation = (String) destinationComboBox.getSelectedItem();

            // Convert the station names to station indices
            int sourceIndex = getStationIndex(sourceStation);
            int destinationIndex = getStationIndex(destinationStation);
            int time = 0;
            double price = 0;

            // Perform the DFS algorithm
            DepthFirstSearch dfs = new DepthFirstSearch(graph.getNumVertices());
            List<Integer> path = dfs.findPathDFS(graph, sourceIndex, destinationIndex);

            if (path != null) {
                StringBuilder pathText = new StringBuilder("ROUTE           : ");
                for (int i = 0; i < path.size(); i++) {
                    int stationIndex = path.get(i);
                    String stationName = stationNames[stationIndex];
                    pathText.append(stationName);
                    time += 5;  // Each edge taken is 5 minutes
                    price += 1.00;  // Each edge taken is RM1.00
                    if (i < path.size() - 1) {
                        pathText.append(" -> ");
                    }
                }
                time -= 5;
                price -= 1.00;

                // Display the time taken and ticket price
                String formattedTime = String.format("%.2f", price);
                pathLabel.setText(pathText.toString());
                timeLabel.setText("TIME TAKEN  : " + time + " MINUTES");
                priceLabel.setText("TICKET PRICE  : RM" + formattedTime);

                // Remove the labels and combo boxes
                panel.remove(sourceLabel);
                panel.remove(sourceComboBox);
                panel.remove(destinationLabel);
                panel.remove(destinationComboBox);
                enterButton.setVisible(false);
            }

            else {
                pathLabel.setText("No path found from " + sourceStation + " to " + destinationStation);
            }
        });

        // Set the frame visible
        setVisible(true);
    }

    // Return the station name based on index declared above
    private int getStationIndex(String stationName) {
        for (int i = 0; i < stationNames.length; i++) {
            if (stationNames[i].equals(stationName)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int numVertices = 11;   // Number of stations

        Graph graph = new Graph(numVertices);

        // Declare the edges between stations
        graph.addEdge(0, 1);
        graph.addEdge(0, 10);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);
        graph.addEdge(4, 5);
        graph.addEdge(5, 6);
        graph.addEdge(5, 8);
        graph.addEdge(6, 7);
        graph.addEdge(7, 8);
        graph.addEdge(8, 0);
        graph.addEdge(8, 9);
        graph.addEdge(9, 0);
        graph.addEdge(9, 10);

        SwingUtilities.invokeLater(() -> new AppFrame(graph));
    }
}