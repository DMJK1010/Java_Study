// CH8_3: Main.java
//
//  Created on: 2024.11. 18.
//      Author: Junha Kim
//
//  + FileManager:: TEXT_PATH_NAME, 메뉴추가, 멤버함수 추가
//  + FileManager:: saveText(), loadText(), saveTextAs(), loadTextFrom() 함수 추가
//  + Person, Student, Worker 클래스에 getDelimChar() 멤버 함수 추가
//  + Person::inputMembers(Scanner sc)에 마지막에 set() 함수 호출
//  + Factory 멤버 모두 static으로 선언
//  + PersonManager: factory 멤버 삭제, 생성자 매개변수 factory 삭제, 
//  +    append(), insert(), find()에서 Factory의 static 함수를 호출하도록 수정
//  + Multimanager에서 PersonManager 객체 생성 시 매개변수 factory 삭제
//

import java.util.*;
import java.io.*;

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
        // int chk = 1; if (chk != 0) new AutoCheck(chk, true).run(); else

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
        String menuStr =  // 7_3 수정
                "************* Main Menu **************\n" +
                "* 0.exit 1.PersonManager 2.ch2 3.ch3 *\n" +
                "* 4.ch5 5.PMbyMap 6.MyVectorTest     *\n" +
                "**************************************\n";
        // 위 메뉴(menuStr)를 화면에 출력하고 메뉴 항목(정수 값)을 입력 받는다.
        final int MENU_COUNT = 7;
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
            case 5: new PMbyMap().run(); break; // 7_2
            case 6: new MyVectorTest().run(); break; // 7_3
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
	/*
    public static void run() {
        new Memo("").run();
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
        if(line.isEmpty()) {
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
class Person implements Comparable< Person > // interface 7_1
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
 public char getDelimChar() { return 'P'; } // 8_3
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
     set(name, "", id, weight, married, address, null); // 8_3
 }
 /*
 private void printMembers() {
	 System.out.print(name+" "+id+" "+weight+" "+married+" :"+address+": ");
 }
 */
 @Override
 public String toString() {
	 return name+" "+id+" "+weight+" "+married+" :"+address+":";
 }
 
 public int compareTo(Person p) {
	 return this.name.compareTo(p.name);
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
 @Override
 public char getDelimChar() { return 'S'; } // 8_3
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
	 return super.toString() + " " + department+" "+GPA+" "+year; 
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
 @Override
 public char getDelimChar() { return 'W'; } // 8_3
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
	 return super.toString() + " " + company + " " + position;
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
         "ten ten ten ten ten ten ten ten ten ten\n" + 
      "eight eight eight eight eight eight eight eight\n" + 
      "EIGHT EIGHT EIGHT EIGHT EIGHT EIGHT EIGHT EIGHT\n" + 
      "seven seven seven seven seven seven seven\n" + 
      "six six six six six six\n" + 
      "five five five five five\n" + 
      "four four four four\n" + 
      "three three three\n" + 
      "- - - - - - - - - - - - - - - - - - - - -\n" +
      "The Last of the Mohicans\n"+
      "James Fenimore Cooper\n"+
      "Author's Introduction\n"+
      "It is believed that the scene of this tale, and most of the information \n"+
      "necessary to understand its allusions, are rendered sufficiently \n"+
      "obvious to the reader in the text itself, or in the accompanying notes.\n"+
      "Still there is so much obscurity in the Indian traditions, and so much \n"+
      "confusion in the Indian names, as to render some explanation useful.\n"+
      "Few men exhibit greater diversity, or, if we may so express it, \n"+
      "greater antithesis of character, \n"+
      "than the native warrior of North America.";
 
 public void run() {
     String menuStr =  // ch7_3
     "+++++++++++++++++++++ Memo Management Menu +++++++++++++++++++\n"+
     "+ 0.exit 1.display 2.find 3.findReplace 4.compare 5.dispByLn +\n"+
     "+ 6.delLn 7.replLn 8.scrollUp 9.scrollDown 10.inputMemo      +\n"+
     "+ 11.wordCount 12.countWordList                              +\n"+
     "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";

     // 멤버 mStr이 비었을 경우 위 memoData로 초기화한다.
     if (mStr.length() == 0) mStr.append(memoData);
     final int MENU_COUNT = 13; // 상수 정의

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
         case 11:wordCount();     break; // ch7_3
         case 12:countWordList(); break; // ch7_3
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
 
 private Map< String, Integer > getWordCountMap() { // ch7_3
     String words[] = mStr.toString().split("\\s");
     TreeMap <String, Integer> wordCountMap = new TreeMap <String, Integer>();
     Integer cnt;
     for(var w : words) {
    	 if(!w.equals("")) {
    		 if((cnt = wordCountMap.get(w)) == null)
    			 wordCountMap.put(w, 1);
    		 else
    			 wordCountMap.put(w, ++cnt);
    	 }
    		 
     }
     /* ToDo:
     mStr에서 전체를 하나의 문자열로 빼낸다. (compare() 함수 참고)
     이 문자열을 공백문자("\\s")를 구분자로 해서 토큰을 자른 후 문자열 배열인 words 저장한다.
     문제의 목적(단어별 정렬이 미리 되어 있는)에 맞는 <단어, 출현횟수>를 저장할 수 있는 
         적절한 Map을 생성해서 wordCountMap에 저장한다.
     for-each 문을 이용하여 words의 각 단어 w에 대해
 		빈 문자열("")이 있을 수 있으므로 w가 빈 문자열인 경우 스킵한다.
 		wordCountMap에서 단어 w를 검색한다. 검색된 값은 출현 횟수인 Integer 객체이다.
 		검색 성공 실패는 교제 예제 7-5를 참고하라.
 		검색 실패면(처음 삽입 시) 횟수 1을 그냥 맵에 삽입한다. (그냥 숫자 1을 삽입하면 된다.)
 		검색에 성공했으면 기존 횟수를 1 증가시켜 다시 맵에 삽입한다. 
 		(Integer 객체에 대해 ++ 또는 += 연산자 적용 가능함)
 	}
 	*/
 	return wordCountMap; // Map으로 자동 업 케스팅되어 리턴됨
 }
 
 void wordCount() { // Menu item 11
     var wordCountMap = getWordCountMap();
     System.out.println("----------------");
     System.out.println("Word      Count");
     System.out.println("----------------");
     Set< Map.Entry< String, Integer > > wcEntries = wordCountMap.entrySet();
     for (var e: wcEntries) {
    	 String word = e.getKey();
    	 int count = e.getValue();
         if (count > 1)  // %-7s: 문자열을 7 칸 안에 출력하되 좌 맞춤
             System.out.printf("%-7s    %2d\n", word, count);
     }
         
     /* 
     ToDo: PMbyMap::display(map)을 참고하여 아래를 구현하라.
     1) 위 wordCountMap의 엔트리 집합을 구해서 wcEntries에 저장하라.
            엔트리 집합 wcEntries은 Set< Map.Entry < String, Integer > > 타입이어야 한다.
            각 엔트리는 <단어, 단어의 출현횟수>이다. 
     2) for-each 문을 이용하여 엔트리 집합 wcEntries의 각 엔트리 wc에 대해
            wc에서 키(단어, word)와 값(단어 출현 횟수, count)을 구한 후 
            (PMbyMap::display(map)을 참고할 것) 이를 아래처럼 출력한다.
            if (count > 1)  // %-7s: 문자열을 7 칸 안에 출력하되 좌 맞춤
                System.out.printf("%-7s    %2d\n", word, count);
     */
     System.out.println("----------------");
 }
 
 void countWordList() { // Menu item 12,  ch7_3
     var wordCountMap = getWordCountMap();
     Set< Map.Entry< String, Integer > > wcEntries = wordCountMap.entrySet();
     
     TreeMap< Integer, Vector< String > > countWordsMap = new TreeMap<>();
     
     for(var wc : wcEntries) {
    	 String word = wc.getKey();
    	 int count = wc.getValue();
    	 
    	 Vector<String> wordVct = countWordsMap.get(count);
    	 if(wordVct == null) {
    		 wordVct = new Vector<String>();
    		 countWordsMap.put(count, wordVct);
    	 }
    	 wordVct.add(word);
     }
     
     /* ToDo: 먼저 구현한 wordCount()을 참고하여 아래를 구현하라.
     1) 위 wordCountMap의 엔트리 집합을 구해서 wcEntries에 저장하라.
     2) 키는 (단어의 출현 회수)이고, 값은 (동일한 출현 횟수를 가진 단어들을 저장할 수 있는 Vector)인
        Map을 생성하여 countWordsMap에 저장한다.
        즉, 이 맵의 엔트리는 < Integer, Vector < String > > 이다.
        이때 생성할 맵은 출현 횟수별로 정렬이 되어 있는 맵을 선택해야 한다.(작은 수에 큰 수로 정렬) 
     3) for-each 문을 이용하여 엔트리 집합 wcEntries의 각 엔트리 wc에 대해
            wc에서 키인 word와 값인 count를 구한다.
            count를 키로 사용해서 countWordsMap의 값인 wordVct 벡터를 검색한다.
            만약 검색에 실패 했다면 (처음 삽입 시)
                문자열 백터를 새로 생성하여 wordVct에 저장한다.
                wordVct를 countWordsMap에 삽입한다.
            wordVct에 word를 삽입한다.
     */
     System.out.println("----------------");
     System.out.println("Count  Words");
     System.out.println("----------------");

     // 아래는 키의 순서를 역순으로 재배치한 새로운 맵 reverseCWMap을 생성한다.
     // 이 맵은 단어의 출현 횟수가 큰 수부터 작은 수 순서로 횟수를 저장하고 있다.
     var reverseCWMap = countWordsMap.descendingMap();
     Set< Map.Entry < Integer, Vector< String > > > cwEntries = reverseCWMap.entrySet();
     
     for(var cw : cwEntries) {
    	 int count = cw.getKey();
    	 if(count == 1)
    		 continue;
    	 System.out.printf("%2d     ", count);
    	 Vector< String > wordVct = cw.getValue();
    	 
    	 for(var w : wordVct)
    		 System.out.print(w+ " ");
    	 System.out.println();
     }
     /* ToDo:
     1) 위 reverseCWMap의 엔트리 집합을 구해서 cwEntries에 저장하라.
     2) for-each 문을 이용하여 엔트리 집합 cwEntries의 각 엔트리 cw에 대해
            cw에서 키인 count를 구한다.
            count가 1이면 스킵한다.
            System.out.printf("%2d     ", count);
            cw에서 값인 wordVct 벡터를 구한다.
            for-each 문을 이용하여 벡터 wordVct의 각 원소 w에 대해
                System.out.print(w+ " ");
            System.out.println();
     */
     System.out.println("----------------");
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


//----------------------------------------------------------------------------
//키보드 또는 파일(8장) 등으로부터 Person 객체의 각 멤버 정보를 읽어 들인 후 
//새로운 Person 객체를 생성하여 리턴해 준다. (추후 계속 기능이 추가될 예정임)
//----------------------------------------------------------------------------
class Factory
{
 public static void printInputNotice(String preMsg, String postMsg) {
	 System.out.println("input"+preMsg+" [delimiter(P,S,or W)]"+
             " [person information]"+postMsg+":");
 }
 public static Person inputPerson(Scanner sc) { 
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
 // private VectorPerson pVector;
 private Vector< Person > pVector;
 //private MyVector < Person > pVector;
 private Person array[];
 private Random rand; // 7_1 추가

 public PersonManager(Person array[]) {
     //System.out.println("PersonManager(array[])");
     pVector = new Vector < Person >();
     //pVector = new MyVector< Person >();
     this.array = array;
     cpCount = 0;
     addArray();
     SmartPhone.setBaseStation(this);
     rand = new Random(0);  // 0: seed 값임, 7_1 추가
     display();
 }

 public void run() {
     String menuStr =
             "=================== Person Management Menu =====================\n" +
             "= 0.exit 1.display 2.clear 3.reset 4.remove 5.copy 6.append    =\n" +
             "= 7.insert 8.login 9.dispStudent(5_3) 10.dispPhone(5_3)        =\n" +
             "= 11.find(6_1) 12.wrapper(6_1) 13.shuffle(6_1) 14.setDate(6_1) =\n" +
             "= 15.chAddress(6_2) 16.collections(7_1) 17.fileManager(8_1)    =\n" +
             "================================================================\n";
     final int MENU_COUNT = 18; // 상수 정의
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
         case 16:collections();		break;
         case 17:fileManager();     break;   // ch8_1
         case 0:                    return;
         }
     }
 }
 
 public static void display(List< Person > list) {
     int count = list.size(); // ToDo: pVector의 모든 원소의 개수
     //System.out.println("display(): count " + count);
     for (int i = 0; i < count; ++i)
         // ToDo: pVector의 인덱스 i번째 객체 출력
         System.out.println("[" + i + "] " + list.get(i));  
     //System.out.println("empty():" + pVector.isEmpty() + ", size():" + pVector.size()
     //     + ", capacity():" + pVector.capacity());
 }
 
 public void display() { // Menu item 1
	 display(pVector);

 }
 public void clear() {  // Menu item 2
     pVector.clear(); // ToDo: pVector의 모든 원소를 삭제하라.
     display();
 } 
 public void reset() { // Menu item 3
     pVector.clear(); // ToDo: pVector의 모든 원소를 삭제하라.
     addArray();
     display();
 }
 public void remove() { // Menu item 4
     if (pVector.isEmpty()) { // ToDo: pVector의 원소가 하나도 없을 경우
         System.out.println("no entry to remove");
         return;
     }
     int index = UI.getIndex("index to delete? ", pVector.size());
     pVector.remove(index); // ToDo: pVector의 index 원소를 삭제하라.
     display();
 }
 public void copy() { // Menu item 5
     cpCount++;
                   // ToDo: pVector의 모든 원소의 개수
     for (int i = 0, size = pVector.size(); i<size; ++i) { 
         Person p = pVector.get(i).clone(); // ToDo: pVector의 i번째 원소를 복제해서 p에 저장하라.
         String name = p.getName();
         for (int j = 0; j < cpCount; ++j)
             name = name.charAt(0)+name;
         p.set(name);
         p.set(p.getId() + 20 * cpCount);
         p.set(p.getWeight() + cpCount);
         if (cpCount % 2 == 1)
             p.set(!p.getMarried());
         pVector.add(p); // ToDo: p를 pVector의 맨 뒤에 추가하라.
     }
     display();
 }
 public void append() { // Menu item 6
     int count = UI.getPosInt("number of persons to append? ");
     Factory.printInputNotice(" "+Integer.toString(count), " to append");
     for (int i = 0; i < count; ++i) {
         Person p = Factory.inputPerson(UI.scan);
         if(p!=null)
        	 pVector.add(p);
         else
        	 i--;
         
         // ToDo: p가 잘못 입력된 객체가 아닌 경우 p를 pVector의 맨 뒤에 추가하고,
         //       p가 잘못 입력된 객체인 경우 입력 개수에 포함시키지 않는다.
         //       (즉, i 값이 증가되지 말아야 함) 과거 코드가 잘못되었을 수 있음
     }
     display();
 }
 public void insert() { // Menu item 7
     int index = 0;
     if (pVector.size() > 0) {
         // ToDo: 새로운 원소를 삽입할 장소는 pVector의 
         //       인덱스 0에서부터 마지막 원소 바로 다음 장소까지 삽입 가능하다. 
         index = UI.getIndex("index to insert in front? ", pVector.size()+1);
         if (index < 0) return;
     }
     Factory.printInputNotice("", " to insert");
     Person p = Factory.inputPerson(UI.scan);
     if(p==null) // ToDo: 객체 p가 잘못 입력된 객체인 경우 여기서 리턴하라.
    	 return;
     pVector.add(index, p); // ToDo: 객체 p를 pVector의 index 위치에 삽입하라.
     display();
 }
 
 private void addArray() {
	 for(var arr : array)
		 pVector.add(arr);
     // ToDo: array의 모든 원소를 순서적으로 복사한 후 pVector에 추가하라.
     //       이때 for-each 문장을 사용하라. 즉, for (원소변수 : 배열변수)
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
     
     Person p = Factory.inputPerson(UI.scan);
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
		 //int j = (int)(Math.random() * pVector.size());
		 int j = rand.nextInt(pVector.size()); // [0 ~ (pVector.size()-1)] 
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
 
 public void collections() { // Menu item 16: ch7_1
     new CollectionsByList(pVector).run();
 }
 
 public void fileManager() { // Menu item 17: ch8_1
     new FileManager(pVector).run();
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
     var pm = new PersonManager(allPersons);
     pm.run();
     //System.out.println("PersonManager::run() returned");
 }
} // class MultiManager: ch4_1

abstract class CollectionsMenu // ch7_1
{
    public void run() {
        String menuStr =
            "======================= Collections Menu =======================\n" +
            "= 0.exit 1.display 2.min 3.max 4.sort 5.reverse 6.binarySearch =\n" +
            "================================================================\n";
        final int MENU_COUNT = 7;
        while (true) {
            int menuItem = UI.selectMenu(menuStr, MENU_COUNT);
            switch(menuItem) {
            case 1:display();         break;
            case 2:min();             break;
            case 3:max();             break;
            case 4:sort();            break;
            case 5:reverse();         break;
            case 6:binarySearch();    break;
            case 0:                   return;
            }
        }
    }
    abstract public void display(); // Menu item 1
    abstract public void min(); // Menu item 2
    abstract public void max(); // Menu item 3
    abstract public void sort(); // Menu item 4
    abstract public void reverse(); // Menu item 5
    abstract public void binarySearch(); // Menu item 6
}

class CollectionsByList extends CollectionsMenu // ch7_1
{
    // List는 인터페이스로 ArrayList, Vector, LinkedList의 수퍼 클래스이다.
    private List< Person > list; // list는 벡터라 생각하고 사용하면 된다.

    public CollectionsByList(List< Person > list) { 
    	this.list = list; // list는 PersonManager의 pVector가 업캐스팅된 것이다.
    }
    @Override
    public void display() { // Menu item 1
        //for (int i = 0, count = list.size(); i < count; ++i)
        //    System.out.println("[" + i + "] " + list.get(i));
    	PersonManager.display(list);
    }
    @Override
    public void min() { // Menu item 2
    	if(list.isEmpty())
    		return;
    	System.out.println(Collections.min(list));
        // ToDo: list(사실 pVector임)의 모든 객체들 중 사전적 순서상으로
        //       가장 빠른 이름을 찾아 그 객체를 출력하라.
        //       Collections 클래스의 적절한 함수를 호출하면 됨.
        //       list가 비었을 경우는 그냥 리턴하라.
    }
    @Override
    public void max() { // Menu item 3
    	if(list.isEmpty())
    		return;
    	System.out.println(Collections.max(list));
    	
        // ToDo: list(사실 pVector임)의 모든 객체들 중 사전적 순서상으로
        //       가장 나중에 나오는 이름을 찾아 그 객체를 출력하라.
        //       list가 비었을 경우는 그냥 리턴하라.
    }
    @Override
    public void sort() { // Menu item 4
    	Collections.sort(list);
        // ToDo: list의 모든 원소들을 이름(name) 순으로 정렬(sort)하라. 
        //       (Collections 클래스의 적절한 함수를 호출할 것)
        display();
    }
    @Override
    public void reverse() { // Menu item 5
    	Collections.reverse(list);
        // ToDo: list의 모든 원소들의 배치 순서를 거꾸(reverse)로  
        //       배치하라. (Collections 클래스의 적절한 함수를 호출할 것)
        display();
    }
    @Override
    public void binarySearch() {  // Menu item 6
        String name = UI.getNext("For binary search, it's needed to sort in advance. Name to search? ");
        Person tmp =new Person(name);
        
        int index = Collections.binarySearch(list, tmp);
        
        if(index < 0)
        	System.out.println(name + " is NOT found.");
        else
        	System.out.println(list.get(index));
        
        // ToDo: name을 입력 받은 후 name을 원소로 가지는 임시 Person 객체를 생성한 후
        //       이 객체와 동일한 이름을 가진 객체를 list에서 이진 검색하여 찾는다. 
        //       (Collections 클래스의 적절한 함수를 호출할 것:  
        //        이 함수는 찾은 원소의 인덱스를 반환하고 못 찾은 경우 -1을 반환한다.)
        //       list에서 찾았으면 해당 객체를 출력하고
        //           찾지 못했으면 실행결과처럼 "이름 is NOT found." 를 출력하라.
        // 주의: 이진 검색하기 전에 메뉴 4를 실행하여 list가 먼저 정렬이 되어 있어야 한다.
    }
}   // ch7_1: CollectionsByList class

abstract class CollectionsByMap extends CollectionsMenu // ch7_2
{
    public void display(Map< String, Person > map) {
        // ToDo: PMbyMap의 display(map)을 호출하라.
    	PMbyMap.display(map);
    }
    public void searchMap(Map< String, Person > map) {
    	String name = UI.getNext("Name to search? ");
        Person find = map.get(name);
        if(find != null)
            System.out.println(find);
        else
            System.out.println(name + ": NOT found");
        // ToDo: PMbyMap::find()를 참고하여 "Name to search? " 출력한 후
        //       이름을 입력 받아라. 나머지는 PMbyMap::find()의 내용을 복사해 와라.
        //       즉, 이름을 map에서 검색하고 찾은 객체를 출력하라.
    }
}   // ch7_2: CollectionsByMap class

class CollectionsByTreeMap extends CollectionsByMap // ch7_2
{
    private TreeMap< String, Person > map;

    public CollectionsByTreeMap(TreeMap< String, Person > map) { this.map = map; }
    
    public void display() { display(map); }
    public void min() {
        // 첫번째 entry가 키가 가장 작은 엔트리이다. 이름 순서상 가장 앞쪽 이름임
        Map.Entry< String, Person > e = map.firstEntry();
        if (e != null) System.out.println(e.getValue());
    }
    public void max() {
        // 마지막 entry가 키가 가장 큰 엔트리이다. 이름 순서상 가장 뒤쪽 이름임
        Map.Entry< String, Person > e = map.lastEntry();
        if (e != null) System.out.println(e.getValue());
    }
    public void sort() { display(); } // 키가 이미 정렬되어 있으므로 바로 보여 줌
    
    // descendingMap()을 통해 키의 역순으로 된 map를 구할 수 있음
    public void reverse() { display(map.descendingMap()); }
    
    public void binarySearch() { searchMap(map); } // 맵에서 바로 검색함
    
}   // ch7_2: CollectionsByTreeMap class

class CollectionsByHashMap extends CollectionsByMap // ch7_2
{
    private HashMap< String, Person > map;

    public CollectionsByHashMap(HashMap< String, Person > map) { this.map = map; }
    
    public void display() {  display(map); }
    public void min() { 
    	if(map.isEmpty())
    		return;
    	System.out.println(map.get(Collections.min(map.keySet())));
        // ToDo: map이 비었을 경우는 그냥 리턴하라.
        //       CollectionsByList::min()을 참조하라.
        //       map의 keySet()을 매개변수로 넘겨 주어 Collections.min() 호출하라.
        //       (Collections.min(), max()는 List와 Set을 매개변수로 줄 수 있다.)
        //       그러면 가장 작은 키(사전 순으로 가장 빠른 이름)를 얻을 수 있다.
        //       이 키를 이용하여 map을 검색하여 값(Person 객체)을 찾아 출력하라.
    }
    public void max() { 
    	if(map.isEmpty())
    		return;
    	System.out.println(map.get((Collections.max(map.keySet()))));
        // ToDo: 위 min() 함수를 참고하여 가장 큰 키를 얻고 그 키의 값인 객체를 출력하라. 
    }
    public void sort() { 
        // map의 keySet을 이용하여 벡터를 생성함
        var keyList = new Vector< String >(map.keySet());
        Collections.sort(keyList);
        for(var name : keyList)
        	System.out.println(map.get(name));
        
        // ToDo: keyList를 정렬하라. 정렬 시 CollectionsByList::sort()을 참조하라.
        //       for-each 문을 이용하여 keyList의 각각 원소 key에 대해
        //           key를 이용하여 map을 검색하여 값(Person 객체)을 찾아 출력하라.
        //       위 for-each 구현 시 PMbyMap::display(Map< String, Person > map)의 
        //           주석 처리된 /* */ 부분 코드 참고하라. 차이점은 여기서는 keySet이 아니고 
        //           keyList 즉, Vector라는 것이고 구현 코드는 동일하다.
    }
    public void reverse() { 
    	var keyList = new Vector< String >(map.keySet());
    	Collections.reverse(keyList);
    	for(var name : keyList)
        	System.out.println(map.get(name));
        // ToDo: 위 sort() 함수를 참고하되 
        //       keyList를 원소들의 배치 순서를 역으로 배치한 후 출력하라.
        //       keyList의 역순 배치 시 CollectionsByList::reverse() 참고
    }
    public void binarySearch() { searchMap(map); }  // 맵에서 바로 검색함
    
}   // ch7_2: CollectionsByHashMap class

class MyVector<E> // ch7_3
{
 static final int DEFAULT_SIZE = 10;

 private Object [] persons; // Person 객체 참조들의 배열, 즉 배열에 저장된 값이 Person 객체의 주소이다.
 private int count;        // persons 배열에 현재 삽입된 객체의 개수

 public MyVector() { this(DEFAULT_SIZE); }

 public MyVector(int capacity) {
     count = 0; // persons 배열에 현재 삽입된 객체의 개수는 0
     //System.out.println("VectorPerson::VectorPerson("+capacity+")");
     persons = new Object[capacity]; // 객체 참조 배열 할당
 }
 // persons[index]의 값을 반환
 @SuppressWarnings("unchecked")
 public E get(int index) { return (E)persons[index]; }
 
 // persons[index]의 값을 p로 새로 교체하고 과거의 persons[index] 값을 반환
 @SuppressWarnings("unchecked")
 public E set(int index, E p) {
	 Object tmp = persons[index];
	 persons[index] = p;
	 return (E)tmp;
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
	 for(int i = index; i < count-1; i++) {
		 persons[i] = persons[i+1];
	 }
	 count--;
 }
 // persons[]의 배열 크기가 작으면 extend_capacity()를 호출하여 먼저 배열을 확장한다.
 // persons 배열에 마지막 삽입된 원소 뒤에 새로운 원소 p를 삽입하고 현재 삽입된 객체 개수 증가
 public void add(E p) {
      if (count >= persons.length)
          extend_capacity();
      persons[count++] = p;
 }
 // persons[]의 배열 크기가 작으면 extend_capacity()를 호출하여 먼저 배열을 확장한다.(위 코드 참고)
 // 먼저 persons[] 배열 마지막 원소부터 index까지 객체를 한칸씩 뒤로 밀어 준다.
 // (count가 0일수도 있고 index가 count와 같은 위치일 수도 있다. if 없이 하나의 for 문으로 가능함) 
 // index 위치에 객체 p를 삽입하고, count 값 조정
 public void add(int index, E p) {
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
	 Object[] saved_persons = new Object[persons.length];
	 for(int i = 0; i < persons.length; i++)
		 saved_persons[i] = persons[i];
	 persons = new Object[persons.length * 2];
	 
	 for(int i = 0; i < saved_persons.length; i++)
		 persons[i] = saved_persons[i];
	 
     //System.out.println("VectorPerson: capacity extended to " + persons.length);
 }
}   // MyVector class: ch7_3

class MyVectorTest extends BaseManager // ch7_3
{
    private MyVector< String>  nameVct;    // Person::name 멤버들을 저장하는 벡터
    private MyVector< Integer> idVct;      // Person::id 멤버들을 저장하는 벡터
    private MyVector< Double>  weightVct;  // Person::weight 멤버들을 저장하는 벡터
    private MyVector< Boolean> marriedVct; // Person::married 멤버들을 저장하는 벡터
    private MyVector< String>  addressVct; // Person::address 멤버들을 저장하는 벡터

    public MyVectorTest() {
        // ToDo: MyVector< >의 기본 생성자를 이용하여 5개의 멤버 벡터들을 생성하라.
        nameVct		= new MyVector< String >();
        idVct		= new MyVector< Integer >();
        weightVct	= new MyVector< Double >();
        marriedVct	= new MyVector< Boolean >();
        addressVct	= new MyVector< String >();
        // 아래 add()는 각 벡터에 ADD_SIZE개의 원소를 자동 삽입함
        add();  // BaseManager의 멤버 함수임; 이 함수는 다시 아래의 addElements()를 호출함
    }
    public void displayPerson(int i) { // 한 사람의 정보를 출력함 
        System.out.println("[" + i + "] " + nameVct.get(i)+" "+idVct.get(i)+
        " "+weightVct.get(i)+" "+marriedVct.get(i)+" :"+addressVct.get(i)+":");
    }
    public void display() { // Menu item 1: 모든 사람들의 정보를 출력함
        for (int i = 0, count = idVct.size(); i < count; ++i)
            displayPerson(i);
    }
    public void replace() { // Menu item 4
        // 각 벡터의 총 원소 중 초반의 REPLACE_SIZE개의 사람 정보를 모두 수정함
        int count = Math.min(REPLACE_SIZE, idVct.size());
        for (int i = 0; i < count; ++i) {
        	Person p = getNewPerson(); // 새로운 객체를 자동으로 생성함
            nameVct.set(i, p.getName());
            idVct.set(i, p.getId());
            weightVct.set(i, p.getWeight());
            marriedVct.set(i, p.getMarried());
            addressVct.set(i, p.getAddress());
        }
        display();
    }
    public void remove() { // Menu item 5
        if (idVct.size() == 0) {
            System.out.println("no entry to remove");
            return;
        }
        int index = UI.getIndex("index to delete? ", idVct.size());
    	nameVct.remove(index);
    	idVct.remove(index);
    	weightVct.remove(index);
    	marriedVct.remove(index);
    	addressVct.remove(index);
        
        // ToDo: 5개의 각각의 멤버 벡터에 대해 index 항목을 삭제하라.
        display();
    }
    public void find() { // Menu item 6
        String name = UI.getNext("name to find? ");
        for(int i=0; i < nameVct.size(); i++) {
        	if(nameVct.get(i).equals(name)) {
        		System.out.println("[" + i + "] " + nameVct.get(i)+" "+idVct.get(i)+
        		        " "+weightVct.get(i)+" "+marriedVct.get(i)+" :"+addressVct.get(i)+":");
        		return;
        	}
        }
        
        // ToDo: for를 이용하여 nameVct에서 입력 받은 name 같은 이름을 찾아서
        //       찾은 경우 그 사람의 정보를 출력하라.
        //       찾지 못한 경우 아래를 출력하라.
        System.out.println(name + ": NOT found");
    }
    public void collections() { // Menu item 7
        System.out.println("not supported by MyVector");
    }
    public void clearAllElements() {  // BaseManager::clear()에 의해 호출됨
        // ToDo: 5개의 각각의 멤버 벡터에 대해 모든 원소들을 삭제하라.
    	nameVct.clear();
    	idVct.clear();
    	weightVct.clear();
    	marriedVct.clear();
    	addressVct.clear();
    }
    public void addElements() {   // BaseManager::add()에 의해 호출됨
        // ADD_SIZE개의 Person 정보를 자동 생성하여 별개의 벡터에 보관함
        for (int i = 0; i < ADD_SIZE; ++i) {
            // 각각의 벡터의 끝에 자동 생성된 Person 멤버들을 추가한다.
        	Person p = getNewPerson();
            int id = p.getId();
            nameVct.add(p.getName()); 
            idVct.add(id); 
            weightVct.add(p.getWeight()); 
            marriedVct.add(p.getMarried()); 
            addressVct.add(p.getAddress()); 
        }
    }
}   // ch7_3: MyVectorTest class

class PersonGenerator
{
    static final int ADD_SIZE = 10;
    static final int REPLACE_SIZE = 5;

    private Random rand;
    int count, id, stYear;
    String name, address, department, company, position;
    double weight, GPA;
    boolean married;

    public PersonGenerator() {
        rand = new Random(0);
        count = 10;
    }
    public Person getNewPerson() {
        name = families[rand.nextInt(families.length)] + (count++);
        id = 1000+rand.nextInt(1000);
        weight = 40 + rand.nextDouble() * 60;
        weight = (int)(weight * 10) / (double)10;
        married = (id % 2) == 1;
        address = cities[rand.nextInt(cities.length)] + " " + gus[rand.nextInt(gus.length)]; 
        stYear = rand.nextInt(4)+1;
        GPA = rand.nextDouble() * 4.5;
        GPA = (int)(GPA * 10) / (double)10;
        department = departs[rand.nextInt(departs.length)];
        company = companies[rand.nextInt(companies.length)];
        position = positions[rand.nextInt(positions.length)];

        switch (rand.nextInt(3)) {
        case 0: return new Person(name, id, weight, married, address);
        case 1: return new Student(name, id, weight, married, address, department, GPA, stYear);
        case 2: return new Worker(name, id, weight, married, address, company, position);
        default: return null;
        }
    }
    private String families[] = 
        { "Kimm", "Leem", "Park", "Choi", "Jeon", "Shim", "Shin", "Kang", "Yang", "Yoon",
          "Baek", "Ryoo", "Seoh", "Johh", "Baeh", "Moon", "Nahh", "Jooh", "Song", "Hong" };
    private String cities[] =
        { "Seoul", "Busan", "Gwangju", "Daejeon", "Incheon", "Daegu", "Ulsan" }; 
    private String gus[] =
        { "Jung-gu", "Nam-gu", "Buk-gu", "Dong-gu", "Seo-gu", }; 
    private String departs[] =
        { "Physics", "Electronics", "Computer", "Chemistry", "Biology", "Micanical" }; 
    private String companies[] =
        { "Samsung", "Hyundai", "Kia", "SK", "LG", "Naver", "Kakao", "KT" }; 
    private String positions[] =
        { "Director", "Manager", "CEO", "CTO", "Chief", "CFO" }; 
}   // ch8_2: PersonGenerator class

abstract class BaseManager extends PersonGenerator
{


    public void run() {
        String menuStr = 
            "======= Base Person Management Menu ========\n" +
            "= 0.exit 1.display 2.clear 3.add 4.replace =\n" +
            "= 5.remove 6.find 7.collections            =\n" +
            "============================================\n";
        final int MENU_COUNT = 9; // 상수 정의
        while (true) {
            int menuItem = UI.selectMenu(menuStr, MENU_COUNT);
            switch(menuItem) {
            case 1: display();       break;
            case 2: clear();         break;
            case 3: add();           break;
            case 4: replace();       break;
            case 5: remove();        break;
            case 6: find();          break;
            case 7: collections();   break;
            case 0:                  return;
            }
        }
    }
    abstract public void display(); // Menu item 1

    public void clear() {  // Menu item 2
    	clearAllElements(); // 아래 추상함수 설명 참고
        display();
    }
    public void add() { // Menu item 3
    	addElements();
        display();
    }
    abstract public void replace(); // Menu item 4
    abstract public void remove(); // Menu item 5
    abstract public void find(); // Menu item 6
    abstract public void collections(); // Menu item 7
    //-------------------------------------------------------------------------------------------
    // 기타 abstract functions
    //-------------------------------------------------------------------------------------------
    abstract public void clearAllElements(); // 컬렉션의 모든 원소를 삭제함
    // Person 객체들을 자동으로 생성하여 컬렉션에 추가함; 이때 아래 getXXX() 함수를 활용한다.
    abstract public void addElements(); 

}   // ch7_2: BaseManager class

class PMbyMap extends BaseManager // ch7_2
{
    // Map은 HashMap과 TreeMap의 수퍼 클래스(인터페이스)이며 map = hashMap으로 업캐스팅 가능함
    private Map< String, Person > map;
    private HashMap< String, Person > hashMap = null;
    private TreeMap< String, Person > treeMap = null;

    public PMbyMap() {
        String menuStr = 
                "====== Map Menu =======\n" +
                "= 0.HashMap 1.TreeMap =\n" +
                "=======================\n";
        int mapType = UI.selectMenu(menuStr, 2);
        if (mapType == 0)
            map = hashMap = new HashMap< String, Person >();
        else if (mapType == 1)
            map = treeMap = new TreeMap< String, Person >();
            
        // 위에서 선택된 Map의 종류에 상관없이 이 이후부터는 map을 교재의 예제에서 보여준
        // HashMap이라 생각하고 멤버 함수들을 사용하여 코딩하면 된다.
        add(); // BaseManager 멤버 함수
    }
    static public void display(Map< String, Person > map) {
        System.out.println("Map Size: "+map.size());
        System.out.println("---------------------------------------------");
        // 교재에서 소개된 이 방식은 간단하기는 하나 각 키에 대해 매번 다시 map.get(name)을 통해
        // map 검색을 다시 해야 하는 오버헤드가 있다.
        /* 
        Set< String > keySet = map.keySet();
        for (var name: keySet)
            System.out.println(map.get(name));
        */
        // 아래 방식은 EntrySet을 구하는데 map의 모든 entry를 다 가지고 있는 집합이다.
        // 각 entry는 { 키, 값 }으로 구성된다.
        // 각 entry e에 저장된 키와 값은 e.getKey(), e.getValue()를 통해 얻을 수 있다.
        // 이 방식을 사용하면 매번 map을 다시 검색하지 않아도 된다.
        Set< Map.Entry< String, Person > > entrySet = map.entrySet();
        for (var e: entrySet)
            System.out.println(e.getValue());
    }
    public void display() { // Menu item 1
        display(map);
    }
    // 모든 키 값에 대해 새로운 객체를 생성한 후 이름은 기존과 동일하게 설정한다.
    // 그런 후 동일한 키에 대해 값(객체)만 교체한다.  
    public void replace() { // Menu item 4
    	int i = 0;
        Set< String > keySet = map.keySet();
        for (var name: keySet) {
        	var p = getNewPerson();
        	p.set(name);
        	map.put(name, p);
            // ToDo: 새로 생성된 Person 객체 p의 이름을 name으로 변경하라.
            //       name과 p를 map에 추가하라. 기존에 동일한 키가 존재하기 때문에 값만 교체됨
        	if (++i == REPLACE_SIZE) break; // 초반의 REPLACE_SIZE 개수만큼만 교체함
        }
        display();
    }
    public void remove() { // Menu item 5
    	if(map.isEmpty())
    		System.out.println("no entry to remove");
    	String name = UI.getNext("name to delete? ");
    	map.remove(name);
        // ToDo: map이 비어 있으면 "no entry to remove"를 출력하고 리턴하라.
        //       PersonManager::login()을 참조하여 
        //          "name to delete? "를 출력하고 name을 입력 받아라.
        //       map에서 name 엔트리를 제거하라.
        display();
    }
    public void find() { // Menu item 6
    	String name = UI.getNext("name to find? ");
    	Person find = map.get(name);
    	if(find != null)
    		System.out.println(find);
    	else
    		System.out.println(name + ": NOT found");
        // ToDo: "name to find? "를 출력하고 name을 입력 받아라.
        //       map에서 name을 검색하여 결과를 저장하라.
        //       name을 찾았으면 Person 객체를 출력하고
        //       찾지 못했으면 name + ": NOT found"를 출력하라.
    }
    public void collections() { // Menu item 7
    	
        if (hashMap == null)
            new CollectionsByTreeMap(treeMap).run();
        else
            new CollectionsByHashMap(hashMap).run();
    }
    public void clearAllElements() {  // BaseManager::clear()에서 호출됨
        // ToDo: map의 모든 원소를 삭제하라.
    	map.clear();
    }

    public void addElements() {  // BaseManager::add()에서 호출됨
        for (int i = 0; i<ADD_SIZE; ++i) {
        	var p = getNewPerson();
        	map.put(p.getName(), p);
            // ToDo: 위 replace()를 참고하여 새로운 Person 객체 p를 생성하라.
            //       생성된 객체를 값으로 해서 map에 추가하라. 키는 p의 이름이다.
    	}
    }
}   // ch7_2: PMbyMap class

class FileManager extends PersonGenerator  // ch8_1
{
    static final String HOME_DIR = "data"; // 상수 정의: 파일들을 생성할 폴더 이름
    static final String TEXT_PATH_NAME = HOME_DIR+"/persons.txt"; // 8_3
    private List< Person > list;
    
    FileManager(List< Person > list) { 
        var dir = new File(HOME_DIR);
        if (!dir.exists()) dir.mkdir(); // 프로젝트 폴더에 "data" 폴더가 없을 경우 새로 생성
        this.list = list;
    }
    public void run() {
        String menuStr = 
            "====================== File Management Menu =====================\n" +
            "= 0.exit 1.fileList 2.delete 3.rename 4.addFiles 5.addDir       =\n" +
            "= 6.deleteAll 7.show 8.copy 9.append 10.display 11.clear 12.add =\n" +
            "= 13.saveText   14.loadText   15.saveTextAs   16.loadTextFrom   =\n" +
            "=================================================================\n";
        final int MENU_COUNT = 17; // 상수 정의
        
        while (true) {
            int menuItem = UI.selectMenu(menuStr, MENU_COUNT);
            try {  // ch8_2
            	switch(menuItem) {
                case 1: fileList();      break;
                case 2: delete();        break;
                case 3: rename();        break;
                case 4: addFiles();      break;
                case 5: addDir();        break;
                case 6: deleteAll();     break;
                case 7: show();          break;  // ch8_2
                case 8: copy();          break;  // ch8_2
                case 9: append();        break;  // ch8_2
                case 10: display();      break;  // ch8_2
                case 11: clear();        break;  // ch8_2
                case 12: add();          break;  // ch8_2
                case 13: saveText();     break;  // ch8_3
                case 14: loadText();     break;  // ch8_3
                case 15: saveTextAs();   break;  // ch8_3
                case 16: loadTextFrom(); break;  // ch8_3
                case 0:                  return;
                }
            } catch (IOException e) { System.out.println("file I/O error: "+e); }
        }
    }
    void printFileInfo(File f) {
        long t = f.lastModified();
        t = 1700000000000L;  // 2023-11-15 오전 07:13; 자동 체크 때 사용할 예정임
        System.out.printf("%-20s %c %tF %tH:%tM %9d\n", 
                f.getName(), f.isDirectory()? 'D':'F', t, t, t, f.length());
    }
    File[] fileList() { // menu item 1
        var dir = new File(HOME_DIR);
        File files[] = dir.listFiles();
        if (files == null) return null; // HOME_DIR이 존재하지 않을 경우 null
        for (int i=0; i < files.length; i++) {
            File f = files[i];
            System.out.printf("[%d] ", i);
            printFileInfo(f);
        }
        System.out.println("-----------------");
        System.out.println("["+HOME_DIR+"] directory: "+files.length+" files");
        return files;
    }
    File selectFile(String preMsg, String postMsg, boolean onlyFile) { 
        File files[] = fileList();
        if (preMsg.length() != 0) preMsg += " ";
        while (true) {
            String msg = "index number of a "+preMsg+"file to "+postMsg+" [-1:stop]? ";
            int val = UI.getInt(msg);
            
            	
            if(val < files.length && val >= 0) {
            	if(onlyFile == true && files[val].isDirectory()) {
                	System.out.println("can't select directory: " + files[val].getName());
                	continue;
                }
            	return files[val];
            }
            else if(val == -1)
            	return null;
            else
            	System.out.println("invalid index number: " + val);
            /* ToDo
            UI의 적절한 함수를 사용하여 msg를 출력하고 정수 값(val)을 입력 받는다.
            val이 정상적인 인덱스 값이면 해당 File 객체를 리턴한다.
            사용자가 파일 선택을 취소한 경우 즉 val이 -1이면 null을 리턴한다. 그렇지 않으면
            에러 메시지("invalid index number: ")와 val을 출력한다.(다시 입력 받아야 함) 
            */
        }
    }
    void delete() { // menu item 2
    	File f = selectFile("", "delete", false);
    	if(f == null)
    		return;
    	f.delete();
    	fileList();
        /* ToDo
        selectFile() 함수를 호출하여 삭제할 파일의 File 객체 f를 구한다.
            selectFile() 함수 호출 시 필요한 경우 인자를 ""로 지정해 주어도 된다.
        사용자가 파일 선택을 취소했을 경우 바로 리턴한다.
        File 객체 f를 이용하여 해당 파일을 삭제한다.
        파일 목록을 보여 준다.
        */
    }
    void rename() { // menu item 3
        File src = selectFile("source", "rename", false);
        if (src == null) return; // 사용자가 파일 선택을 취소했을 경우
        String NewName = UI.getNext("target file name? ");
        var dst = new File(src.getParent() + "/"+ NewName);
        if(dst.exists()) {
        	System.out.println(NewName + " already exists");
        	return;
        }
        src.renameTo(dst);
        fileList();
        /* ToDo
        UI의 적절한 함수를 사용하여 "target file name? "을 출력하고 
        변경할 새 파일 이름(한 단어로)을 입력 받는다.
        변경할 이름의 File 객체 dst를 생성한다.
        (이때 디렉터리는 src의 부모 디렉터리(src.getParent())를 얻어와 동일하게 설정해 주어야 한다. 
         addFiles()의 새로 생성할 파일의 File 객체 생성 참조)
        파일이 이미 존재하면 파일이름과 함께 " already exists" 출력하고 리턴한다.
        src의 이름을 dst의 이름으로 변경한다.
        파일 목록을 보여 준다.
        */
    }
    void addFiles() { // menu item 4: 4개의 text 파일을 자동 생성함
        for (int i = 0, count = 0; count < 4; ++i, ++count) {
            var f = new File(HOME_DIR + "/t" + i + ".txt"); // 파일 이름이 t0부터 시작함
            if (f.exists()) { --count; continue; }          // 동일한 이름이 있을 경우 건너 뜀
            try {
                var fout = new PrintStream(f);
                for (int j = 0; j <= i; ++j)
                    fout.println("This is a text file to test File Management Menu.");
                fout.close();
            } catch (IOException e) { System.out.println(e); }
        }
        fileList();
    }
    void addDir() { // menu item 5: 2개의 디렉터리를 자동으로 생성함
        for (int i = 0, count = 0; count < 2; ++i, ++count) {
            var f = new File(HOME_DIR + "/d" + i); // 파일 이름이 t0부터 시작함
            if (f.exists()) { --count; continue; }          // 동일한 이름이 있을 경우 건너 뜀
            f.mkdir();
            
        }
        fileList();
        /* ToDo: 먼저 addFiles() 함수의 내용을 복사해서 삽입하라.
        count를 수정하고 아래 실행 결과를 참고하여 생성할 디렉토리 이름을 적절하게 지정하라. 
        try catch 문장 전체를 삭제하고 대신 그 자리에 디렉토리를 생성하라.
        */
    }
    void deleteAll() { // menu item 6: 모든 파일 및 빈 디렉터리들을 삭제함
        var dir = new File(HOME_DIR);
        File files[] = dir.listFiles();
        if (files == null) return ; // HOME_DIR이 존재하지 않을 경우 return
        for (int i=0; i < files.length; i++) {
        	files[i].delete();
        }
        //System.out.println("-----------------");
        fileList();
        /* ToDo: 먼저 fileList() 함수의 내용을 복사해서 삽입하라.
        for 문 내에서 
            files[i]의 파일을 삭제한다. 
            나머지 문장들은 모두 삭제한다.
        파일 목록을 보여 준다.
        */
    }
    void copyFile(InputStream in, OutputStream out) throws IOException {
    	
    	byte [] buf = new byte [1024*8];
    	for(int n; (n = in.read(buf)) > 0; )
    		out.write(buf, 0, n);
        /* ToDo
        교제 예제 8-11을 참고하여 (강의노트의 짧게 변형시킨 코드를 사용하면 더 좋음)
        in 파일의 내용을 모두 읽어 out 파일에 기록하는 코드를 삽입하라.
        단, buf의 크기는 8KB로 하고, 
        매개변수인 in, out을 이미 열려진 FileInputStream, FileOutputStream이라 
        생각하고, in, out을 사용하여 파일 내용만 처음부터 끝까지 복사하라.
        Stream을 새로 생성하지도 말고 close() 하지도 마라. 어떤 출력도 하지 마라.
        try catch도 여기서 처리하지 마라.
        */
    }
    void show() throws IOException { // menu item 7: show file content: 8_2
    	File src = selectFile("", "display", true);
    	if(src == null)
    		return;
    	FileInputStream fi = new FileInputStream(src);
        System.out.println("-----------------");
    	copyFile(fi, System.out);
        System.out.println("-----------------");
    	fi.close();
        /* ToDo
        selectFile()을 호출하여 console에 출력할 File 객체 src를 구한다.
        사용자가 파일 선택을 취소한 경우 리턴한다.
        src 파일에 대한 FileInputStream 객체 fi를 생성한다.
        copyFile(fi, System.out)을 호출한다. 즉, fi 파일을 console(System.out)에 복사한다.
        fi를 닫는다.
        */
        // 위와 같이 copyFile(fi, System.out)에서 out.write()를 이용하여 파일 내용을 
        // console에 출력한다. 즉, System.out에 print() println() 뿐만 아니라
        // write(buf)를 이용해서도 text 파일 내용을 출력할 수 있다는 것을 의미한다. 
    }
    void copy() throws IOException { // menu item 8: file copy: 8_2
    	File src = selectFile("source", "copy", true);
    	if(src == null)
    		return;
    	String target = UI.getNext("target file name? ");
    	var dst = new File(src.getParent() + "/"+ target);
    	
    	FileInputStream fi = new FileInputStream(src);
    	FileOutputStream fo = new FileOutputStream(dst);
    	copyFile(fi, fo);
    	fi.close();
    	fo.close();
    	fileList();
    	
        /* ToDo
        selectFile()을 호출하여 복사할 원본 File 객체 src를 구한다.
        사용자가 파일 선택을 취소한 경우 리턴한다.
        UI의 함수를 이용하여 "target file name? " 을 출력하고 target 파일이름(한 단어)을 입력 받는다.
        입력 받은 이름 target을 이용하여 File 객체 dst을 생성한다.
        (이때 디렉터리는 src의 부모 디렉터리(src.getParent())를 얻어와 
         동일하게 설정해 주어야 한다. rename() 함수 참조)
        src 파일에 대한 FileInputStream 객체 fi를 생성한다.
        dst 파일에 대한 FileOutputStream 객체 fo를 생성한다.
        copyFile()을 호출하여 파일 내용을 복사한다.
        열러진 파일들을 닫는다.
        파일 목록을 보여 준다.
        */
    }
    void append() throws IOException { // menu item 8: file copy: 8_2
    	File src = selectFile("source", "append", true);
    	if(src == null)
    		return ;
    	File dst = selectFile("target", "append", true);
    	if(dst == null)
    		return ;
    	FileInputStream fi = new FileInputStream(src);
    	FileOutputStream fo = new FileOutputStream(dst, true);
    	copyFile(fi, fo);
    	fi.close();
    	fo.close();
    	fileList();
        /* ToDo: 위 copy() 내용을 복사해 온 후 아래 내용을 참고하여 수정하라.
        selectFile()을 호출하여 원본 File 객체 src를 구한다.
        사용자가 파일 선택을 취소한 경우 리턴한다.
        selectFile()을 호출하여 원본 파일을 추가할 타켓 File 객체 dst를 구한다.
        사용자가 파일 선택을 취소한 경우 리턴한다.
        src 파일에 대한 FileInputStream 객체 fi를 생성한다.
        dst 파일에 대한 FileOutputStream 객체 fo를 생성하되, 
            항상 파일 끝에 데이타를 추가할 수 있게 옵션을 설정해 준다.
        copyFile()을 호출하여 파일 내용을 복사한다. 그러면 자동으로 dst의 뒤에 src 내용이 추가된다.
        열러진 파일들을 닫는다.
        파일 목록을 보여 준다.
        */
    }
    void display() {  // menu item 10: 8_2
    	PersonManager.display(list);
        System.out.println("-----------------");
        /* ToDo: 실행 결과처럼 list의 원소의 개수와 " entries"를 출력한다. */
        System.out.println(list.size() + " entries");
    	
    }
    void clear() {  // menu item 11: 8_2
    	list.clear();
    	display();
    }
    void add() { // menu item 12: 8_2
    	for (int i = 0; i<ADD_SIZE; ++i)
    		list.add(getNewPerson());
    	display();
    }
    void saveTextFile(String pathName) throws IOException { // 8_3
    	PrintStream fout = new PrintStream(pathName);
    	for(var t : list) {
    		fout.println(t.getDelimChar()+" "+t);
    	}
    	fout.close();
    	fileList();
        /* ToDo: 강의노트 "텍스트 파일에 println()으로 쓰기" 부분을 참고하여
        pathName을 주고 PrintStream 객체 fout을 생성한다.
        for-each 문을 이용하여 list의 각각의 원소 t에 대해
            fout.println(t.getDelimChar()+" "+t)를 호출한다.
            // 위 문장은 PersonManager::display(list)에서 
            // System.out.println()을 이용하여 각 객체를 콘솔에 출력하는 것과 비슷함
            // 즉, 궁극적으로 각 객체의 toString()을 호출하여 출력됨
            // System.out도 결국은 PrintStream 객체임
        파일을 닫는다.
        data 디렉터리의 모든 파일들의 목록을 보여 준다.
        */ 
    }
    void saveText() throws IOException { // menu item 13: 8_3
    	saveTextFile(TEXT_PATH_NAME);
    }
    void loadTextFile(String pathName) throws IOException {  // 8_3
    	FileInputStream fin = new FileInputStream(pathName);
    	Scanner sc = new Scanner(fin);
    	list.clear();
        boolean saved_echo_input = UI.echo_input;
        UI.echo_input = false;  // 자동오류체크시 파일에서 입력될 경우 출력하지 않도록 함
        while (true) {
            try {
            	Person p = Factory.inputPerson(sc);
            	if(p != null)
            		list.add(p);
            }
            catch (NoSuchElementException e) { break; }
            // 스캐너로 파일을 끝까지 다 읽었는데 또 읽으면 위 예외가 발생함; 
            // 중요: 스캐너를 통해 파일을 읽을 때는 이걸 통해 읽기를 종료해야 함
            catch (Exception e) { System.out.println("scanner error: "+e); }
        }
        UI.echo_input = saved_echo_input;
        sc.close();
        display();
    }

    void loadText() throws IOException { // menu item 14: 8_3
    	loadTextFile(TEXT_PATH_NAME);
    }
    String getNewFilePath(String msg) {  // 8_3
    	fileList();
    	String fileName = UI.getNext("new "+msg+" file name to save? ");
    	var f = new File(HOME_DIR + "/" + fileName);
    	if (f.exists()) { System.out.println(fileName + ": already exists"); return null;}
    	return f.getPath();
    	
        /* ToDo
        파일의 목록을 보여 주어라.
        UI의 적절한 함수를 호출하여 "new "+msg+" file name to save? " 보여 주고
        새로운 파일 이름 fileName을 한 단어로 입력 받아라.
        HOME_DIR과 fileName을 이용하여 File 객체 f를 생성하라. (addFiles() 참고)
        파일이 이미 존재하는 파일이라면 
            파일 이름과 ": already exists"를 출력한 후 null을 반환하라.
        객체 f의 path를 리턴하라.
        */
    }
    void saveTextAs() throws IOException { // menu item 15: 8_3
    	String fileName = getNewFilePath("text");
    	if(fileName != null)
    		saveTextFile(fileName);
    }
    void loadTextFrom() throws IOException { // menu item 16: 8_3
    	File f = selectFile("text", "load", false);
    	if(f != null)
    		loadTextFile(f.getPath());
    	else
    		return;
    }
}  // ch8_1: FileManager class

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