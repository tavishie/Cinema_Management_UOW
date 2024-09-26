import java.util.InputMismatchException;
import java.util.Scanner;

public class CinemaManagement {
    private static final int ROWS = 3;
    private static final int SEATS = 16;
    private static final int[] PRICES = {12, 10, 8}; // Prices for each row
    private static int[][] seats = new int[ROWS][SEATS];
    private static Ticket[] tickets = new Ticket[ROWS * SEATS];
    private static int ticketCount = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nHi, Welcome to The London Lumiere");
        while (true) {
            displayMenu();

            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        buy_ticket(scanner);
                        break;
                    case 2:
                        cancel_ticket(scanner);
                        break;
                    case 3:
                        print_seating_area();
                        break;
                    case 4:
                        find_first_available();
                        break;
                    case 5:
                        print_tickets_info();
                        break;
                    case 6:
                        search_ticket(scanner);
                        break;
                    case 7:
                        sort_tickets();
                        break;
                    case 8:
                        System.out.println("Exiting program.");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (InputMismatchException inputMismatchException) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }

    private static void displayMenu() {
        System.out.format("""

                1. Buy a ticket
                2. Cancel a ticket
                3. Print seating plan
                4. Find first available seat
                5. Print tickets information
                6. Search for a ticket
                7. Sort tickets by price
                8. Exit

                Please select an option: """);
    }

    private static void buy_ticket(Scanner scanner) {
        int row = getValidSeatInput(scanner, "Enter row number (1-3): ", ROWS) - 1;
        int seat = getValidSeatInput(scanner, "Enter seat number (1-16): ", SEATS) - 1;

        if (seats[row][seat] == 0) {
            scanner.nextLine();
            String name = getValidName(scanner, "Enter your name: ");
            String surname = getValidName(scanner, "Enter your surname: ");
            String email = getValidEmail(scanner, "Enter your email: ");

            Person person = new Person(name, surname, email);
            int price = PRICES[row];
            Ticket ticket = new Ticket(row, seat, price, person);

            seats[row][seat] = 1;
            tickets[ticketCount++] = ticket;
            System.out.println("The seat has been booked");
        } else {
            System.out.println("Seat already booked.");
        }
    }

    private static String getValidName(Scanner scanner, String prompt) {
        String name;
        while (true) {
            System.out.print(prompt);
            name = scanner.nextLine().trim();
            if (name.matches("[a-zA-Z]+")) {
                return name;
            } else {
                System.out.println("Invalid name. Please enter letters only.");
            }
        }
    }

    private static String getValidEmail(Scanner scanner, String prompt) {
        String email;
        while (true) {
            System.out.print(prompt);
            email = scanner.nextLine().trim();
            if (email.contains("@")) {
                return email;
            } else {
                System.out.println("Invalid email. Please enter a valid email address.");
            }
        }
    }

    private static void cancel_ticket(Scanner scanner) {
        int row = getValidSeatInput(scanner, "Enter row number (1-3): ", ROWS) - 1;
        int seat = getValidSeatInput(scanner, "Enter seat number (1-16): ", SEATS) - 1;

        if (seats[row][seat] == 1) {
            seats[row][seat] = 0;
            for (int i = 0; i < ticketCount; i++) {
                if (tickets[i].getRow() == row && tickets[i].getSeat() == seat) {
                    tickets[i] = tickets[--ticketCount]; // Remove the ticket
                    tickets[ticketCount] = null; // Nullify the last element
                    System.out.println("The seat has been cancelled");
                    return;
                }
            }
        } else {
            System.out.println("This seat is already available");
        }
    }

    private static void print_seating_area() {
        System.out.println("\nSeating area (O = available, X = sold):");

        System.out.format("""

                *************************
                *        SCREEN         *
                *************************

                """);

        for (int row = 0; row < ROWS; row++) {
            for (int seat = 0; seat < SEATS; seat++) {
                if (seat == 8) System.out.print("  "); // Gap between seats 8 and 9
                System.out.print(seats[row][seat] == 0 ? 'O' : 'X');
            }
            System.out.println("\t" + "($" + PRICES[row] + ")");
        }
    }

    private static void find_first_available() {
        for (int row = 0; row < ROWS; row++) {
            for (int seat = 0; seat < SEATS; seat++) {
                if (seats[row][seat] == 0) {
                    System.out.println("First available seat: Row " + (row + 1) + ", Seat " + (seat + 1));
                    return;
                }
            }
        }
        System.out.println("No available seats");
    }

    private static void print_tickets_info() {
        int total = 0;
        for (int i = 0; i < ticketCount; i++) {
            tickets[i].printTicketInfo();
            total += tickets[i].getPrice();
        }
        System.out.println("Total price of tickets sold: $" + total);
    }

    private static void search_ticket(Scanner scanner) {
        System.out.println("Enter row number (1-5): ");
        int row = scanner.nextInt() - 1;
        System.out.println("Enter seat number (1-16): ");
        int seat = scanner.nextInt() - 1;

        if (isValidSeat(row, seat) && seats[row][seat] == 1) {
            for (int i = 0; i < ticketCount; i++) {
                if (tickets[i].getRow() == row && tickets[i].getSeat() == seat) {
                    tickets[i].printTicketInfo();
                    return;
                }
            }
        } else if (isValidSeat(row, seat) && seats[row][seat] == 0) {
            System.out.println("This seat is available");
        } else {
            System.out.println("Invalid seat number");
        }
    }

    private static void sort_tickets() {
        for (int i = 0; i < ticketCount - 1; i++) {
            for (int j = 0; j < ticketCount - i - 1; j++) {
                if (tickets[j].getPrice() > tickets[j + 1].getPrice()) {
                    Ticket temp = tickets[j];
                    tickets[j] = tickets[j + 1];
                    tickets[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < ticketCount; i++) {
            tickets[i].printTicketInfo();
        }
    }

    private static boolean isValidSeat(int row, int seat) {
        return row >= 0 && row < ROWS && seat >= 0 && seat < SEATS;
    }

    private static int getValidSeatInput(Scanner scanner, String prompt, int max) {
        int input = -1;
        boolean valid = false;
        while (!valid) {
            System.out.print(prompt);
            try {
                input = scanner.nextInt();
                if (input >= 1 && input <= max) {
                    valid = true;
                } else {
                    System.out.println("Invalid seat number. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.next(); // Clear the invalid input
            }
        }
        return input;
    }
}
