package gzb.sdk;
public class BaseSdk {
    public static class Message {
        public long id;
        public String data;

        public Message(long id, String data) {
            this.id = id;
            this.data = data;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static class Request {
        public Object object;
        public Call call;
        public long sid;
    }
    public static class Call {
        public void run(Object obj) {

        }
    }

}