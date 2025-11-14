package com.thegoate.utils.fill.serialize.automap;

import com.thegoate.expect.Expectation;
import com.thegoate.testng.TestNGEngineMethodDL;
import com.thegoate.utils.fill.serialize.AutoMap;
import com.thegoate.utils.fill.serialize.automap.pojos.*;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AutoMapTests extends TestNGEngineMethodDL {

    @Test(groups = {"unit"})
    public void testAutoMap() {
        Children familyA = new Children();
        familyA.setWord(true);
        InnerListChildren theChildren = new InnerListChildren();
        List<InnerListChild> myChildren = new ArrayList<>();
        myChildren.add(new InnerListChild("Roger"));
        myChildren.add(new InnerListChild("Dodger"));
        theChildren.setChildren(myChildren);
        familyA.setTheKids(theChildren);

        Offspring familyB = new Offspring();
        familyB.theTruth = true;
        InnerListOffspring theOffspring = new InnerListOffspring();
        List<InnerListItemOffspring> myOffspring = new ArrayList<>();
        myOffspring.add(new InnerListItemOffspring("Dodger"));
        myOffspring.add(new InnerListItemOffspring("Roger"));
        theOffspring.kids = myOffspring;
        familyB.theKids = theOffspring;

        ComplexKid family1 = new AutoMap(familyA).dataSource(Children.class).to(ComplexKid.class);
        ComplexKid family2 = new AutoMap(familyB).dataSource(Offspring.class).to(ComplexKid.class);
        expect(Expectation.build()
                .actual(family1)
                .isEqualToIgnoreOrder(family2));
    }
}
