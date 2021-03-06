package com.sorint.openshift;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Random;

public class ServiceDb {
	
	
	public String generate() {
		String words[][] = {{"Artless", "Bawdy", "Beslubbering"}, {"Base-court", "Bat-fowling", "Beef-witted"}, {"Apple-john", "Baggage", "Barnacle"}};
		String vowels = "AEIOU";
		String article = "an";
		String firstAdjective = words[0][new Random().nextInt(words[0].length)];
		String secondAdjective = words[1][new Random().nextInt(words[1].length)];
		String noun = words[2][new Random().nextInt(words[2].length)];
		if (vowels.indexOf(firstAdjective.charAt(0)) == -1) {
			article = "a";
		}
		
			String out = "";
			try {
				out = getLocalHostLANAddress().toString();
				out = out.substring(1,out.length());
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			out="IP "+out;

		
		return String.format(out+"  UPD %s %s %s %s!", article, firstAdjective, secondAdjective, noun);
	}

	
	private static InetAddress getLocalHostLANAddress() throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			// Iterate all NICs (network interface cards)...
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// Iterate all IP addresses assigned to each card...
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {

						if (inetAddr.isSiteLocalAddress()) {
							// Found non-loopback site-local address. Return it immediately...
							return inetAddr;
						} else if (candidateAddress == null) {
							// Found non-loopback address, but not necessarily site-local.
							// Store it as a candidate to be returned if site-local address is not
							// subsequently found...
							candidateAddress = inetAddr;
							// Note that we don't repeatedly assign non-loopback non-site-local addresses as
							// candidates,
							// only the first. For subsequent iterations, candidate will be non-null.
						}
					}
				}
			}
			if (candidateAddress != null) {
				// We did not find a site-local address, but we found some other non-loopback
				// address.
				// Server might have a non-site-local address assigned to its NIC (or it might
				// be running
				// IPv6 which deprecates the "site-local" concept).
				// Return this non-loopback candidate address...
				return candidateAddress;
			}
			// At this point, we did not find a non-loopback address.
			// Fall back to returning whatever InetAddress.getLocalHost() returns...
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		} catch (Exception e) {
			UnknownHostException unknownHostException = new UnknownHostException(
					"Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}
	
	
}
