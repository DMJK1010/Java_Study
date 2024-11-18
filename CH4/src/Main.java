// CH4: Main.java
//
//  Created on: 2024. 9. 19.
//      Author: Junha Kim
//
//  + CurrentUser, MultiManager, Person 클래스 추가
//  + MainMenu 수정
//

// 문제 1-3까지 완료 1-4 부터 시작 필요

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

        // TODO: System.in을 인자로 하는 Scanner 객체를 생성한 후 생성된 객체를 
        //       아래 run(null) 함수 호출 시 인자로 바로 넘겨 주어라. (null 대신에 넘겨 줄 것)
        //       위 기능을 아래처럼 반드시 하나의 문장으로 완성해야 한다.  
        //       즉, run( Scanner 객체를 생성 ); 형태가 되어야 한다. 
        //       변수 선언하지 말고 바로 넘겨 주어야 한다.
        //  경고: 만약 아래처럼 두 문장으로 작성하면 잘못된 것이다. 절대 이렇게 작성하지 마라. 절대로.
        //       Scanner s = Scanner 객체를 생성;
        //       run(s);
        //       위 처럼 두 개의 문장으로 처리하면 실행 결과가 자동체크시 문제가 발생한다.

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
                "* 2.ch2 3.ch3            *\n" +
                "**************************\n";
        // 위 메뉴(menuStr)를 화면에 출력하고 메뉴 항목(정수 값)을 입력 받는다.
        final int MENU_COUNT = 4;
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
        	}
        }
        System.out.println("\nGood bye!!");
    }
} // class MainMenu

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

 // 생성자 함수들
 public Person(String name) {
	 this(name, 0, 0.0, false, "");
 }
 
 public Person(String name, int id, double weight, boolean married, String address) { 
     set(name, id, weight, married, address);
     println("Person(): ");
 }  
 
 public void println() { 
     print(); System.out.println(); 
 }
  
 public void println(String msg) { 
      System.out.print(msg); print(); System.out.println(); 
 }
 
 // assign() 함수
 public void assign(Person user) {
	 set(user.name, user.id, user.weight, user.married, user.address);
 }
 
 // Getter: getXXX() 관련 함수들
 public String	getName()	{ return name; }
 public int 	getId()		{ return id; }
 public double	getWeight()	{ return weight; }
 public boolean	getMarried(){ return married; }
 public String	getAddress(){ return address; }
  
 // Setter: overloading: set() 함수들 중복
 public void set(String name)			{ this.name = name; }
 public void set(int id)				{ this.id = id; }
 public void set(double weight)			{ this.weight = weight; }
 public void set(boolean married)		{ this.married = married; }
 public void setAddress(String address) { this.address = address; }

 // Candidates for virtual functions and overriding 
 // print(), clone(), whatAreYouDoing(), equals(), input() 함수
 public void print() { 
     System.out.print(name+" "+id+" "+weight+" "+married+" :"+address+":");
 }
 public void input(Scanner s) { 
	 name = s.next();
	 id = s.nextInt();
	 weight = s.nextDouble();
	 married = s.nextBoolean();
     // 아래는 주소 필드를 입력 받는 부분이며 수정없이 그대로 사용하면 된다.
     // :로 시작하고 :로 끝나는 부분의 서브 문자열을 읽어 냄
     while ((address = s.findInLine(":.*:")) == null)
         s.nextLine();
     address = address.substring(1, address.length()-1); 
     // 양쪽의 : :를 제거한 서브 문자열을 넘겨 받음
 }
 
 public void set(String name, int id, double weight, boolean married, String address) {
	 this.name = name;
	 this.id = id;
	 this.weight = weight;
	 this.married = married;
	 this.address = address;
 }

 public Person clone() {
	 System.out.println("Person::clone()");
	 Person tmp = new Person(getName(), getId(), getWeight(), getMarried(), getAddress());
	 return tmp;
 }
 
 public void whatAreYouDoing() {
	 System.out.println(name +" is taking a rest.");
 }
 
 public boolean equals(Person user) {
	 if(this.name.equals(user.name) && (this.id == user.id))
		 return true;
	 else
		 return false;
 }
}

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
    		System.out.print(index + ": OUT of selection range(0 ~ " + (size - 1) + ") Try again!!");
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
         "+ 6.equals 7.update                                             +\n" +
         "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";
     final int MENU_COUNT = 8; // 상수 정의
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
         case 0:                    return;
         }
     }
 }
 void display() { 
     user.println(); 
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
     p.println("p.set(): ");
 }
 void copy() { // Menu item 4
     user.println("user: ");
     var p = user.clone(); // user와 동일한 멤버 값을 가지는 새로운 객체를 생성하여 반환
     p.println("p: ");
 }
 void whatAreYouDoing() {  // Menu item 5
	 user.whatAreYouDoing();
 }
 void equals() { // Menu item 6
     user.println("user: ");
     var p = new Person("user"); p.set(1);
     p.println("p: ");
     System.out.println("p.equals(user): " + p.equals(user));
     p.assign(user);
     p.println("p.assign(user): ");
     System.out.println("p.equals(user): " + p.equals(user));
 }
 void update() { // Menu item 7
     System.out.println("input person information:");
     user.input(UI.scan); // user 100 65 true :426 hakdong-ro, Gangnam-gu, Seoul:
     if (UI.echo_input) user.println(); // 자동오류체크시 출력됨
     display();
 }
} // CurrentUser class: ch4_1

//===============================================================================
//class MultiManager: ch4_1
//===============================================================================
class MultiManager
{
 private Person person = new Person("p0", 0, 70.0, false, "Gwangju Nam-gu 21");

 void run() {
     new CurrentUser(person).run();
 }
} // class MultiManager: ch4_1

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