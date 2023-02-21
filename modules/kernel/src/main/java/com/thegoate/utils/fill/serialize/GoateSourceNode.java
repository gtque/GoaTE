package com.thegoate.utils.fill.serialize;

import com.thegoate.logging.BleatBox;
import com.thegoate.logging.BleatFactory;

public class GoateSourceNode {
	GoateSourceNode next = null;
	GoateSource theSource = null;
	BleatBox LOG = BleatFactory.getLogger(getClass());

	public void addSource(GoateSource source) {
		if (source != null) {
			if (theSource == null) {
				theSource = source;
			} else {
				if(source.key().equals(theSource.key())){
					LOG.debug("Adding Source", "source key already exists, no reason to add it again.");
				} else {
					if (getPriority() <= source.priority()) {
						if (next == null) {
							next = new GoateSourceNode();
						}
						next.addSource(source);
					} else {
						GoateSourceNode temp = new GoateSourceNode();
						temp.addSource(theSource);
						temp.next = next;
						next = temp;
						theSource = source;
					}
				}
			}
		}
	}

	public int getPriority() {
		return theSource == null ? 0 : theSource.priority();
	}

	public GoateSource getTheSource() {
		return theSource;
	}

	public GoateSourceNode getNext() {
		return next;
	}
}
