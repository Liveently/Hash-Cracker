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
    "answers"
})
@XmlRootElement(name = "CrackHashWorkerResponse", namespace = "http://ccfit.nsu.ru/schema/crack-hash-response")
public class CrackHashWorkerResponse {

    @XmlElement(name = "RequestId", required = true)
    protected String requestId;

    @XmlElement(name = "PartNumber")
    protected int partNumber;

    @XmlElement(name = "Answers", required = true)
    protected CrackHashWorkerResponse.Answers answers;

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

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "words"
    })
    public static class Answers {

        protected List<String> words;

        public List<String> getWords() {
            if (words == null) {
                words = new ArrayList<>();
            }
            return words;
        }
    }
}
