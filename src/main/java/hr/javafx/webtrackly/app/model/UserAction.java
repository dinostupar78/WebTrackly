package hr.javafx.webtrackly.app.model;

import hr.javafx.webtrackly.app.enums.BehaviourType;

import java.time.LocalDateTime;

public class UserAction extends Entity{
    private User user;
    private BehaviourType action;
    private Session session;
    private Website page;
    private LocalDateTime actionTimestamp;
    private String details;

    public UserAction(Long id, User user, BehaviourType action, Session session, Website page, LocalDateTime actionTimestamp, String details) {
        super(id);
        this.user = user;
        this.action = action;
        this.session = session;
        this.page = page;
        this.actionTimestamp = actionTimestamp;
        this.details = details;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BehaviourType getAction() {
        return action;
    }

    public void setAction(BehaviourType action) {
        this.action = action;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Website getPage() {
        return page;
    }

    public void setPage(Website page) {
        this.page = page;
    }

    public LocalDateTime getTimestamp() {
        return actionTimestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.actionTimestamp = timestamp;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    @Override
    public String toString() {
        return "UserAction{" +
                "user=" + user +
                ", action=" + action +
                ", session=" + session +
                ", page=" + page +
                ", actionTimestamp=" + actionTimestamp +
                ", details='" + details + '\'' +
                '}';
    }

    public static class Builder{
        private Long id;
        private User user;
        private BehaviourType action;
        private Session session;
        private Website page;
        private LocalDateTime actionTimestamp;
        private String details;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setUser(User user) {
            this.user = user;
            return this;
        }

        public Builder setAction(BehaviourType action) {
            this.action = action;
            return this;
        }

        public Builder setSession(Session session) {
            this.session = session;
            return this;
        }

        public Builder setPage(Website page) {
            this.page = page;
            return this;
        }

        public Builder setActionTimestamp(LocalDateTime actionTimestamp) {
            this.actionTimestamp = actionTimestamp;
            return this;
        }

        public Builder setDetails(String details) {
            this.details = details;
            return this;
        }

        public UserAction build(){
            return new UserAction(id, user, action, session, page, actionTimestamp, details);
        }
    }
}
