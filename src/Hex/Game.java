package Hex;
import java.util.*;

public class Game {
    public int[][] data = new int[9][9];
    public int[] pre_selected_box = {-1, -1};
    int[][] dir_1 = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, -1}, {-1, -1}};
    int[][] dir_2 = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, 1}};
    int[][] dir_sec_1 = {{-2, -1}, {-2, 0}, {-2, 1}, {-1, -2}, {-1, 1}, {0, -2}, {0, 2}, {1, -2}, {1, 1}, {2, -1}, {2, 0}, {2, 1}};
    int[][] dir_sec_2 = {{-2, -1}, {-2, 0}, {-2, 1}, {-1, -1}, {-1, 2}, {0, -2}, {0, 2}, {1, -1}, {1, 2}, {2, -1}, {2, 0}, {2, 1}};
    boolean start = false;
    boolean turn = true;
    public Game(){
        reset();
    }
    public void reset(){
        for(int i = 0; i < data.length; i++){
            Arrays.fill(data[i], 0);
        }
        data[1][1] = -1; data[7][7] = -1;
        data[1][7] = 1; data[7][1] = 1;
        pre_selected_box[0] = -1; pre_selected_box[1] = -1;
        turn = true;
        start = false;
    }
    public boolean clicked(int i, int j){
        if(!start) return false;
        boolean changed = false;
        if(data[j][i] == ((turn)?1:-1)){
            pre_selected_box[0] = j;
            pre_selected_box[1] = i;
            return true;
        }
        else{
            HashSet<Integer[]> layer_1 = getSurroundings(pre_selected_box[0], pre_selected_box[1], true);
            HashSet<Integer[]> layer_2 = getSurroundings(pre_selected_box[0], pre_selected_box[1], false);
            // System.out.println(j + " " + i + " " + data[j][i]);
            // data[j][i] = (turn)?1:2;
            if(pre_selected_box[0] == -1) return false;
            for(Integer[] coor: layer_1){
                if(j == coor[0] && i == coor[1] && data[j][i] == 0){
                    // System.out.println("Wow");
                    data[j][i] = data[pre_selected_box[0]][pre_selected_box[1]];
                    add_new(j, i);
                    changed = true;
                    turn = !turn;
                }
            }
            for(Integer[] coor: layer_2){
                if(j == coor[0] && i == coor[1] && data[j][i] == 0){
                    // System.out.println("motherfucker");
                    data[j][i] = data[pre_selected_box[0]][pre_selected_box[1]];
                    data[pre_selected_box[0]][pre_selected_box[1]] = 0;
                    add_new(j, i);
                    changed = true;
                    turn = !turn;
                }
            }
            if(changed){
                pre_selected_box[0] = -1;
                pre_selected_box[1] = -1;
            }
            // System.out.println(j + " " + i + " " + data[j][i]);
        }
        return changed;
    }
    public HashSet<Integer[]> getSurroundings(int x, int y, boolean first){
        int[][] dir = (x%2==0)?dir_1:dir_2;
        if(!first) dir = (x%2==0)?dir_sec_1:dir_sec_2;
        HashSet<Integer[]> surroundings = new HashSet<>();
        for(int i = 0; i < dir.length; i++){
            Integer[] tmp = new Integer[2];
            tmp[0] = x + dir[i][0];
            tmp[1] = y + dir[i][1];
            if(tmp[0] >= 0 && tmp[0] <= 8 && tmp[1] >= 0 && tmp[1] <= 8){
                surroundings.add(tmp);
            }
        }
        return surroundings;
    }
    public void add_new(int x, int y){
        for(Integer[] coor: getSurroundings(x, y, true)){
            // System.out.println(coor[0] + " " + coor[1]);
            if(data[coor[0]][coor[1]] == -data[x][y]){
                data[coor[0]][coor[1]] *= -1;
            }
        }
    }
    public int win(){
        boolean survive_1 = false, survive_2 = false;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(data[i][j] != 0){
                    if(!(getSurroundings(i, j, true).isEmpty() && getSurroundings(i, j, false).isEmpty())){
                        if(data[i][j] == 1) survive_1 = true;
                        if(data[i][j] == -1) survive_2 = true;
                    }
                    if(survive_1 && survive_2) return 0;
                }
            }
        }
        return (!survive_1)?-1:1;
    }
}