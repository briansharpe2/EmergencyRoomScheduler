package scheduler;

// Represents a patient in an Emergency Room Scheduler Scenerio.
// Stores patient information including priority level, arrival time, treatment info, and personal details
// Allows comparison, hashing, and formatting within my scheduler


public class Patient implements Comparable<Patient> {

    //Patient Detail Fields
    private String name; // full name
    private int ssn; //ssn
    private String dateOfBirth; // MM/DD/YYYY Format
    private String address;
    private String phoneNumber;
    private int priorityLevel; // 1- high:red   , 2- medium:yellow  3- low:blue
    private int arrivalTime; // Military Time
    private String treatmentDescription; // Situation Presented onsite, Plan of Action


    // Constructor
    public Patient(String name, int ssn, String dateOfBirth, String address, String phoneNumber, int priorityLevel, int arrivalTime, String treatmentDescription) {
        this.name = name;
        this.ssn = ssn;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.priorityLevel = priorityLevel;
        this.arrivalTime = arrivalTime;
        this.treatmentDescription = treatmentDescription;
    }


    //Getter methods: Allows EmergencyRoomScheduler class to retrieve whatever field needed.
    public String getName() {return name;}
    public int getSsn() {return ssn;}
    public String getDateOfBirth() {return dateOfBirth;}
    public String getAddress() {return address;}
    public String getPhoneNumber() {return phoneNumber;}
    public int getPriorityLevel() {return priorityLevel;}
    public int getArrivalTime() {return arrivalTime;}
    public String getTreatmentDescription() {return treatmentDescription;}


    //Overrides HashCode() for enhanced hashing based on pt's unique identifiers ssn and name.
    @Override
    public int hashCode() {
        int hash = 17; // small non zero constant helps initial hash be more unique // prevent clustering
        hash = 31 * hash + Integer.hashCode(ssn); // adds ssn contribution to hashcode
        hash = 31 * hash + name.hashCode();       // adds pt name's contribution to hashcode
        return hash;                              // final hashcode value. this will go into the hash function.
    }



    // Overrides to compare patients equality based on name and ssn.
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass())
            return false; //checks if obj is null, or obj is a Patient Class type
        if (this == obj) return true;  // this checks to see if they are literally identical objs in memory.
        Patient patient = (Patient) obj; // getclass() tells us they are both Patients, so we cast obj to get pt fields
        return this.ssn == patient.ssn && this.name.equals(patient.name); //checks to see if ssn & name between the 2 are the same values. returns t/f
    }

    // Overriding compareTo to prioritize based on priority level and then arrival time.
    //Critical Method in Ordering of our PriorityQueue / Wait List.
    @Override
    public int compareTo(Patient other) {
        int priorityComparison = Integer.compare(this.priorityLevel, other.priorityLevel);
        if (priorityComparison == 0) {
            return Integer.compare(this.arrivalTime, other.arrivalTime); // If priority levels match, use arrival time
        }
        return priorityComparison;
    }

    @Override
    // Provides readable summary of an individual instance of patient. Good Display setup for patient Info.
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Patient Details: \n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("SSN: ").append(ssn).append("\n");
        sb.append("DOB: ").append(dateOfBirth).append("\n");
        sb.append("Address: ").append(address).append("\n");
        sb.append("Phone Number: ").append(phoneNumber).append("\n");
        sb.append("Priority Level: ").append(priorityLevel).append("\n");
        sb.append("Arrival Time: ").append(arrivalTime).append("\n");
        sb.append("Treatment Description: ").append(treatmentDescription).append("\n");
        return sb.toString();
    }


// ***************************************************************************************************************************************
//TESTING METHODS BELOW:
// ***************************************************************************************************************************************


    public static void main(String[] args) {
        //testEquals();     //Done
        //testHashCode();   //Done
        //testCompareTo();  //Done
        //testToString();   //Done
    }


    //Verifying my Overridden equals method based on SSN and Name Holds true:
    public static void testEquals() {
        Patient testpt1 = new Patient("Red", 888888888, "3/3/2000", "9000 Bear Den Drive", "444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");
        Patient testpt2 = new Patient("Red", 888888888, "3/3/2000", "9000 Bear Den Drive","444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");
        //Case Test: Identical Patient entries
        if (testpt1.equals(testpt2)){System.out.println("Test Passed: Identical patients considered equal");}
        else {System.out.println("Test Failed: Identical Patients are not considered equal.");}

        //Case Test: Different SSN for Red
        Patient testpt3 = new Patient("Red", 555555555, "3/3/2000", "9000 Bear Den Drive", "444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");
        if(!testpt1.equals(testpt3)){System.out.println("Test Passed: Different SSN should call for different patient objects.");}
        else{System.out.println("Test Failed: Different SSN's / DIfferent Patients are considered equal here.");}

        //Case Test: One Patient Equals himself:
        if(testpt1.equals(testpt1)){System.out.println("Test Passed: Patient is equal to themselves");}
        else{System.out.println("Test Failed: Patient is not considered equal to themselves here");}

        //Case Test: Null Compare
        if (!testpt1.equals(null)){System.out.println("Test Passed: Patient Compared to Null Is not equal.");}
        else{System.out.println("Test Failed: The comparison between Patient and Null was considered equal here");}

        //Test Case : Comparing Different Classes
        String fakePatient = "This is a String, not a patient";
        if (!testpt1.equals(fakePatient)){System.out.println("Test Passed: Our Patient object was not considered equal to a string fake patient");}
        else {System.out.println("Test Failed: A String is not a person in the ER!");}
    }



    public static void testHashCode() {
        Patient testpt1 = new Patient("Red", 888888888, "3/3/2000", "9000 Bear Den Drive", "444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");
        int testHashCode1 = testpt1.hashCode();
        int testHashcode2 = testpt1.hashCode();

        //Test Case: One Object
        if (testHashCode1 == testHashcode2) {System.out.println("Test Passed: HashCode Is Consistent for ONE object");}
        else {System.out.println("Test Failed:HashCode is not equal");}

        //Test Case: Two Instances of Same Object
        Patient testpt2 = new Patient("Red", 888888888, "3/3/2000", "9000 Bear Den Drive", "444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");
        if (testpt1.hashCode() == testpt2.hashCode()){System.out.println("Test Passed: Two Instances Of Same Object have Same hashcode");}
        else{System.out.println("Test Failed: Same Objects do not have same HashCode here");}

        //Test Case: Unequal Objects by SSN
        Patient testpt4 = new Patient("Red", 744525687, "3/3/2000", "9000 Bear Den Drive", "444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");
        if (testpt1.hashCode() != testpt4.hashCode()){System.out.println("Test Passed: Different Objects have different hashCodes");}
        else{System.out.println("Test Failed: Different Objects have same hashcode here");}

        //Test Case: Edge Case: VERY similar SSN should still produce different HashCodes()
        Patient testpt5 = new Patient("Red", 744525688, "3/3/2000", "9000 Bear Den Drive", "444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");
        if(testpt4.hashCode() != testpt5.hashCode()){System.out.println("Test Passed: Slightly Different SSN produce different hashCodes");}
        else{System.out.println("Test Failed: Slightly Different SSN Produced the Same HashCode");}

    }



    public static void testCompareTo() {
        //Test Case: Different Priority Levels
        Patient lowerPriorityPt = new Patient("testpt20", 982187888, "3/3/2001", "8700 Bear Den Drive", "444-780-5300", 3, 809, "Chest Pains + Shortness of Breath");
        Patient higherPrioritypt = new Patient("testpt21", 48588877, "3/3/2000", "9000 Bear Den Drive", "444-787-5300", 1, 809, "Chest Pains + Shortness of Breath");
        if (higherPrioritypt.compareTo(lowerPriorityPt) < 0) {
            System.out.println("Test Passed: Higher Priority Pt is considered less than lower priority pt in our MinPQ");
        } else {System.out.println("Test Failed: Higher priority Pt is not considered less than lower priority pt in our MinPQ");}

        //Test Case: Same priority level but different arrival times
        Patient earlierArrivalpt = new Patient("Earlier Arrival", 982447888, "8/10/2001", "8730 Bear Den Drive", "444-780-6600", 3, 1000, "Chest Pains + Shortness of Breath");
        Patient laterArrivalpt = new Patient("Later Arrival", 983387855, "3/8/2011", "8700 Bear Den Drive", "444-780-5300", 3, 1015, "Chest Pains + Shortness of Breath");
        if(earlierArrivalpt.compareTo(laterArrivalpt) < 0 ){System.out.println("Test Passed: Patient with less arrival time + same Priority Level is considered less than");}
        else{ System.out.println("Test Failed: Patient with less arrival time + same priority level IS NOT considered less than here");}


        //Test Case: Same Priority Levels and Same Arrival Times.
        Patient clone1TimeAndPriority = new Patient("Clone1", 272347578, "8/10/2001", "8730 Bear Den Drive", "444-780-6600", 3, 1000, "Chest Pains + Shortness of Breath");
        Patient clone2TimeAndPriority = new Patient("Clone2", 982447888, "8/10/2001", "8730 Fox Den Drive", "444-780-4300", 3, 1000, "Chest Pains + Shortness of Breath");
        if(clone2TimeAndPriority.compareTo(clone2TimeAndPriority) == 0){System.out.println("Test Passed: Clones of time and priority are considered equal in our method");}
        else{System.out.println("Test Failed: Clones of time and priority are NOT considered equal in our compare to Method.");}

        //Test Cases for invalid inputs (negatives or 0's or incorrect lengths) are all handled in the runCommandLineInterface() method
        // As it does not allow the user to enter invalid input.

    }


    //Testing Formating of an instance of a patient
    public static void testToString(){
        Patient testStringPt = new Patient(
                "Golden Retriever",
                123987456,
                "11/1/2000",
                "748 Cherry Lane",
                "555-333-2039",
                1,
                1045,
                "Sad Dog"
        );

        //Expected Formating:
        String expectedOutput = "Patient Details: \n" +
                "Name: " + "Golden Retriever" + "\n" +
                "SSN: " + 123987456 + "\n" +
                "DOB: " + "11/1/2000" + "\n" +
                "Address: " + "748 Cherry Lane" + "\n" +
                "Phone Number: " + "555-333-2039" + "\n" +
                "Priority Level: " + 1 + "\n" +
                "Arrival Time: " + 1045 + "\n" +
                "Treatment Description: " + "Sad Dog";

        //Actual Format with toString();
        String actualOutput = testStringPt.toString();

        //Test TO Compare Format Equality:
        if (expectedOutput.equals(actualOutput)){System.out.println("Test Passed: toString() 's output matches expected");}

        else{System.out.println("Test Failed: toString() output does not match expected format");
            System.out.println("Expected Format: \n" + expectedOutput);
            System.out.println("Actual Format: \n" + actualOutput);
        }
    }



}