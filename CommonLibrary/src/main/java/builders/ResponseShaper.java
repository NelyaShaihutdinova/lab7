package builders;

import java.io.Serializable;

public class ResponseShaper implements Serializable {
    private static final long serialVersionUID = 1L;
    private String response;

    public ResponseShaper(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
