// CH6_2: Main.java
//
//  Created on: 2024.11. 5.
//      Author: Junha Kim
//
//  + PersonManager: 메뉴 변경, chAddress() 추가
//  + Calculator, Galaxy, IPhone::calculate(String expr) 추가
//  + CurrentUser::memo(), calcString() 추가, 메뉴 수정; "2+3" 가능
//  + Memo 클래스 추가
//

import java.util.*;

//===============================================================================
// main()
//===============================================================================
public class Main 
{
    public static void main(String[] args) {
        //--------------------------------
        // AutoCheck(chk, trace)
        // chk: 1(자동 오류 체크), 0(키보드에서 직접 입력하여 프로그램 실행)
        // trace: true(오류발생한 곳 출력), false(단순히 O, X만 표시)
        //--------------------------------
        //int chk = 1; if (chk != 0) new AutoCheck(chk, true).run(); else

        run(new Scanner(System.in)); // 위 내용 참고하여 적절히 수정할 것
    }

    public static void run(Scanner scan) {
        // UI 클래스의 setScanner() 함수를 호출함; setScanner()가 static 함수라 이렇게 호출 가능함
        UI.setScanner(scan); // UI 클래스 내의 static 함수 호출
        MainMenu.run(); // MainMenu 클래스 내의 static 함수 호출방법: 클래스이름.함수이름();
        // TODO: scan이 더 이상 필요없으므로 닫아라.
        scan.close();
    }
}

//===============================================================================
// Main Menu
//===============================================================================
class MainMenu
{
	
    public static void run() {
        String menuStr =
                "******* Main Menu ********\n" +
                "* 0.exit 1.PersonManager *\n" +
                "* 2.ch2 3.ch3 4.ch5      *\n" +
                "**************************\n";
        // 위 메뉴(menuStr)를 화면에 출력하고 메뉴 항목(정수 값)을 입력 받는다.
        final int MENU_COUNT = 5;
        int menuItem;
        while((menuItem = UI.selectMenu(menuStr, MENU_COUNT)) != 0) {
        	switch(menuItem) {
        	case 1:
        		new MultiManager().run();
        		break;
        	case 2:	
        		Ch2.run();
        		break;
        	case 3:
        		Ch3.run();
        		break;
        	case 4:
        		new Inheritance().run();
        		break;
        		
        	}
        }
        System.out.println("\nGood bye!!");
    }
    /*
    public static void run() {
        var user = new Student("s1", 1, 65.4, true,  "Jongno-gu Seoul", "Physics", 3.8, 1);
     Memo m = new Memo(user.getMemo());
        m.run();
        user.setMemo(m.toString());
        System.out.println("\nGood bye!!");
    }
    */
} // class MainMenu

interface BaseStation
{
    boolean connectTo(String caller, String callee);
    // 이 메소드는 Phone::sendCall(String caller)에서 호출되어야 한다.
    // callee라는 사람이 존재할 경우
    //     System.out.println("base station: sends a call signal of "+caller+
    //               " to "+callee)를 출력하고
    //     이 사람의 등록된 Phone의 receiveCall(caller, callee)을 호출하고 true를 리턴
    // 존재하지 않을 경우 "callee_name: NOT found"라는 에러 메시지 출력하고 false 리턴
}

interface Phone {
    void sendCall(String callee);
    // 이 메소드는 "made a call to 수신자_이름(callee)"라고 출력해야 하며 이 출력의 앞 또는 뒤에 
    // 발신자 이름도 함께 출력하되 메이커가 알아서 적절히 회사명, 모델명 등과 함께 표시하면 된다. 
    // 그런 후 baseStation.connectTo(caller, callee)를 호출해야 한다.
    void receiveCall(String caller);
    // 이 메소드는 "received a call from 송신자_이름(caller)"라고 출력해야 하며 이 출력의 앞 또는 뒤에 
    // 수신자 이름도 함께 출력하되 메이커가 알아서 적절히 회사명, 모델명 등과 함께 표시하면 된다. 
}

interface Calculator {
    // +, -, *, / 사칙연산만 지원하고 그 외의 연산자일 경우 "NOT supported operator" 에러 메시지 출력
    // 수식과 계산 결과 또는 에러 메시지를 출력해야 하며 이 출력의 앞 또는 뒤에 
    // 계산기 소유주 이름도 함께 출력하되 메이커가 알아서 적절히 회사명, 모델명 등과 함께 표시하면 된다. 
    void calculate(double oprd1, String op, double oprd2); // 예: (3, "+", 2.0)
    void calculate(String expr);                     // 예: ("3+2") ("2+ 3")
}

abstract class SmartPhone implements Phone, Calculator { 
    protected static BaseStation baseStation;
    protected static Calendar userDate = null; // ch6_1
    public static void setBaseStation(BaseStation bs) { baseStation = bs; }
    public static void setDate(String line) { // ch6_1
        if(line.equals("")) {
        	userDate = null;
        	return;
        }
        Scanner s = new Scanner(line);
        
        userDate = Calendar.getInstance();
        userDate.clear();
        
        userDate.set(s.nextInt(), s.nextInt()-1, s.nextInt(), s.nextInt(), s.nextInt(), s.nextInt());
        s.close();
        	
    	/* 
        TODO: 
        line이 빈 문자열이면 userDate를 null로 설정하고 바로 리턴한다.
        Scanner s = new Scanner(line); 
        // 참고로 스캐너는 키보드가 아닌 문자열인 line에서도 데이타를 입력 받을 수 있다.
        // 또한 스캐너는 텍스트(.txt) 파일에서도 입력 받을 수 있다. (추후 파일에서 예제 나옴)
        Calendar에서 현재 날짜와 시간 정보를 가지는 새로운 객체를 생성하여 userDate에 저장한다.
        userDate의 날짜와 시간 정보를 모두 지운다. [교재 예제 6-11 main() 참조]
        스캐너 s에서 연속적으로 정수(nextInt()) 값을 입력 받아 userDate에 설정한다.
        즉, s에서 (년 월 일 시 분 초)를 순서적으로 정수 값으로 입력 받아 
        userDate의 적절한 set()을 호출하여 설정하면 된다.
        s.close();
        */
        // 위와 같이 Scanner s를 이용하면 문자열 line에서 쉽게 정수,실수,문자열을 분리해 읽을 수 있다.
        // 만약 이 방법을 사용하지 않을 경우 교재 예제 6-12 방법을 사용해야 하는데 조금 복잡해진다.
    }
    
    protected String owner;  // 스마트폰 소유주 이름
    protected Calendar date; // 스마트폰 구입 날짜: ch6_1
    
    public SmartPhone(String owner) { this.owner = owner; date = (userDate == null)? Calendar.getInstance(): (Calendar)userDate.clone();}
    public abstract String getMaker(); // 스마트폰 제조사명: 상속 받은 클래스에서 구현해야 함
    public abstract SmartPhone clone();
    public void setOwner(String owner) { this.owner = owner; }
    //public void print() { System.out.print(owner+"'s Phone: "+getMaker()); }
    //public void println() { print(); System.out.println(); }
    @Override
    public String toString()	{ return owner+"'s Phone: " + getMaker() + " Phone (" + date.get(Calendar.YEAR) + "." + (date.get(Calendar.MONTH)+1) + "." + date.get(Calendar.DATE) + " " + (date.get(Calendar.AM_PM)==Calendar.AM ? "AM" : "PM") + " " + date.get(Calendar.HOUR) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND) + ")"; }
}

class GalaxyPhone extends SmartPhone {
    private void printTradeMark(String appName) {
        System.out.println(" @ "+owner+"'s Galaxy "+appName);
    }
    public GalaxyPhone(String owner) { super(owner); }
    @Override
    public void sendCall(String callee)        { 
        System.out.print("made a call to "+callee); printTradeMark("Phone");
        baseStation.connectTo(owner, callee);
    }
    @Override
    public void receiveCall(String caller)     { 
        System.out.print("received a call from "+caller); printTradeMark("Phone");
    }
    @Override
    public void calculate(double oprd1, String op, double oprd2) {
        System.out.print(oprd1+" "+op+" "+oprd2+" = ");
        switch (op) {
        case "+": System.out.print(oprd1 + oprd2); break;
        case "-": System.out.print(oprd1 - oprd2); break;
        case "*": System.out.print(oprd1 * oprd2); break;
        case "/": System.out.print(oprd1 / oprd2); break;
        default:  System.out.print("NOT supported operator"); break;
        }
        printTradeMark("Calculator");
    }
    @Override
    public void calculate(String expr) {
        String oprs[] = { "+", "-", "*", "/" };
        int i;
        for (i = 0; i < oprs.length; i++) // expr에 oprs[i] 있는지 조사하고
        		if(expr.indexOf(oprs[i]) != -1)  // 있으면 expr 내의 인덱스, 없으면 음수 반환
        			break;                      
        if ((i >= oprs.length))             // expr에 적절한 연산자가 없을 경우
            calculate(0, expr, 0);          // 에러 처리를 위해 호출함
        else {
        	String[] tmp = expr.split("\\"+oprs[i]);
        	double token1 = Double.parseDouble(tmp[0]);
        	double token2 = Double.parseDouble(tmp[1]);
        	calculate(token1, oprs[i], token2);
        }
    }
    @Override
    public String getMaker() { return "SAMSUNG"; }
    @Override
    public SmartPhone clone() {
    	return new GalaxyPhone(super.owner);
    }
}

class IPhone extends SmartPhone {
    String model;
    public IPhone(String owner, String model) { super(owner); this.model = model; };
    
    @Override
    public void sendCall(String callee)        { 
    	System.out.println(owner + "'s IPhone " + model + ": made a call to " + callee);
    	baseStation.connectTo(owner, callee);
    }
    @Override
    public void receiveCall(String caller)     {
    	System.out.println(owner + "'s IPhone " + model + ": received a call from " + caller);
    }
    private double add(double oprd1, double oprd2) { return oprd1 + oprd2; }
    private double sub(double oprd1, double oprd2) { return oprd1 - oprd2; }
    private double mul(double oprd1, double oprd2) { return oprd1 * oprd2; }
    private double div(double oprd1, double oprd2) { return oprd1 / oprd2; }
    @Override
    public void calculate(double oprd1, String op, double oprd2) {
    	System.out.print(owner + "'s IPhone " + model + ": ");
    	double tmp = 0;
        switch (op) {
        case "+": tmp = add(oprd1, oprd2); break;
        case "-": tmp = sub(oprd1, oprd2); break;
        case "*": tmp = mul(oprd1, oprd2); break;
        case "/": tmp = div(oprd1, oprd2); break;
        default:  System.out.print(op + " = NOT supported operator"); return;
        }
        System.out.print(oprd1+" "+op+" "+oprd2+" = " + tmp);
    }
    @Override
    public void calculate(String expr) {
    	String oprs[] = { "+", "-", "*", "/" };
        int i, j=0;
        for (i = 0; i < oprs.length; i++) // expr에 oprs[i] 있는지 조사하고
        		if((j=expr.indexOf(oprs[i])) != -1)  // 있으면 expr 내의 인덱스, 없으면 음수 반환
        			break;                      
        if ((i >= oprs.length))             // expr에 적절한 연산자가 없을 경우
            calculate(0, expr, 0);          // 에러 처리를 위해 호출함
        else {
        	String[] tmp = expr.split("\\"+oprs[i]);
        	String token1 = expr.substring(0, j);
        	String op = expr.substring(j, j+1);
        	String token2 = expr.substring(j+1);
        	expr = token1 + " " + op + " " + token2;
        	
        	Scanner s = new Scanner(expr);
        	double num1 = s.nextDouble();
        	String str = s.next();
        	double num2 = s.nextDouble();
        	
        	calculate(num1, str, num2);
        	s.close();
        }
    }
    @Override
    public String getMaker() { return "Apple"; }
    @Override
    public SmartPhone clone() {
    	return new IPhone(super.owner, model);
    }
}

//===============================================================================
//class Person: ch4_1
//===============================================================================
class Person 
{
 private String  name;    // 이름
 private int     id;      // Identifier
 private double  weight;  // 체중
 private boolean married; // 결혼여부
 private String  address; // 주소
 private String passwd;
 private SmartPhone smartPhone; // 스마트폰: 5_3에서 추가
 private String     memo;       // 메모: 6_2
 
 // 생성자 함수들
 public Person(String name) {
	 this(name, 0, 0.0, false, "");
 }
 public Person(String name, int id, double weight, boolean married, String address) { 
     this(name, id, weight, married, address, null);
     //System.out.print("Person(): ");  printMembers();  System.out.println();
 }  
 public Person(Scanner sc) { inputMembers(sc); }
 public Person(Person p) { // 복사 생성자: p객체를 복사하여 현재 객체를 초기화 한다.
     assign(p); 
     //System.out.print("Person(p): ");  printMembers();  System.out.println();
 }
 public Person(String name, int id, double weight,  // 5_3
         boolean married, String address, SmartPhone smartPhone) {
     set(name, "", id, weight, married, address, smartPhone);
 }
 /*
 public void println() { 
     print(); System.out.println(); 
 }
 public void println(String msg) { 
      System.out.print(msg); print(); System.out.println(); 
 }
 */
 
 // Getter: getXXX() 관련 함수들
 public String	getName()	{ return name; }
 public int 	getId()		{ return id; }
 public double	getWeight()	{ return weight; }
 public boolean	getMarried(){ return married; }
 public String	getAddress(){ return address; }
 public String	getPasswd()	{ return passwd; }
 public SmartPhone getSmartPhone() { return smartPhone; }
 public Calculator getCalculator() { return smartPhone; }
 public Phone getPhone()	{ return smartPhone; }
 public String  getMemo()    { return memo; } // 6_2
  
 // Setter: overloading: set() 함수들 중복
 public void set(String name)           { this.name = name; smartPhone.setOwner(name); } // 5_3: smartPhone
 public void set(int id)				{ this.id = id; }
 public void set(double weight)			{ this.weight = weight; }
 public void set(boolean married)		{ this.married = married; }
 public void set(String name, String passwd ,int id, double weight, boolean married, String address, SmartPhone smartPhone) {
	 this.name = name;
	 this.passwd = passwd;
	 this.id = id;
	 this.weight = weight;
	 this.married = married;
	 this.address = address;
	 setSmartPhone(smartPhone);
     memo = ""; // 6_2
 }
 public void setAddress(String address) { this.address = address; }
 public void setPasswd(String passwd)	{ this.passwd = passwd; }
 public void setSmartPhone(SmartPhone smPhone) { // 5_3
     if (smPhone != null) {        // smartPhone 멤버가 이미 초기화된 경우
         smartPhone = smPhone;     // smartPhone을 smPhone으로 변경
         smartPhone.setOwner(name);
     }
     else // 멤버 초기화: smartPhone 멤버를 아래와 같이 초기화함
         smartPhone = ((id % 2) == 1) ? new GalaxyPhone(name) : new IPhone(name, "13"); // id가 짝수인 경우
 }
 public void setMemo(String m) { memo = m; } // 6_2

 // Candidates for virtual functions and overriding 
 // assign() 함수
 public void assign(Person user) {
	 set(user.name, user.passwd, user.id, user.weight, user.married, user.address, user.smartPhone.clone());
 }
 // print(), clone(), whatAreYouDoing(), equals(), input() 함수
 //public void print() { printMembers(); }
 public void input(Scanner sc) { inputMembers(sc); }

 public Person clone() {
     //System.out.println("Person::clone()");
     return new Person(this); 
 }
 public void whatAreYouDoing() {
	 System.out.println(name +" is taking a rest.");
 }
 @Override
 public boolean equals(Object o) {
	 Person p = (Person)o;
	 if(this.name.equals(p.name) && (this.id == p.id))
		 return true;
	 else
		 return false;
 }
 private void inputMembers(Scanner sc) {
	 name = sc.next();
	 passwd = "";
	 id = sc.nextInt();
	 weight = sc.nextDouble();
	 married = sc.nextBoolean();
     // 아래는 주소 필드를 입력 받는 부분이며 수정없이 그대로 사용하면 된다.
     // :로 시작하고 :로 끝나는 부분의 서브 문자열을 읽어 냄
     while ((address = sc.findInLine(":.*:")) == null)
         sc.nextLine();
     address = address.substring(1, address.length()-1); 
     // 양쪽의 : :를 제거한 서브 문자열을 넘겨 받음	 
 }
 /*
 private void printMembers() {
	 System.out.print(name+" "+id+" "+weight+" "+married+" :"+address+": ");
 }
 */
 @Override
 public String toString() {
	 return name+" "+id+" "+weight+" "+married+" :"+address+": ";
 }
}
//===============================================================================
//class Student: ch5_1
//===============================================================================
//Person 클래스를 상속하며 학생 정보를 몇 가지 추가하여 가지고 있음
class Student extends Person { // TODO: Person 클래스를 상속하라.
 private String department; // 학과
 private double GPA;        // 평균평점
 private int year;          // 학년

 public Student(String name, int id, double weight, boolean married, String address,
                String department, double GPA, int year) {
	 super(name, id, weight, married, address);
     set(department, GPA, year); // 현재 객체의 멤버들을 초기화 
     //System.out.print("Student(): "); printMembers(); System.out.println();
 }
 public Student(Student s) {
	 super(s);
	 set(s.department, s.GPA, s.year);
	 //System.out.print("Student(s): "); printMembers(); System.out.println();
 }
 public Student(Scanner sc) { super(sc); inputMembers(sc); }
 // getter and setter
 public String getDepartment()	{ return department; }
 public double getGPA()			{ return GPA; }
 public int getYear()			{ return year; }
  
 public void setDepartment(String department)	{ this.department = department; }
 public void setGPA(double GPA)					{ this.GPA = GPA; }
 public void setYear(int year)					{ this.year = year; }
 public void set(String department, double GPA, int year) {
	 this.department = department;
	 this.GPA = GPA;
	 this.year = year;
 }
 
 // Overriding
 @Override
 public void assign(Person user) {
	 Student s = (Student)user;
	 super.assign(s);
	 set(s.department, s.GPA, s.year);
 }
 /*
 @Override
 public void print() { 
	 super.print();
	 printMembers();
 }
 */
 @Override
 public boolean equals(Object o) {
     Student s = (Student)o;
     return (super.equals(o) && this.year == s.year && this.department.equals(s.department)) ? true : false;
 }
 @Override
 public void whatAreYouDoing() {
	 System.out.println("~~~~~~~~~~~~~~~~ Student::whatAreYouDoing() ~~~~~~~~~~~~~~~~");
	 study();
	 takeClass();
	 System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
 }
 @Override
 public Person clone() {
     //System.out.println("Student::clone()");
     return new Student(this); 
 }
 @Override
 public void input(Scanner sc) {
	 super.input(sc);
	 inputMembers(sc);
 }
 
 // printMembers(), inputMembers(Scanner sc)
 /*
 private void printMembers() {
	 System.out.print(department+" "+GPA+" "+year);
 }
 */
 private void inputMembers(Scanner sc) {
	 department = sc.next();
	 GPA = sc.nextDouble();
	 year = sc.nextInt();
 }
 // 새로 추가된 메소드
 public void study() { 
     System.out.println(getName()+" is studying as a "+year+"-year student in "+department); 
 }
 public void takeClass() { 
	 System.out.println(getName()+" took several courses and got GPA "+GPA); 
 }
 
 @Override
 public String toString() {
	 return super.toString() + department+" "+GPA+" "+year; 
 }
}

//===============================================================================
//class Worker: ch5_1
//===============================================================================
//Person 클래스를 상속하며 회사원 정보를 몇 가지 추가하여 가지고 있음
class Worker extends Person { 
 private String company;    // 회사명
 private String position;   // 직급

 public Worker(String name, int id, double weight, boolean married, String address,
               String company, String position) {
	 super(name, id, weight, married, address);
	 set(company, position);
	 //System.out.print("Worker(): "); printMembers(); System.out.println();
 }
 public Worker(Worker w) {
	 super(w);
	 set(w.company, w.position);
	 //System.out.print("Worker(w): "); printMembers(); System.out.println();
 }
 public Worker(Scanner sc) { super(sc); inputMembers(sc); }
 
 // getter and setter
 public String getCompany()		{ return company; }
 public String getPosition()	{ return position; }
 
 public void setCompany(String company)		{ this.company = company; }
 public void setPosition(String position)	{ this.position = position; }
 public void set(String compnay, String position) {
	 this.company = compnay;
	 this.position = position;
 }
 
 // Overriding
 @Override
 public void assign(Person user) {
	 Worker w = (Worker)user;
	 super.assign(w);
	 set(w.company, w.position);
 }
 /*
 @Override
 public void print() { 
	 super.print();
	 printMembers();
 }
 */
 @Override
 public boolean equals(Object o) {
     Worker w = (Worker)o;
     return (super.equals(o) && this.company.equals(w.company) && this.position.equals(w.position)) ? true : false;
 }
 @Override
 public void whatAreYouDoing() {
	 System.out.println("!!!!!!!!!!!!!!!! Worker::whatAreYouDoing()!!!!!!!!!!!!!!!!!");
	 work();
	 goOnVacation();
	 System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
 }
 @Override
 public Person clone() {
     //System.out.println("Worker::clone()");
     return new Worker(this); 
 }
 @Override
 public void input(Scanner sc) {
	 super.input(sc);
	 inputMembers(sc);
 }
 // printMembers(), inputMembers(Scanner sc)
 /*
 private void printMembers() {
	 System.out.print(company + " " + position);
 }
 */
 private void inputMembers(Scanner sc) {
	 company = sc.next();
	 position = sc.next();
 }
 // 새로 추가된 메소드
 public void work() { 
     System.out.println(getName()+" works in "+company+" as "+position); 
 }
 public void goOnVacation() { 
     System.out.println(getName()+" is now enjoying his(her) vacation."); 
 }
 @Override
 public String toString() {
	 return super.toString() + company + " " + position;
 }
}

//String과 StringBuffer 클래스의 활용을 연습하기 위한 클래스
class Memo
{
 private StringBuffer mStr;  // 메모를 저장하고 수정하기 위한 문자열 버퍼

 // 문자열 m을 이용하여 StringBuffer를 생성한다.
 public Memo(String m)    { mStr = new StringBuffer(m); }

 // StringBuffer mStr을 문자열로 변환하여 리턴한다.
 @Override
 public String toString() { return mStr.toString(); }

 private String memoData =  
     "The Last of the Mohicans\n"+
     "James Fenimore Cooper\n"+
     "Author's Introduction\n"+
     "It is believed that the scene of this tale, and most of the information\n"+
     "necessary to understand its allusions, are rendered sufficiently \n"+
     "obvious to the reader in the text itself, or in the accompanying notes.\n"+
     "Still there is so much obscurity in the Indian traditions, and so much\n"+
     "confusion in the Indian names, as to render some explanation useful.\n"+
     "Few men exhibit greater diversity, or, if we may so express it, \n"+
     "greater antithesis of character, \n"+
     "than the native warrior of North America.";
 
 public void run() {
     String menuStr =
     "+++++++++++++++++++++ Memo Management Menu +++++++++++++++++++\n"+
     "+ 0.exit 1.display 2.find 3.findReplace 4.compare 5.dispByLn +\n"+
     "+ 6.delLn 7.replLn 8.scrollUp 9.scrollDown 10.inputMemo      +\n"+
     "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";

     // 멤버 mStr이 비었을 경우 위 memoData로 초기화한다.
     if (mStr.length() == 0) mStr.append(memoData);
     final int MENU_COUNT = 11; // 상수 정의

     while (true) {
         int menuItem = UI.selectMenu(menuStr, MENU_COUNT);
         switch(menuItem) {
         case 1: display();     break;
         case 2: find();        break;
         case 3: findReplace(); break;
         case 4: compare();     break;
         case 5: dispByLn();    break;
         case 6: delLn();       break;
         case 7: replLn();      break;
         case 8: scrollUp();    break;
         case 9: scrollDown();  break;
         case 10:inputMemo();   break;
         case 0:                return;
         }
     }
 }
 void display() { // Menu item 1
     System.out.println("------- Memo -------");
     System.out.print(mStr);
     if (mStr.length() > 0 && mStr.charAt(mStr.length()-1) != '\n')
         System.out.println(); // mStr 끝에 줄바꾸기 문자가 없을 경우 대신 출력
     System.out.println("--------------------");
 }
 // content 문자열의 start 인덱스부터 word 문자열 단어를 찾아 그 단어의 시작 인덱스를 반환함
 // 찾지 못한 경우 -1를 반환함
 // 찾는 단어는 " "로 분리된 word 단어만 찾고 다른 긴 단어 속에 포함된 경우에는 스킵한다.
 private int findWord(String content, String word, int start) {
     // content.indexOf(word, start): 
     //    content 문자열의 start 인덱스 위치부터 word를 검색하고 
     //    찾은 경우 찾은 시작 위치를, 못 찾은 경우 -1를 반환함
     //    word가 다른 긴 단어의 속에 포함되어 있어도 검색이 된다.
     if ((start = content.indexOf(word, start)) == -1) 
         return -1; // 못 찾은 경우
     if (start > 0) {
         // 찾은 단어의 앞이 공백이 아닌 경우 (긴 단어 속에 포함되어 있는 경우인데 이 경우 스킵함)
         //    찾은 단어 뒤쪽으로 계속 찾음; 재귀 함수
         if (!Character.isWhitespace(content.charAt(start-1))) 
             return findWord(content, word, start+word.length());
     }
     // 이 경우 찾은 단어가 content의 맨 앞이거나 찾은 단어 앞에 공백이 있는 경우임
     // 이제 단어의 뒤가 공백이어야 함

     // 찾은 단어가 content의 끝에 있을 경우
     if ((start+word.length()) == content.length()) 
         return start; // 찾은 경우

     // 찾은 단어의 뒤가 공백이 아닌 경우 (긴 단어 속에 포함되어 있는 경우인데 이 경우 스킵함)
     // 찾은 단어 뒤쪽으로 계속 찾음; 재귀 함수
     if(!Character.isWhitespace(content.charAt(start+word.length()))) 
         return findWord(content, word, start+word.length());

     return start; // 찾은 경우: 단어 뒤가 공백임
 }
 void find() { // Menu item 2
     String word = UI.getNext("word to find? ");
     // 전체 메모 mStr를 문자열로 변환한 후 이를 행 단위로 쪼갬
     // 아래 구분자 "\\v"은 행 단위로 쪼개라는 구분자를 의미함; 즉, '\n','\r','\f'등을 의미함 
     String lines[] = mStr.toString().split("\\v");
     int tot_count = 0; // 단어를 찾은 총 횟수 
     for(int i=0; i<lines.length; i++) {
    	 int count = 0;
    	 for(int j=0; j<lines[i].length(); j++) {
    		 if((j=findWord(lines[i],word,j)) != -1) {
    			 j+=word.length();
    			 count++;
    		 }
    		 else
    			 break;
    	 }
    	 if(count != 0) {
    		 tot_count += count;
    		 System.out.println("[" + i + "] " + lines[i]);
    	 }
     }
     System.out.println("--------------------");
     System.out.println(tot_count + " words found");
     /*
     TODO:
     for(i)문을 이용하여 각각의 lines[i]에 대해
         int count = 0;  // lines[i]에서 단어 word를 찾은 횟수
         // 아래 for문은 lines[i] 내에서 word 단어를 반복적으로 찾는다.
         for(j=0)문을 이용하여 j=findWord(lines[i],word,j)를 호출하여 
            lines[i] 내의 i 위치에서부터 word를 찾고
            이 단어를 찾은 경우 j+=word.length()한 후 반복하여 findWord()를 호출하고
            찾은 회수(count)도 증가시킨다. 찾지 못한 경우 for문 종료한다.
         lines[i]내 word가 하나라도 존재할 경우(count 체크) 실행결과처럼 
             행 인덱스 [i] 와 함께 lines[i]를 출력하고 tot_count를 적절히 증가시킨다.
     System.out.println("--------------------");
     찾은 단어의 총 회수를 출력한다.
     */
 }
 private int findWord(StringBuffer content, String word, int start) {
     // content.indexOf(word, start): 
     //    content 문자열의 start 인덱스 위치부터 word를 검색하고 
     //    찾은 경우 찾은 시작 위치를, 못 찾은 경우 -1를 반환함
     //    word가 다른 긴 단어의 속에 포함되어 있어도 검색이 된다.
     if ((start = content.indexOf(word, start)) == -1) 
         return -1; // 못 찾은 경우
     if (start > 0) {
         // 찾은 단어의 앞이 공백이 아닌 경우 (긴 단어 속에 포함되어 있는 경우인데 이 경우 스킵함)
         //    찾은 단어 뒤쪽으로 계속 찾음; 재귀 함수
         if (!Character.isWhitespace(content.charAt(start-1))) 
             return findWord(content, word, start+word.length());
     }
     // 이 경우 찾은 단어가 content의 맨 앞이거나 찾은 단어 앞에 공백이 있는 경우임
     // 이제 단어의 뒤가 공백이어야 함

     // 찾은 단어가 content의 끝에 있을 경우
     if ((start+word.length()) == content.length()) 
         return start; // 찾은 경우

     // 찾은 단어의 뒤가 공백이 아닌 경우 (긴 단어 속에 포함되어 있는 경우인데 이 경우 스킵함)
     // 찾은 단어 뒤쪽으로 계속 찾음; 재귀 함수
     if(!Character.isWhitespace(content.charAt(start+word.length()))) 
         return findWord(content, word, start+word.length());

     return start; // 찾은 경우: 단어 뒤가 공백임
 }
 void findReplace() { // Menu item 3
     String find = UI.getNext("word to find? ");
     String repl = UI.getNext("word to replace? ");
     int count = 0; // 단어를 교체(찾은)한 횟수
     for(int i=0; i < mStr.length(); i++) {
    	 if((i = findWord(mStr, find, i)) != -1) {
    		 mStr.replace(i, i + find.length(), repl);
    		 count++;
    	 }
    	 else
    		 break;
     }
     display();
     System.out.println(count + " words replaced");
     /*
     TODO:
     for(i=0)문을 이용하여 i=findWord(mStr,find,i))를 호출하여 
         mStr 내의 i위치에서부터 word를 찾고
         이 단어를 찾은 경우 이 단어를 repl 단어로 교체한다. count도 수정
             다음 찾을 위치 i를 repl의 길이만큼 증가한 후 반복적으로 findWord()를 호출한다.
         찾지 못한 경우 for문 종료한다.
     display();
     교체(찾은)된 횟수를 출력한다.
     */
 }
 void compare() { // Menu item 4
     String word = UI.getNext("word to compare? ");
     String tmp = mStr.toString();
     String tokens[] = tmp.split("\\s");
     int less = 0, same = 0, larger = 0;
     for(int i=0; i<tokens.length; i++) {
    	 if(!tokens[i].isEmpty()) {
    		 if(word.compareTo(tokens[i])==0)
    			 same++;
    		 else if(word.compareTo(tokens[i]) < 0)
    			 larger++;
    		 else if(word.compareTo(tokens[i]) > 0)
    			 less++;
    	 }
     }
     System.out.println("less: " + less);
     System.out.println("same: " + same);
     System.out.println("larger: " + larger);
 }
 void dispByLn() { // Menu item 5
     System.out.println("--- Memo by line ---");
     if(mStr.length() > 0) {
    	 String tmp = mStr.toString();
    	 String lines[] = tmp.split("\\v");
    	 for(int i=0; i<lines.length; i++) {
    		 System.out.println("[" + i + "] " + lines[i]);
    	 }
     }
     /*
     TODO:
     mStr이 빈 문자열이 아닌 경우, 즉 if (mStr.length() > 0)
         mStr을 String 문자열로 변환한 후 
         이 문자열을 구분자 "\\v"를 사용하여 행 단위로 토큰으로 잘라 
         lines[] 문자열 배열에 저장한다.
            여기서 구분자 "\\v"는 행 분리 문자들('\r', '\f', '\n')을 의미한다. 
         for를 사용하여 각각의 lines[i]에 대해
             lines[i]를 인덱스 i와 함께 출력한다. lines[i]가 빈 행이거나 
             또는 행의 끝에 '\n'이 없을 경우(마지막 행처럼) 행 끝에 줄 바꾸기를 출력한다.
     */
     System.out.println("--------------------");
 }
 
 // 두 개의 정수 값(start, end)을 가지는 클래스
 // 함수에서 두 개의 값을 한꺼번에 리턴하고자 할 때 이 클래스의 객체를 생성하여 반환한다.
 // private 클래스이므로 이 클래스는 Memo 클래스 내에서만 사용가능하다.
 private class Pair {
     public int start, end;
     Pair(int s, int e) { start = s; end = e; }
 }
 // 행번호 lineNum(행은 0부터 시작)인 행의 시작 위치인 start와 (행의 끝+1)의 위치인 end를 찾아줌.
 // end는 사실 그 다음 행(lineNum+1)의 시작 위치이며 마지막 행인 경우 mStr.length()와 같다.
 // 해당 행 번호를 찾았으면 start, end 값을 가진 Pair 객체를, 찾지 못했으면 null을 반환
 private Pair find_line(int lineNum) {
     int start = 0, end = 0;
     for(int i=0; i<lineNum; i++) {
    	 end = mStr.indexOf("\n", start);
    	 if(end == -1)
    		 return  null;
    	 start = end+1;
     }
     if(start == mStr.length())
    	 return null;
     
     end = mStr.indexOf("\n", start);
     
     if(end != -1)
    	 end++;
     else
    	 end = mStr.length();
     return new Pair(start, end);
     
     
     /*
     TODO: 
     for(i) 문을 이용하여 lineNum만큼 반복하면서 각 행의 끝을 찾는다. 
         각 행의 끝을 찾을 때는 indexOf("\n", start) 함수를 활용하라.
         [indexOf()의 사용법은 findWord(StringBuffer, ...)를 참고하라.]
         즉, mStr 내에서 각 행의 끝 문자인 "\n"를 찾은 후 위치를 end에 저장한다.
         그 다음 행의 시작 위치(start)를 end+1로 변경하고 위 과정을 반복한다.
         각 행의 끝인 "\n"를 찾지 못한 경우 null을 반환한다.(해당 lineNum을 찾지 못한 경우임)
     for문을 수행한 후 만약 start가 mStr.length()와 같다면 
        즉 lineNum이 (마지막 행+1) 행이라면 null 리턴(이 경우 못 찾은 것임)
     시작 위치를 찾았으면 이제 행의 끝 위치('\n')인 end를 찾아라.
     end를 찾았으면 end 값이 그 다음 행(lineNum+1)의 시작 위치가 되게 end 값을 1 증가하고 
     end를 찾지 못한 경우(lineNum이 마지막 행인데 끝에 '\n'이 없는 경우) 
        end를 mStr.length()로 설정하라.
     start, end를 이용하여 Pair 객체를 생성한 후 이 객체를 리턴한다.
     */
 }
 
 void delLn() { // Menu item 6
     int lineNum = UI.getPosInt("line number to delete? ");
     Pair p;
     if (mStr.length() == 0 || (p=find_line(lineNum)) == null)
         System.out.println("Out of line number range");
     else {
    	 mStr.delete(p.start, p.end);
         /*
         TODO: mStr의 내용 중 p.start에서 p.end 앞까지 삭제하라.
         */
         dispByLn();
     }
 }
 void replLn() { // Menu item 7
	 int lineNum = UI.getPosInt("line number to replace? ");
	 Pair p = find_line(lineNum);
	 if(p == null) {
		 System.out.println("Out of line number range");
		 return;
	 }
	 
	 String line = UI.getNextLine("input content to replace:\n");
	 mStr.replace(p.start, p.end, line+"\n");
	 dispByLn();
     /*
     TODO: 
     위 delLn() 함수와 실행 결과를 참고하여 교체할 행번호를 입력 받아라.
     find_line(lineNum)를 호출하여 해당 행의 start와 다음 행의 시작 위치(end)를 구한다.
     해당 행을 찾지 못한 경우 적절한 에러 메시지를 출력하고 리턴한다.
     교체할 행의 내용을 입력 받아라. (한줄 전체를 입력 받을 것, CurrentUser::calcString() 참고)
     mStr 내에서 start에서 end 앞까지의 내용을 새로 입력받은 내용으로 교체하라.
     dispByLn();
     */
 }
 void scrollUp() { // Menu item 8
	 if(mStr.length()==0) {
		 dispByLn();
		 return;
	 }
	 if(mStr.charAt(mStr.length() - 1) != '\n')
		 mStr.append('\n');
	 
	 Pair p0 = find_line(0);
	 String sub = mStr.substring(p0.start, p0.end);
	 mStr.append(sub);
	 mStr.delete(p0.start, p0.end);
	 dispByLn();
     /*
     TODO: 
     mStr이 비어 있으면 dispByLn()을 호출하고 바로 리턴한다.
     mStr의 마지막 문자(즉 마지막 행의 끝에)가 '\n'가 아닌 경우 
         mStr의 마지막에 '\n'를 추가한다.
     find_line(...)를 호출하여 첫 행의 start와 다음 행의 시작 위치(end)를 구한다.
     mStr의 첫 행을 서브 문자열로 얻어 내어 별도의 문자열로 저장한다.
     첫 행을 mStr의 맨 뒤에 추가한다.
     mStr의 첫 행을 삭제한다.
     dispByLn();
     */
 }
 
 // 마지막 행의 시작 위치를 구하여 반환한다.
 private int find_last_line() {
     int start = 0, pos;
     for(int i=0; i<mStr.length(); i++) {
    	 pos = mStr.indexOf("\n", start);
    	 if(pos == -1 || pos + 1 == mStr.length())
    		 break;
    	 start = pos+1;
     }
     return start;
     /*
     TODO: 
     for 문을 이용하여 반복적으로 
         start 위치부터 "\n"의 위치를 찾아 pos에 저장한다.
         (찾는 방법은 find_line(int)을 참고할 것.)
         찾지 못했거나(마지막 행 끝에 '\n'가 없는 경우) 또는 
         찾았다면 찾은 '\n'가 마지막 행인 경우(pos+1이 mStr.length()와 같은 경우)
             이 때의 start 값이 마지막 행의 시작 위치이므로 이를 반환한다.
         위치(pos)를 start에 저장하고 위 과정을 반복한다.
     */
 }
 
 void scrollDown() { // Menu item 9
	 if(mStr.length()==0) {
		 dispByLn();
		 return ;
	 }
	 
	 if(mStr.charAt(mStr.length() - 1) != '\n')
		 mStr.append('\n');
	 int start = find_last_line();
	 String tmp = mStr.substring(start, mStr.length());
	 mStr.delete(start, mStr.length());
	 mStr.insert(0, tmp);
	 dispByLn();
	 /* 
     TODO: 
     mStr이 비어 있으면 dispByLn()을 호출하고 바로 리턴한다.
     mStr의 마지막 문자(즉 마지막 행의 끝에)가 '\n'가 아닌 경우 
         mStr의 마지막에 '\n'를 추가한다.
     find_last_line()를 호출하여 마지막 행의 시작 위치를 구한다.
     참고로 마지막 행의 끝 위치는 mStr.length()이다.
     mStr의 마지막 행을 서브 문자열로 얻어 내어 별도의 문자열로 저장한다.
     mStr의 마지막 행을 삭제한다.
     별도로 저장한 마지막 행을 mStr의 맨 처음에 삽입한다.
     dispByLn();
     */
 }
/*
In war, he is daring, boastful, cunning, ruthless, self-denying,
and self-devoted; in peace, just, generous, hospitable, revengeful,
superstitious, modest, and commonly chaste.
These are qualities, it is true, which do not distinguish all alike;
but they are so far the predominating traits of these remarkable people
as to be characteristic.
It is generally believed that the Aborigines of the American continent
have an Asiatic origin.
*/
 void inputMemo() { // Menu item 10
	 mStr.setLength(0);
	 System.out.println("--- input memo lines, and then input empty line at the end ---");
	 while(true) {
		 String line = UI.getNextLine("");
		 if(line.equals(""))
			 break;
		 mStr.append(line+"\n");
	 }
     /* 
     TODO: 
     setLength(...)를 사용하여 mStr의 내용을 모두 비워라.
     System.out.println("--- input memo lines, and then input empty line at the end ---");
     while() 문을 이용하여 반복적으로
         키보드에서 한 행을 한꺼번에 입력 받는다. UI.getNextLine("")를 사용하라.
         빈 줄일 경우 while을 빠져 나간다. (빈줄 비교는 == ""가 아님을 명심하라.)
         참고로 입력받은 행 끝에는 "\n"가 없기 때문에 이를 추가해 주어야 한다.
         그 행을 mStr에 추가한다.
     */
 }
}   // Memo class: ch6_2


//===============================================================================
// User Interface
//===============================================================================
class UI
{
    public static boolean echo_input = false; // 자동오류체크 시 true로 설정됨
    public static Scanner scan;

    public static void setScanner(Scanner s) { scan = s; }

    // 입력을 받기 위해 static Scanner scan 멤버를 활용하라. 즉, scan.함수() 형식으로 호출
    // 정수 값을 입력 받아 리턴함 (음수, 0, 양수)
    public static int getInt(String msg) { 

        // TODO: msg를 화면에 출력한 후 정수 값을 입력 받아 지역 변수 value에 저장함 (변수 선언할 것)
        //       입력 시 이 클래스의 scan 멤버 변수를 활용하라. 
        //       (이 변수는 setScanner(Scanner s)에 의해 이미 초기화 되었음)
    	   	
    	int value;
    	while(true) {
    		try {
    			System.out.print(msg);
    			value = scan.nextInt();
    			break;
    		}
    		catch(InputMismatchException e) {
    			System.out.println("Input an INTEGER. Try again!!");
    			scan.nextLine();
    		}
    	}
        if (echo_input) System.out.println(value); // 자동오류체크 시 입력 값을 출력함
        // 위 문장은 자동오류체크 시에 사용되는 문장임; 일반적으로 키보드로부터 입력받을 경우 
        // 화면에 자동 echo되지만, 자동오류체크 시에는 입력파일에서 입력받은 값이 자동 echo 되지  
        // 않으므로 명시적으로 출력 버퍼에 출력(echo) 해 주어야 한다.

        // 나중에 [문제 2-3-1] 해결할 때 구현하시오. 
        // 입력 버퍼에 남아 있는 '\n'를 제거하지 않으면 다음번 getNextLine() 함수 호출 시 
        // 버퍼에 남아 있는 '\n'로 인해 빈 줄이 입력되므로 입력 버퍼에 남아 있는 '\n'를 
        // 사전에 제거해야 함; 즉 scan.nextLine()을 호출하라.
        scan.nextLine();
        return value;
    }

    // 0 또는 양의 정수 값을 입력 받아 리턴함
    public static int getPosInt(String msg) { 
        // TODO: 위 getInt(String msg)를 호출해 정수값을 입력 받은 후 입력된 값이 음수일 경우
        //       에러("Input a positive INTEGER. Try again!!") 출력하고 다시 입력 받는다.
        //       원하는 값이 입력될 때까지 위 과정을 계속 반복하여야 한다.
    	int val;
    	while((val = getInt(msg)) < 0) {
    		System.out.println("Input a positive INTEGER. Try again!!");
    	}
    	
        return val; // TODO: 입력된 0 또는 양수 값 리턴
    }
    
    // [0, (size-1)] 사이의 인덱스 값을 리턴함
    // 존재하지 않는 메뉴항목을 선택한 경우 에러 출력
    public static int getIndex(String msg, int size) { 
        // TODO: 위 getPosInt(String msg)를 호출해 0 또는 양수를 입력 받은 후 적절하지 않은 인덱스(index)일 경우 
        //       에러("index: OUT of selection range(0 ~ size-1) Try again!!")를 
        //       출력하고 다시 입력 받는다.
    	int index;
    	while((index = getPosInt(msg))  >= size) {
    		System.out.println(index + ": OUT of selection range(0 ~ " + (size - 1) + ") Try again!!");
    	}
        return index;
    }

    // 위 getIndex(String msg, int size)를 호출해서
    // msg로 "\n"+menuStr+"menu item? " 을 보여주고 
    // 사용자가 선택한 메뉴항목의 인덱스(0 ~ (menuCount-1))를 리턴함
    // menuCount: 메뉴항목의 개수임
    public static int selectMenu(String menuStr, int menuCount) { return getIndex("\n"+menuStr+"menu item? ", menuCount); }
    
    // 중요: 위의 selectMenu(), getIndex(), getPosInt() 함수 구현 시 각 함수는
    // selectMenu() -> getIndex() -> getPosInt() -> getInt() 순서로 호출해서 구현하여야 한다.

    // 위 getInt()를 참고하여 msg를 화면에 출력한 후 문자열 단어 하나를 입력 받아 리턴함
    public static String getNext(String msg) { 
    	System.out.print(msg);
    	String token = UI.scan.next();
        if (echo_input) System.out.println(token);

        // 나중에 [문제 2-3-1] 해결할 때 입력 버퍼에 남아 있는 '\n'를 제거하라.
        scan.nextLine();
        return token;  // TODO: 입력 받은 한 단어를 리턴할 것
    }

    // msg를 화면에 출력한 후 하나의 행 전체를 입력 받아 리턴함
    public static String getNextLine(String msg) { 
    	System.out.print(msg);
        String line = UI.scan.nextLine();
    	if (echo_input) System.out.println(line); // 자동체크 시 출력됨
        return line;
    }
}

//===============================================================================
//CurrentUser class: ch4_1
//===============================================================================
class CurrentUser
{
 Person user;

 CurrentUser(Person user) { 
     this.user = user; 
 }

 public void run() {
     String menuStr =
             "++++++++++++++++++++++ Current User Menu ++++++++++++++++++++++++\n" +
             "+ 0.logout 1.display 2.getter 3.setter 4.copy 5.whatAreYouDoing +\n" +
             "+ 6.isSame 7.update 8.chPasswd(4_2) 9.chSmartPhone(5_3)         +\n" +
             "+ 10.clone(5_3) 11.calc(5_3) 12.phoneCall(5_3) 13.chWeight(6_1) +\n" +
             "+ 14.calcString(6_2) 15.memo(6_2)                               +\n" +
             "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";
     final int MENU_COUNT = 16; // 상수 정의
     while (true) {
         int menuItem = UI.selectMenu(menuStr, MENU_COUNT);
         switch(menuItem) {
         case 1: display();         break;
         case 2: getter();          break;
         case 3: setter();          break;
         case 4: copy();            break;
         case 5: whatAreYouDoing(); break;
         case 6: equals();          break;
         case 7: update();          break;
         case 8: chPasswd();		break;
         case 9: chSmartPhone();	break;
         case 10: userClone();		break;
         case 11: calc();			break;
         case 12: phoneCall();		break;
         case 13: chWeight();		break;
         case 14: calcString();		break;
         case 15: memo();			break;
         case 0:                    return;
         }
     }
 }
 void display() { 
     //user.println();
     //user.getSmartPhone().println();
	 System.out.println(user);
	 System.out.println(user.getSmartPhone());
 } // Menu item 1

 void getter() { // Menu item 2
     System.out.println("name:" + user.getName() + ", id:" + user.getId() + ", weight:" +
             user.getWeight() + ", married:" + user.getMarried() +
             ", address:" + user.getAddress());
 }
 void setter() { // Menu item 3
	 var p = new Person("p");
     p.set(p.getName());
     p.set(user.getId());
     p.set(user.getWeight());
     p.set(user.getMarried());
     p.setAddress(user.getAddress());
     //p.println("p.set(): ");
     System.out.println("p.set(): " + p);
 }
 void copy() { // Menu item 4
     //user.println("user: ");
     System.out.println("user: " + user);
     var p = user.clone(); // user와 동일한 멤버 값을 가지는 새로운 객체를 생성하여 반환
     //p.println("p: ");
     System.out.println("p: " + p);
 }
 void whatAreYouDoing() {  // Menu item 5
	 user.whatAreYouDoing();
 }
 void equals() { // Menu item 6
     //user.println("user: ");
	 System.out.println("user: " + user);
     var p = new Person("user"); p.set(1);
     //p.println("p: ");
     System.out.println("p: " + p);
     System.out.println("p.equals(user): " + p.equals(user));
     p.assign(user);
     //p.println("p.assign(user): ");
     System.out.println("p.assign(user): " + p);
     System.out.println("p.equals(user): " + p.equals(user));
 }
 void update() { // Menu item 7
     System.out.println("input person information:");
     user.input(UI.scan); // user 100 65 true :426 hakdong-ro, Gangnam-gu, Seoul:
     if (UI.echo_input) System.out.println(user); // 자동오류체크시 출력됨
     display();
 }
 void chPasswd() { // Menu item 8
     String passwd = UI.getNext("new password: ");
     user.setPasswd(passwd);
     System.out.println("password changed");
 }
 void chSmartPhone() { // Menu item 9
     String maker = UI.getNext("maker of phone to change(ex: Samsung or Apple)? ");
     if(maker.equals("Samsung"))
    	 user.setSmartPhone(new GalaxyPhone(user.getName()));
     else if(maker.equals("Apple"))
    	 user.setSmartPhone(new IPhone(user.getName(), "14"));
     else {
    	 System.out.println(maker +": WRONG phone's maker");
    	 return;
     }
    	 
     display();
 }
 void userClone() { // Menu item 10
     display();
     Person c = user.clone();
        System.out.println("------------------\nclone:");
        //c.println(); 
        //c.getSmartPhone().println();
        System.out.println(c);
        System.out.println(c.getSmartPhone());
        System.out.println("\nchange clone's name "+c.getName()+" to c1\n");
        c.set("c1"); // clone의 이름을 c1으로 변경함: 이 경우 스마트폰의 소유주도 c1으로 변경함
    display();   // 현 user를 출력함
        System.out.println("------------------\nclone:");
        //c.println(); 
        //c.getSmartPhone().println();
        System.out.println(c);
        System.out.println(c.getSmartPhone());
 }
 void calc() { // Menu item 11: 연산자와 피연산자는 스페이스로 분리되어 있어야 함
	 System.out.print("expression: ");
	 double d1 = UI.scan.nextDouble();
	 String op = UI.scan.next();
	 double d2 = UI.scan.nextDouble();
     if (UI.echo_input) System.out.println(d1+" "+op+" "+d2); // 자동오류체크시 출력됨
     user.getCalculator().calculate(d1, op, d2);
 }
 void phoneCall() { // Menu item 12
     // PersonManager에 등록되어 있는 사용자 중 한명의 이름을 입력하라.
     String callee = UI.getNext("name to call? ");
     user.getPhone().sendCall(callee);
 }
 
 void chWeight() { // Menu item 13
	 double sr = Math.sqrt(user.getWeight());
	 System.out.println("weight:" + user.getWeight() + " sqrt:" + sr + " ceil:" + Math.ceil(sr) + " floor:" + Math.floor(sr) + " round:" + Math.round(sr));
	 user.set(Math.ceil(sr) * Math.floor(sr));
	 System.out.println(user);
     /* 
     TODO: 
     현재 사용자 user의 몸 무게의 제곱근을 sr라 했을 때 실행결과를 참고하여
     [몸무게 sr (sr의 ceil) (sr의 floor) (sr의 반올림)] 순서로 출력하라.
     user의 몸무게를 (sr의 ceil)과 (sr의 floor)를 곱한 값으로 새로 설정하라.
     user를 출력하라.
     */
 }
 
 // Menu item 14:ch6_2: "2+3", "2+ 3"처럼 연산자와 피연산자가 붙어 있어도 됨
 void calcString() {
	 String line = UI.getNextLine("expression: ");
	 user.getCalculator().calculate(line);
     /* TODO:
     UI의 기존 함수를 이용해 화면에 "expression: "을 출력하고 한줄을 통채로 입력 받아 
     문자열 변수 line에 저장하라. (PersonManager의 setDate() 참고) 
     user 계산기의 calculate(line)을 호출하라. (CurrentUser::calc() 참고)
     */
 }
 
 void memo() { // Menu item 15: ch6_2
     Memo m = new Memo(user.getMemo());
     m.run();
     user.setMemo(m.toString());
 }
 
} // CurrentUser class: ch4_1

//----------------------------------------------------------------------------
//VectorPerson 클래스는 나중에 배우게 될 Vector< Person> 클래스와 동일한 기능을 수행한다.
//즉, 여러 개의 Person 객체들을 persons 배열에 저장하고 있다가 필요한 경우
//배열의 맨 끝에 또는 배열의 중간에 객체들을 삽입, 배열 중간의 객체 삭제, 배열 인덱스로 객체 얻어 오기,
//삽입 시 배열이 가득 찼으면 자동으로 배열 크기 늘려주기 등의 기능을 수행한다.  
//----------------------------------------------------------------------------
class VectorPerson
{
 static final int DEFAULT_SIZE = 10;

 private Person[] persons; // Person 객체 참조들의 배열, 즉 배열에 저장된 값이 Person 객체의 주소이다.
 private int count;        // persons 배열에 현재 삽입된 객체의 개수

 public VectorPerson() { this(DEFAULT_SIZE); }

 public VectorPerson(int capacity) {
     count = 0; // persons 배열에 현재 삽입된 객체의 개수는 0
     //System.out.println("VectorPerson::VectorPerson("+capacity+")");
     persons = new Person[capacity]; // 객체 참조 배열 할당
 }
 // persons[index]의 값을 반환
 public Person get(int index) { return persons[index]; }
 
 // persons[index]의 값을 p로 새로 교체하고 과거의 persons[index] 값을 반환
 public Person set(int index, Person p) {
	 Person tmp = persons[index];
	 persons[index] = p;
	 return tmp;
     /* 
     TODO: 예전의 persons[index] 값을 임시 변수에 저장하고 
           persons[index]를 p로 새로 교체하고 임시 변수에 저장된 값 반환
     */
 }
  
 // 할당 받은 persons 배열의 전체 길이를 반환함 (count가 아님)
 public int capacity() { return persons.length; }
  
 // persons 배열에 현재 삽입된 객체의 개수를 0으로 설정
 public void clear() { count = 0; }
  
 // 현재 삽입된 객체가 하나도 없으면 true, 있으면 false를 반환한다.
 // if 문장을 사용하지 말고 한 문장(return 비교연산자;)으로 완셩할 것
 public boolean isEmpty() { return (count == 0 ? true : false); }
  
 // 현재 삽입된 객체의 개수를 반환
 public int size() { return count; }

 // index 위치의 객체 p를 삭제한다. 즉, index+1부터 끝까지 객체들을 한칸씩 왼쪽으로 밀어 주어야 한다.
 // 자바에는 동적으로 할당된 객체를 반납하는 delete 명령어가 없다. (나중에 시스템이 자동으로 삭제함)
 // 따라서 index 위치의 객체를 별도로 삭제할 필요는 없고 그냥 무시하면 된다.
 void remove(int index) {
	 for(int i = index; i < count; i++) {
		 persons[i] = persons[i+1];
	 }
	 count--;
 }
 // persons[]의 배열 크기가 작으면 extend_capacity()를 호출하여 먼저 배열을 확장한다.
 // persons 배열에 마지막 삽입된 원소 뒤에 새로운 원소 p를 삽입하고 현재 삽입된 객체 개수 증가
 public void add(Person p) {
      if (count >= persons.length)
          extend_capacity();
      persons[count++] = p;
 }
 // persons[]의 배열 크기가 작으면 extend_capacity()를 호출하여 먼저 배열을 확장한다.(위 코드 참고)
 // 먼저 persons[] 배열 마지막 원소부터 index까지 객체를 한칸씩 뒤로 밀어 준다.
 // (count가 0일수도 있고 index가 count와 같은 위치일 수도 있다. if 없이 하나의 for 문으로 가능함) 
 // index 위치에 객체 p를 삽입하고, count 값 조정
 public void add(int index, Person p) {
	 if (count >= persons.length)
		 extend_capacity();
	 for(int i = count; i >= index; i--)
		 persons[i+1] = persons[i];
	 persons[index] = p;
	 count++;
 }
 // persons[]의 배열 크기를 두배로 확장한다. 즉,
 // 기존 persons 변수를 다른 배열 변수 saved_persons 선언 후 여기에 임시로 저장한 후 
 // 현재 persons 배열의 두배 크기의 배열을 새로 할당 받아 persons에 저장한다.
 // 임시 변수 saved_persons에 있던 기존 값들을 모두 persons[]에 복사한다.
 public void extend_capacity() {
	 Person[] saved_persons = new Person[persons.length];
	 for(int i = 0; i < persons.length; i++)
		 saved_persons[i] = persons[i];
	 persons = new Person[persons.length * 2];
	 
	 for(int i = 0; i < saved_persons.length; i++)
		 persons[i] = saved_persons[i];
	 
     //System.out.println("VectorPerson: capacity extended to " + persons.length);
 }
}   // VectorPerson class: ch4_2

//----------------------------------------------------------------------------
//키보드 또는 파일(8장) 등으로부터 Person 객체의 각 멤버 정보를 읽어 들인 후 
//새로운 Person 객체를 생성하여 리턴해 준다. (추후 계속 기능이 추가될 예정임)
//----------------------------------------------------------------------------
class Factory
{
 public void printInputNotice(String preMsg, String postMsg) {
	 System.out.println("input"+preMsg+" [delimiter(P,S,or W)]"+
             " [person information]"+postMsg+":");
 }
 public Person inputPerson(Scanner sc) { 
     Person p = null;
     String delimiter = sc.next();
     switch (delimiter) {
     case "S": p = new Student(sc); break;
     case "W": p = new Worker (sc); break;
     case "P": p = new Person (sc); break;
     default : 
         // 구분자가 잘못 입력되었을 경우 그 행 전체 스킵하고 에러 메시지 출력
         String nextLn = sc.nextLine();
         if (UI.echo_input) System.out.println(delimiter+nextLn); // 자동오류체크시
         System.out.println(delimiter+": WRONG delimiter");
         return null;
     }
     // 자동오류체크시 입력 받은 정보를 그대로 다시 출력쪽에 에코해 줌
     if (UI.echo_input) System.out.println(delimiter.equals("") ? "" : delimiter+" " + p);
     
     return p;
 }
}   // Factory class: ch4_2

//----------------------------------------------------------------------------
//사용자에게 메뉴를 보여 주고 사용자가 선택한 메뉴 항목에 따라 VertorPerson에 Person 객체를 
//삽입하거나 VertorPerson에 있는 기존의 객체들을 삭제, 검색, 모든 객체 보여주기 등의 기능을 수행한다.
//PersonManager 생성자에서 매개변수로 넘어 온 배열 array[]에 저장된 각 객체를 복사하여 
//VectorPerson에 삽입한다.
//----------------------------------------------------------------------------
class PersonManager implements BaseStation
{
 int cpCount;
 private VectorPerson pVector;
 private Factory factory;
 private Person array[];

 public PersonManager(Person array[], Factory factory) {
     //System.out.println("PersonManager(array[])");
     pVector = new VectorPerson();
     this.factory = factory;
     this.array = array;
     cpCount = 0;
     addArray();
     SmartPhone.setBaseStation(this);
     display();
 }

 public void run() {
     String menuStr =
             "=================== Person Management Menu =====================\n" +
             "= 0.exit 1.display 2.clear 3.reset 4.remove 5.copy 6.append    =\n" +
             "= 7.insert 8.login 9.dispStudent(5_3) 10.dispPhone(5_3)        =\n" +
             "= 11.find(6_1) 12.wrapper(6_1) 13.shuffle(6_1) 14.setDate(6_1) =\n" +
             "= 15.chAddress(6_2)                                            =\n" +
             "================================================================\n";
     final int MENU_COUNT = 16; // 상수 정의
     while (true) {
         int menuItem = UI.selectMenu(menuStr, MENU_COUNT);
         switch(menuItem) {
         case 1: display();         break;
         case 2: clear();           break;
         case 3: reset();           break;
         case 4: remove();          break;
         case 5: copy();            break;
         case 6: append();          break;
         case 7: insert();          break;
         case 8: login();           break;
         case 9: dispStudent();		break;
         case 10:dispPhone();		break;
         case 11:find();			break;
         case 12:wrapper();			break;
         case 13:shuffle();			break;
         case 14:setDate();			break;
         case 15:chAddress();		break;
         case 0:                    return;
         }
     }
 }
 public void display() { // Menu item 1
     int count = pVector.size();
     //System.out.println("display(): count " + count);
     for (int i = 0; i < count; ++i) {
         System.out.print("[" + i + "] ");
         //pVector.get(i).println();
         System.out.println(pVector.get(i));
     }
     //System.out.println("empty():" + pVector.isEmpty() + ", size():" + pVector.size()
     //     + ", capacity():" + pVector.capacity());
 }
 public void clear() {  // Menu item 2
     cpCount = 0;
     pVector.clear();
     display();
 }
 public void reset() { // Menu item 3
     cpCount = 0;
     pVector.clear();
     addArray();
     display();
 }
 public void remove() { // Menu item 4
     if (pVector.size() == 0) {
         System.out.println("no entry to remove");
         return;
     }
     int index = UI.getIndex("index to delete? ", pVector.size());
     pVector.remove(index);
     display();
 }
 public void copy() { // Menu item 5
     cpCount++;
     for (int i = 0, size = pVector.size(); i < size; ++i) {
         Person p = pVector.get(i).clone();
         String name = p.getName();
         for (int j = 0; j < cpCount; ++j)
             name = name.charAt(0)+name;
         p.set(name);
         p.set(p.getId() + 20 * cpCount);
         p.set(p.getWeight() + cpCount);
         if (cpCount % 2 == 1)
             p.set(!p.getMarried());
         pVector.add(p);
     }
     display();
 }
 // 아래 함수는 사용자로부터 새로 추가할 Person 객체의 수를 입력 받고 for문을 이용하여
 // 그 개수만큼의 Person 객체를 생성하고 factory.inputPerson()을 통해 인적정보를 입력받은 후 
 // VectorPerson pVector의 맨 끝에 추가한다.
 /* append() 실행 한 후 아래 항목들을 복사해서 순서적으로 입력하면 편하게 인적정보를 입력할 수 있음
 3
 HongGilDong 0 71.5 false :Gwangju Nam-gu Bongseon-dong 21:
 LeeMongRyong 1 65 true :Jong-ro 1-gil, Jongno-gu, Seoul:
 LeeSoonShin 2 80 true :1001, Jungang-daero, Yeonje-gu, Busan:
 */
 /*
 5
 K p3 11 83.3 true :100 Dunsan-ro Seo-gu Daejeon:
 P p3 11 83.3 true :100 Dunsan-ro Seo-gu Daejeon:
 S s3 12 71.5 false :Gwangju Nam-gu Bongseon-dong 21: Computer 3.3 2
 W w3 13 65 true :Jong-ro 1-gil, Jongno-gu, Seoul: Kia CEO
 S s4 15 80 true :1001, Jungang-daero, Yeonje-gu, Busan: Biology 3.8 3
 W w4 16 77 false :Buk-ro 3, Kangdong-gu, Seoul: Naver Department-Head
 */
 public void append() { // Menu item 6
     int cnt = UI.getPosInt("number of persons to append? ");
     factory.printInputNotice(" "+Integer.toString(cnt), " to append");
     for (int i = 0 ; i < cnt; ++i) {
         Person p = factory.inputPerson(UI.scan); // 한 사람의 정보를 입력 받음
         if (p != null) pVector.add(p);
     }
     display();
 }
 
 
 public void insert() { // Menu item 7
     int index = 0;
     if (pVector.size() > 0) {
         index = UI.getIndex("index to insert in front? ", pVector.size()+1);
         if (index < 0) return;
     }
     factory.printInputNotice("", " to insert");
     Person p = factory.inputPerson(UI.scan);
     if (p == null) return;
     pVector.add(index, p);
     display();
 }
 // 사용자로부터 VectorPerson pVector에 저장된 사람들 중에서 로그인할 사람의 
 // 이름(name)과 비번을 입력받고 해당 비번이 맞으면 CurrentUser의 객체를 생성하고 
 // 이 객체의 run() 멤버 함수를 호출한다. 초기 비번은 설정되어 있지 않기에 그냥 엔터치면 된다.
 public void login() {  // Menu item 8
     String name = UI.getNext("user name: ");
     Person p = findByName(name);
     if (p == null) return;
     String passwd = UI.getNextLine("password: ");
     //passwd.strip();
     if (passwd.equals(p.getPasswd()))
         new CurrentUser(p).run();
     else
         System.out.println("WRONG password!!");
 }
 
 public void dispStudent() { // Menu item 9: ch5_3
	 int count = pVector.size();
	 System.out.println("dispStudent(): ");
	 for(int i=0; i<count; i++) {
		 if(pVector.get(i) instanceof Student) {
	         System.out.print("[" + i + "] ");
	         //pVector.get(i).println();
	         System.out.println(pVector.get(i));
		 }
	 }
 }
 
 public void dispPhone() { // Menu item 10: ch5_3
     int count = pVector.size();
     System.out.println("dispPhones(): count " + count);
     for (int i = 0; i<count; i++) {
         System.out.print("[" + i + "] "); //pVector.get(i).getSmartPhone().println();
         System.out.println(pVector.get(i).getSmartPhone());
     }
 }
 
 public void find() { // Menu item 11: ch6_1
     boolean found = false;
     System.out.println("input [delimiter(P,S,or W)] [person information] to find by equals():");
     
     Person p = factory.inputPerson(UI.scan);
     if (p == null) return;

     for(int i=0; i<pVector.size(); i++) {
    	 if(p.getClass() == pVector.get(i).getClass()) {
    		 if(p.equals(pVector.get(i))) {
        		 System.out.print("[" + i + "] ");
        		 System.out.println(pVector.get(i));
        		 found = true;
        	 } 
    	 }
    	 
    		 
     }
     if(!found)
    	 System.out.println("NOT found by equals()");
 }
 
 void wrapper() { // Menu item 12: ch6_1
	 for(int i=0; i<pVector.size(); i++) {
		 Person p = pVector.get(i);
		 System.out.print("[" + i + "] ");
		 dispPersonInfo(p.getName(), Integer.toString(p.getId()), Double.toString(p.getWeight()), Boolean.toString(p.getMarried()));
	 }
     /* 
     TODO: 
     for을 이용하여 pVector의 각 멤버(pVector.get(i))를 Person p에 저장한 후
        p의 name, id, weight, married 멤버에 대해
        아래의 2)의 dispPersonInfo(String, String, String, String) 함수를 호출하라. 
        이때 필요한 함수 인자들은 String으로 변환한 후 호출해야 한다. String으로 변환 시
        Integer, Double, Boolean 등과 같은 Wapper 클래스의 toString() 함수를 활용하라.
        주의: 절대 dispPersonInfo(String, int, double, boolean)를
             바로 호출하지 마라.
     */
 }
 
 public void shuffle() { // Menu item 13: ch6_1
	 for(int i=0; i<pVector.size(); i++) {
		 int j = (int)(Math.random() * pVector.size());
		 pVector.set(j, pVector.set(i, pVector.get(j)));
	 }
     /* 
     TODO: 
     for을 이용하여 각각의 인덱스 i에 대해
        [0 ~ pVector.size()-1] 사이의 임의의 정수 j를 구함 (Math.random()을 활용)
        pVector.set(j, pVector.set(i, pVector.get(j)))를 호출함
     */
     display();
 }
 
 public void setDate() { // Menu item 14: ch6_1
     // 년 월 일 시 분 초 순서로 시간정보를 입력한다.
     String line = UI.getNextLine("date and time(ex: 2021 10 1 18 24 30)? ");
     SmartPhone.setDate(line);
 }
 
 // pVector에 저장된 모든 사람들의 주소를 newAddress(String)를 호출하여 변경한다.
 void chAddress() { // Menu item 15: ch6_2
	 
     for (int i = 0; i < pVector.size(); ++i) {
         Person p = pVector.get(i);
         p.setAddress(newAddress(p.getAddress()));
             /* TODO: 
             p의 주소를 얻어와 아래 newAddress(String)를 호출하여 
             수정한 주소를 얻어 온 후 이를 다시 p의 주소로 설정하라. (한 줄로 완성해 보라)
             */
     }
     display();
 }
 
 @Override
 public boolean connectTo(String caller, String callee) {
	 Person p = findByName(callee);
	 if (p == null) return false;
	 System.out.println("base station: sends a call signal of " + caller +" to " + callee);
	 p.getPhone().receiveCall(caller);
	 return true;
 }
 
 // pVector에 삽입되어 있는 Person 객체들 중 사용자가 입력한 이름 name과 
 // 동일한 이름을 가진 객체를 찾아 리턴한다.
 private Person findByName(String name) {
     int i, count = pVector.size();
     for (i = 0; i < count; ++i)
         if (name.equals(pVector.get(i).getName())) 
             return pVector.get(i);
     System.out.println(name + ": NOT found");
     return null;
 }
 
 private void addArray() {
     for (Person p : array)
         pVector.add(p.clone()); // 배열의 각 원소를 복사한 후 pVector에  삽입함
 }
 
 private void dispPersonInfo(String sname, String sid, String sweight, String smarried) {
	 sname = sname.charAt(sname.length() - 1) + sname.substring(1, sname.length() - 1) + sname.charAt(0);
     if(sid.charAt(sid.length() - 1) == '0') {
    	 sid = sid.substring(0, sid.length() - 1) + '1';
     }
     dispPersonInfo(sname, Integer.parseInt(sid), Double.parseDouble(sweight), !Boolean.parseBoolean(smarried));
	  /* 
      TODO: 
      String 클래스의 charAt(), substring(), + 연산자 등을 사용하여
      sname의 첫 글자와 끝 글자를 서로 바꾸어라. 예를 들어 "Park" -> "karP"
      sid의 끝 글자가 '0'이면 '1'로 변환하라. "2340" -> "2341"
      dispPersonInfo(String, int, double, boolean)를 호출하라.
      이때 필요한 함수 인자들은 int, double, boolean 으로 변환한 후 호출해야 한다.
      String으로 변환 시 Integer, Double, Boolean 클래스의 parseXXXXX() 함수를 활용하라.
      변환한 후 married의 경우 true는 false로, false는 true로 반대로 값을 바꾼 후 호출하라.
      */
  }
  private void dispPersonInfo(String sname, int id, double weight, boolean married) {
     
	  System.out.println(sname + " " + id + " 0x" + Integer.toHexString(id)
    		 + " 0" +Integer.toOctalString(id) + " 0b" + Integer.toBinaryString(id)
    		 + " " + weight + " " +married);

	  /* 
      TODO: 실행 결과를 참고하여 
      sname id 0x(id의 16진수 문자열) 0(id의 8진수 문자열) 0b(id의 2진수 문자열) weight married
      순서로 출력하라. 
      (16, 8, 2진수로 변경시 Integer 클래스의 적절한 toXXXXString() 함수를 활용하라)
      */
  }
  
  private String newAddress(String address) {
	  address = address.toLowerCase();
	  address = address.replace("-gu", "_gu");
	  
	  String[] tmp = address.split(",");
	  address = "";
	  
	  for(int i=0; i<tmp.length; i++) {
		  address += tmp[i].trim() + " ";
	  }
	  address = address.substring(0, address.length()-1);
	  return address;
      /* 
      TODO: 

      address에서 ","의 앞뒤 공백(','를 포함하여)을 하나의 공백문자 " "로 교체한 
      새로운 address 문자열을 만든 다음 이를 반환하라. (아래 힌트 참고)

      힌트: address에서 ","를 구분자로 사용하여 여러 개의 토큰으로 잘라라. split() 사용
           address = "";
           for 문: 각 토큰의 양쪽 끝쪽에 있는 공백(스페이스, 탭, 엔터) 문자를 모두 제거하라.
           for 문: 각 토큰을 address의 뒤에 추가하여(+) 새로운 address를 만들고 
                  그 뒤에 매번 " "를 추가(+)하라. (가능하면 두 개의 for를 하나로 만들어 보라)
           address의 맨 끝의 " "를 제거하라.
           새로 완성된 address를 반환하라.
      */
  }
}   // ch4_2: PersonManager class

//===============================================================================
//class MultiManager: ch4_1
//===============================================================================
//----------------------------------------------------------------------------
//여러 Person 객체를 미리 생성하여 persons[] 배열에 저장하고 있다가
//PersonManager 생성 시 초기값으로 이 배열을 넘겨 주는 역할을 한다. 
//----------------------------------------------------------------------------
class MultiManager
{
 private Person persons[] = {
	 new Person("p0", 10, 70.0, false, "Gwangju ,Nam-gu , Bongseon-dong 21"),
	 new Person("p1", 11, 61.1, true,  "Jong-ro 1-gil,Jongno-gu,   Seoul"),
     new Person("p2", 12, 52.2, false, "1001, Jungang-daero, Yeonje-gu, Busan"),
     new Person("p3", 13, 83.3, true,  "100 Dunsan-ro Seo-gu Daejeon"),
     new Person("p4", 14, 64.4, false, "88 Gongpyeong-ro, Jung-gu, Daegu"),
 };

 // 지금은 allPersons[] 배열에 Person 객체만 가지고 있지만 나중에 
 // Person을 상속한 Student, Worker 클래스 객체들이 이 배열에 추가될 예정이다.
 private Student students[] = {
     new Student("s1", 1, 65.4, true,  "Jongno-gu Seoul", "Physics", 3.8, 1),
     new Student("s2", 2, 54.3, false, "Yeonje-gu Busan", "Electronics", 2.5, 4),
 };

 private Worker workers[] = {
     new Worker("w1", 3, 33.3, false, "Kangnam-gu Seoul",  "Samsung", "Director"),
     new Worker("w2", 4, 44.4, true,  "Dobong-gu Kwangju", "Hyundai", "Manager"),
 };

 private Person allPersons[] = {
     persons[0], persons[1], students[0], students[1], workers[0], workers[1],
 };

 public void run() {
     //System.out.println("PersonManager::run() starts");
     var pm = new PersonManager(allPersons, new Factory());
     pm.run();
     //System.out.println("PersonManager::run() returned");
 }
} // class MultiManager: ch4_1

//Student, Worker 클래스를 테스트하기 위한 다양한 함수들을 가지고 있음
class Inheritance
{
 Student s;
 Worker  w;

 public Inheritance() {
     s = new Student("s1", 1, 65.4, true,  "Jongno-gu Seoul",  "Physics", 3.8, 1);
     w = new Worker ("w1", 3, 33.3, false, "Kangnam-gu Seoul", "Samsung", "Director");
 }    
 public void run() {
     String menuStr =
          "***** Inheritance Menu ******\n" +
          "* 0.exit 1.Student 2.Worker *\n" +
          "*****************************\n";
     final int MENU_COUNT = 3; // 상수 정의
     while (true) {
         int menuItem = UI.selectMenu(menuStr, MENU_COUNT);
         switch(menuItem) {
         case 1: student(); break;
         case 2: worker();  break;
         case 0:            return;
         }
     }
 }
 void compare(Person p1, Person p2) {
     //p1.println("p1: ");
     //p2.println("p2: ");
	 System.out.println("p1: " + p1);
	 System.out.println("p2: " + p2);
     System.out.println("p1.equals(p2) : " + p1.equals(p2));
     System.out.println("--------------------");
 }
 Person whatAreYouDoing(Person p) { // 매개변수와 리턴 타입이 Person임
     // 아래 p.whatAreYouDoing() 호출은 Person::whatAreYouDoing()이 호출되는 것이 아니라
     // p의 원본 객체(Student 또는 Worker)에 있는 오버라이딩된 whatAreYouDoing()이 호출된다.
     p.whatAreYouDoing();
     return p;
 }
 void input(Person p, String msg) {
     System.out.print("input "+msg+": ");
     p.input(UI.scan);
     if (UI.echo_input) System.out.println(p); // 자동체크에서 사용됨
     
 }
 Person clone(Person p) { // 매개변수와 리턴 타입이 Person임
     // 아래 p.clone() 호출은 Person의 clone()이 호출되는 것이 아니라
     // p의 원본 객체(Student 또는 Worker)에 있는 오버라이딩된 clone()이 호출된다.
     Person c = p.clone();
     return c;
 }
 void assign(Person d, Person s) { // 매개변수 타입이 Person임
     d.assign(s); // s를 d에 복사
 }
 Person newInput(Boolean isStudent, String msg) {
     Person p = null;
     System.out.print("input new "+msg+": ");
     if (isStudent)
         p = new Student(UI.scan);
     else
         p = new Worker(UI.scan);
     if (UI.echo_input) System.out.println(p); // 자동체크에서 사용됨
     return p;
 }
 void student() {
     var s1 = new Student(s);
     var s2 = new Student(s1);
     System.out.println("--------------------");
     s2.set("s2");
     compare(s1, s2); // 업캐스팅
     s2.set(s1.getName());
     s2.setGPA(s2.getGPA()-1.0);
     compare(s1, s2);

     s2.setDepartment(s1.getDepartment()+"-Electronics");
     compare(s1, s2);

     s2.setDepartment(s1.getDepartment());
     s2.setYear(s1.getYear()+1);
     compare(s1, s2);

     s2.setYear(s1.getYear());
     compare(s1, s2);
     
     s2.set("s2");
     // 아래 함수호출 시 s2는 Person으로 자동 업캐스팅되지만 (Person이 s2의 수퍼 클래스이므로)
     // 리턴 값은 (Student)로 강제로 다운 캐스팅해 주어야 함
     Student s3 = (Student)whatAreYouDoing(s2); 
     System.out.println();
     s3.whatAreYouDoing();    // Student::whatAreYouDoing()이 호출됨
     
     s3 = (Student)clone(s2);
     //s3.println("s3: ");
     System.out.println("s3: " + s3);
     System.out.println("--------------------");
     
     //s2.println("s2: ");
     System.out.println("s2: " + s2);
     s1 = new Student("", 0, 0.0, false, "", "", 0.0, 0);
     assign(s2, s1); // (destination, source): source 멤버들을 destination에 복사
     //s2.println("s2: ");
     System.out.println("s2: " + s2);
     System.out.println("--------------------");
     
     input(s2, "student"); // s2 1 56.9 false :Gangnam-gu Seoul: Physics 2.0 1
     //s2.println("s2: ");
     System.out.println("s2: " + s2);
     System.out.println("--------------------");
     
     Student s4 = (Student)newInput(true, "student"); 
     // s4 1 56.9 false :Gangnam-gu Seoul: Physics 2.0 1
     //s4.println("s4: ");
     System.out.println("s4: " + s4);
 }
 void worker() {
     var w1 = new Worker(w);
     var w2 = new Worker(w1);
     System.out.println("--------------------");
     w2.set("w2");
     compare(w1, w2); // 업캐스팅
     w2.set(w1.getName());
     w2.setCompany(w1.getCompany()+"-Hyundai");
     w2.setPosition(w1.getPosition());
     compare(w1, w2);
     w2.setCompany(w1.getCompany());
     w2.setPosition(w1.getPosition()+"-Manager");
     compare(w1, w2);
     w2.setPosition(w1.getPosition());
     compare(w1, w2);
     w2.set("w2");
     Worker w3 = (Worker)whatAreYouDoing(w2);  // 매개변수:업캐스팅, 리턴:다운캐스팅
     System.out.println();
     w3.whatAreYouDoing();    // Worker::whatAreYouDoing()이 호출됨
     w3 = (Worker)clone(w2);
     //w3.println("w3    : ");
     System.out.println("w3: " + w3);
     System.out.println("--------------------");
     //w2.println("w2: ");
     System.out.println("w2: " + w2);
     w1 = new Worker("", 0, 0.0, false, "", "", "");
     assign(w2, w1); // (destination, source): source 멤버들을 destination에 복사
     //w2.println("w2: ");
     System.out.println("w2: " + w2);
     System.out.println("--------------------");
     input(w2, "worker"); // w2 3 44.4 true :Jongno-gu Seoul: Samsung Director
     //w2.println("w2: ");
     System.out.println("w2: " + w2);
     System.out.println("--------------------");
     Worker w4 = (Worker)newInput(false, "worker"); 
     // w4 3 44.4 true :Jongno-gu Seoul: Samsung Director
     //w4.println("w4: ");
     System.out.println("w4: " + w4);
 }
}   // Inheritance class: ch5_1

//===============================================================================
//class Ch3
//===============================================================================
class Ch3 
{
  public static void run() {
      String menuStr =
           "************* Ch3 Menu **************\n" +
           "* 0.Exit 1.array 2.exception 3.game *\n" +
           "*************************************\n";

      // TODO: Ch2::run() 함수를 참고하여 while문과 switch문을 작성하라.
      //       switch에서는 각 메뉴 항목별로 아래의 상응하는 함수를 호출하고, 
      //       MENU_COUNT도 적절히 수정하라.
      final int MENU_COUNT = 4;
      int menuItem;
      while((menuItem = UI.selectMenu(menuStr, MENU_COUNT)) != 0) {
      	switch(menuItem) {
      	case 1:	
      		array();
      		break;
      	case 2:
      		exception();
      		break;
      	case 3:
      		game();
      		break;
      	}
      }
      
  }

  public static void array() {
      double arr1[][] = { {0}, {1,2}, {3,4,5} };
      printArray(arr1);
      double arr2[][] = { {0,1,2,3}, {4,5,6}, {7,8}, {9} };
      printArray(arr2);
      
      var arr3 = inputArray();
      printArray(arr3);
      arr3 = inputArray();
      printArray(arr3);
  }
  public static void printArray(double arr[][]) {
	  int size = arr.length;
	  System.out.println("arr length: " + size);
	  for(int i = 0; i<size; i++) {
		  System.out.print("arr[" + i + "]");
		  for(int j  = 0; j < arr[i].length; j++) {
			  System.out.print(" " + (double)arr[i][j]);
		  }
		  System.out.println();
	  }
	  System.out.println();
  }
  
  public static double[][] inputArray() {
	  int size = UI.getPosInt("array rows? ");
	  double arr[][] = new double[size][];
	  for(int i=0; i<size; i++) {
		  arr[i] = new double[i+1];
	  }
	  for(int i=0; i<size; i++) {
		  System.out.print("input " + (i+1) + " doubles for row " + i +": ");
		  for(int j=0; j<i+1; j++) {
			  arr[i][j] = UI.scan.nextDouble();
		  }
	  }
	  System.out.println();
	  return arr;
  }
  
  //------------------------------------------------------------------------
  // Exception
  //------------------------------------------------------------------------
  static Random random = null; // 난수 발생기
  
  public static void exception() {
      var random = new Random(UI.getInt("seed for random number? "));// 난수 발생기
      String str;
      int i;
      int[] arr;
      while(true) {
    	  try {
    		  str = UI.getNext("array[] size? ");
    		  i = Integer.parseInt(str);   // 문자열 숫자를 정수로 변환: "123" -> 123
    		  arr = new int[i];
    		  break;
    	  }
    	  catch(NumberFormatException e) {	// 문자열 숫자를 정수로 변환할 때 예외
    		  System.out.println(e);
    	  }
    	  catch(NegativeArraySizeException e) {	// 음의 정수일때 예외
    		  System.out.println(e);
    	  }
      }

      for (i = 0; i < arr.length; ++i) // arr[] 전체를 난수 값으로 초기화
          arr[i] = random.nextInt(3);  // [0,1,2] 범위 내에서 난수 발생
      System.out.print("array[]: { ");
      for (var v : arr)                // 배열 전체 출력
          System.out.print(v+" ");     // 각각의 v=arr[i] 원소 값을 출력함  
      System.out.println("}");

      //i = UI.getPosInt("array[] index? ");
      //System.out.println("array["+i+"] = "+arr[i]);
      
      while(true) {
    	  try {
    		  i = UI.getPosInt("array[] index? ");
    		  System.out.println("array["+i+"] = "+arr[i]);
    		  break;
    	  }
    	  catch(ArrayIndexOutOfBoundsException e) {
    		  System.out.println(e);
    	  }
      }
      

      int numerator   = UI.getIndex("numerator   index? ", arr.length); // 분자 index
      //int denominator = UI.getIndex("denominator index? ", arr.length); // 분모 index
      int denominator;
      //System.out.println(arr[numerator]+" / "+arr[denominator]+" = "
      //    +(arr[numerator] / arr[denominator]));
      
      while(true) {
    	  try {
    		  denominator = UI.getIndex("denominator index? ", arr.length); // 분모 index
    		  System.out.println(arr[numerator]+" / "+arr[denominator]+" = "
    		          +(arr[numerator] / arr[denominator]));
    		  break;
    	  }
    	  catch(ArithmeticException e) {
    		  System.out.println(e);
    	  }
    	  
      }
      
      
      System.out.println("makeArray(): first");
      while(true) {
    	  try {
    		  arr = makeArray();
    		  break;
    	  }
    	  catch(OutOfMemoryError e) {
    		  System.out.println(e);
    	  }
      }
      

      System.out.println("makeArray(): second");
      while(true) {
    	  try {
    		  arr = makeArray();
    		  System.out.println("array length = "+arr.length);
    		  break;
    	  }
    	  catch(NullPointerException e) {
    		  System.out.println("NullPointerException");
    	  }
      }
      
      //System.out.println("array length = "+arr.length);
  }

  // tag 0: OutOfMemoryError, 1: return null pointer, 2: return normal array
  public static int[] makeArray() { 
      int tag = UI.getPosInt("makeArray tag[0,1,2]? ");
   return (tag == 0)? new int[0x7fffffff]: (tag == 1)? null: new int[10]; 
  }
  
  public static void game() {
      final int USER = 0;     // 상수 정의
      final int COMPUTER = 1;
      String MJBarray[] = { "m", "j", "b" }; // 묵(m) 찌(j) 빠(b) 문자열을 가진 배열
      System.out.println("Start the MUK-JJI-BBA game.");
      // 난수 발생기
      random = new Random(UI.getInt("seed for random number? "));
      // 누가 우선권을 가졌는지 저장하고 있음, USER:사용자 우선권, COMPUTER:computer 우선권
      int priority = USER; 
      String priStr[] = { "USER", "COMPUTER"}; // 우선권을 화면에 출력할 때 사용할 문자열

      while(true) {
          System.out.println();
          System.out.println(priStr[priority] + " has the higher priority.");
          // 화면에 (priStr[priority] + " has the higher priority.")를 출력
          
          String user = UI.getNext("m(muk), j(jji), b(bba) or stop? ");
          //UI.getNext()를 사용하여 화면에 "m(muk), j(jji), b(bba) or stop? "를 출력하고
          //사용자가 입력한 묵찌빠 문자열을 넘겨 받아 user 변수에 저장 (사용자가 묵찌빠 중 하나 선택)
          if(user.equals("stop"))
        	  break;
          //사용자가 입력한 문자열이 "stop"이면 while 루프를 빠져나감 
          // 사용자가 입력한 문자열을 비교할 때는 if (user.equals("stop")) 문장을 사용
          
	      if (!user.equals("m") && !user.equals("j") && !user.equals("b")) {
	          System.out.println("Select one among m, j, b.");
	          continue;
	      }
          //사용자가 "m", "j", "b"를 입력하지 않고 다른 문자열을 입력했다면 
          //    "Select one among m, j, b."를 출력하고 while()의 처음부터 다시 시작
          // [0,1,2] 난수를 이용하여 COMPUTER가 묵찌빠 중 하나를 선택함
          String computer = MJBarray[random.nextInt(MJBarray.length)];
          
          
          //user와 computer 변수를 이용하여 화면에 사용자, 컴퓨터가 낸 묵찌빠 값을 출력 
          //    (예:User = m, Computer = b, )
          System.out.print("User = " + user + ", Computer = " + computer + ", ");
          
          if(user.equals(computer))
        	  System.out.println(priStr[priority] + " WINs.");
          else {
        	  System.out.println("SAME.");
        	  if((user.equals("m") && computer.equals("j")) || (user.equals("j") && computer.equals("b") || (user.equals("b") && computer.equals("m"))))
        			  priority = USER;
        	  else if((computer.equals("m") && user.equals("j")) || (computer.equals("j") && (user.equals("b")) || (computer.equals("b") && user.equals("m"))))
        			  priority = COMPUTER;
          }
      }
  }
}


//===============================================================================
// class Ch2
//===============================================================================
class Ch2 
{
    public static void run() {
        String menuStr =
             "************* Ch2 Menu ***********\n" +
             "* 0.exit 1.output 2.readToken    *\n" +
             "* 3.readLine 4.operator 5.switch *\n" +
             "**********************************\n";
        final int MENU_COUNT = 6;
        int menuItem;
        while((menuItem = UI.selectMenu(menuStr, MENU_COUNT)) != 0) {
        	switch(menuItem) {
        	case 1:	
        		output();
        		break;
        	case 2:
        		readToken();
        		break;
        	case 3:
        		readLine();
        		break;
        	case 4:
        		operator();
        		break;
        	case 5:
        		switchCase();
        		break;
        	}
        }
        
        
    }

    public static void output()     { 
        String toolName = "JDK";
        double version = 1.8;
        String released = "is released.";
        
        // TODO: 하나의 출력문으로 위 세 변수를 이용하여 "JDK1.8is released."가 출력되게 하라.
        System.out.println(toolName+version+released);
        // TODO: 하나의 출력문으로 위 세 변수를 이용하여 "JDK 1.8 is released."가 출력되게 하라.
        System.out.println(toolName+ " " + version + " " + released);

        int i1 = 1, i2 = 2, i3 = 3; // 변수 선언과 함께 초기화

        // TODO: 하나의 출력문으로 위 세 변수를 이용하여 "6" 이 출력되게 하라.
        System.out.println(i1+i2+i3);
        // TODO: 하나의 출력문으로 위 세 변수를 이용하여 "123" 이 출력되게 하라.
        System.out.println("" + i1 + i2 + i3);
        // TODO: 하나의 출력문으로 위 세 변수를 이용하여 "6 123" 이 출력되게 하라.
        System.out.println(i1 + i2 + i3 + " " + i1 + i2 + i3);
        
        boolean b = true;
        double d = 1.2;

        // TODO: 하나의 출력문으로 위 두 변수를 이용하여 "true false 1.2"가 출력되게 하라.
        //       변수만 사용하고 "true", "false"를 직접 출력하지는 마라. 힌트: ! 연산자
        System.out.println(b + " " + !b + " " + d);
    }
    public static void readToken()  {  
        String  name;    // 이름
        int     id;      // Identifier
        double  weight;  // 체중
        boolean married; // 결혼여부
        String  address; // 주소

        // 아래 실행결과를 참고하여 
        // TODO: "person information(... ):" 문자열을 출력하라. (끝에 줄 바꾸기도 출력할 것)
        System.out.println("person information(name id weight married :address:):");
        // TODO: UI.scan 변수를 직접 이용하여 name, id, weight, married 값을 입력 받아라.
        name = UI.scan.next();
        id = UI.scan.nextInt();
        weight = UI.scan.nextDouble();
        married = UI.scan.nextBoolean();
        

        // 주소의 패턴 ":address:"을 읽어 들임: 이미 완성된 코드이므로 아래 address를 바로 활용하면 됨
        while ((address = UI.scan.findInLine(":.*:")) == null)
         UI.scan.nextLine();  // 현재 행에 주소가 없다면 그 행을 스킵함
        address = address.substring(1, address.length()-1); // 주소 양 끝의 ':' 문자 삭제

        // TODO: "이름 id 몸무게 혼인여부 :주소:"를 출력함
        System.out.println(name + " " + id + " " + weight + " " + married + " :" + address + ":");
    }
    public static void readLine()   {  
        String name = UI.getNext("name? "); // "name? "을 출력한 후 이름을 입력 받음
        // TODO: 실행결과("name: p")처럼 입력된 이름(변수 name 값)을 출력하라.
        System.out.println("name: " + name);
        int id = UI.getInt("id? ");         // "id? "을 출력한 후 id을 입력 받음
        // TODO: 실행결과("id: 1")처럼 입력된 정수(변수 id 값)를 출력하라.
        System.out.println("id: " + id);
        
        String address = UI.getNextLine("address? ");// "address? " 출력 후 한줄 전체 입력받음
        // TODO: 입력 받은 address를 실행결과("address :seoul gangnam:")처럼 출력하라.
        System.out.println("address :" + address + ":");
    }
    public static void printBinStr(int v) {
        String s = Integer.toBinaryString(v);
           for (int i=0; i < (32-s.length()); ++i)
               System.out.print('0');
           System.out.println(s);
    }
    public static void operator()   {  
    	int b = 0b11111111_00000000_11111111_11111111;
        printBinStr(b);

        // TODO: 변수 b를 왼쪽으로 4비트 이동시킨 값을 출력하라.
        printBinStr(b<<4);
        System.out.println();
        b = 0b11111111_00000000_00000000_11111111;
        printBinStr(b);

        // TODO: 변수 b를 4비트 산술적 오른쪽 시프트를 한 값을 출력하라.
        printBinStr(b>>4);
        // TODO: 변수 b를 4비트 논리적 오른쪽 시프트를 한 값을 출력하라.
        printBinStr(b>>>4);
    }
    public static void switchCase() {  
        String menuStr =
                "********* Switch Menu *********\n" +
                "* 0.exit 1.output 2.readToken *\n" +
                "* 3.readLine 4.operator       *\n" +
                "*******************************\n";
               
            while (true) {
                String menu = UI.getNext("\n"+menuStr+"menu item string? ");
                // menu는 메뉴항목 번호가 아닌 메뉴항목 단어를 직접 입력 받은 것임
                // TODO: Ch2.run()을 참조하여 switch 문장을 이용하여 상응하는 함수를 호출하라.
                //      단, 입력된 메뉴항목이 정수가 아니라 문자열(menu)임을 명심하라.
                //      즉, case 문장이 정수가 아니라 문자열과 비교 되어야 한다.
                switch(menu) {
            	case "output":	
            		output();
            		break;
            	case "readToken":
            		readToken();
            		break;
            	case "readLine":
            		readLine();
            		break;
            	case "operator":
            		operator();
            		break;
            	case "exit":
            		return;
                }
            }
    }
}