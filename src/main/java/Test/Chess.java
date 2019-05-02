package Test;

public class Chess {
    public static void main(String[] args) {
        for (int z = 0; z < 4; z++) {
            for (int i = 0; i < 8; i++) {
                if (i % 2 == 0) {
                    System.out.print(1);
                } else
                    System.out.print(0);
            }
            System.out.println();
            for (int i = 0; i < 8; i++) {
                if (i % 2 == 0) {
                    System.out.print(0);
                } else
                    System.out.print(1);
            }
            System.out.println();
        }
    }
}
