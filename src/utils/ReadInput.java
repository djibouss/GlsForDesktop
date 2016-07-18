package utils;


import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public final class ReadInput {

	// utility class
	private ReadInput() {
		super();
	}

	public static Integer getInputFromUser(int lastOptions) {
		Set<Integer> it = new HashSet<Integer>(10);
		for (int i = 1; i <= lastOptions; i++) {
			it.add(i);
		}
		return ReadInput.getInputFromUser(it);
	}

	/**
	 * will never return <code>null</code>: keep asking the user until he
	 * chooses an allowed value.
	 */
	public static <T> T getInputFromUser(Iterable<T> allowedInput) {
		T chosenValue = null;
		boolean firstTime = true;
		Scanner sc = new Scanner(System.in);
		outer:
		do {
			if (!firstTime) {
				System.out.println("Not allowed input :( ");
			}
			System.out.println("You can choose one of this: " + allowedInput);
			String r = sc.nextLine();
			firstTime = false;
			for (T current : allowedInput) {
				String full = current.toString();
				String s = full.substring(0, 1);
				if (r.equalsIgnoreCase(full) || r.equalsIgnoreCase(s)) {
					chosenValue = current;
					break outer;
				}
			}
	
		} while (chosenValue == null);
		return chosenValue;
	}

}
