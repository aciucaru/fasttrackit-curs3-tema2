package ro.fasttrackit.curs3tema2.gym;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// clasa utilitara ce contine metode ce lucreaza cu Gym, metode ce nu are logica sa fie incluse in clasa Gym
// de aceea s-a facut aceasta clasa separata, cu metode statice utilitare

public class GymUtil
{
	// variabila ce numara cate fisiere au fost generate, ca numele fisierelor sa fie mereu diferite chiar si in aceeasi zi
	private static int generatedFilesCount = 0;
	
	// aceasta metoda printeaza toti membrii inregistrati la sala de fitness
	public static void listMembers(Gym gym)
	{
		if(gym != null)
		{
			List<GymMember> membersArray = gym.getMembersArray();
			for(GymMember member : membersArray)
			{
				System.out.println(member.toString());
			}
		}
	}
	
	// aceasta metoda printeaza toti membrii inregistrati la sala de fitness, grupati dupa timpul ramas in abonament
	public static void generateReport(Gym gym)
	{
		// observatie: se folosesc secunde in loc de ore, caci pentru testare orele trec greu pana se vede o schimbare,
		// iar durata unei sesiuni este calculata automat intre inceputul si sfarsitul unei sesiuni, deci trebuie sa treaca
		// timp suficient pt. a putea vedea o schimbare
		
		if(gym != null)
		{
			List<GymMember> membersArray = gym.getMembersArray();
			
			List<String> redListNames = membersArray
										.stream()
										.filter((GymMember member) -> member.getSubscriptionDuration().toSeconds() < 100)
										.map(GymMember::getName)
										.collect(Collectors.toList());
			
			List<String> yellowListNames = membersArray
										.stream()
										.filter((GymMember member) -> member.getSubscriptionDuration().toSeconds() >= 100)
										.filter((GymMember member) -> member.getSubscriptionDuration().toSeconds() < 300)
										.map(GymMember::getName)
										.collect(Collectors.toList());
			
			List<String> greenListNames = membersArray
										.stream()
										.filter((GymMember member) -> member.getSubscriptionDuration().toSeconds() >= 300)
										.map(GymMember::getName)
										.collect(Collectors.toList());
			
			String membersReport = "RED: " + redListNames.toString() + "\n" +
									"YELlOW: " + yellowListNames.toString() + "\n" +
									"GREEN: " + greenListNames.toString() + "\n";
			
			List<String> membersReportLineByLine = Arrays.asList(membersReport.split("\n"));
			
			// se printeaza report-ul si in consola
			System.out.print(membersReport);
			
			// se determina data la care se genereaza report-ul si se creeaza un String pe baza acestei date
			LocalDate currentDate= LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String currentDateFormatted = currentDate.format(formatter);
			
			// se genereaza numele final al fisierului in care va fi pus reportul
			// la fiecare generare se va creea un fisier .txt nou
			String reportFileName = "remaining-time-report-" + currentDateFormatted + "_" + generatedFilesCount + ".txt";
			
			// se creeaza un obiect de tip file, fisierul efectiv inca nu a fost creat
			File newFile = new File(reportFileName);
			
			// se incearca creearea fisierului pe hardisk
			boolean fileCreated = false;
			try { fileCreated = newFile.createNewFile(); }
			catch(IOException excep) { System.out.println("erorr: report file could not be created."); }
			
			// se incearca scrierea report-ului in fisierul nou creat
			if(fileCreated == true)
			{
				try
				{
				    Path path = Files.write(Paths.get(reportFileName), membersReportLineByLine, StandardCharsets.UTF_8);
				    
				    // se poate si ca in varianta de mai jos
//				    Files.write(Paths.get(reportFileName), membersReport.getBytes(), StandardOpenOption.APPEND);
				    
				    System.out.println("report written successfully at " + path.toString());
				}
				catch(IOException excep) { System.out.println("error: file was found, but could not write to file."); }
				
				// pentru ca fisierul s-a creat garantat, se incrementeaza numarul de fisiere, ca urmatorul fisier sa aiba
				// alt nume (alt sufix); in acest fel, toate fisierele generate vor avea nume diferite
				generatedFilesCount++;
			}
		}
	}
}
