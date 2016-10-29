package components;


import java.io.Serializable;

    public class Message implements Serializable{

    private int idMessage;
        private String username;
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

    public Message(String text, int readed, int idCient, int idAuction) {
        this.setText(text);
        this.setReaded(readed);
        this.setIdCient(idCient);
        this.setIdAuction(idAuction);
    }

        public Message( int idmessage,int idCient, String text, int idAuction, String user) {
            this.setText(text);
            this.setIdMessage(idmessage);
            this.setIdCient(idCient);
            this.setIdAuction(idAuction);
            this.setUsername(user);
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

        public int getIdMessage() {
            return idMessage;
        }

        public void setIdMessage(int idMessage) {
            this.idMessage = idMessage;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

    }
