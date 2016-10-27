package components;


import java.io.Serializable;

public class Message implements Serializable{

    private int idMessage;
    private String text;
    private int readed;
    private int idCient;
    private int idAuction;

    public Message(int idMessage, String text, int readed, int idCient, int idAuction) {
        this.idMessage = idMessage;
        this.setText(text);
        this.setReaded(readed);
        this.setIdCient(idCient);
        this.setIdAuction(idAuction);
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    public int getIdCient() {
        return idCient;
    }

    public void setIdCient(int idCient) {
        this.idCient = idCient;
    }

    public int getIdAuction() {
        return idAuction;
    }

    public void setIdAuction(int idAuction) {
        this.idAuction = idAuction;
    }
}
