package ru.kosolap.json;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "requestId",
    "partNumber",
    "answers",
    "progress"  
})
@XmlRootElement(name = "CrackHashWorkerResponse", namespace = "http://ccfit.nsu.ru/schema/crack-hash-response")
public class CrackHashWorkerResponse {

    @XmlElement(name = "RequestId", required = true)
    protected String requestId;

    @XmlElement(name = "PartNumber", required = true)
    protected int partNumber;

    @XmlElement(name = "Progress", required = true) 
    protected double progress;

    @XmlElement(name = "Answers", required = true)
    protected CrackHashWorkerResponse.Answers answers;

    public CrackHashWorkerResponse() {
        this.answers = new Answers(); 
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String value) {
        this.requestId = value;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(int value) {
        this.partNumber = value;
    }

    public CrackHashWorkerResponse.Answers getAnswers() {
        return answers;
    }

    public void setAnswers(CrackHashWorkerResponse.Answers value) {
        this.answers = value;
    }

    public double getProgress() { 
        return progress;
    }

    public void setProgress(double progress) { 
        this.progress = progress;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "words"
    })
    public static class Answers {

        protected List<String> words;

        public Answers() {
            this.words = new ArrayList<>(); 
        }

        public List<String> getWords() {
            return words;
        }

        public void setWords(List<String> words) {
            this.words = words;
        }
    }

    @Override
    public String toString() {
        return "CrackHashWorkerResponse{" +
                "requestId='" + requestId + '\'' +
                ", partNumber=" + partNumber +
                ", answers=" + (answers != null ? answers.getWords() : "null") +
                ", progress=" + progress +
                '}';
    }
}
