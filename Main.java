package tester;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;

public class Main {
	
	public static Scanner s;
	
    public static void main(String[] args) {
    	Expression.operatorListInit();
    	try {
    		
    		read(args[0]);
			execute(Integer.valueOf(args[1]));
		} catch (FileNotFoundException e) { e.printStackTrace();}
    }
    
    public static void read(String str) throws FileNotFoundException {
    	s = new Scanner(new File(str));

    	while(s.hasNextLine()) {
    		String buffer = s.nextLine();
    		program.add((buffer.startsWith("^"))? new MaxCondition(buffer) :
    					(buffer.endsWith("$"))? new LimitedCondition(buffer) : new Condition(buffer));
    	}
    }
    
    public static void execute(int m) {
        if(m > 0) {
        	b:
        	for(int n = 1; n <= m; n++) {
        		for(Command c : program)
        			if(c.check(n,m)) continue b;
        		System.out.println(n);
        	}
        }else {
        	System.out.println(m);
        }
    }

    public static List<Command> program = new ArrayList<Command> ();
}

interface Command{
	public boolean check(int n, int m);
}

class Condition implements Command {
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
        System.out.println(StringProcess.format(p, n, m));
        return true;
    }
}

class MaxCondition implements Command {
	
	public String p;
	
	public MaxCondition(String z) {
		String[] temp = z.split(":|\\|");
		p = temp[1];
	}
	
	public boolean check(int n, int m){
        if(n != m) return false;
        System.out.println(StringProcess.format(p, n, m));
        return true;
    }
	
}

class LimitedCondition implements Command {
	
	public int v;
	public int r = 0;
	public String p;
	public int l;

	public LimitedCondition(String z){
		String[] temp = z.split(":|\\||\\$");
		if(temp.length == 3){
			v = Integer.valueOf(temp[0]);
			p = temp[1];
			l = Integer.valueOf(temp[2]);
		}else{
			v = Integer.valueOf(temp[0]);
			r = Integer.valueOf(temp[1]);
			p = temp[2];
			l = Integer.valueOf(temp[3]);
		}
	}

	public boolean check(int n, int m){
		if(n % v != r) return false;
		System.out.println(StringProcess.format(p, n, m));
		if(--l == 0) Main.program.remove(this);
		return true;
	}
}

class StringProcess extends Expression{
	public static String format(String str, int n, int m) {
		String temp = str	.replace("^", String.valueOf(m))
							.replace("~", String.valueOf(n))
							.replace("\\n", "\n");
		String[] tempArr = temp.split("&");
		String output = "";
		for(String t : tempArr) 
			output += solve(toExpression(t));
		return output;
	}
}

class Expression {
	private String value;
	private Expression L;
	private Expression R;
	
	public Expression() {}
	public Expression(String value) { setValue(value);}
	public Expression(	String value,
						Expression left,
						Expression right) {
		setValue(value);
		setNodeL(left);
		setNodeR(right);
	}
	
	public String getValue() { return value;}
	public Expression getNodeL() { return L;}
	public Expression getNodeR() { return R;}
	public void setValue(String value) { this.value = value;}
	public void setNodeL(Expression nodeL) { this.L = nodeL;}
	public void setNodeR(Expression nodeR) { this.R = nodeR;}
	

	public boolean hasNodeL() { return getNodeL() != null;}
	public boolean hasNodeR() { return getNodeR() != null;}
	
	public boolean fullNode() { return hasNodeL() && hasNodeR();}
	public boolean hasNode() { return hasNodeL() || hasNodeR();}
	public boolean noNode() { return !hasNode();}
	
	public int getHeight() {
		if(noNode()) return 0;
		int l, r;
		return ((l = getNodeL().getHeight() ) > (r = getNodeR().getHeight()))? l + 1 : r + 1;
	}
	
	public static String trim(String in) {
		int layer = 0;
		boolean fallback = false; boolean uphill = true;
		int submin = 0;
		for(int i = 0; i < in.length(); i++) {
			if(in.charAt(i) == '(') layer++;
			if(in.charAt(i) == ')') {
				if(!fallback) {
					fallback = true;
					submin = layer;
				}
				layer--;
			}
			if(uphill && !in.substring(i).contains("(")) uphill = false;
			if(fallback && uphill) submin = (submin < layer)? submin : layer;
		}
		return trimIterate(in,submin);
	}
	
	public static String trimIterate(String in, int g) {
		if(g == 0) return in;	// iterate trim
		return (in.startsWith("(") && in.endsWith(")"))?
				trimIterate(in.substring(1,in.length() - 1), g - 1) : in;
	}
	
	public static boolean isEnclosed(String expression) {
		int layer = 0;
		for(int i = 0; i < expression.length(); i++) {
			if(expression.charAt(i) == '(') layer++;
			if(expression.charAt(i) == ')') layer--;
			if(layer < 0) return false;
		}
		return layer == 0;
	}
	
	public static Expression toExpression(String expression) {
		if(expression.equals("")) return null; // null check
		final String exp = trim(expression);
		int floor;
		for(Operator operator : operatorList) {
			if(!exp.contains(operator.getValue())) continue;
			floor = 0;
			int range = exp.length() - (operator.length() - 1);
			for(int i = 0; i < range; i++) {
				if(exp.charAt(i) == ')') floor--;
				if(floor != 0) continue;
				if(exp.charAt(i) == '(') floor++;
				if(exp.substring(i, i + operator.length()).equals(operator.getValue()))
					return new Expression(	operator.getValue(),
											toExpression(exp.substring(0,i)),
											toExpression(exp.substring(i + operator.length()))
											);
			}
		}
		return new Expression(exp);
	}
	
	public static String solve(Expression expression) {
		for(Operator operator : operatorList) 
			if(expression.getValue().equals(operator.getValue())) return operator.operate(expression);
		return expression.getValue();
	}
	
	private static boolean isNumeric(String str) { return str.matches("-?\\d+(\\.\\d+)?");}
	private static boolean isNull(String str) { return str == null;}
	
	public static void operatorListInit() {
		operatorList.add(constructOperation("+", (L, R) -> {
			if(isNumeric(L) && isNumeric(R)) return String.valueOf((Integer.parseInt(L) + Integer.parseInt(R)));
			if(isNull(L) || isNull(R)) return null;
			return L + "+" + R;
		}));
		operatorList.add(constructOperation("-", (L, R) -> {
			if(isNumeric(L) && isNumeric(R)) return String.valueOf((Integer.parseInt(L) - Integer.parseInt(R)));
			if(isNull(L) || isNull(R)) return null;
			return L + "-" + R;
		}));
		operatorList.add(constructOperation("*", (L, R) -> {
			if(isNumeric(L) && isNumeric(R)) return String.valueOf((Integer.parseInt(L) * Integer.parseInt(R)));
			if(isNull(L) || isNull(R)) return null;
			return L + "*" + R;
		}));
		operatorList.add(constructOperation("/", (L, R) -> {
			if(isNumeric(L) && isNumeric(R)) return String.valueOf((Integer.parseInt(L) / Integer.parseInt(R)));
			if(isNull(L) || isNull(R)) return null;
			return L + "/" + R;
		}));
		operatorList.add(constructOperation("%", (L, R) -> {
			if(isNumeric(L) && isNumeric(R)) return String.valueOf((Integer.parseInt(L) % Integer.parseInt(R)));
			if(isNull(L) || isNull(R)) return null;
			return L + "%" + R;
		}));
	}
	
	public static Operator constructOperation(String symbol, BiFunction<String, String, String> function) {
		return new Operator() {
			String value = symbol; 
			@Override public boolean compare(String str) { return str.equals(value);}
			@Override public int length() { return value.length();}
			@Override public String getValue() { return value;}
			@Override
			public String operate(Expression expression) {
				String nodeL = solve(expression.getNodeL());
				String nodeR = solve(expression.getNodeR());
				return function.apply(nodeL, nodeR);
			}
		};
	}
	
	public static List<Operator> operatorList = new ArrayList<Operator>();
}

interface Operator {
	public boolean compare(String str);
	public int length();
	public String getValue();
	public String operate(Expression expression);
}
