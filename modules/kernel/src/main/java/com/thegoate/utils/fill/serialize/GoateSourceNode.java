package com.thegoate.utils.fill.serialize;

public class GoateSourceNode {
	GoateSourceNode next = null;
	GoateSource theSource = null;

	public void addSource(GoateSource source) {
		if (source != null) {
			if (theSource == null) {
				theSource = source;
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
