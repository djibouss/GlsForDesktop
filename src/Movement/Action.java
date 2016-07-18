package Movement;

public enum Action {
	MOVE("move"), ADD("add");

	private String value;

	Action(String s) {
		this.value = s;
	}

	public static Action findValue(String s) {
		if(s==null || s.length()==0){
			return null;
		}
		Action res = null;

		for (Action a : Action.values()) {
			if (a.value.equalsIgnoreCase(s) || a.value.startsWith(s)) {
				res = a;
			}
		}
		if (res == null) {
			System.out.println("you need to enter Add or Move");
		}

		return res;
	}

}
