// CH2: Main.java
//
//  Created on: 2024. 9. 5.
//      Author: Junha Kim
//
//  1) 입출력 함수 호출 연습
//  2) while, switch 문장, operator
//  3) User Interface 용 함수들 구현
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
        //int chk = 0; if (chk != 0) new AutoCheck(chk, true).run(); else

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
                "******** Main Menu ********\n" +
                "* 0.exit 1.JavaBasic(ch2) *\n" +
                "***************************\n";

        // 위 메뉴(menuStr)를 화면에 출력하고 메뉴 항목(정수 값)을 입력 받는다.
        final int MENU_COUNT = 2;
        int menuItem;
        while((menuItem = UI.selectMenu(menuStr, MENU_COUNT)) != 0) {
        	switch(menuItem) {
        	case 1:	
        		Ch2.run();
        		break;
        	}
        }
        //int menuItem = UI.selectMenu(menuStr, MENU_COUNT);
        // TODO: 입력 받은 memnuItem 값을 프로그램 실행결과처럼 출력하라. 
        //       예)menu item: 1
        //System.out.print("menu item: " + menuItem);

        System.out.println("\nGood bye!!");
    }
} // class MainMenu

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
    	System.out.print(msg);
    	int value = scan.nextInt();
    	
    	
        if (echo_input) System.out.println(value); // 자동오류체크 시 입력 값을 출력함
        // 위 문장은 자동오류체크 시에 사용되는 문장임; 일반적으로 키보드로부터 입력받을 경우 
        // 화면에 자동 echo되지만, 자동오류체크 시에는 입력파일에서 입력받은 값이 자동 echo 되지  
        // 않으므로 명시적으로 출력 버퍼에 출력(echo) 해 주어야 한다.

        // 나중에 [문제 2-3-1] 해결할 때 구현하시오. 
        // 입력 버퍼에 남아 있는 '\n'를 제거하지 않으면 다음번 getNextLine() 함수 호출 시 
        // 버퍼에 남아 있는 '\n'로 인해 빈 줄이 입력되므로 입력 버퍼에 남아 있는 '\n'를 
        // 사전에 제거해야 함; 즉 scan.nextLine()을 호출하라.
        scan.nextLine();
        return value; // TODO: 입력 받은 정수 value를 리턴할 것 
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
        return index; // TODO: 입력 받은 값 리턴
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
        // TODO: msg를 화면에 출력한 후 하나의 토큰(단어)을 입력 받아 변수 token에 저장함
    	System.out.print(msg);
    	String token = UI.scan.next();
        if (echo_input) System.out.println(token); // 자동오류체크 시 입력 값을 출력함

        // 나중에 [문제 2-3-1] 해결할 때 입력 버퍼에 남아 있는 '\n'를 제거하라.
        scan.nextLine();
        return token;  // TODO: 입력 받은 한 단어를 리턴할 것
    }

    // msg를 화면에 출력한 후 하나의 행 전체를 입력 받아 리턴함
    public static String getNextLine(String msg) { 
        // TODO: msg를 화면에 출력한 후 한 행 전체를 입력 받아 변수 line에 저장함
    	System.out.print(msg);
        String line = UI.scan.nextLine();
    	if (echo_input) System.out.println(line); // 자동체크 시 출력됨
        return line;  // TODO: 입력 받은 한 행 전체를 리턴할 것
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