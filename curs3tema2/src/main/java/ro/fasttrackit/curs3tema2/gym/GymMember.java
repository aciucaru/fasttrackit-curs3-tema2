package ro.fasttrackit.curs3tema2.gym;

import java.time.LocalDate;
import java.time.Month;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GymMember
{
	private int id = 0;
	private String name = "n/a"; // numele membrului
	private LocalDate birthdate = LocalDate.of(1900, Month.JANUARY, 1); // data de nastere
	private Duration subscriptionDuration = Duration.ofSeconds(0); // cate secunde de abonament mai are membrul
	// observatie: se folosesc secunde in loc de ore, deoarece durata unei sesiuni de fitness se calculeaza automat,
	// intre apelarea metodelor startSession(Instant) si endSession(Instant) (adica trebuie sa treaca timp pt. a vedea
	// o modificare; timpul ramas in abonament se calculeaza de asemenea automat, in metoda endSession(Instant)
	
	// momentul cand membrul a pus cartela la intrare, incepand astfel sesiunea de training
	private Instant sessionStart = Instant.MIN;
	
	// momentul cand membrul a pus cartela la iesire, terminand astfel sesiunea de training
	private Instant sessionEnd = Instant.MIN;
	// diferenta intre 'sessionEnd' si 'sessionStart' reprezinta cat s-a antrenat membrul in acea sesiune
	// aceasta diferenta se va scadea din numarul total de secunde ramase in abonament
	
	private boolean newSessionStarted = false; // aceasta variabila spune daca s-a inceput o nou sesiune
	// cand s-a terminat o sesiune, 'newSessionStarted' va fi false
	
	private static int instanceCount = 0;
	
	private static final Logger LOG = LogManager.getLogger(GymMember.class);
	
	public GymMember(String name, LocalDate birthdate, Duration subscriptionDuration)
	{
		// mai inainte de toate, se da un id unic fiecarui membru
		this.id = instanceCount;
		instanceCount++;
		
		if(name != null)
			this.name = name;
		else
			LOG.error("null argument was passed.");
		
		if(birthdate != null)
			this.birthdate = birthdate;
		else
			LOG.error("null argument was passed.");
		
		if(subscriptionDuration != null)
			this.subscriptionDuration = subscriptionDuration;
		else
			LOG.error("null argument was passed.");
	}
	
	public GymMember(String name, LocalDate birthdate)
	{
		this(name, birthdate, Duration.ofHours(0));
	}
	
	// getteri:
	public int getId() { return this.id; }
	
	public String getName() { return this.name; }
	
	public LocalDate getBirthdate() { return this.birthdate; }
	
	public Duration getSubscriptionDuration() { return this.subscriptionDuration; }
	
	public Instant getSessionStart() { return this.sessionStart; }
	
	public Instant getSessionEnd() { return this.sessionEnd; }
	
	public long getAge()
	{
		return ChronoUnit.YEARS.between(this.birthdate, LocalDate.now());
	}
	
	public void setName(String name)
	{
		if(name != null)
			this.name = name;
		else
			LOG.error("null argument was passed.");
	}
	
	// setteri si alte metode:
	public void setBirthdate(LocalDate birthdate)
	{
		if(birthdate != null)
			this.birthdate = birthdate;
		else
			LOG.error("null argument was passed.");
	}
	
	// protected, caci modificarea abonamentului trebuie sa treaca prin sala de fitness (Gym), o persoana din afara
	// nu ar trebui sa poata modifica abonamentul (doar clasele din acest pachet si subclase au acces la metoda)
	protected void setSubscription(Duration subscriptionDuration)
	{
		if(subscriptionDuration != null)
		{
			// numai daca durata pasata nu este negativa, atunci se seteaza ca durata curenta
			if(subscriptionDuration.isNegative() == false)
				this.subscriptionDuration = subscriptionDuration;
			else
				// altfel abonamentul ramane nemodificat si se logheaza eroarea
				LOG.error("illegal argument was passed, subscription was not modified.");
		}
		else
			LOG.error("null argument was passed.");
	}
	
	// protected, caci modificarea abonamentului trebuie sa treaca prin sala de fitness (Gym), o persoana din afara
	// nu ar trebui sa poata modifica abonamentul (doar clasele din acest pachet si subclase au acces la metoda)
	protected void incrementSubscription(Duration duration)
	{
		if(duration != null)
		{
			// daca durata pasata nu este negativa, atunci se incrementeaza durata curenta
			if(duration.isNegative() == false)
				this.subscriptionDuration = subscriptionDuration.plus(duration);
			else
				LOG.error("illegal argument was passed, subscription was not modified.");
		}
		else
			LOG.error("null argument was passed.");
	}
	
	// protected, caci modificarea abonamentului trebuie sa treaca prin sala de fitness (Gym), o persoana din afara
	// nu ar trebui sa poata modifica abonamentul (doar clasele din acest pachet si subclase au acces la metoda)
	protected void decrementSubscription(Duration duration)
	{
		if(duration != null)
		{
			// daca durata pasata nu este negativa, atunci se decrementeaza durata curenta
			if(duration.isNegative() == false)
				this.subscriptionDuration = subscriptionDuration.minus(duration);
			else
				LOG.error("illegal argument was passed, subscription was not modified.");
		}
		else
			LOG.error("null argument was passed.");
	}
	
	// protected, caci modificarea sesiunii de fitness trebuie sa treaca prin sala de fitness (Gym), o persoana din afara
	// nu ar trebui sa poata modifica sesiunea (doar clasele din acest pachet si subclase au acces la metoda)
	protected void startSession(Instant newSessionStart)
	{
		if(newSessionStart != null)
		{
			// daca inca nu a inceput o nou sesiune de training
			if(newSessionStarted == false)
			{
				// cand incepe o noua sesiune, aceasta trebuie sa fie dupa ce s-a terminat ultima sesiune
				// se verifica daca momentul inceperii noii sesiuni este dupa momentul terminarii sesiunii trecute
				if(newSessionStart.isAfter(sessionEnd))
				{
					// atunci se seteaza momentul de inceput
					this.sessionStart = newSessionStart;
					// si se marcheaza ca membrul a inceput sesiunea
					this.newSessionStarted = true;
				}
				else
					LOG.error("illegal argument was passed.");
			}
			// daca sesiunea a inceput deja, inseamna ca membrul a pus cartela la intrare de doua ori consecutiv
			// (fara sa puna cartela si la iesire)
			// in acest caz, nu se mai face nimic (optional, s-ar putea loga si aceste "inceputuri" de sesiune)
			else
				LOG.warn("session already started.");
		}
		else
			LOG.error("null argument was passed.");
	}
	
	// protected, caci modificarea sesiunii de fitness trebuie sa treaca prin sala de fitness (Gym), o persoana din afara
	// nu ar trebui sa poata modifica sesiunea (doar clasele din acest pachet si subclase au acces la metoda)
	protected void endSession(Instant newSessionEnd)
	{
		if(newSessionEnd != null)
		{
			// cand se termina o noua sesiune, aceasta trebuie sa fie dupa ce a inceput noua sesiune
			if(newSessionStarted == true) // daca a inceput o noua sesiune
			{
				// daca noul moment de sfarsit al sesiunii curente este dupa inceputul sesiunii curente
				if(newSessionEnd.isAfter(sessionStart))
				{
					 // atunci se inregistreaza momentul de sfarsit
					this.sessionEnd = newSessionEnd;
					
					// se calculeaza durata ultimei sesiuni de training
					Duration newSessionDuration = Duration.between(this.sessionStart, this.sessionEnd);
					
					// se scade durata ultimei sesiuni din abonamentul membrului
					this.subscriptionDuration = this.subscriptionDuration.minus(newSessionDuration);
					
					this.newSessionStarted = false; // se marcheaza ca sesiunea s-a terminat
				}
				else
					LOG.error("illegal argument was passed.");
			}
			else // daca sesiunea s-a terminat deja
			{
				// atunci, poate ca membrul salii a pus cartela la iesire de doua ori consecutiv
				// se verifica daca membrul salii a pus cartela de doua ori consecutiv:
				if(newSessionEnd.isAfter(sessionEnd))
					// atunci noul sfarsit de sesiune va fi cel mai recent
					this.sessionEnd = newSessionEnd;
			}
		}
		else
			LOG.error("null argument was passed.");
	}
	
	public String toString()
	{
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		String birthdateFormatted = birthdate.format(formatter);
		
		// deoarece orele trec greu (pana se vede o schimbare), este mai usor pentru testare sa se afiseze timpul in secunde
		String subscriptionFormatted = subscriptionDuration.toSeconds() + " seconds";
		
		return "id: " + id + ", name: " + name + ", birthdate: " + birthdateFormatted +
				", subscription: " + subscriptionFormatted;
	}
	
	@Override
	public boolean equals(Object outerObj)
	{
		boolean areEqual = false;
		
		if(outerObj != null)
		{
			if(outerObj instanceof GymMember)
			{
				GymMember outerMember = (GymMember) outerObj;
				
				if(this.id == outerMember.id)
					areEqual = true;
			}
		}
		
		return areEqual;
	}
	
	@Override
	public int hashCode() { return this.id; }
	
	
}
