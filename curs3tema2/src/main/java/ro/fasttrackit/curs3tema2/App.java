package ro.fasttrackit.curs3tema2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import ro.fasttrackit.curs3tema2.gym.Gym;
import ro.fasttrackit.curs3tema2.gym.GymMember;
import ro.fasttrackit.curs3tema2.gym.GymUtil;

public class App 
{
    private static void showCommands()
    {
        System.out.println("Available commands: ");
        	System.out.println("    listMembers");
            System.out.println("    addMember name:string birthdate:dd-MM-yyyy subscriptionSeconds:int");
            System.out.println("    startMemberSession memberID:int");
            System.out.println("    endMemberSession memberID:int");
            System.out.println("    membersAverageAge");
            System.out.println("    membersMinAge");
            System.out.println("    membersMaxAge");
            System.out.println("    memberAddDuration memberID:int seconds:int");
            System.out.println("    memberInfo name:string");
            System.out.println("    genReport");
            System.out.println("    help");
            System.out.println("    exit");
    }
    
    // metoda utilitara ce verifica daca un string reprezinta un numar
    private static boolean isNumber(String wordToParse)
    {
        boolean isNumber = false;
        
        if(wordToParse != null)
        {
            try
            {  
                Double.parseDouble(wordToParse);
                
                // metoda de mai sus arunca exceptie daca nu este vorba de un numar intreg sau zecimal
                // deci daca s-a ajuns pana aici, inseamna ca nu a fost nici o exceptie, adica stringul este garantat un numar
                isNumber = true;
            }
            catch(NumberFormatException e)
            {  
                // daca s-a ajuns pana aici, inseamna ca acel cuvant nu poate reprezenta un numar
                isNumber = false;  
            }  
        }
        
        return isNumber;
    }
    
    public static void main( String[] args )
    {
        boolean continueToRun = true;
        final Gym gym = new Gym();
        
        // scaneaza o intreaga linie (adica o comanda cu tot cu argumentele ei)
        Scanner inputScanner = new Scanner(System.in);
        
        // scaneaza cuvintele din interiorul unei linii scanate (adica scaneaza, pe rand, comanda si argumentele)
        Scanner commandScanner = null;
        
        System.out.println("This is a gym program");
        showCommands();
        System.out.println();
        
        String currentInputLine = null;
        String currentCommand = null;
        

        while(continueToRun)
        {
            // se reseteaza comenzile la inceputul fiecarei iteratii (noii comenzi)
            currentInputLine = null;
            currentCommand = null;
            
            System.out.print("please enter a command: ");
            // daca s-a introdus o linie de comanda
            if(inputScanner.hasNextLine())
            {
                // se ia intreaga linie introdusa (linia ar trebui sa reprezinte comanda cu tot cu argumentele ei)
            	currentInputLine = inputScanner.nextLine();
                 
            	// se creeaza un Scanner al String-ului ce reprezinta intreaga linie
            	commandScanner = new Scanner(currentInputLine);
                
                // daca linia scanata are macar un cuvant (ce ar putea fi o comanda)
                if(commandScanner.hasNext())
                {
                    // atunci se ia primul cuvant al liniei, ce ar trebui sa fie chiar comanda in sine
                	currentCommand = commandScanner.next();
            
                	// se realizeaza logica specifica
                    switch(currentCommand)
                    {
                        default:
                            System.out.println("no such command, please try again");
                            break;
                            
                        case "listMembers":
                        	GymUtil.listMembers(gym);
                        	break;
                        	
                        case "addMember":
						{
						    // comanda arata asa: addMember name birthdate subscriptionDuration
						    // argumentele au initial valori ce le face sa nu treaca de validare, a.i.
							// sa se stie daca utilizatorul nu a scris comanda corect
						    String name = null;
						    String birthdateString = null;
						    LocalDate birthdate = null;
						    int subscriptionTime = -1;
						    Duration subscriptionDuration = null;
						   
						    // se incearca sa se ia numele
						    if(commandScanner.hasNext()) // daca linia comenzii mai are macar un cuvant
						    {
						        name = commandScanner.next(); // atunci acel cuvant reprezinta numele
						   
							    // totusi, se verifica daca acest cuvant nu este de fapt un numar, adica inacceptabil
							    // pentru un nume de persoana
							    if(isNumber(name)) // daca textul reprezinta un numar
							        name = null; // se reseteaza numele, ca sa se stie ca este gresit
						    }
						    
						    // se incearca sa se ia data nasterii
						    if(commandScanner.hasNext()) // daca linia comenzii mai are macar un cuvant
						    {
						    	birthdateString = commandScanner.next(); // atunci acel cuvant reprezinta numele
						    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
						    	
						    	birthdate = LocalDate.parse(birthdateString, formatter);
						    }
						    
						    // se incearca sa se ia numarul de secunde (secunde in loc de ore, caci orele treg greu
						    // si este greu de testat (deoarece durata unei sesiuni se calculeaza automat)
						    if(commandScanner.hasNextInt()) // daca linia comenzii mai are macar un numar
						    {
						    	subscriptionTime = commandScanner.nextInt(); // atunci acel cuvant reprezinta durata
						    	subscriptionDuration = Duration.ofSeconds(subscriptionTime);
						    }
						    
                            // daca argumentele sunt in regula
                            if(name != null && birthdateString != null && birthdate != null
                            		&& subscriptionTime >= 0 && subscriptionDuration != null)
                            {
                                // atunci se poate creea un obiect de tip GymMember
                                GymMember member = new GymMember(name, birthdate, subscriptionDuration);
                                     
                                // obiectul de tip GymMember se poate adauga la Gym
                                gym.addMember(member);
                            }
                            else
                                System.out.println("error: arguments in 'addMember' command are incorrect"); 
						}
                        	break;
                        	
                        case "startMemberSession":
						{
						    // comanda arata asa: startMemberSession memberID
						    // argumentele au initial valori ce le face sa nu treaca de validare, a.i.
							// sa se stie daca utilizatorul nu a scris comanda corect
						    int id = -1;
						    
						    // se incearca sa se ia id-ul sub forma de numar
						    if(commandScanner.hasNextInt()) // daca linia comenzii mai are macar un numar
						    	id = commandScanner.nextInt(); // atunci acel cuvant reprezinta id-ul
						    
						    // daca argumentele sunt in regula
						    if(id >= 0)
						    	gym.startMemberSession(id);
						    else
						    	System.out.println("error: arguments in 'startMemberSession' command are incorrect"); 
						}
                        	break;
                        	
                        case "endMemberSession":
						{
						    // comanda arata asa: endMemberSession memberID
						    // argumentele au initial valori ce le face sa nu treaca de validare, a.i.
							// sa se stie daca utilizatorul nu a scris comanda corect
						    int id = -1;
						    
						    // se incearca sa se ia id-ul sub forma de numar
						    if(commandScanner.hasNextInt()) // daca linia comenzii mai are macar un numar
						    	id = commandScanner.nextInt(); // atunci acel cuvant reprezinta id-ul
						    
						    // daca argumentele sunt in regula
						    if(id >= 0)
						    	gym.endMemberSession(id);
						    else
						    	System.out.println("error: arguments in 'endMemberSession' command are incorrect"); 
						}
                        	break;
                        	
                        case "membersAverageAge":
                        	System.out.println("average member age: " + gym.membersAverageAge());
                        	break;
                        	
                        case "membersMinAge":
                        	System.out.println("minimum member age: " + gym.membersMinAge());
                        	break;
                        	
                        case "membersMaxAge":
                        	System.out.println("maximum member age: " + gym.membersMaxAge());
                        	break;
                        	
                        case "memberAddDuration":
						{
						    // comanda arata asa: memberAddDuration memberID secunde
						    // argumentele au initial valori ce le face sa nu treaca de validare, a.i.
							// sa se stie daca utilizatorul nu a scris comanda corect
							int id = -1;
							int duration = -1;
							Duration extraDuration = null;
							
						    // se incearca sa se ia id-ul sub forma de numar
						    if(commandScanner.hasNextInt()) // daca linia comenzii mai are macar un numar
						    	id = commandScanner.nextInt(); // atunci acel cuvant reprezinta id-ul
						    
						    // se incearca sa se ia timpul adaugat la abonament, sub forma de numar (secunde)
						    if(commandScanner.hasNextInt()) // daca linia comenzii mai are macar un numar
						    {
						    	duration = commandScanner.nextInt(); // atunci acel cuvant reprezinta nr. de secunde
						    	extraDuration = Duration.ofSeconds(duration);
						    }
						    
						    // daca argumentele sunt in regula
						    if(id >= 0 && duration >= 0 && extraDuration != null)
						    	// atunci se adauga durata suplimentara la abonamentul membrului
						    	gym.addTimeToMember(id, extraDuration);
						    else
						    	System.out.println("error: arguments in 'memberAddDuration' command are incorrect"); 
						}
                        	break;
                        	
                        case "memberInfo":
						{
						    // comanda arata asa: memberInfo name
						    // argumentele au initial valori ce le face sa nu treaca de validare, a.i.
							// sa se stie daca utilizatorul nu a scris comanda corect
							String name = null;
							
						    // se incearca sa se ia numele
						    if(commandScanner.hasNext()) // daca linia comenzii mai are macar un cuvant
						    {
						        name = commandScanner.next(); // atunci acel cuvant reprezinta numele
						   
							    // totusi, se verifica daca acest cuvant nu este de fapt un numar, adica inacceptabil
							    // pentru un nume de persoana
							    if(isNumber(name)) // daca textul reprezinta un numar
							        name = null; // se reseteaza numele, ca sa se stie ca este gresit
						    }
						    
						    // daca argumentele comenzii sunt in regula
						    if(name != null)
						    	// se incearca printarea informatiilor pt. membrul specificat
						    	System.out.println(gym.infoOfMember(name));
						    else
						    	System.out.println("error: arguments in 'memberInfo' command are incorrect");
						}
                        	break;
                        	
                        case "genReport":
                        	GymUtil.generateReport(gym);
                        	break;
                        	
                        case "help":
                        	showCommands();
                        	break;
                        	
                        case "exit":
                        	continueToRun = false;
                        	break;
                    }
                }
            }
        }
    }
}
