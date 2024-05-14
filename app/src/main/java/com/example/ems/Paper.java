package com.example.ems;

public class Paper {
    private int paperId;
    private String subject;
    private String year;
    private String timeDuration;
    private String formLink;
    private String degree;

    public Paper(int paperId, String subject, String year, String timeDuration, String formLink, String degree) {
        this.paperId = paperId;
        this.subject = subject;
        this.year = year;
        this.timeDuration = timeDuration;
        this.formLink = formLink;
        this.degree = degree;
    }

    public int getId() { return paperId;}

    public void setId(int paperId){this.paperId=paperId;}
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }
    public String getFormLink() {return formLink;}
    public void setFormLink(String formLink) { this.formLink = formLink; }

    public String getDegree() {return degree;}
    public void setDegree(String degree) { this.degree = degree; }

}
