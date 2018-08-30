/*
 * Copyright (c) 2017. Eric Angeli
 *
 *  Permission is hereby granted, free of charge,
 *  to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"),
 *  to deal in the Software without restriction,
 *  including without limitation the rights to use, copy,
 *  modify, merge, publish, distribute, sublicense,
 *  and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so,
 *  subject to the following conditions:
 *
 *  The above copyright notice and this permission
 *  notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 *  AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 *  DEALINGS IN THE SOFTWARE.
 */

package com.thegoate.dsl.words;

import com.thegoate.Goate;
import com.thegoate.annotations.GoateDescription;
import com.thegoate.data.PropertyFileDL;
import com.thegoate.dsl.DSL;
import com.thegoate.dsl.GoateDSL;
import com.thegoate.utils.GoateUtils;
import com.thegoate.utils.fill.FillString;

import java.io.File;
import java.util.List;

/**
 * Checks for a properties file in a folder called eut
 * whith a file name using the pattern eut.properties, where env is set or passed as a system property or environment
 * variable where eut is replaced with the name of the environment under test.<br>
 * example: -Deut=local<br>
 *      then in the resource directory:<br>
 *          resources/eut/local.properties<br>
 * You can override the properties file by setting the same parameter in your run data.<br>
 * To reference something in the eut properties use: eut::name,default
 * <br>where name is the name of the property and default is a default value to use if not found.
 * <br>default is not required and may be left off.
 *<p><br> The eut folder location may be configured by adding a file called eut.config, this file must be in the eut folder " +
 "<br>     but you may configure a different default name, a name pattern, file extension, and a location for the eut properties." +
 "<br>     example eut/eut.config (default is the default properties file, including the path, to load if no profile or eut is specified):"+
 "<br>default=config"+
 "<br>pattern=config-${profile}"+
 "<br>extension=properties"+
 "<br>location=setup</p>
 * Created by gtque on 5/19/2017.
 */
@GoateDSL(word = "eut")
@GoateDescription(description = "Returns the value of the specified property in the defined eut properties file." +
        "\n The eut can be specified by setting a system or environment variable called eut." +
        "\n The corresponding  property must be in the eut folder and be named <system/environment variable>.properties." +
        "\n The eut folder location may be configured by adding a file called eut.config, this file must be in the eut folder " +
        "\n     but you may configure a different default name, a name pattern, file extension, and a location for the eut properties." +
        "\n     example eut/eut.config (default is the default properties file, including the path, to load if no profile or eut is specified):"+
        "\ndefault=eut/local.properties"+
        "\npattern=${profile}"+
        "\nextension=properties"+
        "\nlocation=eut" +
        "\ndefault.profile=localdev",
    parameters = {"The name of the property","Optional default value."})
public class EutConfigDSL extends DSL {
    public EutConfigDSL(Object value) {
        super(value);
    }
    protected static Goate eut = new Goate();
    protected static volatile boolean loaded = false;
    protected String path = "eut";
    protected String defaultPropeties = "eut/local.properties";
    protected String fileName = "${eut}";
    protected String extension = "properties";

    public static void clear(){
        loaded = false;
        eut = new Goate();
    }
    @Override
    public Object evaluate(Goate data) {
        eut = (Goate)data.get("_goate_:eutConfig", eut);
        eut.put("_init_", true);
        if(!loaded) {
            loaded = true;
            List<Goate> configs = new PropertyFileDL().file("eut/eut.config").load();
            Goate config = new Goate();
            if(configs.size()>0){
                config = configs.get(0);
            }
            if(data.get("eut", data.get("profile",config.get("default.profile", null)))==null){
                LOG.debug("eut", "No profile found, will default to default properties.");
            }
            path = config.get("location", path, String.class);
            extension = config.get("extension", extension, String.class);
            defaultPropeties = config.get("default", defaultPropeties, String.class);
            fileName = config.get("pattern", fileName, String.class);
            String eutProfile = ""+new FillString(fileName).with(data);//"" + data.get("eut", data.get("profile","local"));
            String file = path+"/"+eutProfile+"."+extension;
            if(!new File(GoateUtils.getFilePath(file)).exists()){
                file = defaultPropeties;
            }
            List<Goate> d = new PropertyFileDL().file(file).load();
            if(d!=null&&d.size()>0){
                eut = d.get(0);
                data.put("_goate_:eutConfig", eut);
            }
        }
        String key = ""+get(1,data);
        Object def = get(2,data);
        return data.get(key, eut!=null?(eut.get(key)==null?def:eut.get(key)):def);
    }
}
