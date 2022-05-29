package ro.fasttrackit.curs3tema2.gym;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Comparator;

import java.time.LocalDate;
import java.time.Month;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Gym
{
	private List<GymMember> membersArray;
	
	private static final Logger LOG = LogManager.getLogger(Gym.class);
	
	public Gym()
	{
		membersArray = new ArrayList<GymMember>();
	}
	
	public List<GymMember> getMembersArray() { return this.membersArray; }
	
	public void addMember(GymMember member)
	{
		if(member != null)
			membersArray.add(member);
		else
			LOG.error("null argument was passed.");
	}
	
	public void startMemberSession(GymMember member)
	{
		// inainte de toate, se ia momentul cand membrul a pus cartela la intrare
		// (se presupune ca sala de fitness functioneaza cu cartele)
		Instant sessionStart = Instant.now();
		
		if(member != null)
		{
			// daca membrul este inregistrat in sala de fitness
			if(membersArray.contains(member))
				// atunci se seteaza momentul inceperii sesiunii
				member.startSession(sessionStart);
			else
				LOG.warn("member is not registered.");
		}
		else
			LOG.error("null argument was passed.");
	}
	
	public void startMemberSession(int memberID)
	{
		// inainte de toate, se ia momentul cand membrul a pus cartela la intrare
		// (se presupune ca sala de fitness functioneaza cu cartele)
		Instant sessionStart = Instant.now();
		
		if(memberID >= 0)
		{
			// se testeaza daca membrul este inregistrat in sala de fitness
			Optional<GymMember> optMember;
			optMember = membersArray
						.stream()
						.filter((GymMember member) -> member.getId() == memberID)
						.findFirst();
			
			// daca s-a gasit ID-ul ca fiind inregistrat in sala de fitness
			if(optMember.isPresent())
			{
				// atunci se seteaza momentul inceperii sesiunii
				optMember.get().startSession(sessionStart);
				
				System.out.println("session started for: " + optMember.get().toString());
			}
			else
				LOG.warn("member is not registered.");
		}
		else
			LOG.error("illegal argument was passed.");
	}
	
	public void endMemberSession(GymMember member)
	{
		// inainte de toate, se ia momentul cand membrul a pus cartela la iesire
		// (se presupune ca sala de fitness functioneaza cu cartele)
		Instant sessionEnd = Instant.now();
		
		if(member != null)
		{
			// daca membrul este inregistrat in sala de fitness
			if(membersArray.contains(member))
				// atunci se seteaza momentul terminarii sesiunii
				member.startSession(sessionEnd);
			else
				LOG.warn("member is not registered.");
		}
		else
			LOG.error("null argument was passed.");
	}
	
	public void endMemberSession(int memberID)
	{
		// inainte de toate, se ia momentul cand membrul a pus cartela la iesire
		// (se presupune ca sala de fitness functioneaza cu cartele)
		Instant sessionEnd = Instant.now();
		
		if(memberID >= 0)
		{
			// se testeaza daca membrul este inregistrat in sala de fitness
			Optional<GymMember> optMember;
			optMember = membersArray
						.stream()
						.filter((GymMember member) -> member.getId() == memberID)
						.findFirst();
			
			// daca s-a gasit ID-ul ca fiind inregistrat in sala de fitness
			if(optMember.isPresent())
			{
				// atunci se seteaza momentul terminarii sesiunii
				optMember.get().endSession(sessionEnd);
				
				System.out.println("session ended for: " + optMember.get().toString());
			}
			else
				LOG.warn("member is not registered.");
		}
		else
			LOG.error("illegal argument was passed.");
	}
	
	public double membersAverageAge()
	{
//		Duration avrgAge = Duration.ZERO; // media varstelor membrilor salii de fitness
		double avrgAge = 0; // media varstelor membrilor salii de fitness
		
		// daca exista macar un membru inregistrat la sala de fitness
		if(membersArray.size() > 0)
		{
			// atunci se calculeaza suma varstelor membrilor
			long sum = 0;
			long currentAge = 0;
			for(GymMember member : membersArray)
			{
				// varsta membrului curent este diferenta dintre data actuala si data sa de nastere
				currentAge = member.getAge();
				
				sum += currentAge; // se adauga varsta curenta la suma
			}
			
			// pentru determinarea mediei se imparte la nr. de membrii (care garantat nu este zero)
			avrgAge = (double)sum / (double)membersArray.size();
		}

		return avrgAge; // se returneaza media expimata in ani, nu in zile
	}
	
	public long membersMinAge()
	{
//		Duration minAge = Duration.ZERO; // varsta minima, implicit este zero
		long minAge = 0; // varsta minima, implicit este zero
		
		// se determina membrul cu varsta cea mai mica
		Optional<GymMember> youngestMember = null;
		youngestMember = membersArray
						.stream()
						.min(Comparator.comparing(GymMember::getAge));
		
		// daca s-a gasit (returnat) membrul cu varsta cea mai mica
		if(youngestMember.isPresent())
			// atunci se stocheaza varsta sa
			minAge = youngestMember.get().getAge();
			
		return minAge; // se returneaza varsta cea mai mica
	}
	
	public long membersMaxAge()
	{
//		Duration maxAge = Duration.ZERO; // varsta maxima, implicit este zero
		long maxAge = 0; // varsta maxima, implicit este zero
		
		// se determina membrul cu varsta cea mai mare
		Optional<GymMember> oldestMember = null;
		oldestMember = membersArray
						.stream()
						.max(Comparator.comparing(GymMember::getAge));
		
		// daca s-a gasit (returnat) membrul cu varsta cea mai mare
		if(oldestMember.isPresent())
			// atunci se stocheaza varsta sa
			maxAge = oldestMember.get().getAge();
			
		return maxAge; // se returneaza varsta cea mai mica
	}
	
	public Duration membersTotalRemainingTime()
	{
		Duration totalRemainingTime = Duration.ZERO;
		
		for(GymMember member : membersArray)
		{
			totalRemainingTime.plus(member.getSubscriptionDuration());
		}
		
		return totalRemainingTime;
	}
	
	public void addTimeToMember(GymMember member, Duration duration)
	{
		if(member != null && duration != null)
		{
			// daca membrul este inregistrat in sala de fitness
			if(membersArray.contains(member))
			{
				// daca durata de timp este valida
				if(duration.isNegative() == false)
					// atunci se incearca augmentarea orelor din abonament
					member.incrementSubscription(duration);
			}
			else
				LOG.warn("member is not registered.");
		}
		else
			LOG.error("null argument was passed.");
	}
	
	public void addTimeToMember(int memberID, Duration duration)
	{
		if(memberID >= 0 && duration != null)
		{
			// se testeaza daca membrul este inregistrat in sala de fitness
			Optional<GymMember> optMember;
			optMember = membersArray
						.stream()
						.filter((GymMember member) -> member.getId() == memberID)
						.findFirst();
			
			// daca s-a gasit ID-ul ca fiind inregistrat in sala de fitness
			if(optMember.isPresent())
			{
				// daca durata de timp este valida
				if(duration.isNegative() == false)
					// atunci se incearca augmentarea orelor din abonament
					optMember.get().incrementSubscription(duration);
				else
					LOG.error("illegal argument was passed.");
			}
			else
				LOG.warn("member is not registered.");
		}
		else
			LOG.error("null and/or illegal argument was passed.");
	}
	
	public String infoOfMember(String memberName)
	{
		String memberInfo = "n/a"; 
		
		if(memberName != null)
		{
			Optional<GymMember> gymMember;
			gymMember = membersArray
						.stream()
						.filter((GymMember member) -> member.getName().equals(memberName))
					    .findFirst();
			if(gymMember.isPresent())
				memberInfo = gymMember.get().toString();
			else
				memberInfo = "member not found";
		}
		
		return memberInfo;
	}
	

}
