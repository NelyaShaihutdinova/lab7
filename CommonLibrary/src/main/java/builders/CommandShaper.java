package builders;

import java.io.Serializable;

public class CommandShaper implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String param;
    private Auntification auntification;


    public CommandShaper(String name, String param, Auntification auntification) {
        this.name = name;
        this.param = param;
        this.auntification = auntification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Auntification getAuntification() {
        return auntification;
    }

    public void setAuntification(Auntification auntification) {
        this.auntification = auntification;
    }
}

