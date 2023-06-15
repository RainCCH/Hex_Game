package Hex;
import javax.swing.*;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.*;

import java.util.*;

public class MyJFrame extends JFrame implements ActionListener, MouseListener {
    Game game = new Game();
    int GAP_0 = 50;
    int WINDOW_WIDTH = 670, WINDOW_HEIGHT = 700;
    int GRID_SIZE = 60;
    int GAP_1 = GRID_SIZE;
    int last_time = -1;

    public MyJFrame(){
        initJFrame();
        initMenu();
        initData();
        setImage();
        this.addMouseListener(this);
        this.setVisible(true);
    }

    public void initJFrame(){
        this.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Play Some Hex!");
        this.setLayout(null);
    }

    public void initMenu(){
        JMenuBar jMenuBar;
        jMenuBar = new JMenuBar();
        jMenuBar.setSize(514,20);
        JMenu jMenu = new JMenu("Choices");
        JMenuItem jMenuItem = new JMenuItem("Restart");
        jMenuItem.addActionListener(this);
        jMenu.add(jMenuItem);
        jMenuBar.add(jMenu);
        this.setJMenuBar(jMenuBar);
    }
    
    public void setImage(){
        this.getContentPane().removeAll();
        for (int i = 0; i < game.data.length; i++){
            for (int j = 0; j < game.data[i].length; j++) {
                // JPanel panel = new JPanel(new FlowLayout());
                int num = game.data[i][j];
                int diff = (i%2 == 1)?GRID_SIZE/2:0;
                ImageIcon imageIcon = new ImageIcon("images/background.png");
                // JLabel jlabel_bg = new JLabel(scaleImage(imageIcon, 60, 60));
                // jlabel_bg.setLocation(j*50 + 30+diff,i*50 + 30);
                // panel.add(jlabel_bg);
                if(num == 1){
                    imageIcon = new ImageIcon("images/red_2.png");
                }
                else if(num == -1){
                    imageIcon = new ImageIcon("images/blue_2.png");
                }
                if(game.pre_selected_box[0] != -1){
                    HashSet<Integer[]> surroundings = game.getSurroundings(game.pre_selected_box[0], game.pre_selected_box[1], true);

                    if(j == game.pre_selected_box[1] && i == game.pre_selected_box[0]){
                        imageIcon = (num == 1)?(new ImageIcon("images/red_white_2.png")):(new ImageIcon("images/blue_white_2.png"));
                    }
                    for(Integer[] coor: surroundings){
                        if(j == coor[1] && i == coor[0] && game.data[i][j] == 0){
                            imageIcon = new ImageIcon("images/yellow_background2.png");
                        }
                    }
                    HashSet<Integer[]> surrondings_sec = game.getSurroundings(game.pre_selected_box[0], game.pre_selected_box[1], false);
                    for(Integer[] coor: surrondings_sec){
                        if(j == coor[1] && i == coor[0] && game.data[i][j] == 0){
                            imageIcon = new ImageIcon("images/green_background2.png");
                        }
                    }
                }
                JLabel jLabel = new JLabel(scaleImage(imageIcon, GRID_SIZE, GRID_SIZE));
                jLabel.setLocation(j*GAP_1 + GAP_0+diff,i*GAP_1 + GAP_0);
                jLabel.setSize(GRID_SIZE, GRID_SIZE);
                this.add(jLabel);
            }
        }
        ShowTurn();
        this.getContentPane().setBackground(new Color(50, 50, 50));
        if(!game.start){
            JButton start_button = new JButton("Start the Game!");
            start_button.setLocation(WINDOW_WIDTH/2-100, WINDOW_HEIGHT-100);
            start_button.setSize(200, 50);
            start_button.addActionListener(this);
            this.add(start_button);
        }
        else{
            JButton stop_button = new JButton("Stop the Game!");
            stop_button.setLocation(WINDOW_WIDTH/2-100, WINDOW_HEIGHT-100);
            stop_button.setSize(200, 50);
            stop_button.addActionListener(this);
            this.add(stop_button);
        }
        this.getContentPane().repaint();
    }
    
    public void initData(){
        game.reset();
        setImage();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if(action.equals("Restart")){
            initData();
        }
        else if(action.equals("Start the Game!")){
            game.start = true;
        }
        else if(action.equals("Stop the Game!")){
            game.start = false;
            game.pre_selected_box[0] = -1; game.pre_selected_box[1] = -1;
        }
        setImage();
    }

    public ImageIcon scaleImage(ImageIcon icon, int w, int h)
    {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();
        if(icon.getIconWidth() > w)
        {
          nw = w;
          nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }
        if(nh > h)
        {
          nh = h;
          nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }
        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
 
    @Override
    public void mouseReleased(MouseEvent e) {
    }
 
    @Override
    public void mouseEntered(MouseEvent e) {
    }
 
    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // System.out.println(e.getX() + " " + e.getY());
        if(!(e.getX() < GAP_1*9+GAP_0 && e.getX() > GAP_0 && e.getY() < GAP_1*9+GAP_0+GRID_SIZE/2 && e.getY() > GAP_0)){
            return;
        }
        int j = (e.getY()-GAP_1-GAP_0)/GAP_1;
        int i;
        
        if(j%2 == 1){
            i = (e.getX()-GAP_0-GRID_SIZE/2)/GAP_1;
        }
        else i = (e.getX()-GAP_0)/GAP_1;
        if(!(i >= 0 && i <= 8 && j >= 0 && j <= 8)){
            return;
        }
        // System.out.println(i + " " + j);
        if(game.clicked(i, j)){
            setImage();
        }
        if(game.win() != 0){
            for(int k = 0; k < 9; k++){
                Arrays.fill(game.data[k], game.win());
            }
            setImage();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String message = game.win()==1?"Player I Wins!":"Player II Wins!";
            JOptionPane.showMessageDialog(this, message);
            game.reset();
            setImage();
        }
    }

    public void ShowTurn(){
        String text = game.turn?"Player I's Turn":"Player II's Turn";
        if(!game.start){
            text = "Click the button at the bottom to start the game!";
        }
        JLabel jlabel = new JLabel(text);
        jlabel.setFont(new java.awt.Font("Dialog", 1, 25));
        jlabel.setForeground(game.turn?Color.RED:Color.BLUE);
        jlabel.setVerticalAlignment(JLabel.CENTER);
        jlabel.setHorizontalAlignment(JLabel.CENTER);
        jlabel.setSize(WINDOW_WIDTH, 50);
        if(!game.start){
            jlabel.setFont(new java.awt.Font("Dialog", 1, 15));
            jlabel.setForeground(Color.WHITE);
            jlabel.setVerticalAlignment(JLabel.CENTER);
            jlabel.setHorizontalAlignment(JLabel.CENTER);
            jlabel.setSize(WINDOW_WIDTH, 50);
        }
        this.add(jlabel);
    }
}