package scheduler;
import java.util.InputMismatchException;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Scanner;

// Emergency Room Scheduler that prioritizes patient care based on urgency and arrival time
// Contains a priority queue for quick retrieval of the most urgent patient and a hash table for efficient lookups.
public class EmergencyRoomScheduler {

    //instance variables // fields
    private PriorityQueue<Patient> patientPQueue; // Handle Patient Scheduling by urgency
    private int m = 101; // size of hash table
    private HashMap<Integer, Patient> patientTable; // HashTable stores patient details for quick access


    //Constructor: Initializes an empty ER scheduler with a priority queue and hash table for storing patient details.
    public EmergencyRoomScheduler(){
        patientPQueue = new PriorityQueue<>();
        patientTable = new HashMap<>(m);  // Initialize hash table with size `m`
    }


    // Hash Value for location in hash table
    private int hash(Patient patient){
        return (patient.hashCode()& 0x7fffffff) % m;
    }

    //adds patient to HashTable for quick info lookups + PQ:
    public void addPatient(Patient patient){
        if (patient == null){
            System.out.println("Cannot add null patient");
            return;
        }
        patientPQueue.add(patient);
        patientTable.put(hash(patient), patient);
        System.out.println("Patient Added:\n" + patient);
    }


    // Treats then removes highest priority patient from the Scheduler.
    public void treatCurrentPatient(){
        if (!patientPQueue.isEmpty()){
            Patient patient = patientPQueue.remove(); //removes current pt from queue
            patientTable.remove(hash(patient));
            System.out.println("Currently Treating: \n" + patient.toString());
        }
        else {System.out.println("No patients currently requiring treatment");}
    }

    // Displays a summary of the current waiting list, Next Pt to be treated at the top. Rest Unordered.
    private void viewPQueue(){
        if (patientPQueue.isEmpty()){
            System.out.println("Wait List Is Currently Empty.");
        }
        else {
            System.out.println("Current Waiting List:");
            for (Patient patient : patientPQueue){
                System.out.println("Name: " + patient.getName() +"\n"+
                        "Priority Level:" + patient.getPriorityLevel() +"\n"+
                        "Treatment:" + patient.getTreatmentDescription() +"\n");
            }
        }
    }


    // Retrieves and displays all patient details based on SSN for quick lookup.
    // Most Effective Use of HashTable
    // Need to create temp patient object with same hash location criteria to be considered equal
    public void viewPatientDetails(String name, int ssn) {
        Patient patientObject = new Patient(name, ssn, "", "", "", 0, 0, "");
        int hashKey = hash(patientObject);
        Patient wantedPatient = patientTable.get(hashKey);

        if (wantedPatient == null) {
            System.out.println("No patient found with name: " + name + " and SSN " + ssn +" Combination. Please review");
        } else {
            System.out.println(wantedPatient.toString());
        }
    }




    // Main interactive CLI for the ER Scheduler, allowing users to add patients, treat patients, view the waiting list, or exit.
    // Each input option corresponds to a specific scheduler action, providing input validation where necessary.
    // Note: For buffer performance issue: code requires //  scanner.nextLine() after all scanner.readInt(); to remove lagging newline chars
    public void runCommandLineInterface() {
        Scanner scanner = new Scanner(System.in); // creates scanner for input

        while (true) {
            System.out.println("Welcome to our ER Scheduler: Select Option Below (1 - 5)");
            System.out.println("1: Add New Patient");
            System.out.println("2: Treat Next Patient");
            System.out.println("3: View Current Waiting List");
            System.out.println("4: View A Patient's Details");
            System.out.println("5: Exit Program");

            // each case represents a choice between 1 and 5
            int selection = scanner.nextInt();
            scanner.nextLine();

            switch (selection) {
                // Case 1: Option 1: Add New Pt
                case 1:
                    System.out.println("Enter Patient First and Last Name:");
                    String name = scanner.nextLine();

                    // Validation: SSN
                    int ssn;
                    while (true) {
                        System.out.println("Please enter 9 digit SSN: ");
                        try {
                            int trySsn = scanner.nextInt();
                            scanner.nextLine(); //consumes leftover newline

                            //Makes sure its proper input of exactly 9 digits
                            if (trySsn < 100000000 || trySsn > 999999999) {
                                throw new InputMismatchException();
                            }

                            ssn = trySsn;
                            break;

                        } catch (InputMismatchException e) {
                            System.out.println("Invalid SSN. Please enter exactly 9 digits. Press enter to try again.");
                            scanner.nextLine();
                        }
                    }

                    System.out.println("Enter Date of Birth");
                    String dateOfBirth = scanner.nextLine();

                    System.out.println("Enter Address:");
                    String address = scanner.nextLine();

                    System.out.println("Enter Phone Number: ");
                    String phoneNumber = scanner.nextLine();


                    // Validation: Priority level within range 1-3
                    int priorityLevel;
                    while (true) {
                        System.out.println("Enter Priority Level (1: HIGH, 2: MEDIUM, 3: LOW):");
                        try {
                            int tryPriorityLevel = scanner.nextInt();
                            scanner.nextLine();

                            if (tryPriorityLevel < 1 || tryPriorityLevel > 3) {
                                throw new InputMismatchException();
                            }
                            priorityLevel = tryPriorityLevel;
                            break;

                        } catch (InputMismatchException e) {
                            System.out.println("Invalid Priority Level: Must be Numbers 1, 2, or 3. Press enter to try again.");
                            scanner.nextLine();
                        }
                    }

                    // Validation: Arrival Time in correct military format (1-2359)
                    System.out.println("Enter Arrival Time in Military Format (Ex: 1350 for 1:50 PM)");
                    System.out.println("Input must be between 1 and 2359. Do not lead with zeros.");
                    int arrivalTime;
                    while(true) {
                        try {
                            int tryArrivalTime = scanner.nextInt();
                            scanner.nextLine();

                            if (tryArrivalTime < 1 || tryArrivalTime > 2359) {
                                throw new InputMismatchException();
                            }
                            arrivalTime = tryArrivalTime;
                            break;

                        } catch (InputMismatchException e) {
                            System.out.println("Invalid Arrival time. Must be between 1 and 2359. Press enter to try again.");
                            scanner.nextLine();
                        }
                    }


                    System.out.println("Enter Treatment Description:");
                    String treatmentDescription = scanner.nextLine();

                    Patient newPatient = new Patient(name, ssn, dateOfBirth, address, phoneNumber, priorityLevel, arrivalTime, treatmentDescription);
                    addPatient(newPatient);
                    break;


                case 2:
                    // Treats the highest priority patient (Priority Lvl + earlier arrival time for ties)
                    treatCurrentPatient();
                    break;


                case 3:
                    // Displays the current waiting list, Most Urgent at top. Other pts unordered.
                    viewPQueue();
                    break;


                case 4:
                    // View specific patient details by name and SSN
                    System.out.println("Enter Patient Name:");
                    String enteredName = scanner.nextLine();

                    int enteredSSN;
                    while (true) {
                        System.out.println("Please enter 9 digit SSN: ");
                        try {
                            int trySsn = scanner.nextInt();
                            scanner.nextLine(); //consumes leftover newline

                            //Makes sure its proper input of exactly 9 digits
                            if (trySsn < 100000000 || trySsn > 999999999) {
                                throw new InputMismatchException();
                            }

                            enteredSSN = trySsn;
                            break;

                        } catch (InputMismatchException e) {
                            System.out.println("Invalid SSN. Please enter exactly 9 digits. Press enter to try again.");
                            scanner.nextLine();
                        }
                    }

                    viewPatientDetails(enteredName, enteredSSN);
                    break;

                case 5:
                    System.out.println("Exiting ER Scheduler. Bye!");
                    return;

               default:
                    System.out.println("Invalid Option. Choose a digit between 1 and 5.");
            }
        }
    }




    // ***************************************************************************************************************************************
    //TESTING METHODS BELOW:
    // ***************************************************************************************************************************************

    public static void main(String[] args){
        EmergencyRoomScheduler testSchedule = new EmergencyRoomScheduler();
        testSchedule.runCommandLineInterface();
        //testAddPatient(testSchedule);
        //testTreatCurrentPatient(testSchedule);
        //testViewPQueue(testSchedule);
        //testViewPatientDetails(testSchedule);

    }


    public static void testAddPatient(EmergencyRoomScheduler testSchedule){
        //standard case test:
        Patient testPatient1 = new Patient("Dog", 123456789,"1/2/2000","777 Main Street", "444-999-6789", 3,1420, " Broke both arms and both legs");
        testSchedule.addPatient(testPatient1);

        //Pt 1: did test patient make it to queue? Marks yes if true
        boolean patientInPQ = false;
        for (Patient patient: testSchedule.patientPQueue) {
            if (patient.equals(testPatient1)) {
                patientInPQ = true;
                break;
            }
        }
        if (patientInPQ) {System.out.println("Test Passed: Pt added to PQ");}
        else{System.out.println("Test Failed: Pt Not added");}

        // Pt 2: Did pt make it into hashMap?
        int ptHashKey = testSchedule.hash(testPatient1);
        if (testSchedule.patientTable.containsKey(ptHashKey)){
            System.out.println("Test Passed: Pt was added to HashTable ");
        }
        else {System.out.println("Test Failed: Pt was not added");}

        //Edge case Null:
        System.out.println("Edge Case: Trying To Add Null Pt After Adding Dog");
        testSchedule.addPatient(null);
        if (testSchedule.patientTable.size() == 1) {
            System.out.println("Test Passed: Adding Null Patient Did not alter table");
        }
        else{
            System.out.println("Test Failed: Adding Null Pt Altered table Size");
            System.out.println("Unexpected Table Size: " + testSchedule.patientTable.size());
        }
    }



    public static void testTreatCurrentPatient(EmergencyRoomScheduler testSchedule){
        Patient testPatientGreen = new Patient("Green", 777777777, "4/4/2000", "2000 Water Way Lane", "888-555-2020", 3,805, "Flu Like Symptoms");
        Patient testPatientYellow = new Patient("Yellow",999999999,"2/2/2000","7000 Fox Cove","444-444-4444", 2, 807, "Crushed Right Arm: Pain Level High");
        Patient testPatientRed = new Patient("Red", 888888888, "3/3/2000", "9000 Bear Den Drive","444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");

        //Standard Case Testing:
        //Create Waiting List:
        testSchedule.addPatient(testPatientGreen);
        testSchedule.addPatient(testPatientYellow);
        testSchedule.addPatient(testPatientRed);

        System.out.println("Size of Queue Before treatment: " + testSchedule.patientPQueue.size());
        System.out.println("Initial Queue Prior to Treatment:");
        testSchedule.viewPQueue();

        //Now We Treat & Remove Highest Priority:
        testSchedule.treatCurrentPatient();
        int redHashKey = testSchedule.hash(testPatientRed);
        if (testSchedule.patientTable.containsKey(redHashKey)){
            System.out.println("Removal Test Failed: Highest Priority Pt (Red) Not Removed");
        }
        else{System.out.println("Removal Test Passed: Highest Priority Pt (Red) Removed");}

        System.out.println("New Waiting List After Highest Priority Pt Treated (Red)");
        testSchedule.viewPQueue();

        System.out.println("Test Case: Size after removal of pt Red");
        if (testSchedule.patientPQueue.size() == 2){
            System.out.println("Size Test Passed: Size of Patient Priority Queue is 2 after removing treated patient");
        }
        else {
            System.out.println("Size Test Failed: The Size is incorrect. See Incorrect Size Below");
            System.out.println("Incorrect Size of Queue After treatment: " + testSchedule.patientPQueue.size());
        }

        //Edge Case Testing: Attempt to treat pt on an empty queue
        EmergencyRoomScheduler emptySchedule = new EmergencyRoomScheduler();
        System.out.println("Edge Case: Attempting to treat on empty schedule below:");
        emptySchedule.treatCurrentPatient();
        if (emptySchedule.patientPQueue.size() == 0){System.out.println("Test Passed: Treating on empty schedule keeps size at 0");}
        else{System.out.println("Test Failed: Size was altered after treating an empty queue");}
        emptySchedule.viewPQueue();

        //Edge Case Testing: Only One patient in the Queue.
        EmergencyRoomScheduler oneManSchedule = new EmergencyRoomScheduler();
        oneManSchedule.addPatient(testPatientRed);
        System.out.println("Edge Case: Attempting to treat on one man schedule below:");
        oneManSchedule.treatCurrentPatient();
        if(oneManSchedule.patientPQueue.isEmpty()){
            System.out.println("Test Passed: Patient PQ is empty after treating the only person.");
        }
        else{System.out.println("Test Failed: Patient PQ is not empty after treating the only person");}
    }




    public static void testViewPQueue(EmergencyRoomScheduler testSchedule){
        Patient testPatientGreen = new Patient("Green", 777777777, "4/4/2000", "2000 Water Way Lane", "888-555-2020", 3,805, "Flu Like Symptoms");
        Patient testPatientYellow = new Patient("Yellow",999999999,"2/2/2000","7000 Fox Cove","444-444-4444", 2, 807, "Crushed Right Arm: Pain Level High");
        Patient testPatientRed = new Patient("Red", 888888888, "3/3/2000", "9000 Bear Den Drive","444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");

        //add pts to testSchedule
        testSchedule.addPatient(testPatientGreen);
        testSchedule.addPatient(testPatientYellow);
        testSchedule.addPatient(testPatientRed);

        // Standard Case: view them in correct order in Priority Queue.
        System.out.println("\n Stadndard Case Test of Viewing Patient Queue with the 3 patients above in correct order.");
        testSchedule.viewPQueue();

        //Edge Case: Viewing Empty Waiting List / Queue After Treatment
        testSchedule.treatCurrentPatient();
        testSchedule.treatCurrentPatient();
        testSchedule.treatCurrentPatient();
        System.out.println("Edge Case: Viewing Patient Priority Queue After Treating all Patients: Empty list");
        if (testSchedule.patientPQueue.isEmpty()){
            System.out.println("Test Passed. No Error on Empty Queue. Wait List Is Currently Empty Message Received");
        }
        else{System.out.println("Test Failed: Queue Is Not Empty");}

        //Final Edge Case: Viewing Empty Waiting List / Queue with no Patients ever created.
        EmergencyRoomScheduler emptySchedule = new EmergencyRoomScheduler();
        System.out.println("Edge Case 2: Viewing Empty Waiting List / Queue with no Patients ever created:");
        emptySchedule.viewPQueue();
    }



    public static void testViewPatientDetails(EmergencyRoomScheduler testSchedule){
        //Adding Patients to Schedule for testing
        Patient ptA = new Patient("Rick", 111111111, "3/29/2000", "9000 Fox Cove Drive","666-787-5200", 3, 849, "Cough");
        Patient ptB = new Patient("Bob", 222222222, "4/2/2000", "9080 Bear Den Drive","984-234-3920", 1, 1009, "Chest Pains + Shortness of Breath");
        Patient ptC = new Patient("Jeff", 333333333, "9/27/2000", "90 Fish Creek Drive","444-000-3300", 1, 2009, "Chest Pains + Shortness of Breath");
        testSchedule.addPatient(ptA);
        System.out.println();
        testSchedule.addPatient(ptB);
        System.out.println();
        testSchedule.addPatient(ptC);

        testSchedule.viewPQueue();
        System.out.println();

        // Test Case 1: Standard Case - Retrieve details of an existing patient
        System.out.println("Test Case: Retrieving details of existing patient ssn 111111111");
        testSchedule.viewPatientDetails("Rick",111111111);


        // Edge Case: Non-existent SSN
        System.out.println("Edge Case: Retrieving details of non existent patient with Name Frank and SSN 999999999");
        testSchedule.viewPatientDetails("Frank", 999999999);

        //Edge Case: Same SSN but different name
        System.out.println("Edge Case: Retrieving details of correct snn but wrong name");
        testSchedule.viewPatientDetails("Matt", 222222222);



    }
}