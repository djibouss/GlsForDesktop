package Observer;

import utils.Box;
import utils.Race;

public interface Observer {
	void update(Race r);

	void update(Box b);
}
