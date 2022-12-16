package com.thegoate.utils.fill.serialize;

import com.thegoate.Sponge;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SerializerSponge extends Sponge {

    String mapping = null;

    public SerializerSponge(String mapping) {
        this.mapping = mapping;
    }

    @Override
    public String soap(String dirty) {
        String soap = dirty;
        if (mapping != null) {
            String mapTo = mapping;
            if (mapping.contains("[0-9]+")) {
                mapTo = mapToRegex("[0-9]+", dirty);
                System.out.println(soap + "->" + mapTo);
            } else if (mapping.contains(".+")) {
                mapTo = mapToKeyPath(".+\\.", dirty);
                System.out.println(soap + "->" + mapTo);
            }
            if (mapTo != null) {
                soap = mapTo;
            }
        }
        return soap;
    }

    private String mapToRegex(String pattern, String dirty) {
        String mapTo = null;
        Matcher m = Pattern.compile("(" + pattern + ")").matcher(dirty);
        List<String> indexes = new ArrayList<>();
        while (m.find()) {
            indexes.add(m.group());
        }
        if (indexes.size() > 0) {
            mapTo = String.format(mapping.replace(pattern, "%s"), indexes.toArray());
        }

        return mapTo;
    }

    private String mapToKeyPath(String pattern, String dirty) {
        String mapTo = null;
        String placeHolder = System.nanoTime() + "_(dot_plus)_";
        String[] mapped = mapping.replace(".+", placeHolder).split("\\.");
        String[] dirtied = dirty.split("\\.");
        StringBuilder sb = new StringBuilder();
        if(dirtied.length == mapped.length) {
            for (int index = 0; index < mapped.length; index++) {
                if (index > 0) {
                    sb.append(".");
                }
                if (mapped[index].equals(placeHolder)) {
                    sb.append(dirtied[index]);
                } else {
                    sb.append(mapped[index]);
                }
            }
        }
        mapTo = sb.toString();
        return mapTo.isEmpty()?null:mapTo;
    }
}
