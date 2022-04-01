package 流程控制;

/**
 * @author shengwenzhu
 * @date 2022/4/1 0:40
 */
public class ProcessControl {

    public static void main(String[] args) {
        switchTest();
    }

    /**
     * if-else语句示例
     */
    public static void ifTest() {
        int grade = 80;
        if (grade < 60) {
            System.out.println("不及格");
        } else if (grade < 80) {
            System.out.println("及格");
        } else if (grade < 90) {
            System.out.println("良好");
        } else {
            System.out.println("优秀");
        }
    }

    /**
     * switch语句示例
     */
    public static void switchTest() {
        String str = "good";
        switch (str) {
            case "fail": {
                System.out.println("不及格");
                break;
            }
            case "pass": {
                System.out.println("及格");
                break;
            }
            case "good": {
                System.out.println("良好");
                break;
            }
            default: {
                System.out.println("优秀");
            }
        }
    }

}
