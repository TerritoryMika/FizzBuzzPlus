package tester;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	
	public static Scanner s;
	
    public static void main(String[] args) {
    	try {
    		read(args[0]);
			execute(Integer.valueOf(args[1]));
		} catch (FileNotFoundException e) { e.printStackTrace();}
    }
    
    public static void read(String str) throws FileNotFoundException {
    	s = new Scanner(new File(str));

    	while(s.hasNextLine()) 
    		program.add(new Condition(s.nextLine()));

    }
    
    public static void execute(int m) {
        if(m > 0) {
        	b:
        	for(int n = 1; n <= m; n++) {
        		for(Condition c : program)
        			if(c.check(n,m)) continue b;
        		System.out.println(n);
        	}
        }else {
        	System.out.println(m);
        }
    }

    public static List<Condition> program = new ArrayList<Condition> ();
}

class Condition{
    public int v;
    public int r = 0;
    public String p;

    public Condition(String z){
        String[] temp = z.split(":|\\|");
        if(temp.length == 2){
            v = Integer.valueOf(temp[0]);
            p = temp[1];
        }else{
            v = Integer.valueOf(temp[0]);
            r = Integer.valueOf(temp[1]);
            p = temp[2];
        }
    }

    public boolean check(int n, int m){
        if(n % v != r) return false;
        System.out.println(p.replace("`", String.valueOf(m)).replace("~", String.valueOf(n)));
        return true;
    }
}